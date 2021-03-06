package com.darkxell.common.ai.states;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.AI.AIState;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.turns.GameTurn;
import com.darkxell.common.pokemon.LearnedMove;

public class AIStateChargedAttack extends AIState
{

	public final int moveID;

	public AIStateChargedAttack(AI ai, int moveID)
	{
		super(ai);
		this.moveID = moveID;
	}

	@Override
	public DungeonEvent takeAction()
	{
		LearnedMove m = null;
		GameTurn last = this.ai.floor.dungeon.getLastTurn();
		for (DungeonEvent e : last.events())
			if (e instanceof MoveSelectionEvent && ((MoveSelectionEvent) e).usedMove().user == this.ai.pokemon) m = ((MoveSelectionEvent) e).usedMove().move;
		LearnedMove move = new LearnedMove(this.moveID);
		if (m != null) move.setAddedLevel(m.getAddedLevel());
		return new MoveSelectionEvent(this.ai.floor, move, this.ai.pokemon);
	}

}
