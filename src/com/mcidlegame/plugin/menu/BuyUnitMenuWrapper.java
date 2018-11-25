package com.mcidlegame.plugin.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mcidlegame.plugin.ItemUtils;
import com.mcidlegame.plugin.PlayerUtils;
import com.mcidlegame.plugin.data.UnitData;
import com.mcidlegame.plugin.data.UnitType;
import com.mcidlegame.plugin.units.ally.PlayerUnit;
import com.skitskurr.inventorywrapper.implementations.StaticMenu;
import com.skitskurr.inventorywrapper.implementations.StaticMenu.IconPair;

public class BuyUnitMenuWrapper {

	private static final List<IconPair> icons = new ArrayList<>();
	private static StaticMenu menu;
	static {
		initializeMenu();
	}

	private static void initializeMenu() {
		for (final UnitType unitType : UnitType.values()) {
			createIconForUnit(unitType);
		}
		menu = new StaticMenu("Buy Unit", icons);
	}

	private static void createIconForUnit(final UnitType unitType) {
		final ItemStack item = new ItemStack(unitType.getBaseType());
		final List<String> lore = ItemUtils.getLore(unitType.getBaseCost());
		ItemUtils.setNameAndLore(item, "Buy " + unitType.getName(), lore);
		icons.add(new IconPair(item, new Consumer<Player>() {
			@Override
			public void accept(final Player player) {
				final PlayerUnit playerUnit = new PlayerUnit(player.getLevel());
				final boolean successfullPayment = PlayerUtils.removeItems(player, unitType.getBaseCost());
				if (successfullPayment) {
					player.getInventory().addItem(new UnitData(unitType, 1).toItem());
				}
			}
		}));
	}

	public static void openMenu(final Player player) {
		menu.openToPlayer(player);
	}
}
