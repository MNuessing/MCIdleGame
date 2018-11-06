package com.mcidlegame.plugin.data;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.mcidlegame.plugin.Main;
import com.mcidlegame.plugin.WorldManager;
import com.mcidlegame.plugin.units.ally.AllyUnit;
import com.mcidlegame.plugin.units.ally.ShooterUnit;
import com.mcidlegame.plugin.units.enemy.EnemyUnit;

public class RoomData {

	public static final String metaString = "room";

	private static final Map<Chunk, RoomData> rooms = new HashMap<>();

	private EnemyUnit target = null;
	private final Map<Slot, AllyUnit> allies = new HashMap<>();
	private final Chunk chunk;
	private BukkitTask respawn;
	private static final BlockHandler blockHandler = new BlockHandler(8, 64, 8);

	public static void checkChunk(final Chunk chunk) {
		if (chunk == null) {
			return;
		}

		final Block block = blockHandler.getBlock(chunk);
		if (block == null || block.getType() != Material.COMMAND) {
			return;
		}

		if (WorldManager.getCommandString(block).equals("blocked")) {
			return;
		}

		new RoomData(chunk);
	}

	public static RoomData getRoom(final Chunk chunk) {
		return rooms.get(chunk);
	}

	public static void unloadRoom(final Chunk chunk) {
		rooms.remove(chunk);
	}

	private RoomData(final Chunk chunk) {
		this.chunk = chunk;
		blockHandler.getBlock(this.chunk).setMetadata(metaString, new FixedMetadataValue(Main.main, this));
		rooms.put(chunk, this);

		setup();
	}

	private void setup() {
		final Block targetBlock = blockHandler.getBlock(this.chunk);
		final String targetLine = WorldManager.getCommandString(targetBlock);
		if (!targetLine.equals("")) {
			final Location targetSpawn = blockHandler.getLocation(this.chunk);
			this.target = EnemyUnit.fromString(targetLine, targetSpawn, () -> onKill());
		}

		for (final Slot slot : Slot.values()) {
			final Block block = slot.getBlock(this.chunk);
			final String line = WorldManager.getCommandString(block);
			if (!line.equals("")) {
				final AllyUnit ally = AllyUnit.fromString(line, slot.getSpawnLocation(this.chunk));
				this.allies.put(slot, ally);
				ally.spawn();
			}
		}
		this.target.spawn();
		startShooting();
	}

	public void onKill() {
		stopShooting();

		this.respawn = new BukkitRunnable() {
			@Override
			public void run() {
				if (RoomData.this.target != null) {
					RoomData.this.target.spawn();
					startShooting();
				}
			}
		}.runTaskLater(Main.main, 40L);
	}

	public void startShooting() {
		for (final AllyUnit ally : this.allies.values()) {
			if (ally instanceof ShooterUnit) {
				((ShooterUnit) ally).startShooting();
			}
		}
	}

	public void stopShooting() {
		for (final AllyUnit ally : this.allies.values()) {
			if (ally instanceof ShooterUnit) {
				((ShooterUnit) ally).stopShooting();
			}
		}
	}

	public ItemStack removeTarget() {
		if (this.respawn != null && !this.respawn.isCancelled()) {
			this.respawn.cancel();
		}
		WorldManager.setCommand(blockHandler.getBlock(this.chunk), "");
		stopShooting();
		this.target.remove();
		this.target = null;
		return this.target.toItem();
	}

	public void setTarget(final ItemStack item) {
		final Location targetSpawn = blockHandler.getLocation(this.chunk);
		final EnemyUnit target = EnemyUnit.fromItem(item, targetSpawn, () -> onKill());
		if (target == null) {
			return;
		}
		WorldManager.setCommand(blockHandler.getBlock(this.chunk), target.toString());
		this.target = target;
		this.target.spawn();
		startShooting();
	}

	public ItemStack removeAlly(final Slot slot) {
		WorldManager.setCommand(slot.getBlock(this.chunk), "");
		final AllyUnit ally = this.allies.get(slot);
		ally.remove();
		this.allies.remove(slot);
		return ally.toItem();
	}

	public void addAlly(final Slot slot, final ItemStack item) {
		final AllyUnit ally = AllyUnit.fromItem(item, slot.getSpawnLocation(this.chunk));
		if (ally == null) {
			return;
		}
		WorldManager.setCommand(slot.getBlock(this.chunk), ally.toString());
		this.allies.put(slot, ally);
		ally.spawn();
		if (this.target != null && !this.target.isDead() && ally instanceof ShooterUnit) {
			((ShooterUnit) ally).startShooting();
		}
	}

	public void joinRoom(final Player player) {
		if (this.target != null && !this.target.isDead()) {
			this.target.addToHealthbar(player);
		}
	}

	public void leaveRoom(final Player player) {
		if (this.target != null) {
			this.target.removeHealthbar(player);
		}
	}

}
