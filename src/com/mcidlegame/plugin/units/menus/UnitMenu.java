package com.mcidlegame.plugin.units.menus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.mcidlegame.plugin.ItemUtils;
import com.skitskurr.inventorywrapper.InventoryWrapper;

public class UnitMenu extends InventoryWrapper {

	private final Inventory inventory;
	private final Consumer<Player> remove;
	private Consumer<Player> upgrade;

	public UnitMenu(final String name, final Consumer<Player> remove) {
		this.inventory = Bukkit.createInventory(null, 27, name);

		this.remove = remove;

		final ItemStack removeItem = new ItemStack(Material.REDSTONE);
		ItemUtils.setName(removeItem, ChatColor.RED + "Remove from slot.");
		this.inventory.setItem(16, removeItem);
	}

	public void setUpgradeLevel(final int level, final Map<Material, Integer> upgradeCost,
			final Consumer<Player> upgrade) {
		final List<String> lore = new ArrayList<>(upgradeCost.size());
		for (final Entry<Material, Integer> cost : upgradeCost.entrySet()) {
			lore.add(ChatColor.GRAY + CraftItemStack.asNMSCopy(new ItemStack(cost.getKey())).getName() + ": "
					+ cost.getValue());
		}

		final ItemStack levelUp = new ItemStack(Material.SIGN);
		ItemUtils.setNameAndLore(levelUp, "Upgrade to level " + level, lore);

		this.inventory.setItem(13, levelUp);

		this.upgrade = upgrade;
	}

	@Override
	protected Inventory getInventory(final Player player) {
		return this.inventory;
	}

	@Override
	protected void clicked(final Player player, final ItemStack item, final int position) {
		if (position == 13 && this.upgrade != null) {
			this.upgrade.accept(player);
		} else if (position == 16) {
			this.remove.accept(player);
		}
	}

}
