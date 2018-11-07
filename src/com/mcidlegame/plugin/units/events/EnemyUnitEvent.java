package com.mcidlegame.plugin.units.events;

import com.mcidlegame.plugin.units.enemy.EnemyUnit;

public abstract class EnemyUnitEvent {

	private final EnemyUnit unit;

	public EnemyUnitEvent(final EnemyUnit unit) {
		this.unit = unit;
	}

	public EnemyUnit getUnit() {
		return this.unit;
	}

}
