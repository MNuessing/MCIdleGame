package com.mcidlegame.plugin.data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.mcidlegame.plugin.ItemUtils;

public enum UnitType {
	SNOWMAN("Snowman", UnitTeam.ALLY, Material.SNOW_BLOCK, (byte) 0, "Snowman Shooter"),
	ZOMBIE("Zombie", UnitTeam.ENEMY, Material.SKULL_ITEM, (byte) 2, "Zombie Spawner");

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

}
