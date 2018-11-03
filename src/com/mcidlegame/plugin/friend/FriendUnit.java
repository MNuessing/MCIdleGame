package com.mcidlegame.plugin.friend;

public class FriendUnit {
	// TODO: write lvl / wave in file
	private static int lvl = 1;
	private static int damage;

	public FriendUnit() {
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
