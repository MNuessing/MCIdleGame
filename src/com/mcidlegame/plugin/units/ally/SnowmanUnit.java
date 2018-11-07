package com.mcidlegame.plugin.units.ally;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowball;

import com.mcidlegame.plugin.data.UnitType;
import com.mcidlegame.plugin.units.spawner.LivingEntitySpawner;

public class SnowmanUnit extends ShooterUnit {

	public SnowmanUnit(final int level, final Location location) {
		super(UnitType.SNOWMAN, level, new LivingEntitySpawner("Snowman level " + level, location, EntityType.SNOWMAN),
				20L, Snowball.class, 1.0);
	}

}
