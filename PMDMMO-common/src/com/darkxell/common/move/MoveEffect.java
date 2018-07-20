package com.darkxell.common.move;

import java.util.ArrayList;

import com.darkxell.common.ai.AIUtils;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Room;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEvent.MessageEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.move.Move.MoveCategory;
import com.darkxell.common.move.Move.MoveRange;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.DungeonStats;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.language.Message;

public class MoveEffect
{
	public final int id;

	public MoveEffect(int id)
	{
		this.id = id;
		MoveEffects.effects.put(this.id, this);
	}

	protected double accuracyStat(MoveUse move, DungeonPokemon target, Floor floor)
	{
		Stat acc = Stat.Accuracy;
		int accStage = move.user.stats.getStage(acc);
		accStage = this.applyStatStageModifications(acc, accStage, move, target, floor);

		DungeonStats stats = move.user.stats.clone();
		stats.setStage(acc, accStage);
		double accuracy = stats.getStat(acc);
		accuracy = this.applyStatModifications(acc, accuracy, move, target, floor);
		if (accuracy < 0) accuracy = 0;
		if (accuracy > 999) accuracy = 999;

		return accuracy;
	}

	protected double applyStatModifications(Stat stat, double value, MoveUse move, DungeonPokemon target, Floor floor)
	{
		value = move.user.usedPokemon.ability().applyStatModifications(stat, value, move, target, true, floor);
		value = target.usedPokemon.ability().applyStatModifications(stat, value, move, target, false, floor);
		if (move.user.usedPokemon.item() != null) value = move.user.usedPokemon.item().item().applyStatModifications(stat, value, move, target, true, floor);
		if (target.usedPokemon.item() != null) value = target.usedPokemon.item().item().applyStatModifications(stat, value, move, target, false, floor);
		value = floor.currentWeather().weather.applyStatModifications(stat, value, move, target, false, floor);
		return value;
	}

	protected int applyStatStageModifications(Stat stat, int stage, MoveUse move, DungeonPokemon target, Floor floor)
	{
		stage = move.user.usedPokemon.ability().applyStatStageModifications(stat, stage, move, target, true, floor);
		stage = target.usedPokemon.ability().applyStatStageModifications(stat, stage, move, target, false, floor);
		if (move.user.usedPokemon.item() != null)
			stage = move.user.usedPokemon.item().item().applyStatStageModifications(stat, stage, move, target, true, floor);
		if (target.usedPokemon.item() != null) stage = target.usedPokemon.item().item().applyStatStageModifications(stat, stage, move, target, false, floor);
		stage = floor.currentWeather().weather.applyStatStageModifications(stat, stage, move, target, false, floor);
		return stage;
	}

	protected int attackStat(MoveUse move, DungeonPokemon target, Floor floor)
	{
		Stat atk = move.move.move().category == MoveCategory.Special ? Stat.SpecialAttack : Stat.Attack;
		int atkStage = move.user.stats.getStage(atk);
		atkStage = this.applyStatStageModifications(atk, atkStage, move, target, floor);

		DungeonStats stats = move.user.stats.clone();
		stats.setStage(atk, atkStage);
		double attack = stats.getStat(atk);
		attack = this.applyStatModifications(atk, attack, move, target, floor);
		if (attack < 0) attack = 0;
		if (attack > 999) attack = 999;

		return (int) attack;
	}

	protected boolean criticalLands(MoveUse move, DungeonPokemon target, Floor floor)
	{
		int crit = move.move.move().critical;
		crit = move.user.usedPokemon.ability().applyCriticalRateModifications(crit, move, target, true, floor);
		crit = target.usedPokemon.ability().applyCriticalRateModifications(crit, move, target, false, floor);

		if (this.effectiveness(move, target, floor) == PokemonType.SUPER_EFFECTIVE) crit = 40;
		return floor.random.nextInt(100) < crit;
	}

	/** @param move - The Move being used.
	 * @param user - The Pok�mon using the move.
	 * @param target - The Pok�mon receiving the move.
	 * @param floor - The floor context.
	 * @param events - The list of Events created by this Move.
	 * @return The damage dealt by this move. */
	public int damageDealt(MoveUse move, DungeonPokemon user, DungeonPokemon target, Floor floor, ArrayList<DungeonEvent> events)
	{
		int attack = this.attackStat(move, target, floor);
		int defense = this.defenseStat(move, target, floor);
		int level = user.level();
		int power = this.movePower(move, user, target, floor);
		double wildNerfer = user.player() != null ? 1 : 0.75;

		double damage = ((attack + power) * 0.6 - defense / 2 + 50 * Math.log(((attack - defense) / 8 + level + 50) * 10) - 311) * wildNerfer;
		if (damage < 1) damage = 1;
		if (damage > 999) damage = 999;

		double multiplier = this.damageMultiplier(move, this.criticalLands(move, target, floor), target, floor);
		damage *= multiplier;

		// Damage randomness
		damage *= (9 - floor.random.nextDouble() * 2) / 8;

		return (int) Math.round(damage);
	}

	protected double damageMultiplier(MoveUse move, boolean critical, DungeonPokemon target, Floor floor)
	{
		double multiplier = 1;
		multiplier *= this.effectiveness(move, target, floor);
		if (move.isStab()) multiplier *= 1.5;
		multiplier *= floor.currentWeather().weather.damageMultiplier(move, target, false, floor);
		if (critical) multiplier *= 1.5;

		multiplier *= move.user.usedPokemon.ability().damageMultiplier(move, target, true, floor);
		multiplier *= target.usedPokemon.ability().damageMultiplier(move, target, false, floor);

		return multiplier;
	}

	protected int defenseStat(MoveUse move, DungeonPokemon target, Floor floor)
	{
		Stat def = move.move.move().category == MoveCategory.Special ? Stat.SpecialDefense : Stat.Defense;
		int defStage = target.stats.getStage(def);
		defStage = this.applyStatStageModifications(def, defStage, move, target, floor);

		DungeonStats stats = target.stats.clone();
		stats.setStage(def, defStage);
		double defense = stats.getStat(def);
		defense = this.applyStatModifications(def, defense, move, target, floor);
		if (defense < 0) defense = 0;
		if (defense > 999) defense = 999;

		return (int) defense;
	}

	protected double effectiveness(MoveUse move, DungeonPokemon target, Floor floor)
	{
		double effectiveness = move.move.move().type.effectivenessOn(target.species());
		// Ask for status effects such as Miracle Eye, or Floor effects such as Gravity later
		return effectiveness;
	}

	protected double evasionStat(MoveUse move, DungeonPokemon target, Floor floor)
	{
		Stat ev = Stat.Evasiveness;
		int evStage = target.stats.getStage(ev);
		evStage = this.applyStatStageModifications(ev, evStage, move, target, floor);

		DungeonStats stats = target.stats.clone();
		stats.setStage(ev, evStage);
		double evasion = stats.getStat(ev);
		evasion = this.applyStatModifications(ev, evasion, move, target, floor);
		if (evasion < 0) evasion = 0;
		if (evasion > 999) evasion = 999;

		return evasion;
	}

	/** Removes all Pok�mon this move is not supposed to target. */
	protected void filterTargets(Floor floor, Move move, DungeonPokemon user, ArrayList<DungeonPokemon> targets)
	{
		switch (move.targets)
		{
			case All:
				break;

			case Allies:
				targets.removeIf((DungeonPokemon p) -> p == user || !p.isAlliedWith(user));
				break;

			case Foes:
				targets.removeIf((DungeonPokemon p) -> p == user || p.isAlliedWith(user));
				break;

			case Others:
				targets.remove(user);
				break;

			case Team:
				targets.removeIf((DungeonPokemon p) -> !p.isAlliedWith(user));
				break;

			case User:
				targets.removeIf((DungeonPokemon p) -> p != user);
				break;

			case None:
				targets.clear();
				break;
		}
	}

	/** @param move
	 * @param user - The Pok�mon using this Move.
	 * @param floor - The Floor context.
	 * @return The Pok�mon affected by this Move. */
	public DungeonPokemon[] getTargets(Move move, DungeonPokemon user, Floor floor)
	{
		ArrayList<DungeonPokemon> targets = new ArrayList<DungeonPokemon>();
		Tile t = user.tile(), front = t.adjacentTile(user.facing());

		switch (move.range)
		{
			case Ambient:
				targets.add(null);
				break;

			case Around2:
				for (int x = -2; x < 3; ++x)
					for (int y = -2; y < 3; ++y)
						if (x == -2 || x == 2 || y == -2 || y == 2)
						{
							Tile t2 = floor.tileAt(t.x + x, t.y + y);
							if (t2.getPokemon() != null) targets.add(t2.getPokemon());
						}

			case Around:
				for (Direction d : Direction.directions)
					if (t.adjacentTile(d).getPokemon() != null) targets.add(t.adjacentTile(d).getPokemon());
				break;

			case Floor:
				targets.addAll(floor.listPokemon());
				break;

			case Front_row:
				for (Direction d : new Direction[] { user.facing().rotateCounterClockwise(), user.facing(), user.facing().rotateClockwise() })
					if (t.adjacentTile(d).getPokemon() != null) targets.add(t.adjacentTile(d).getPokemon());
				break;

			case Line:
				int distance = 0;
				boolean done;
				Tile current = t;
				do
				{
					current = current.adjacentTile(user.facing());
					if (current.getPokemon() != null) targets.add(current.getPokemon());
					++distance;
					done = !targets.isEmpty() || distance > 10 || current.type() == TileType.WALL || current.type() == TileType.WALL_END;
				} while (!done);
				break;

			case Room:
				Room r = floor.roomAt(user.tile().x, user.tile().y);
				if (r == null)
				{
					for (Tile tile : AIUtils.visibleTiles(floor, user))
						if (tile.getPokemon() != null) targets.add(tile.getPokemon());
				} else for (Tile t2 : r.listTiles())
					if (t2.getPokemon() != null) targets.add(t2.getPokemon());
				break;

			case Self:
				targets.add(user);
				break;

			case Two_tiles:
				if (front.getPokemon() != null) targets.add(front.getPokemon());
				else if (front.type() != TileType.WALL && front.type() != TileType.WALL_END)
				{
					Tile behind = front.adjacentTile(user.facing());
					if (behind.getPokemon() != null) targets.add(behind.getPokemon());
				}
				break;

			case Front:
			case Front_corners:
			default:
				DungeonPokemon f = user.tile().adjacentTile(user.facing()).getPokemon();
				if (f != null)
				{
					boolean valid = true;
					if (user.facing().isDiagonal() && move.range != MoveRange.Front_corners)
					{
						Tile t1 = user.tile().adjacentTile(user.facing().rotateClockwise());
						if (t1.type() == TileType.WALL || t1.type() == TileType.WALL_END) valid = false;
						t1 = user.tile().adjacentTile(user.facing().rotateCounterClockwise());
						if (t1.type() == TileType.WALL || t1.type() == TileType.WALL_END) valid = false;
					}
					if (valid) targets.add(f);
				}
		}

		this.filterTargets(floor, move, user, targets);
		if (move.range == MoveRange.Room || move.range == MoveRange.Floor)
			targets.sort((DungeonPokemon p1, DungeonPokemon p2) -> floor.dungeon.compare(p1, p2));

		return targets.toArray(new DungeonPokemon[targets.size()]);
	}

	/** Main method called when a Pok�mon uses a Move on a target. */
	public void mainUse(MoveUse usedMove, DungeonPokemon target, Floor floor, ArrayList<DungeonEvent> events)
	{
		Move move = usedMove.move.move();
		boolean missed = this.misses(usedMove, target, floor);
		float effectiveness = move.type == null ? PokemonType.NORMALLY_EFFECTIVE : move.type.effectivenessOn(target.species());
		if (effectiveness == PokemonType.NO_EFFECT) events.add(new MessageEvent(floor, move.unaffectedMessage(target)));
		else
		{
			if (!missed && this != MoveEffects.Basic_attack) target.receiveMove(usedMove.move.isLinked() ? DungeonPokemon.LINKED_MOVES : DungeonPokemon.MOVES);
			if (move.power != -1)
			{
				if (effectiveness == PokemonType.SUPER_EFFECTIVE)
					events.add(new MessageEvent(floor, new Message("move.effectiveness.super").addReplacement("<pokemon>", target.getNickname())));
				else if (effectiveness == PokemonType.NOT_VERY_EFFECTIVE)
					events.add(new MessageEvent(floor, new Message("move.effectiveness.not_very").addReplacement("<pokemon>", target.getNickname())));
				this.useOn(usedMove, target, floor, missed, events);
			}
		}
	}

	/** @param usedMove - The Move used.
	 * @param target - The Pok�mon receiving the Move.
	 * @param floor - The Floor context.
	 * @return True if this Move misses. */
	public boolean misses(MoveUse usedMove, DungeonPokemon target, Floor floor)
	{
		if (usedMove.user == target) return false;

		int accuracy = usedMove.move.move().accuracy;

		double userAccuracy = this.accuracyStat(usedMove, target, floor);
		double evasion = this.evasionStat(usedMove, target, floor);

		accuracy = (int) (accuracy * userAccuracy * evasion);

		return floor.random.nextDouble() * 100 > accuracy;
	}

	protected int movePower(MoveUse move, DungeonPokemon user, DungeonPokemon target, Floor floor)
	{
		return move.move.move().power + move.move.getAddedLevel();
	}

	public void prepareUse(MoveUse move, Floor floor, ArrayList<DungeonEvent> events)
	{
		Move m = move.move.move();

		DungeonPokemon[] pokemon = this.getTargets(m, move.user, floor);
		for (int i = 0; i < pokemon.length; ++i)
			events.add(new MoveUseEvent(floor, move, pokemon[i]));

		if (events.size() == 0 && this != MoveEffects.Basic_attack) events.add(new MessageEvent(floor, new Message("move.no_target")));
	}

	protected void useOn(MoveUse usedMove, DungeonPokemon target, Floor floor, boolean missed, ArrayList<DungeonEvent> events)
	{
		if (missed) events.add(new MessageEvent(floor, new Message("move.miss").addReplacement("<pokemon>", target.getNickname())));
		else if (usedMove.move.move().category != MoveCategory.Status)
			events.add(new DamageDealtEvent(floor, target, usedMove, this.damageDealt(usedMove, usedMove.user, target, floor, events)));
	}

}
