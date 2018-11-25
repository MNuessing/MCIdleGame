package com.mcidlegame.plugin.units.ally;

import java.util.Map;
import java.util.function.IntUnaryOperator;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.mcidlegame.plugin.Main;
import com.mcidlegame.plugin.units.UpgradeCost;

public class PlayerUnit implements Damager {

	private static final IntUnaryOperator damageGrowth = n -> 2 * n;

	private final int level;
	private final int damage;

	public PlayerUnit(final int level) {
		this.level = level;
		this.damage = (damageGrowth.applyAsInt(level));
	}

	@Override
	public int getDamage() {
		return this.damage;
	}

	public static void apply(final Player player) {
		player.setMetadata(metaString, new FixedMetadataValue(Main.main, new PlayerUnit(player.getLevel())));
	}

	public Map<Material, Integer> getUpgradeCost() {
		final UpgradeCost upgradeCost = new UpgradeCost();
		upgradeCost.addUpgradeCost(Material.GOLD_NUGGET, (level) -> {
			return 10 * Math.pow(1.5, level);
		});
		return upgradeCost.getUpgradeCosts(this.level);
	}
}
