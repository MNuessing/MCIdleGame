package com.mcidlegame.plugin.units;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.metadata.Metadatable;

import com.mcidlegame.plugin.data.UnitType;
import com.mcidlegame.plugin.units.spawner.Spawner;

public abstract class Unit {

	private final UnitType type;
	protected int level;
	protected final Spawner spawner;
	protected Metadatable unit;
	protected UpgradeCost upgradeCost;

	public Unit(final UnitType type, final int level, final Spawner spawner) {
		this.type = type;
		this.level = level;
		this.spawner = spawner;
		setUpgradeCost();
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

	public void upgradeLevel() {
		this.level++;
		this.spawner.setLevel(this.level);
		onUpgrade();
	}

	public Map<Material, Integer> getUpgradeCost() {
		return this.upgradeCost.getUpgradeCosts(this.level);
	}

	protected abstract void setUpgradeCost();

	protected abstract void onRemove();

	protected abstract void onSpawn();

	protected abstract void onUpgrade();
}
