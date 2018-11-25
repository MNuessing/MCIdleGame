package com.mcidlegame.plugin.units.menus;

import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.mcidlegame.plugin.ItemUtils;
import com.mcidlegame.plugin.units.UpgradeCost;
import com.skitskurr.inventorywrapper.ClickEvent;
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
		final ItemStack levelUp = new ItemStack(Material.SIGN);
		UpgradeCost.setUpgradeLevel(level, levelUp, upgradeCost);

		this.inventory.setItem(13, levelUp);

		this.upgrade = upgrade;
	}

	@Override
	protected Inventory getInventory(final Player player) {
		return this.inventory;
	}

	@Override
	protected void clicked(final ClickEvent event) {
		final int slot = event.getSlot();
		if (slot == 13 && this.upgrade != null) {
			this.upgrade.accept(event.getPlayer());
		} else if (slot == 16) {
			this.remove.accept(event.getPlayer());
		}
	}

}
