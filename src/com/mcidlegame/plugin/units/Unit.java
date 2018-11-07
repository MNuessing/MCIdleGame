package com.mcidlegame.plugin.units;

import org.bukkit.metadata.Metadatable;

import com.mcidlegame.plugin.data.UnitType;
import com.mcidlegame.plugin.units.spawner.Spawner;

public abstract class Unit {

	private final UnitType type;
	protected final int level;
	protected final Spawner spawner;
	protected Metadatable unit;

	public Unit(final UnitType type, final int level, final Spawner spawner) {
		this.type = type;
		this.level = level;
		this.spawner = spawner;
	}

	public final void spawn() {
		this.unit = this.spawner.spawn();
		onSpawn();
	}

	public final void remove() {
		this.spawner.remove();
		onRemove();
	}

	public UnitType getUnitType() {
		return this.type;
	}

	public int getLevel() {
		return this.level;
	}

	protected abstract void onRemove();

	protected abstract void onSpawn();

}
