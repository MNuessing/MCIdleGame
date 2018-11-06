package com.mcidlegame.plugin.units.ally;

import java.util.function.IntUnaryOperator;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.mcidlegame.plugin.Main;

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

}
