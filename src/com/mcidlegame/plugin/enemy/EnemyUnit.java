package com.mcidlegame.plugin.enemy;

public class EnemyUnit {
	// TODO: write lvl / wave in file
	private static int lvl = 1;
	private static int hp;

	public EnemyUnit() {
		calculateHp();
	}

	public static void increaseLvl() {
		lvl++;
		calculateHp();
	}

	private static void calculateHp() {
		hp = lvl ^ 2;
	}

	public int hit(final int dmg) {
		hp -= dmg;
		if (EnemyUnit.hp <= 0) {
			increaseLvl();
			return 0;
		}
		return hp;
	}
}
