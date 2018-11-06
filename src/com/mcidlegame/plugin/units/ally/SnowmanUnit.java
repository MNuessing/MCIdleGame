package com.mcidlegame.plugin.units.ally;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;

import com.mcidlegame.plugin.units.spawner.LivingEntitySpawner;

public class SnowmanUnit extends ShooterUnit {

	public SnowmanUnit(final Location location, final int level) {
		super("Snowman", new LivingEntitySpawner("Snowman level " + level, location, EntityType.SNOWMAN), level, 20L,
				Snowball.class, 1.0);
	}

	@Override
	protected ItemStack getBaseItem() {
		return new ItemStack(Material.SNOW_BLOCK);
	}

}
