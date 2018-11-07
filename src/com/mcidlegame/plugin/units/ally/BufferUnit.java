package com.mcidlegame.plugin.units.ally;

import org.bukkit.metadata.FixedMetadataValue;

import com.mcidlegame.plugin.Main;
import com.mcidlegame.plugin.data.RoomListeners;
import com.mcidlegame.plugin.units.spawner.Spawner;

public abstract class BufferUnit extends AllyUnit {

	public static final String metaString = "bufferUnit";

	protected final RoomListeners listeners;

	public BufferUnit(final String name, final Spawner spawner, final int level, final RoomListeners listeners) {
		super(name, spawner, level);

		this.listeners = listeners;
	}

	@Override
	protected void onRemove() {
		this.listeners.remove(this);
	}

	@Override
	protected void onSpawn() {
		this.unit.setMetadata(metaString, new FixedMetadataValue(Main.main, this));
		initListeners();
	}

	protected abstract void initListeners();

}
