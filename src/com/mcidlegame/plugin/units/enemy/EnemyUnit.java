package com.mcidlegame.plugin.units.enemy;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.IntUnaryOperator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.mcidlegame.plugin.ItemUtils;
import com.mcidlegame.plugin.Main;
import com.mcidlegame.plugin.WorldUtils;
import com.mcidlegame.plugin.data.RoomListeners;
import com.mcidlegame.plugin.data.UnitType;
import com.mcidlegame.plugin.units.Unit;
import com.mcidlegame.plugin.units.ally.Damager;
import com.mcidlegame.plugin.units.events.CalculateLootEvent;
import com.mcidlegame.plugin.units.events.DropLootEvent;
import com.mcidlegame.plugin.units.events.EnemyUnitDamagedByDamagerEvent;
import com.mcidlegame.plugin.units.events.EnemyUnitDeathEvent;
import com.mcidlegame.plugin.units.spawner.Spawner;

public abstract class EnemyUnit extends Unit {

	public static final String metaString = "enemyUnit";
	// TODO: find an appropriate growth value
	private static final IntUnaryOperator healthGrowth = n -> (int) (10 * Math.pow(1.2, n - 1));
	private static final DoubleBinaryOperator lootGrowth = (n, m) -> n * Math.pow(1.25, m - 1);
	protected static final Map<Material, Double> lootMap = new HashMap<>();
	private int maxHealth;
	private final Location dropLocation;
	private final RoomListeners listeners;
	private int health;
	private BossBar healthbar;
	private final double healthModifier;

	public EnemyUnit(final UnitType type, final int level, final Spawner spawner, final Consumer<Player> remove,
			final double healthModifier, final RoomListeners listeners) {
		super(type, level, spawner, remove);
		this.healthModifier = healthModifier;
		this.dropLocation = spawner.getDropLocation().clone();
		this.listeners = listeners;
		this.maxHealth = this.health = (int) (healthGrowth.applyAsInt(level) * healthModifier);
		this.healthbar = Bukkit.createBossBar("Health: " + this.health, BarColor.RED, BarStyle.SEGMENTED_10);

		initLootMap();
	}

	protected abstract void initLootMap();

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
		removeHealthbar();
	}

	public void hit(final Damager damager) {
		final EnemyUnitDamagedByDamagerEvent event = new EnemyUnitDamagedByDamagerEvent(this, damager,
				damager.getDamage());
		this.listeners.listen(event);
		this.health -= event.getDamage();
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
		if (this.healthbar == null) {
			return;
		}
		this.healthbar.removePlayer(player);
	}

	public void removeHealthbar() {
		this.healthbar.removeAll();
	}

	private void die() {
		this.listeners.listen(new EnemyUnitDeathEvent(this));
		this.spawner.kill();
		for (final Entry<Material, Double> entry : lootMap.entrySet()) {
			final Material type = entry.getKey();
			double dropChance = lootGrowth.applyAsDouble(entry.getValue(), this.level);
			final CalculateLootEvent calculateEvent = new CalculateLootEvent(this, type, dropChance);
			this.listeners.listen(calculateEvent);
			dropChance = calculateEvent.getDropChance();

			int fixAmount = (int) dropChance;
			if ((dropChance - fixAmount) > Math.random()) {
				fixAmount++;
			}

			final ItemStack[] loot = ItemUtils.splitIntoStacks(type, fixAmount);
			final DropLootEvent dropEvent = new DropLootEvent(this, loot);
			this.listeners.listen(dropEvent);
			WorldUtils.dropItems(this.dropLocation, loot);
		}
		removeHealthbar();
	}

	public boolean isDead() {
		return this.spawner.isDead();
	}

	@Override
	protected void onUpgrade() {
		this.maxHealth = this.health = (int) (healthGrowth.applyAsInt(this.level) * this.healthModifier);
	}
}
