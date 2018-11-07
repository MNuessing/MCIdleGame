package com.mcidlegame.plugin.units.events;

import org.bukkit.Material;

import com.mcidlegame.plugin.units.enemy.EnemyUnit;

public class CalculateLootEvent extends EnemyUnitEvent {

	private final Material type;
	private double dropChance;

	public CalculateLootEvent(final EnemyUnit unit, final Material type, final double dropChance) {
		super(unit);
		this.type = type;
		this.dropChance = dropChance;
	}

	public Material getMaterial() {
		return this.type;
	}

	public double getDropChance() {
		return this.dropChance;
	}

	public void setDropChance(final double dropChance) {
		this.dropChance = dropChance;
	}

}
