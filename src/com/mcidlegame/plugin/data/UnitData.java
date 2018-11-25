package com.mcidlegame.plugin.data;

import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mcidlegame.plugin.ItemUtils;
import com.mcidlegame.plugin.units.Unit;
import com.mcidlegame.plugin.units.ally.AllyUnit;
import com.mcidlegame.plugin.units.ally.ItemCollectorChest;
import com.mcidlegame.plugin.units.ally.SnowmanUnit;
import com.mcidlegame.plugin.units.enemy.EnemyUnit;
import com.mcidlegame.plugin.units.enemy.ZombieUnit;

public class UnitData {

	public static UnitData fromItem(final ItemStack item) {
		if (item == null) {
			return null;
		}
		final ItemMeta meta = item.getItemMeta();
		if (meta == null) {
			return null;
		}
		if (!meta.hasDisplayName()) {
			return null;
		}
		final List<String> lore = meta.getLore();
		if (lore.size() != 1) {
			return null;
		}
		final UnitType type = UnitType.getFromItemName(meta.getDisplayName());
		if (type == null) {
			return null;
		}
		final String levelString = lore.get(0);
		final int level = Integer.parseInt(levelString.substring(9, levelString.length()));
		return new UnitData(type, level);
	}

	public static UnitData fromString(final String arg) {
		final JsonParser parser = new JsonParser();
		final JsonObject jsonObject = parser.parse(arg).getAsJsonObject();
		final UnitType type = UnitType.valueOf(jsonObject.get("type").getAsString());
		final int level = Integer.parseInt(jsonObject.get("level").getAsString());
		return new UnitData(type, level);
	}

	private final UnitType type;
	private final int level;

	public UnitData(final UnitType type, final int level) {
		this.type = type;
		this.level = level;
	}

	public UnitData(final Unit unit) {
		this.type = unit.getUnitType();
		this.level = unit.getLevel();
	}

	public UnitTeam getUnitTeam() {
		return this.type.getUnitTeam();
	}

	@Override
	public String toString() {
		return "{ type: \"" + this.type + "\"; level: \"" + this.level + "\"}";
	}

	public ItemStack toItem() {
		return ItemUtils.setLore(this.type.getBaseItem(), "§7Level: " + this.level);
	}

	public EnemyUnit toEnemyUnit(final Location location, final Consumer<Player> remove,
			final RoomListeners listeners) {
		if (this.type.getUnitTeam() != UnitTeam.ENEMY) {
			throw new IllegalStateException("Unit is not an enemy.");
		}
		switch (this.type) {
		case ZOMBIE:
			return new ZombieUnit(this.level, location, remove, listeners);
		default:
			throw new IllegalStateException("UnitData was not convertable to enemy.");
		}
	}

	public AllyUnit toAllyUnit(final Location location, final Consumer<Player> remove, final RoomListeners listeners) {
		if (this.type.getUnitTeam() != UnitTeam.ALLY) {
			throw new IllegalStateException("Unit is not an ally.");
		}
		switch (this.type) {
		case SNOWMAN:
			return new SnowmanUnit(this.level, location, remove);
		case LOOT_CHEST:
			return new ItemCollectorChest(this.level, location, remove, listeners);
		default:
			throw new IllegalStateException("UnitData was not convertable to ally.");
		}
	}

}
