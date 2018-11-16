package com.mcidlegame.plugin.units.enemy;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import com.mcidlegame.plugin.data.RoomListeners;
import com.mcidlegame.plugin.data.UnitType;
import com.mcidlegame.plugin.units.UpgradeCost;
import com.mcidlegame.plugin.units.spawner.LivingEntitySpawner;

public class ZombieUnit extends EnemyUnit {

	public ZombieUnit(final int level, final Location location, final RoomListeners listeners) {
		super(UnitType.ZOMBIE, level, new LivingEntitySpawner("Zombie", level, location, EntityType.ZOMBIE), 1.0,
				listeners);
	}

	@Override
	protected void initLootMap() {
		lootMap.put(Material.GOLD_NUGGET, 0.4);
	}

	@Override
	protected void setUpgradeCost() {
		this.upgradeCost = new UpgradeCost();
		this.upgradeCost.addUpgradeCost(Material.GOLD_NUGGET, (level) -> {
			return 10 * Math.pow(1.5, level);
		});
	}

}
