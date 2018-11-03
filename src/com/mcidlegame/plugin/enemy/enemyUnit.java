package com.mcidlegame.plugin.enemy;

public class enemyUnit {
	// TODO: write lvl / wave in file
	private static int lvl = 1;
	private static int hp;

	enemyUnit() {
		calculateHp();
	}

	public static void increaseLvl() {
		lvl++;
		calculateHp();
	}

	private static void calculateHp() {
		hp = lvl ^ 2;
	}

	public boolean hit(final int dmg) {
		enemyUnit.hp -= dmg;
		if (enemyUnit.hp > 0) {
			return false;
		}
		return true;
	}
}
