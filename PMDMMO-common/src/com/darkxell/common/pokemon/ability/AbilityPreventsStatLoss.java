package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;

public class AbilityPreventsStatLoss extends Ability
{

	private final Stat[] protectedStats;

	public AbilityPreventsStatLoss(int id, Stat... protectedStats)
	{
		super(id);
		this.protectedStats = protectedStats;
	}

	@Override
	public void onPreEvent(Floor floor, DungeonEvent event, DungeonPokemon concerned, ArrayList<DungeonEvent> resultingEvents)
	{
		super.onPreEvent(floor, event, concerned, resultingEvents);
		if (event instanceof StatChangedEvent)
		{
			StatChangedEvent e = (StatChangedEvent) event;
			if (e.stage < 0) for (Stat s : this.protectedStats)
				if (e.stat == s)
				{
					event.consume();
					resultingEvents.add(new TriggeredAbilityEvent(event.floor, concerned));
				}
		}
	}

}
