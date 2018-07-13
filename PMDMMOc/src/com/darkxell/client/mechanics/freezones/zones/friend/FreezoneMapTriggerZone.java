package com.darkxell.client.mechanics.freezones.zones.friend;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.freezones.TriggerZone;
import com.darkxell.client.state.TransitionState;
import com.darkxell.client.state.menu.freezone.FriendAreaSelectionMapState;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.zones.FreezoneInfo;

public class FreezoneMapTriggerZone extends TriggerZone
{

	private FreezoneInfo zone;

	public FreezoneMapTriggerZone(FreezoneInfo zone, DoubleRectangle hitbox)
	{
		super(hitbox);
		this.zone = zone;
	}

	@Override
	public void onEnter()
	{
		FriendAreaSelectionMapState state = new FriendAreaSelectionMapState();
		state.lockOn(this.zone.maplocation);
		Persistance.stateManager.setState(new TransitionState(Persistance.stateManager.getCurrentState(), state));
	}

}
