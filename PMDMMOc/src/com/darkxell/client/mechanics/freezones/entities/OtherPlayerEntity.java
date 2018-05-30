package com.darkxell.client.mechanics.freezones.entities;

import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.mechanics.freezones.entities.renderers.OtherPlayerPokemonRenderer;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.common.util.Direction;

public class OtherPlayerEntity extends FreezoneEntity
{

	private double destinationX;
	private double destinationY;
	/** The nano timestamp of the last update. */
	public long lastupdate;
	public final String name;
	private PokemonSprite sprite;
	private int spriteID;

	public OtherPlayerEntity(double x, double y, int spriteID, String name, long timestamp)
	{
		super(false, true, x, y);
		this.sprite = new PokemonSprite(PokemonSpritesets.getSpriteset(spriteID));
		this.spriteID = spriteID;
		this.name = name;
		this.destinationX = x;
		this.destinationY = y;
	}

	public void applyServerUpdate(double x, double y, int spriteID)
	{
		this.destinationX = x;
		this.destinationY = y;
		if (this.spriteID != spriteID)
		{
			this.spriteID = spriteID;
			PokemonSprite previous = this.sprite;
			this.sprite = new PokemonSprite(PokemonSpritesets.getSpriteset(spriteID));
			this.sprite.copyState(previous);
		}
		this.lastupdate = System.nanoTime();
	}

	@Override
	public AbstractRenderer createRenderer()
	{
		return new OtherPlayerPokemonRenderer(this, this.sprite);
	}

	@Override
	public void onInteract()
	{}

	public PokemonSprite sprite()
	{
		return this.sprite;
	}

	@Override
	public void update()
	{
		// Calculates the movespeed of the pokemon
		double movespeed = 0.2d;
		boolean up = false, right = false, down = false, left = false;
		if (destinationX > posX + 5 || destinationX < posX - 5 || destinationY > posY + 5 || destinationY < posY - 5) movespeed *= 2;
		// Moves the pokemon accordingly
		if (destinationX > posX + 0.5)
		{
			posX += movespeed;
			right = true;
		} else if (destinationX < posX - 0.5)
		{
			posX -= movespeed;
			left = true;
		}
		if (destinationY > posY + 0.5)
		{
			posY += movespeed;
			down = true;
		} else if (destinationY < posY - 0.5)
		{
			posY -= movespeed;
			up = true;
		}
		// Sets the rotation of the pokemonSprite used
		if (up && right) this.sprite.setFacingDirection(Direction.NORTHEAST);
		else if (right && down) this.sprite.setFacingDirection(Direction.SOUTHEAST);
		else if (down && left) this.sprite.setFacingDirection(Direction.SOUTHWEST);
		else if (left && up) this.sprite.setFacingDirection(Direction.NORTHWEST);
		else if (up) this.sprite.setFacingDirection(Direction.NORTH);
		else if (right) this.sprite.setFacingDirection(Direction.EAST);
		else if (down) this.sprite.setFacingDirection(Direction.SOUTH);
		else if (left) this.sprite.setFacingDirection(Direction.WEST);
		// Sets the running/idle state
		if (up || right || down || left) this.sprite.setState(PokemonSpriteState.MOVE);
		else this.sprite.setState(PokemonSpriteState.IDLE);
	}

}
