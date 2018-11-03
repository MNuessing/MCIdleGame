package com.mcidlegame.plugin.friend;

public abstract class AllyUnit {
	// TODO: write lvl / wave in file
	private final int level;
	private final int damage;

	public AllyUnit(final int level, final double damageModifier) {
		this.level = level;
		this.damage = (int) (level * damageModifier);
	}

	public int getDamage() {
		return this.damage;
	}

}
