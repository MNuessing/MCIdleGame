package com.mcidlegame.plugin.units;

import java.util.Collections;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Unit {

	private final String name;
	private final EntityType type;
	private final Location location;
	private final int level;
	protected LivingEntity entity;

	public Unit(final String name, final EntityType type, final Location location, final int level) {
		this.name = name;
		this.type = type;
		this.location = location;
		this.level = level;
	}

	public final void spawn() {
		this.entity = (LivingEntity) this.location.getWorld().spawnEntity(this.location, this.type);
		this.entity.setAI(false);
		this.entity.setCustomName(this.name + " Level: " + this.level);
		this.entity.setCustomNameVisible(true);
		onSpawn();
	}

	public final void remove() {
		this.entity.remove();
		onRemove();
	}

	public String getName() {
		return this.name;
	}

	public int getLevel() {
		return this.level;
	}

	@Override
	public String toString() {
		return this.name + ";" + this.level;
	}

	public final ItemStack toItem() {
		final ItemStack item = getBaseItem();
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(this.name);
		meta.setLore(Collections.singletonList("Level: " + this.level));
		item.setItemMeta(meta);
		return item;
	}

	protected abstract ItemStack getBaseItem();

	protected abstract void onRemove();

	protected abstract void onSpawn();

}
