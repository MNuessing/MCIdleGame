package com.mcidlegame.plugin.units.spawner;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.Metadatable;

public class LivingEntitySpawner implements Spawner {

	private final String name;
	private int level;
	private final Location location;
	private final EntityType type;
	private ItemStack[] armorContents = {};
	private LivingEntity entity;

	public LivingEntitySpawner(final String name, final int level, final Location location, final EntityType type) {
		this.name = name;
		this.level = level;
		this.location = location;
		this.type = type;
	}

	@Override
	public Metadatable spawn() {
		this.entity = (LivingEntity) this.location.getWorld().spawnEntity(this.location, this.type);
		this.entity.getEquipment().clear();
		this.entity.getEquipment().setArmorContents(this.armorContents);
		this.entity.setAI(false);
		this.entity.setCustomName(this.name + " level: " + this.level);
		this.entity.setCustomNameVisible(true);
		return this.entity;
	}

	public LivingEntity getEntity() {
		return this.entity;
	}

	public void setArmorContents(final ItemStack[] armorContents) {
		this.armorContents = armorContents;
	}

	@Override
	public void kill() {
		if (this.entity != null) {
			this.entity.setHealth(0);
		}
	}

	@Override
	public void remove() {
		if (this.entity != null) {
			this.entity.remove();
		}
	}

	@Override
	public boolean isDead() {
		if (this.entity == null) {
			return true;
		}
		return this.entity.isDead();
	}

	@Override
	public Chunk getChunk() {
		return this.location.getChunk();
	}

	@Override
	public Location getDropLocation() {
		return this.location.clone();
	}

	@Override
	public void setLevel(final int level) {
		this.level = level;
		if (this.entity != null && !this.entity.isDead()) {
			this.entity.setCustomName(this.name + " level: " + level);
		}
	}

}
