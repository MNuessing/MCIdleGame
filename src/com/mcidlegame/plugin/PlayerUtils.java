package com.mcidlegame.plugin;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerUtils {

	public static void decreaseItem(final Player player) {
		final PlayerInventory inventory = player.getInventory();
		final ItemStack item = inventory.getItemInMainHand();
		item.setAmount(item.getAmount() - 1);
		inventory.setItemInMainHand(item);
	}

}
