package com.darkxell.common.ai.states;

import java.util.ArrayList;
import java.util.Comparator;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.AI.AIState;
import com.darkxell.common.ai.AIUtils;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.action.PokemonTravelEvent;
import com.darkxell.common.event.action.TurnSkippedEvent;
import com.darkxell.common.util.Direction;

/** State in which the Pokemon skips turns. */
public class AIStateRunaway extends AIState
{

	private static int score(Tile tile)
	{
		int score = 0;
		for (Direction d : Direction.directions)
			if (tile.adjacentTile(d).getPokemon() != null) ++score;
		return score;
	}

	public AIStateRunaway(AI ai)
	{
		super(ai);
	}

	@Override
	public DungeonEvent takeAction()
	{
		Comparator<Tile> sorter = new Comparator<Tile>() {
			@Override
			public int compare(Tile o1, Tile o2)
			{
				return Integer.compare(score(o1), score(o2));
			}
		};
		ArrayList<Tile> candidates = AIUtils.adjacentReachableTiles(this.ai.floor, this.ai.pokemon);
		if (candidates.size() == 0) return new TurnSkippedEvent(this.ai.floor, this.ai.pokemon);
		candidates.sort(sorter);
		return new PokemonTravelEvent(this.ai.floor, this.ai.pokemon, AIUtils.direction(this.ai.pokemon, candidates.get(0)));
	}

	@Override
	public String toString()
	{
		return "Flees";
	}

}
