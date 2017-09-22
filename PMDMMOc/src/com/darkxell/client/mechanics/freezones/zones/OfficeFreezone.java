package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.client.mechanics.freezones.entities.AnimatedFlowerEntity;
import com.darkxell.client.renderers.layers.BackgroundSeaLayer;
import com.darkxell.common.util.DoubleRectangle;

public class OfficeFreezone extends FreezoneMap {

	public BackgroundSeaLayer background = new BackgroundSeaLayer();

	public OfficeFreezone() {
		super("resources\\freezones\\office.xml");
		this.freezonebgm = "14 Pokemon Square.mp3";
		this.warpzones.add(new WarpZone(116, 40, new DoubleRectangle(0, 25, 2, 9)) {
			@Override
			public FreezoneMap getDestination() {
				return new PokemonSquareFreezone();
			}
		});
		
		this.entities.add(new AnimatedFlowerEntity(7, 25, false));
		this.entities.add(new AnimatedFlowerEntity(4, 47, false));
		this.entities.add(new AnimatedFlowerEntity(20, 41, false));
		this.entities.add(new AnimatedFlowerEntity(43, 26, false));
		
		this.entities.add(new AnimatedFlowerEntity(18, 21, true));
		this.entities.add(new AnimatedFlowerEntity(23, 20, true));
		this.entities.add(new AnimatedFlowerEntity(55, 24, true));
		this.entities.add(new AnimatedFlowerEntity(40, 39, true));
		this.entities.add(new AnimatedFlowerEntity(15, 46, true));
	}

}