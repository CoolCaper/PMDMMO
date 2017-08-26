package com.darkxell.common.dungeon.floor;

import java.util.Random;

/** Represents a Room in a Floor. */
public class Room
{
	/** The floor of this Room. */
	public final Floor floor;
	/** True if this Room is a Monster House. */
	public final boolean isMonsterHouse;
	/** This room's dimensions. */
	public final int width, height;
	/** This Room's location. */
	public final int x, y;

	public Room(Floor floor, int x, int y, int width, int height, boolean isMonsterHouse)
	{
		this.floor = floor;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isMonsterHouse = isMonsterHouse;
	}

	/** @return True if the input coordinates are inside this Room. */
	public boolean contains(int x, int y)
	{
		return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
	}

	/** @return A random tile in this Room. */
	public Tile randomTile(Random random)
	{
		return this.floor.tileAt(this.x + random.nextInt(this.width + 1), this.y + random.nextInt(this.height + 1));
	}

}
