package com.mcidlegame.plugin.data;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
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

	public enum Slot {
		NORTH(11, 8, 180f), EAST(8, 11, -90f), SOUTH(5, 8, 0f), WEST(8, 5, 90f);

		private final int x;
		private final int z;
		private final float pitch;

		private Slot(final int x, final int z, final float pitch) {
			this.x = x;
			this.z = z;
			this.pitch = pitch;
		}

		private Block getBlock(final Chunk chunk) {
			return chunk.getBlock(this.x, 64, this.z);
		}

		private Location getSpawnLocation(final Chunk chunk) {
			final Location location = chunk.getBlock(this.x, 66, this.z).getLocation().add(0.5, 0, 0.5);
			location.setPitch(this.pitch);
			return location;
		}
	}

	public static final String metaString = "room";

	private static final Map<Chunk, RoomData> rooms = new HashMap<>();

	private EnemyUnit target = null;
	private final Map<Slot, AllyUnit> allies = new HashMap<>();
	private final Chunk chunk;
	private BukkitTask respawn;

	public static void checkChunk(final Chunk chunk) {
		final Block block = chunk.getBlock(8, 64, 8);
		if (block.getType() != Material.COMMAND) {
			return;
		}

		if (WorldManager.getCommandStringOfCommandBlock(block).equals("blocked")) {
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

	public RoomData(final Chunk chunk) {
		this.chunk = chunk;
		chunk.getBlock(8, 64, 8).setMetadata(metaString, new FixedMetadataValue(Main.main, this));
		rooms.put(chunk, this);

		setup();
	}

	public void setup() {
		final Block targetBlock = this.chunk.getBlock(8, 64, 8);
		final CommandBlock targetState = (CommandBlock) targetBlock.getState();
		final String targetLine = targetState.getCommand();
		if (!targetLine.equals("")) {
			final Location targetSpawn = this.chunk.getBlock(8, 66, 8).getLocation().add(0.5, 0, 0.5);
			this.target = EnemyUnit.fromString(targetLine, targetSpawn, () -> onKill());
		}

		for (final Slot slot : Slot.values()) {
			final Block block = slot.getBlock(this.chunk);
			final CommandBlock state = (CommandBlock) block.getState();
			final String line = state.getCommand();
			if (!line.equals("")) {
				this.allies.put(slot, AllyUnit.fromString(line, slot.getSpawnLocation(this.chunk)));
			}
		}
		for (final AllyUnit ally : this.allies.values()) {
			ally.spawn();
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
		WorldManager.setCommand(this.chunk.getBlock(8, 64, 8), "");
		stopShooting();
		this.target.remove();
		this.target = null;
		return this.target.toItem();
	}

	public void setTarget(final ItemStack item) {
		final Location targetSpawn = this.chunk.getBlock(8, 66, 8).getLocation().add(0.5, 0, 0.5);
		final EnemyUnit target = EnemyUnit.fromItem(item, targetSpawn, () -> onKill());
		if (target == null) {
			return;
		}
		WorldManager.setCommand(this.chunk.getBlock(8, 64, 8), target.toString());
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
