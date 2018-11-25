package com.mcidlegame.plugin.units;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.mcidlegame.plugin.ItemUtils;

public class UpgradeCost {
	private final Map<Material, UpgradeHandler> upgradeCost;

	public UpgradeCost() {
		this.upgradeCost = new HashMap<>();
	}

	public boolean addUpgradeCost(final Material material, final UpgradeHandler upgradeHandler) {
		if (this.upgradeCost.containsKey(material)) {
			return false;
		}
		this.upgradeCost.put(material, upgradeHandler);

		if (this.upgradeCost.containsKey(material)) {
			return true;
		}
		return false;
	}

	public Map<Material, Integer> getUpgradeCosts(final int level) {
		final Map<Material, Integer> concreteUpgradeCost = new HashMap<>();
		for (final Entry<Material, UpgradeHandler> entry : this.upgradeCost.entrySet()) {
			final int upgradeCostValue = (int) (entry.getValue().getUpgradeCosts(level));
			concreteUpgradeCost.put(entry.getKey(), upgradeCostValue);
		}

		return concreteUpgradeCost;
	}

	public static void setUpgradeLevel(final int level, final ItemStack item,
			final Map<Material, Integer> concreteUpgradeCost) {
		final List<String> lore = ItemUtils.getLore(concreteUpgradeCost);
		ItemUtils.setNameAndLore(item, "Upgrade to level " + level, lore);
	}
}
