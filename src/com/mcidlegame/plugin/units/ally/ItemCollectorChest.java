package com.mcidlegame.plugin.units.ally;

import java.util.HashMap;
import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.mcidlegame.plugin.ItemUtils;
import com.mcidlegame.plugin.data.RoomListeners;
import com.mcidlegame.plugin.data.UnitType;
import com.mcidlegame.plugin.units.events.DropLootEvent;
import com.mcidlegame.plugin.units.spawner.BlockSpawner;

import net.md_5.bungee.api.ChatColor;

public class ItemCollectorChest extends BufferUnit {
	private Inventory inventory;
	private final int chestMaxSize = 27;

	public ItemCollectorChest(final int level, final Location loc, final Consumer<Player> remove,
			final RoomListeners listeners) {
		super(UnitType.LOOT_CHEST, level, new BlockSpawner("Loot Chest", level, loc, Material.CHEST), remove,
				listeners);
	}

	@Override
	protected void initListeners() {
		this.listeners.registerDropLootListener(this, this::onLootDrop);
	}

	private void onLootDrop(final DropLootEvent event) {
		if (this.inventory == null) {
			return;
		}
		final ItemStack[] items = event.getItems();

		final HashMap<Integer, ItemStack> notInsertedItems = this.inventory.addItem(items);

		event.setItems(notInsertedItems.values().toArray(new ItemStack[notInsertedItems.values().size()]));
	}

	@Override
	public boolean isRemoveable() {
		for (final ItemStack item : this.inventory) {
			final Material type = item.getType();
			switch (type) {
			case AIR:
				continue;
			case BARRIER:
				return true;
			default:
				return false;
			}
		}
		return true;
	}

	@Override
	protected void initUpgradeCost() {
		this.upgradeCost.addUpgradeCost(Material.GOLD_NUGGET, (level) -> {
			return 10 * Math.pow(1.5, level);
		});
	}

	@Override
	protected void onUpgrade() {
		this.inventory.clear(this.level - 1);
	}

	@Override
	protected void onSpawn() {
		super.onSpawn();

		setInventory();
		addBarrierToChest();
	}

	private void addBarrierToChest() {
		final ItemStack barrier = new ItemStack(Material.BARRIER);
		ItemUtils.setName(barrier, ChatColor.RED + "LOCKED");
		for (int index = this.level; index < this.chestMaxSize; index++) {
			this.inventory.setItem(index, ItemUtils.setLore(barrier,
					ChatColor.GRAY + "Unlock level " + (index + 1) + " to access this slot."));
		}
	}

	private void setInventory() {
		final Block block = ((BlockSpawner) this.spawner).getBlock();
		if (block == null || block.getType() != Material.CHEST) {
			return;
		}
		final BlockState state = block.getState();
		if (state == null || !(state instanceof Chest)) {
			return;
		}
		final Chest chest = (Chest) state;
		this.inventory = chest.getBlockInventory();
	}
}
