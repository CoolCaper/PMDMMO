package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.util.Pair;
import com.darkxell.common.util.language.Message;

public class PeriodicDamageStatusCondition extends StatusCondition
{
	/** The damage this Status Condition deals. */
	public final int damage;
	/** The number of turns between each damage dealt. */
	public final int period;

	public PeriodicDamageStatusCondition(int id, boolean isAilment, int durationMin, int durationMax, int period, int damage)
	{
		super(id, isAilment, durationMin, durationMax);
		this.period = period;
		this.damage = damage;
	}

	@Override
	public Pair<Boolean, Message> affects(AppliedStatusCondition condition, DungeonPokemon pokemon)
	{
		Pair<Boolean, Message> sup = super.affects(condition, pokemon);
		if (!sup.first) return sup;
		if ((this == StatusConditions.Poisoned || this == StatusConditions.Badly_poisoned) && pokemon.species().isType(PokemonType.Poison))
			return new Pair<>(false, this.immune(pokemon));
		if (this == StatusConditions.Burn && pokemon.species().isType(PokemonType.Fire)) return new Pair<>(false, this.immune(pokemon));
		return new Pair<>(true, null);
	}

	@Override
	public void tick(Floor floor, AppliedStatusCondition instance, ArrayList<DungeonEvent> events)
	{
		if (instance.tick % this.period == 0) events.add(new DamageDealtEvent(floor, instance.pokemon, this, DamageType.CONDITION, this.damage));
	}

}
