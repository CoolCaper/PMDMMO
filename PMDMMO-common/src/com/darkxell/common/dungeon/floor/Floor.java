package com.darkxell.common.dungeon.floor;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Predicate;

import com.darkxell.common.dungeon.DungeonInstance;
import com.darkxell.common.dungeon.floor.layout.Layout;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.trap.Trap;
import com.darkxell.common.trap.TrapRegistry;
import com.darkxell.common.util.RandomUtil;

/** Represents a generated Floor in a Dungeon. */
public class Floor
{

	/** This Floor's data. */
	public final FloorData data;
	/** This Floor's Dungeon. */
	public final DungeonInstance dungeon;
	/** This Floor's ID. */
	public final int id;
	private boolean isGenerating = true;
	/** This Floor's layout. */
	public final Layout layout;
	/** RNG for game logic: moves, mob spawning, etc. */
	public final Random random;
	/** This Floor's rooms. null before generating. */
	public Room[] rooms;
	/** The position at which the team will spawn. */
	public Point teamSpawn;
	/** This Floor's tiles. null before generating. Note that this array must NOT be modified. It is only public because the generation algorithm uses this array to generate the floor. */
	public Tile[][] tiles;

	public Floor(int id, Layout layout, DungeonInstance dungeon, Random random)
	{
		this.id = id;
		this.dungeon = dungeon;
		this.data = this.dungeon.dungeon().getData(this.id);
		this.layout = layout;
		this.random = random;
	}

	/** Generates this Floor. */
	public void generate()
	{
		this.layout.generate(this);
		isGenerating = false;
		for (Tile[] row : this.tiles)
			for (Tile t : row)
				t.updateNeighbors();
	}

	public int getHeight()
	{
		return this.tiles[0].length;
	}

	public int getWidth()
	{
		return this.tiles.length;
	}

	/** @return True if this Floor is done generating. */
	public boolean isGenerated()
	{
		return !isGenerating;
	}

	/** Called when a new turn starts. */
	public ArrayList<DungeonEvent> onTurnStart()
	{
		ArrayList<DungeonEvent> e = new ArrayList<DungeonEvent>();
		// e.add(new MessageEvent(this, new Message("New turn!", false)));
		return e;
	}

	/** @param inRoom - True if the Tile should be in a Room (will also avoid tiles adjacent to corridors in rooms).
	 * @return A Random Tile in this floor. */
	public Tile randomEmptyTile(boolean inRoom, Random random)
	{
		return this.randomEmptyTile(inRoom, null, random);
	}

	/** @param inRoom - True if the Tile should be in a Room (will also avoid tiles adjacent to corridors in rooms).
	 * @param type - A Type that the Tile has to match. null for any type.
	 * @return A Random Tile in this floor. */
	public Tile randomEmptyTile(boolean inRoom, TileType type, Random random)
	{
		ArrayList<Tile> candidates = new ArrayList<Tile>();
		if (inRoom) for (Room room : this.rooms)
			candidates.addAll(room.listTiles());
		else
		{
			for (Tile[] row : this.tiles)
				for (Tile tile : row)
					candidates.add(tile);
		}

		if (type != null) candidates.removeIf(new Predicate<Tile>()
		{
			@Override
			public boolean test(Tile t)
			{
				return t.type() != type;
			}
		});

		candidates.removeIf(new Predicate<Tile>()
		{
			@Override
			public boolean test(Tile t)
			{
				return !t.isEmpty();
			}
		});

		return RandomUtil.random(candidates, random);
	}

	/** @return A random Room in this Floor. */
	public Room randomRoom(Random random)
	{
		return this.rooms[random.nextInt(this.rooms.length)];
	}

	/** @return A Random Trap in this Floor. */
	public Trap randomTrap(Random random)
	{
		HashMap<Integer, Integer> traps = this.dungeon.dungeon().traps(this.id);
		return TrapRegistry.find(RandomUtil.weightedRandom(traps, random));
	}

	/** @return The room at the input X, Y coordinates. null if not in a Room. */
	public Room roomAt(int x, int y)
	{
		for (Room room : this.rooms)
			if (room.contains(x, y)) return room;
		return null;
	}

	/** Overrides all of the floor's tiles. */
	public void setTiles(Tile[][] tiles)
	{
		this.tiles = tiles;
	}

	public void summonPokemon(DungeonPokemon pokemon, int x, int y)
	{
		if (!(this.tiles == null || x < 0 || x >= this.tiles.length || y < 0 || y >= this.tiles[x].length)) this.tileAt(x, y).setPokemon(pokemon);
		this.dungeon.registerActor(pokemon);
	}

	/** @return The tile at the input X, Y coordinates. */
	public Tile tileAt(int x, int y)
	{
		if (this.tiles == null || x < 0 || x >= this.tiles.length || y < 0 || y >= this.tiles[x].length) return null;
		return this.tiles[x][y];
	}

	@Override
	public String toString()
	{
		String s = "";
		for (int y = 0; y < this.getHeight(); ++y)
		{
			for (int x = 0; x < this.getWidth(); ++x)
			{
				Tile t = this.tileAt(x, y);
				s += t != null ? t.type().c : "?";
			}
			s += "\n";
		}
		return s;
	}

	public void unsummonPokemon(DungeonPokemon pokemon)
	{
		pokemon.tile.setPokemon(null);
		this.dungeon.unregisterActor(pokemon);
	}

}
