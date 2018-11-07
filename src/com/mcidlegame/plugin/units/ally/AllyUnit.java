package com.mcidlegame.plugin.units.ally;

import com.mcidlegame.plugin.data.UnitType;
import com.mcidlegame.plugin.units.Unit;
import com.mcidlegame.plugin.units.spawner.Spawner;

public abstract class AllyUnit extends Unit {

	public AllyUnit(final UnitType type, final int level, final Spawner spawner) {
		super(type, level, spawner);
	}

}
