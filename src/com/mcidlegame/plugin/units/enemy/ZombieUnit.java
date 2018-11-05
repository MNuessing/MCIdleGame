package com.mcidlegame.plugin.units.enemy;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class ZombieUnit extends EnemyUnit {

	public ZombieUnit(final Location location, final int level, final Runnable deathHandler) {
		super("Zombie", EntityType.ZOMBIE, location, level, 1.0, deathHandler);
	}

	@Override
	protected ItemStack getBaseItem() {
		return new ItemStack(Material.SKULL_ITEM, 1, (byte) 2);
	}

}
