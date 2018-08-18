package com.darkxell.common.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageSource;
import com.darkxell.common.event.stats.ExperienceGeneratedEvent;
import com.darkxell.common.pokemon.AffectsPokemon;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Pair;
import com.darkxell.common.util.RandomUtil;
import com.darkxell.common.util.language.Message;

public class StatusCondition implements AffectsPokemon, DamageSource
{
	private static final HashMap<Integer, StatusCondition> _registry = new HashMap<Integer, StatusCondition>();

	public static final PeriodicDamageStatusCondition Poisoned = new PeriodicDamageStatusCondition(0, -1, -1, 10, 4);
	public static final PeriodicDamageStatusCondition Badly_poisoned = new PeriodicDamageStatusCondition(1, -1, -1, 2, 6);
	public static final PeriodicDamageStatusCondition Burn = new PeriodicDamageStatusCondition(2, -1, -1, 20, 5);
	public static final PreventsActionStatusCondition Asleep = new PreventsActionStatusCondition(3, 3, 6);

	public static final StealsHpStatusCondition Leech_seed = new StealsHpStatusCondition(10, 11, 12, 2, 10);

	public static final PreventsOtherStatusCondition Sleepless = new PreventsOtherStatusCondition(100, 11, 12, Asleep);

	/** @return The Status Condition with the input ID. */
	public static StatusCondition find(int id)
	{
		return _registry.get(id);
	}

	/** This Status condition's duration. -1 for indefinite. */
	public final int durationMin, durationMax;
	/** This Status Condition's ID. */
	public final int id;

	public StatusCondition(int id, int durationMin, int durationMax)
	{
		this.id = id;
		this.durationMin = durationMin;
		this.durationMax = durationMax;
		_registry.put(this.id, this);
	}

	/** @return - True if this Status Condition affects the input Pokemon.<br>
	 *         - A Message to display if this Condition doesn't affect the Pokemon. May be <code>null</code> if there is no necessary message. */
	public Pair<Boolean, Message> affects(DungeonPokemon pokemon)
	{
		if (pokemon.hasStatusCondition(this)) return new Pair<>(false,
				new Message("status.already").addReplacement("<pokemon>", pokemon.getNickname()).addReplacement("<condition>", this.name()));
		for (StatusCondition c : pokemon.activeStatusConditions())
			if (c instanceof PreventsOtherStatusCondition && ((PreventsOtherStatusCondition) c).prevents(this)) new Message("status.prevented.condition")
					.addReplacement("<pokemon>", pokemon.getNickname()).addReplacement("<prevented>", this.name()).addReplacement("<preventer>", c.name());
		return new Pair<>(true, null);
	}

	public AppliedStatusCondition create(DungeonPokemon target, Object source, Random random)
	{
		return new AppliedStatusCondition(this, target, source, RandomUtil.nextIntInBounds(this.durationMin, this.durationMax, random));
	}

	@Override
	public ExperienceGeneratedEvent getExperienceEvent()
	{
		return null;
	}

	Message immune(DungeonPokemon pokemon)
	{
		return new Message("status.immune").addReplacement("<pokemon>", pokemon.getNickname()).addReplacement("<condition>", this.name());
	}

	public Message name()
	{
		return new Message("status." + this.id);
	}

	public void tick(Floor floor, AppliedStatusCondition instance, ArrayList<DungeonEvent> events)
	{}

}
