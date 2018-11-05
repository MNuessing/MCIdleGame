package com.mcidlegame.plugin.units.ally;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.mcidlegame.plugin.units.Unit;

public abstract class AllyUnit extends Unit implements Damager {

	public AllyUnit(final String name, final EntityType type, final Location location, final int level) {
		super(name, type, location, level);
	}

	public static AllyUnit fromString(final String line, final Location location) {
		final String[] args = line.split(";");
		return createUnit(args[0], Integer.parseInt(args[1]), location);
	}

	public static AllyUnit fromItem(final ItemStack item, final Location location) {
		final ItemMeta meta = item.getItemMeta();
		final String name = meta.getDisplayName();
		final String levelString = meta.getLore().get(0);
		final int level = Integer.parseInt(levelString.substring(7, levelString.length()));
		return createUnit(name, level, location);
	}

	private static AllyUnit createUnit(final String name, final int level, final Location location) {
		switch (name) {
		case "Snowman":
			return new SnowmanUnit(location, level);
		}
		return null;
	}

}
