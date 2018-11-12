package com.mcidlegame.plugin.units.spawner;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.Metadatable;

public class LivingEntitySpawner implements Spawner {

	private final String name;
	private final Location location;
	private final EntityType type;
	private ItemStack[] armorContents = {};
	private LivingEntity entity;

	public LivingEntitySpawner(final String name, final Location location, final EntityType type) {
		this.name = name;
		this.location = location;
		this.type = type;
	}

	@Override
	public Metadatable spawn() {
		this.entity = (LivingEntity) this.location.getWorld().spawnEntity(this.location, this.type);
		this.entity.getEquipment().clear();
		this.entity.getEquipment().setArmorContents(this.armorContents);
		this.entity.setAI(false);
		this.entity.setCustomName(this.name);
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
	public Location getDropLacation() {
		return this.location;
	}

	@Override
	public void updateName(final String name) {
		if (this.entity != null && !this.entity.isDead()) {
			this.entity.setCustomName(name);
		}
	}

}
