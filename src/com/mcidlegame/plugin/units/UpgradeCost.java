package com.mcidlegame.plugin.units;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;

public class UpgradeCost {
	private final Map<Material, UpgradeHandler> upgradeCost;

	public UpgradeCost() {
		this.upgradeCost = new HashMap<>();
	}

	public boolean addUpgradeCost(final Material material, final UpgradeHandler upgradeHandler) {
		if (this.upgradeCost.containsValue(material)) {
			return false;
		}
		this.upgradeCost.put(material, upgradeHandler);

		if (this.upgradeCost.containsValue(material)) {
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
}
