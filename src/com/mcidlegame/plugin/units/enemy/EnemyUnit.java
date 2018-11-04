package com.mcidlegame.plugin.units.enemy;

import java.util.function.IntUnaryOperator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.mcidlegame.plugin.Game;
import com.mcidlegame.plugin.Main;
import com.mcidlegame.plugin.units.Unit;

public abstract class EnemyUnit extends Unit {

	public static final String roleString = "enemyUnit";
	// TODO: find an appropriate growth value
	private static final IntUnaryOperator healthGrowth = n -> (n * (n + 1)) / 2;
	// TODO: write lvl / wave in file
	private final LivingEntity entity;
	private final int level;
	private final int maxHealth;
	private int health;
	private final BossBar healthbar;

	public EnemyUnit(final LivingEntity entity, final int level, final double healthModifier) {
		this.entity = entity;
		this.level = level;
		this.maxHealth = this.health = (int) (healthGrowth.applyAsInt(level) * healthModifier);
		this.healthbar = Bukkit.createBossBar("Health: " + this.health, BarColor.RED, BarStyle.SEGMENTED_10);

		// TODO: do this differently
		for (final Player player : Bukkit.getOnlinePlayers()) {
			this.healthbar.addPlayer(player);
		}
	}

	public void hit(final int damage) {
		this.health -= damage;
		if (this.health > 0) {
			this.healthbar.setTitle("Health: " + this.health);
			this.healthbar.setProgress((1.0 / this.maxHealth) * this.health);
		} else {
			die();
		}
	}

	public void addToHealthbar(final Player player) {
		this.healthbar.addPlayer(player);
	}

	public void removeHealthbar() {
		this.healthbar.removeAll();
	}

	private void die() {
		this.entity.setHealth(0);
		this.healthbar.removeAll();

		new BukkitRunnable() {
			@Override
			public void run() {
				Game.spawnMonster(new Location(Bukkit.getWorld("world"), 0.5, 66, 0.5), EnemyUnit.this.level + 1);
			}
		}.runTaskLater(Main.main, 20L);
	}
}
