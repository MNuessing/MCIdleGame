package com.mcidlegame.plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {

	public static ItemStack setName(final ItemStack item, final String name) {
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack setLore(final ItemStack item, final List<String> lore) {
		final ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack setLore(final ItemStack item, final String lore) {
		return setLore(item, Collections.singletonList(lore));
	}

	public static ItemStack setLore(final ItemStack item, final String... lore) {
		return setLore(item, Arrays.asList(lore));
	}

	public static ItemStack setNameAndLore(final ItemStack item, final String name, final List<String> lore) {
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack setNameAndLore(final ItemStack item, final String name, final String lore) {
		return setNameAndLore(item, name, Collections.singletonList(lore));
	}

	public static ItemStack setNameAndLore(final ItemStack item, final String name, final String... lore) {
		return setNameAndLore(item, name, Arrays.asList(lore));
	}

	public static ItemStack[] splitIntoStacks(final Material type, final int amount) {
		if (amount == 0) {
			return new ItemStack[0];
		}
		final int maxStackSize = type.getMaxStackSize();
		final int max = amount / maxStackSize;
		final int remainder = amount % maxStackSize;
		final ItemStack[] items = new ItemStack[remainder == 0 ? max : max + 1];

		for (int i = 0; i < max; i++) {
			items[i] = new ItemStack(type, maxStackSize);
		}
		if (remainder != 0) {
			items[max] = new ItemStack(type, remainder);
		}

		return items;
	}

	public static List<String> getLore(final Map<Material, Integer> costs) {
		final List<String> lore = new ArrayList<>(costs.size());
		for (final Entry<Material, Integer> cost : costs.entrySet()) {
			lore.add(ChatColor.GRAY + CraftItemStack.asNMSCopy(new ItemStack(cost.getKey())).getName() + ": "
					+ cost.getValue());
		}
		return lore;
	}
}
