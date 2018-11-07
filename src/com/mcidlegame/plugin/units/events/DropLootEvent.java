package com.mcidlegame.plugin.units.events;

import org.bukkit.inventory.ItemStack;

import com.mcidlegame.plugin.units.enemy.EnemyUnit;

public class DropLootEvent extends EnemyUnitEvent {

	private ItemStack[] items;

	public DropLootEvent(final EnemyUnit unit, final ItemStack[] items) {
		super(unit);
		this.items = items;
	}

	public ItemStack[] getItems() {
		return this.items;
	}

	public void setItems(final ItemStack[] items) {
		this.items = items;
	}

}
