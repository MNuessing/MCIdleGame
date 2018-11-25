package com.mcidlegame.plugin.units.ally;

import java.util.function.Consumer;

import org.bukkit.entity.Player;

import com.mcidlegame.plugin.data.UnitType;
import com.mcidlegame.plugin.units.Unit;
import com.mcidlegame.plugin.units.spawner.Spawner;

public abstract class AllyUnit extends Unit {

	public AllyUnit(final UnitType type, final int level, final Spawner spawner, final Consumer<Player> remove) {
		super(type, level, spawner, remove);
	}

}
