package com.mcidlegame.plugin.friend;

import java.util.function.IntUnaryOperator;

public abstract class AllyUnit {
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
