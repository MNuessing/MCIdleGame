package com.mcidlegame.plugin;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerUtils {

	public static void decreaseItem(final Player player) {
		final PlayerInventory inventory = player.getInventory();
		final ItemStack item = inventory.getItemInMainHand();
		item.setAmount(item.getAmount() - 1);
		inventory.setItemInMainHand(item);
	}

	/**
	 * If the player has the given amount of the given items they will get removed
	 * and true is returned, otherwise no changes are made and false is returned
	 *
	 * @param player  the player which items are to be removed
	 * @param removed a map containing the material and amount of each item to
	 *                remove
	 * @return
	 */
	public static boolean removeItems(final Player player, final Map<Material, Integer> removed) {
		final Inventory inventory = player.getInventory();
		final Map<ItemStack, Integer> payment = new IdentityHashMap<>();

		for (final ItemStack item : inventory.getContents()) {
			if (item == null) {
				continue;
			}

			final Material type = item.getType();
			final Integer amount = removed.get(type);
			int intAmount;
			if (amount == null || (intAmount = amount.intValue()) == 0) {
				continue;
			}
			final int itemAmount = item.getAmount();
			final int stackAmount = Math.min(intAmount, itemAmount);
			payment.put(item, itemAmount - stackAmount);
			removed.put(type, intAmount - stackAmount);
		}

		for (final Integer remaining : removed.values()) {
			if (remaining.intValue() != 0) {
				return false;
			}
		}

		for (final Entry<ItemStack, Integer> entry : payment.entrySet()) {
			entry.getKey().setAmount(entry.getValue());
		}
		return true;
	}

}
