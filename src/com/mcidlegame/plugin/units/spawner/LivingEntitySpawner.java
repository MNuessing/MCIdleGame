package com.mcidlegame.plugin.units.spawner;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.Metadatable;

public class LivingEntitySpawner implements Spawner {

	private final String name;
	private final Location location;
	private final EntityType type;
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
		this.entity.setAI(false);
		this.entity.setCustomName(this.name);
		this.entity.setCustomNameVisible(true);
		return this.entity;
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

	public LivingEntity getEntity() {
		return this.entity;
	}

	@Override
	public Location getLoacation() {
		return this.location;
	}

}
