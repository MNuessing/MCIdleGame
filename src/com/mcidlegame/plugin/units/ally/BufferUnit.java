package com.mcidlegame.plugin.units.ally;

import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.mcidlegame.plugin.Main;
import com.mcidlegame.plugin.data.RoomListeners;
import com.mcidlegame.plugin.data.UnitType;
import com.mcidlegame.plugin.units.spawner.Spawner;

public abstract class BufferUnit extends AllyUnit {

	public static final String metaString = "bufferUnit";

	protected final RoomListeners listeners;

	public BufferUnit(final UnitType type, final int level, final Spawner spawner, final Consumer<Player> remove,
			final RoomListeners listeners) {
		super(type, level, spawner, remove);

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
