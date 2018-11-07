package com.mcidlegame.plugin.units.events;

import com.mcidlegame.plugin.units.ally.Damager;
import com.mcidlegame.plugin.units.enemy.EnemyUnit;

public class EnemyUnitDamagedByDamagerEvent extends EnemyUnitEvent {

	private final Damager damager;
	private int damage;

	public EnemyUnitDamagedByDamagerEvent(final EnemyUnit unit, final Damager damager, final int damage) {
		super(unit);
		this.damager = damager;
		this.damage = damage;
	}

	public Damager getDamager() {
		return this.damager;
	}

	public int getDamage() {
		return this.damage;
	}

	public void setDamage(final int damage) {
		this.damage = damage;
	}

}
