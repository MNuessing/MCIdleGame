package com.mcidlegame.plugin.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.skitskurr.inventorywrapper.implementations.StaticMenu;
import com.skitskurr.inventorywrapper.implementations.StaticMenu.IconPair;

public class BuySkillsMenuWrapper {

	private static final List<IconPair> icons = new ArrayList<>();
	private static StaticMenu menu;
	static {
		initializeMenu();
	}

	private static void initializeMenu() {
		menu = new StaticMenu("Buy Skills", icons);
	}

	public static void openMenu(final Player player) {
		menu.openToPlayer(player);
	}
}
