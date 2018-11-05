package com.mcidlegame.plugin.units.friend;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;

public class SnowmanUnit extends ShooterUnit {

	public SnowmanUnit(final Location location, final int level) {
		super("Snowman", EntityType.SNOWMAN, location, level, 20L, Snowball.class, 1.0);
	}

	@Override
	protected ItemStack getBaseItem() {
		return new ItemStack(Material.SNOW_BLOCK);
	}

}
