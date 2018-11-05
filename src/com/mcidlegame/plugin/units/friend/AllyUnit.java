package com.mcidlegame.plugin.units.friend;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import com.mcidlegame.plugin.units.Damager;
import com.mcidlegame.plugin.units.Unit;

public abstract class AllyUnit extends Unit implements Damager {

	public AllyUnit(final String name, final EntityType type, final Location location, final int level) {
		super(name, type, location, level);
	}

}
