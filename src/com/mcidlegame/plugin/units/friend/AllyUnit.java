package com.mcidlegame.plugin.units.friend;

import java.util.function.IntUnaryOperator;

import com.mcidlegame.plugin.units.Unit;

public abstract class AllyUnit extends Unit {

	public static final String roleString = "allyUnit";
	// TODO: find an appropriate growth value
	private static final IntUnaryOperator damageGrowth = n -> n;
	// TODO: write lvl / wave in file
	private final int level;
	private final int damage;

	public AllyUnit(final int level, final double damageModifier) {
		this.level = level;
		this.damage = (int) (damageGrowth.applyAsInt(level) * damageModifier);
	}

	public int getDamage() {
		return this.damage;
	}

}
