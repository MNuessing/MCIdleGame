package com.mcidlegame.plugin.data;

import java.util.HashMap;
import java.util.function.Consumer;

import com.mcidlegame.plugin.units.Unit;
import com.mcidlegame.plugin.units.events.CalculateLootEvent;
import com.mcidlegame.plugin.units.events.DropLootEvent;
import com.mcidlegame.plugin.units.events.EnemyUnitDamagedByDamagerEvent;
import com.mcidlegame.plugin.units.events.EnemyUnitDeathEvent;

public class RoomListeners {

	private final HashMap<Unit, Consumer<EnemyUnitDamagedByDamagerEvent>> unitDamagedByDamagerListeners = new HashMap<>();
	private final HashMap<Unit, Consumer<EnemyUnitDeathEvent>> enemyUnitDeathListeners = new HashMap<>();
	private final HashMap<Unit, Consumer<CalculateLootEvent>> calculateLootListeners = new HashMap<>();
	private final HashMap<Unit, Consumer<DropLootEvent>> dropLootListeners = new HashMap<>();

	public void registerUnitDamagedByDamagerListener(final Unit unit,
			final Consumer<EnemyUnitDamagedByDamagerEvent> listener) {
		this.unitDamagedByDamagerListeners.put(unit, listener);
	}

	public void registerEnemyUnitDeathListener(final Unit unit, final Consumer<EnemyUnitDeathEvent> listener) {
		this.enemyUnitDeathListeners.put(unit, listener);
	}

	public void registerCalculateLootEvent(final Unit unit, final Consumer<CalculateLootEvent> listener) {
		this.calculateLootListeners.put(unit, listener);
	}

	public void registerDropLootListener(final Unit unit, final Consumer<DropLootEvent> listener) {
		this.dropLootListeners.put(unit, listener);
	}

	public void remove(final Unit unit) {
		this.unitDamagedByDamagerListeners.remove(unit);
		this.enemyUnitDeathListeners.remove(unit);
		this.calculateLootListeners.remove(unit);
		this.dropLootListeners.remove(unit);
	}

	public void listen(final EnemyUnitDamagedByDamagerEvent event) {
		for (final Consumer<EnemyUnitDamagedByDamagerEvent> listener : this.unitDamagedByDamagerListeners.values()) {
			listener.accept(event);
		}
	}

	public void listen(final EnemyUnitDeathEvent event) {
		for (final Consumer<EnemyUnitDeathEvent> listener : this.enemyUnitDeathListeners.values()) {
			listener.accept(event);
		}
	}

	public void listen(final CalculateLootEvent event) {
		for (final Consumer<CalculateLootEvent> listener : this.calculateLootListeners.values()) {
			listener.accept(event);
		}
	}

	public void listen(final DropLootEvent event) {
		for (final Consumer<DropLootEvent> listener : this.dropLootListeners.values()) {
			listener.accept(event);
		}
	}

}
