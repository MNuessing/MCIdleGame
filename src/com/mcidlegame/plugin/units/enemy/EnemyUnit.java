package com.mcidlegame.plugin.units.enemy;

import java.util.function.IntUnaryOperator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import com.mcidlegame.plugin.Main;
import com.mcidlegame.plugin.units.Unit;
import com.mcidlegame.plugin.units.spawner.Spawner;

public abstract class EnemyUnit extends Unit {

	public static final String metaString = "enemyUnit";
	// TODO: find an appropriate growth value
	private static final IntUnaryOperator healthGrowth = n -> (int) (10 * Math.pow(1.2, n - 1));
	private final int maxHealth;
	private final Runnable deathHandler;
	private LivingEntity entity;
	private int health;
	private BossBar healthbar;

	public EnemyUnit(final String name, final Spawner spawner, final int level, final double healthModifier,
			final Runnable deathHandler) {
		super(name, spawner, level);
		this.deathHandler = deathHandler;
		this.maxHealth = this.health = (int) (healthGrowth.applyAsInt(level) * healthModifier);
		this.healthbar = Bukkit.createBossBar("Health: " + this.health, BarColor.RED, BarStyle.SEGMENTED_10);
	}

	@Override
	protected void onSpawn() {
		this.unit.setMetadata(metaString, new FixedMetadataValue(Main.main, this));
		this.health = this.maxHealth;
		this.healthbar = Bukkit.createBossBar("Health: " + this.health, BarColor.RED, BarStyle.SEGMENTED_10);
		for (final Entity nearby : this.spawner.getChunk().getEntities()) {
			if (nearby instanceof Player) {
				this.healthbar.addPlayer((Player) nearby);
			}
		}
	}

	@Override
	protected void onRemove() {
		if (!this.entity.isDead()) {
			removeHealthbar();
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

	public void removeHealthbar(final Player player) {
		this.healthbar.removePlayer(player);
	}

	public void removeHealthbar() {
		this.healthbar.removeAll();
	}

	private void die() {
		this.spawner.kill();
		removeHealthbar();
		this.deathHandler.run();
	}

	public boolean isDead() {
		return this.spawner.isDead();
	}

	public static EnemyUnit fromString(final String line, final Location location, final Runnable deathHandler) {
		final String[] args = line.split(";");
		return createUnit(args[0], Integer.parseInt(args[1]), location, deathHandler);
	}

	public static EnemyUnit fromItem(final ItemStack item, final Location location, final Runnable deathHandler) {
		final ItemMeta meta = item.getItemMeta();
		final String name = meta.getDisplayName();
		final String levelString = meta.getLore().get(0);
		final int level = Integer.parseInt(levelString.substring(7, levelString.length()));
		return createUnit(name, level, location, deathHandler);
	}

	private static EnemyUnit createUnit(final String name, final int level, final Location location,
			final Runnable deathHandler) {
		switch (name) {
		case "Zombie":
			return new ZombieUnit(location, level, deathHandler);
		}
		return null;
	}
}
