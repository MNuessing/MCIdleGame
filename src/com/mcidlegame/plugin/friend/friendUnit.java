package com.mcidlegame.plugin.friend;

public class friendUnit {
	// TODO: write lvl / wave in file
	private static int lvl = 1;
	private static int damage;

	friendUnit() {
		calculateDamage();
	}

	public static void increaseLvl() {
		lvl++;
		calculateDamage();
	}

	private static void calculateDamage() {
		damage = lvl;
	}

	public int hit() {
		return damage;
	}
}
