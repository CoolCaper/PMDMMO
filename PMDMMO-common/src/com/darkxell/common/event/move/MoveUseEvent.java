package com.darkxell.common.event.move;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.language.Message;

public class MoveUseEvent extends DungeonEvent
{

	public Direction direction;
	private boolean missed = false;
	/** The Targets of this Move. */
	public final DungeonPokemon target;
	/** The move that was used. */
	public final MoveUse usedMove;

	public MoveUseEvent(Floor floor, MoveUse move, DungeonPokemon target)
	{
		super(floor);
		this.usedMove = move;
		this.target = target;
		this.direction = null;
	}

	@Override
	public boolean isValid()
	{
		if (this.target != null) return !this.target.isFainted();
		return super.isValid();
	}

	@Override
	public String loggerMessage()
	{
		return this.target + " received the effect of " + this.usedMove.move.move().name();
	}

	public boolean missed()
	{
		return this.missed;
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		if (this.direction != null) this.usedMove.user.setFacing(this.direction);
		this.missed = this.usedMove.move.move().useOn(this.usedMove, this.target, this.flags(), this.floor, this.resultingEvents);
		if (this.resultingEvents.size() == 0) this.resultingEvents.add(new MessageEvent(this.floor, new Message("move.no_effect")));
		return super.processServer();
	}
}
