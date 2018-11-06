package com.mcidlegame.plugin.units;

import java.util.Collections;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.Metadatable;

import com.mcidlegame.plugin.units.spawner.Spawner;

public abstract class Unit {

	private final String name;
	protected final Spawner spawner;
	private final int level;
	protected Metadatable unit;

	public Unit(final String name, final Spawner spawner, final int level) {
		this.name = name;
		this.spawner = spawner;
		this.level = level;
	}

	public final void spawn() {
		this.unit = this.spawner.spawn();
		onSpawn();
	}

	public final void remove() {
		this.spawner.remove();
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
