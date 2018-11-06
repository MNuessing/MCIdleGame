package com.mcidlegame.plugin.units.spawner;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.Metadatable;

public class LivingEntitySpawner implements Spawner {

	private final Location location;
	private final EntityType type;
	private LivingEntity entity;

	public LivingEntitySpawner(final Location location, final EntityType type) {
		this.location = location;
		this.type = type;
	}

	@Override
	public Metadatable spawn() {
		this.entity = (LivingEntity) this.location.getWorld().spawnEntity(this.location, this.type);
		this.entity.getEquipment().clear();
		return this.entity;
	}

	@Override
	public void kill() {
		this.entity.setHealth(0);
	}

	@Override
	public void remove() {
		this.entity.remove();
	}

}
