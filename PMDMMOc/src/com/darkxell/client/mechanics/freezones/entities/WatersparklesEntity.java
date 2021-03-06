package com.darkxell.client.mechanics.freezones.entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.resources.images.Sprites.Res_FreezoneEntities;

public class WatersparklesEntity extends FreezoneEntity
{

	public static final byte TYPE_SIDE = 1;
	public static final byte TYPE_BOT = 2;
	public static final byte TYPE_TOP = 3;
	public static final byte TYPE_LONG = 4;

	private byte type = 0;
	private byte state = 0;
	private byte counter = 0;

	public WatersparklesEntity(double x, double y, byte type)
	{
		super(false, false, x, y);
		this.type = type;
	}

	@Override
	public void onInteract()
	{}

	@Override
	public void print(Graphics2D g)
	{
		g.drawImage(getSprite(), (int) (super.posX * 8), (int) (super.posY * 8), null);
	}

	@Override
	public void update()
	{
		++counter;
		if (counter >= 10)
		{
			counter = 0;
			if (state >= 5) state = 0;
			else++state;
		}
	}

	private BufferedImage getSprite()
	{
		switch (type)
		{
			case TYPE_SIDE:
				return Res_FreezoneEntities.waterSparkles.side(this.state);
			case TYPE_BOT:
				return Res_FreezoneEntities.waterSparkles.bot(this.state);
			case TYPE_TOP:
				return Res_FreezoneEntities.waterSparkles.top(this.state);
			case TYPE_LONG:
				return Res_FreezoneEntities.waterSparkles.lon(this.state);
			default:
				return null;
		}
	}

}
