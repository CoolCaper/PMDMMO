package com.darkxell.common.event.dungeon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.status.ActiveFloorStatus;
import com.darkxell.common.util.language.Message;

public class FloorStatusCreatedEvent extends DungeonEvent
{
	public final ActiveFloorStatus status;

	public FloorStatusCreatedEvent(Floor floor, ActiveFloorStatus status)
	{
		super(floor);
		this.status = status;
	}

	@Override
	public String loggerMessage()
	{
		return "Created " + this.status.status.name();
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		if (this.floor.hasStatus(this.status.status))
		{
			Message m = new Message("status.floor.already").addReplacement("<status>", this.status.status.name());
			this.resultingEvents.add(new MessageEvent(this.floor, m));
		} else
		{
			this.floor.addFloorStatus(this.status);
			Message m = this.status.startMessage();
			if (m != null) this.messages.add(m);
			this.status.onStatusStart(floor, this.resultingEvents);
		}
		return super.processServer();
	}

}
