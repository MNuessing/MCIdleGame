package com.mcidlegame.plugin.units.ally;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowball;

import com.mcidlegame.plugin.data.UnitType;
import com.mcidlegame.plugin.units.UpgradeCost;
import com.mcidlegame.plugin.units.spawner.LivingEntitySpawner;

public class SnowmanUnit extends ShooterUnit {

	public SnowmanUnit(final int level, final Location location) {
		super(UnitType.SNOWMAN, level, new LivingEntitySpawner("Snowman", level, location, EntityType.SNOWMAN), 20L,
				Snowball.class, 1.0);
	}

	@Override
	protected void setUpgradeCost() {
		this.upgradeCost = new UpgradeCost();
		this.upgradeCost.addUpgradeCost(Material.GOLD_NUGGET, (level) -> {
			return 10 * Math.pow(1.5, level);
		});
	}

}
