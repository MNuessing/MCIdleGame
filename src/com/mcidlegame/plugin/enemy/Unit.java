package com.mcidlegame.plugin.enemy;

public class Unit {
	// TODO: write lvl / wave in file
	private static int lvl = 1;
	private int hp;

	Unit() {
		this.hp = lvl ^ 2;
	}

	public static void increaseLvl() {
		lvl++;
	}

	public boolean hit(final int dmg) {
		this.hp -= dmg;
		if (this.hp > 0) {
			return false;
		}
		return true;
	}
}
