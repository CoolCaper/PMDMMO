package com.darkxell.common.dungeon.floor;

import com.darkxell.common.item.Item;
import com.darkxell.common.pokemon.Pokemon;

/** Represents a single tile in a Floor. */
public class Tile
{

	public final Floor floor;
	/** The Item on this Tile. null if no Item. */
	private Item item;
	/** The Pok�mon standing on this Tile. null if no Pok�mon. */
	private Pokemon pokemon;
	/** This Tile's type. */
	private TileType type;
	/** This Tile's coordinates. */
	public final int x, y;

	public Tile(Floor floor, int x, int y, TileType type)
	{
		this.floor = floor;
		this.x = x;
		this.y = y;
		this.type = type;
	}

	/** @return The Item on this Tile. null if no Item. */
	public Item getItem()
	{
		return item;
	}

	/** @return The Pok�mon standing on this Tile. null if no Pok�mon. */
	public Pokemon getPokemon()
	{
		return pokemon;
	}

	public void setItem(Item item)
	{
		this.item = item;
	}

	public void setPokemon(Pokemon pokemon)
	{
		this.pokemon = pokemon;
	}

	/** Sets this Tile's type. */
	public Tile setType(TileType type)
	{
		this.type = type;
		return this;
	}

	/** @return This Tile's type. */
	public TileType type()
	{
		return this.type;
	}

}
