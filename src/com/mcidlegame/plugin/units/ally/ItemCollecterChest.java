package com.mcidlegame.plugin.units.ally;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.mcidlegame.plugin.ItemUtils;
import com.mcidlegame.plugin.data.RoomListeners;
import com.mcidlegame.plugin.data.UnitType;
import com.mcidlegame.plugin.units.UpgradeCost;
import com.mcidlegame.plugin.units.events.DropLootEvent;
import com.mcidlegame.plugin.units.spawner.BlockSpawner;
import com.mcidlegame.plugin.units.spawner.Spawner;

public class ItemCollecterChest extends BufferUnit {
	private Inventory inventory;
	private final int chestMaxSize = 26;

	public ItemCollecterChest(final UnitType type, final int level, final Spawner spawner,
			final RoomListeners listeners) {
		super(UnitType.LOOT_CHEST, level, spawner, listeners);
	}

	@Override
	protected void initListeners() {
		this.listeners.registerDropLootListener(this, (event) -> onLootDrop(event));
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
	protected void setUpgradeCost() {
		this.upgradeCost = new UpgradeCost();
		this.upgradeCost.addUpgradeCost(Material.GOLD_NUGGET, (level) -> {
			return 10 * Math.pow(1.5, level);
		});
	}

	@Override
	protected void onUpgrade() {
		this.level++;
		this.inventory.clear(this.level);
	}

	@Override
	protected void onSpawn() {
		super.onSpawn();

		setInventory();
		addBarrierToChest();
	}

	private void addBarrierToChest() {
		final ItemStack barrier = new ItemStack(Material.BARRIER);
		ItemUtils.setName(barrier, "LOCKED");
		for (int index = this.level; index < this.chestMaxSize; index++) {
			this.inventory.setItem(index, barrier);
		}
	}

	private void setInventory() {
		final Block block = ((BlockSpawner) this.spawner).getBlock();
		if (block == null || block.getType() != Material.CHEST) {
			return;
		}
		if (block.getState() != null || !(block.getState() instanceof Chest)) {
			return;
		}
		final Chest chest = (Chest) block.getState();
		this.inventory = chest.getBlockInventory();
	}
}
