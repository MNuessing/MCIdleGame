package com.mcidlegame.plugin.units.enemy;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.mcidlegame.plugin.data.RoomListeners;
import com.mcidlegame.plugin.units.spawner.LivingEntitySpawner;

public class ZombieUnit extends EnemyUnit {

	public ZombieUnit(final Location location, final int level, final RoomListeners listeners) {
		super("Zombie", new LivingEntitySpawner("Zombie Level " + level, location, EntityType.ZOMBIE), level, 1.0,
				listeners);
	}

	@Override
	protected ItemStack getBaseItem() {
		return new ItemStack(Material.SKULL_ITEM, 1, (byte) 2);
	}

	@Override
	protected void initLootMap() {
		lootMap.put(Material.GOLD_NUGGET, 0.4);
	}

}
