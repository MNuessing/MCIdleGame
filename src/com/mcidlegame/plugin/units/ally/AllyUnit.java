package com.mcidlegame.plugin.units.ally;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import com.mcidlegame.plugin.Main;
import com.mcidlegame.plugin.units.Unit;
import com.mcidlegame.plugin.units.spawner.Spawner;

public abstract class AllyUnit extends Unit implements Damager {

	public AllyUnit(final String name, final Spawner spawner, final int level) {
		super(name, spawner, level);
	}

	public static AllyUnit fromString(final String line, final Location location) {
		final String[] args = line.split(";");
		return createUnit(args[0], Integer.parseInt(args[1]), location);
	}

	@Override
	public void onSpawn() {
		this.unit.setMetadata(metaString, new FixedMetadataValue(Main.main, this));
	}

	public static AllyUnit fromItem(final ItemStack item, final Location location) {
		if (item == null) {
			return null;
		}
		// this is and "empty" hand
		if (item.getType() == Material.AIR) {
			return null;
		}
		final ItemMeta meta = item.getItemMeta();
		if (!meta.hasDisplayName()) {
			return null;
		}
		final String name = meta.getDisplayName();
		final List<String> lore = meta.getLore();
		if (lore.isEmpty()) {
			return null;
		}
		final String levelString = lore.get(0);
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
