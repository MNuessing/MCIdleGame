package com.mcidlegame.plugin.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mcidlegame.plugin.ItemUtils;
import com.skitskurr.inventorywrapper.implementations.StaticMenu;
import com.skitskurr.inventorywrapper.implementations.StaticMenu.IconPair;

public abstract class CraftingBenchMenuWrapper {

	private static final List<IconPair> icons = new ArrayList<>();
	private static StaticMenu menu;
	static {
		initializeMenu();
	}

	private static void initializeMenu() {
		createBuyUnit();
		createBuyPlayerUpgrade();
		createBuySkils();
		menu = new StaticMenu("Menu Overview", icons);
	}

	private static void createBuySkils() {
		final ItemStack iconStack = new ItemStack(Material.FIREWORK);
		ItemUtils.setName(iconStack, "Buy Unit");
		icons.add(new IconPair(iconStack, new Consumer<Player>() {
			@Override
			public void accept(final Player player) {
				BuyUnitMenuWrapper.openMenu(player);
			}
		}));
	}

	private static void createBuyPlayerUpgrade() {
		final ItemStack iconStack = new ItemStack(Material.SKULL_ITEM);
		ItemUtils.setName(iconStack, "Player Stuff");
		icons.add(new IconPair(iconStack, new Consumer<Player>() {
			@Override
			public void accept(final Player player) {
				BuyPlayerUpgradeMenuWrapper.openNewMenu(player);
			}
		}));
	}

	private static void createBuyUnit() {
		final ItemStack iconStack = new ItemStack(Material.BOOK);
		ItemUtils.setName(iconStack, "Buy Skills");
		icons.add(new IconPair(iconStack, new Consumer<Player>() {
			@Override
			public void accept(final Player player) {
				BuySkillsMenuWrapper.openMenu(player);
			}
		}));
	}

	public static void openMenu(final Player player) {
		menu.openToPlayer(player);
	}
}
