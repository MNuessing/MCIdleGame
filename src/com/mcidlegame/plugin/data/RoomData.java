package com.mcidlegame.plugin.data;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.mcidlegame.plugin.Main;
import com.mcidlegame.plugin.PlayerUtils;
import com.mcidlegame.plugin.WorldUtils;
import com.mcidlegame.plugin.units.ally.AllyUnit;
import com.mcidlegame.plugin.units.ally.ShooterUnit;
import com.mcidlegame.plugin.units.enemy.EnemyUnit;
import com.mcidlegame.plugin.units.events.EnemyUnitDeathEvent;

public class RoomData {

	public static final String metaString = "room";
	public static final String metaSlotString = "roomSlot";

	private static final Map<Chunk, RoomData> rooms = new HashMap<>();

	private EnemyUnit target = null;
	private final Map<Slot, AllyUnit> allies = new HashMap<>();
	private final RoomListeners listeners = new RoomListeners();
	private final Chunk chunk;
	private BukkitTask respawn;
	private static final BlockHandler blockHandler = new BlockHandler();

	public static void checkChunk(final Chunk chunk) {
		if (chunk == null) {
			return;
		}

		final Block block = blockHandler.getBlock(chunk);
		if (block == null || block.getType() != Material.COMMAND) {
			return;
		}

		if (WorldUtils.getCommandString(block).equals("locked")) {
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
		rooms.put(chunk, this);
		this.listeners.registerEnemyUnitDeathListener(null, this::onDeath);

		setup();
	}

	private void setup() {
		final Block targetBlock = blockHandler.getBlock(this.chunk);
		final String targetLine = WorldUtils.getCommandString(targetBlock);
		if (!targetLine.equals("")) {
			final Location targetSpawn = blockHandler.getLocation(this.chunk);
			this.target = EnemyUnit.fromString(targetLine, targetSpawn, this.listeners);
		}
		targetBlock.getRelative(BlockFace.UP).setMetadata(metaString, new FixedMetadataValue(Main.main, this));

		for (final Slot slot : Slot.values()) {
			final Block block = slot.getBlock(this.chunk);
			final String line = WorldUtils.getCommandString(block);
			if (!line.equals("")) {
				final AllyUnit ally = AllyUnit.fromString(line, slot.getSpawnLocation(this.chunk), this.listeners);
				this.allies.put(slot, ally);
				ally.spawn();
			}
			final Block upper = block.getRelative(BlockFace.UP);
			upper.setMetadata(metaString, new FixedMetadataValue(Main.main, this));
			upper.setMetadata(metaSlotString, new FixedMetadataValue(Main.main, slot));
		}
		if (this.target != null) {
			this.target.spawn();
			startShooting();
		}
	}

	private void onDeath(final EnemyUnitDeathEvent event) {
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
		WorldUtils.setCommand(blockHandler.getBlock(this.chunk), "");
		stopShooting();
		this.target.remove();
		final ItemStack item = this.target.toItem();
		this.target = null;
		return item;
	}

	public boolean setTarget(final ItemStack item) {
		final Location targetSpawn = blockHandler.getLocation(this.chunk);
		final EnemyUnit target = EnemyUnit.fromItem(item, targetSpawn, this.listeners);
		if (target == null) {
			return false;
		}
		WorldUtils.setCommand(blockHandler.getBlock(this.chunk), target.toString());
		this.target = target;
		this.target.spawn();
		startShooting();

		return true;
	}

	public ItemStack removeAlly(final Slot slot) {
		WorldUtils.setCommand(slot.getBlock(this.chunk), "");
		final AllyUnit ally = this.allies.get(slot);
		ally.remove();
		this.allies.remove(slot);
		return ally.toItem();
	}

	public boolean addAlly(final Slot slot, final ItemStack item) {
		final AllyUnit ally = AllyUnit.fromItem(item, slot.getSpawnLocation(this.chunk), this.listeners);
		if (ally == null) {
			return false;
		}
		WorldUtils.setCommand(slot.getBlock(this.chunk), ally.toString());
		this.allies.put(slot, ally);
		ally.spawn();
		if (this.target != null && !this.target.isDead() && ally instanceof ShooterUnit) {
			((ShooterUnit) ally).startShooting();
		}

		return true;
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

	public void interact(final Player player, final Block block) {
		final Slot slot = getSlot(block);
		if (slot == null) {
			interactTarget(player);
		} else {
			interactAlly(player, slot);
		}
	}

	private Slot getSlot(final Block block) {
		for (final MetadataValue value : block.getMetadata(metaSlotString)) {
			if (value.getOwningPlugin() == Main.main) {
				return (Slot) value.value();
			}
		}
		return null;
	}

	private void interactTarget(final Player player) {
		if (this.target != null) {
			addItem(player, removeTarget());
		} else if (setTarget(player.getInventory().getItemInMainHand())) {
			PlayerUtils.decreaseItem(player);
		}
	}

	private void interactAlly(final Player player, final Slot slot) {
		if (this.allies.containsKey(slot)) {
			addItem(player, removeAlly(slot));
		} else if (addAlly(slot, player.getInventory().getItemInMainHand())) {
			PlayerUtils.decreaseItem(player);
		}
	}

	private void addItem(final Player player, final ItemStack... items) {
		final Map<Integer, ItemStack> remains = player.getInventory().addItem(items);
		if (remains.isEmpty()) {
			return;
		}
		final Location location = player.getLocation();
		final World world = location.getWorld();
		for (final ItemStack item : remains.values()) {
			world.dropItem(location, item);
		}
	}
}
