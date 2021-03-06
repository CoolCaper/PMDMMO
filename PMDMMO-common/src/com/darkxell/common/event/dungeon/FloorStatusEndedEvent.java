package com.darkxell.common.event.dungeon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.status.ActiveFloorStatus;
import com.darkxell.common.util.language.Message;

public class FloorStatusEndedEvent extends DungeonEvent
{
	public final ActiveFloorStatus status;

	public FloorStatusEndedEvent(Floor floor, ActiveFloorStatus status)
	{
		super(floor);
		this.status = status;
	}

	@Override
	public String loggerMessage()
	{
		return this.status.status.name() + " finished.";
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		if (this.floor.hasStatus(this.status.status))
		{
			this.floor.removeFloorStatus(this.status);
			this.status.onStatusEnd(this.floor, this.resultingEvents);
			Message m = this.status.endMessage();
			if (m != null) this.messages.add(m);
		}
		return super.processServer();
	}

}
