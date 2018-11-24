package com.mcidlegame.plugin.units;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.metadata.Metadatable;

import com.mcidlegame.plugin.PlayerUtils;
import com.mcidlegame.plugin.WorldUtils;
import com.mcidlegame.plugin.data.UnitData;
import com.mcidlegame.plugin.data.UnitType;
import com.mcidlegame.plugin.units.menus.UnitMenu;
import com.mcidlegame.plugin.units.spawner.Spawner;

public abstract class Unit {

	private final UnitType type;
	protected int level;
	protected final Spawner spawner;
	protected Metadatable unit;
	protected final UpgradeCost upgradeCost;
	private final UnitMenu menu;

	public Unit(final UnitType type, final int level, final Spawner spawner) {
		this.type = type;
		this.level = level;
		this.spawner = spawner;

		this.upgradeCost = new UpgradeCost();
		initUpgradeCost();

		this.menu = new UnitMenu(type.name());
		setUpgradeLevel();
	}

	public final void spawn() {
		this.unit = this.spawner.spawn();
		onSpawn();
	}

	public final void remove() {
		this.spawner.remove();
		onRemove();
	}

	public final void openMenu(final Player player) {
		this.menu.openToPlayer(player);
	}

	public UnitType getUnitType() {
		return this.type;
	}

	public int getLevel() {
		return this.level;
	}

	public void upgradeLevel() {
		this.level++;
		this.spawner.setLevel(this.level);
		setUpgradeLevel();
		onUpgrade();
	}

	private void setUpgradeLevel() {
		this.menu.setUpgradeLevel(this.level + 1, getUpgradeCost(), this::upgrade);
	}

	public Map<Material, Integer> getUpgradeCost() {
		return this.upgradeCost.getUpgradeCosts(this.level);
	}

	private void upgrade(final Player player) {
		if (PlayerUtils.removeItems(player, getUpgradeCost())) {
			upgradeLevel();
			final Block commandBlock = this.spawner.getDropLocation().subtract(0, 2, 0).getBlock();
			WorldUtils.setCommand(commandBlock, (new UnitData(this)).toString());
		}
	}

	public boolean isRemoveable() {
		return true;
	}

	protected abstract void initUpgradeCost();

	protected abstract void onRemove();

	protected abstract void onSpawn();

	protected abstract void onUpgrade();
}
