package com.mcidlegame.plugin.data;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.mcidlegame.plugin.ItemUtils;

public enum UnitType {
	SNOWMAN("Snowman", UnitTeam.ALLY, Material.SNOW_BLOCK, (byte) 0, "Snowman Shooter") {
		@Override
		public Map<Material, Integer> getBaseCost() {
			final Map<Material, Integer> baseCost = new HashMap<>();
			baseCost.put(Material.GOLD_NUGGET, 1000);
			return baseCost;
		}
	},
	ZOMBIE("Zombie", UnitTeam.ENEMY, Material.SKULL_ITEM, (byte) 2, "Zombie Spawner") {
		@Override
		public Map<Material, Integer> getBaseCost() {
			final Map<Material, Integer> baseCost = new HashMap<>();
			baseCost.put(Material.GOLD_NUGGET, 1000);
			return baseCost;
		}
	},
	LOOT_CHEST("Loot Chest", UnitTeam.ALLY, Material.CHEST, (byte) 0, "Loot Chest") {
		@Override
		public Map<Material, Integer> getBaseCost() {
			final Map<Material, Integer> baseCost = new HashMap<>();
			baseCost.put(Material.GOLD_NUGGET, 1000);
			return baseCost;
		}
	};

	private final String name;
	private final UnitTeam team;
	private final Material type;
	private final byte data;
	private final String itemName;

	private UnitType(final String name, final UnitTeam team, final Material type, final byte data,
			final String itemName) {
		this.name = name;
		this.team = team;
		this.type = type;
		this.data = data;
		this.itemName = itemName;
	}

	public String getName() {
		return this.name;
	}

	public UnitTeam getUnitTeam() {
		return this.team;
	}

	public ItemStack getBaseItem() {
		return ItemUtils.setName(new ItemStack(this.type, 1, this.data), this.itemName);
	}

	public static UnitType getFromItemName(final String name) {
		for (final UnitType type : values()) {
			if (type.itemName.equals(name)) {
				return type;
			}
		}
		return null;
	}

	public abstract Map<Material, Integer> getBaseCost();

	public Material getBaseType() {
		return this.type;
	}
}
