package com.mcidlegame.plugin.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mcidlegame.plugin.PlayerUtils;
import com.mcidlegame.plugin.units.UpgradeCost;
import com.mcidlegame.plugin.units.ally.PlayerUnit;
import com.skitskurr.inventorywrapper.implementations.StaticMenu;
import com.skitskurr.inventorywrapper.implementations.StaticMenu.IconPair;

public class BuyPlayerUpgradeMenuWrapper {

	private final List<IconPair> icons = new ArrayList<>();
	private StaticMenu menu;
	private final Player player;

	BuyPlayerUpgradeMenuWrapper(final Player player) {
		this.player = player;
		initializeMenu();
	}

	private void initializeMenu() {
		createUpgradeLevel();
		this.menu = new StaticMenu("Buy Player Upgrade", this.icons);
	}

	private void createUpgradeLevel() {
		final ItemStack iconStack = new ItemStack(Material.GOLDEN_APPLE);
		final PlayerUnit playerUnit = new PlayerUnit(this.player.getLevel());
		UpgradeCost.setUpgradeLevel(this.player.getLevel(), iconStack, playerUnit.getUpgradeCost());
		this.icons.add(new IconPair(iconStack, new Consumer<Player>() {
			@Override
			public void accept(final Player player) {
				final PlayerUnit playerUnit = new PlayerUnit(player.getLevel());
				final boolean successfullPayment = PlayerUtils.removeItems(player, playerUnit.getUpgradeCost());
				if (successfullPayment) {
					player.setLevel(player.getLevel() + 1);
					UpgradeCost.setUpgradeLevel(player.getLevel(), iconStack, playerUnit.getUpgradeCost());
					BuyPlayerUpgradeMenuWrapper.this.menu.closeAll();
					openNewMenu(player);
				}
			}
		}));
	}

	public static void openNewMenu(final Player player) {
		final BuyPlayerUpgradeMenuWrapper menuWrapper = new BuyPlayerUpgradeMenuWrapper(player);
		menuWrapper.openMenu();
	}

	public void openMenu() {
		this.menu.openToPlayer(this.player);
	}
}
