package com.darkxell.common.status;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.BaseStats.Stat;

public class ParalyzedStatusCondition extends StatusCondition
{

	public ParalyzedStatusCondition(int id, int durationMin, int durationMax)
	{
		super(id, durationMin, durationMax);
	}

	@Override
	public int applyStatStageModifications(Stat stat, int stage, MoveUse move, DungeonPokemon target, boolean isUser, Floor floor,
			ArrayList<DungeonEvent> events)
	{
		if (stat == Stat.Speed) return stage - 1;
		return super.applyStatStageModifications(stat, stage, move, target, isUser, floor, events);
	}

	@Override
	public boolean preventsUsingMoves(DungeonPokemon pokemon, Floor floor)
	{
		return true;
	}

}