package com.mcidlegame.plugin.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;

import com.mcidlegame.plugin.Main;
import com.mcidlegame.plugin.WorldManager;
import com.mcidlegame.plugin.units.ally.AllyUnit;
import com.mcidlegame.plugin.units.ally.ShooterUnit;
import com.mcidlegame.plugin.units.enemy.EnemyUnit;

public class RoomData {

    public static final String metaString = "room";

    private static final Map<Chunk, RoomData> rooms = new HashMap<>();
    private static final int defaultX = 8;
    private static final int defaultY = 64;
    private static final int defaultZ = 8;

    private EnemyUnit target = null;
    private final Map<Slot, AllyUnit> allies = new HashMap<>();
    private final Chunk chunk;
    private BukkitTask respawn;

    public static void checkChunk(final Chunk chunk) {
        if (Objects.isNull(chunk)) {
            return;
        }

        final Block block = getBlock(chunk, defaultY);
        if (Objects.isNull(block) || !Material.COMMAND.equals(block.getType())) {
            return;
        }

        if ("blocked".equals(WorldManager.getCommandString(block))) {
            return;
        }

        new RoomData(chunk);
    }

    private static Block getBlock(Chunk chunk, int defaultY) {
        return chunk.getBlock(defaultX, defaultY, defaultZ);
    }

    public static RoomData getRoom(final Chunk chunk) {
        return rooms.get(chunk);
    }

    public static void unloadRoom(final Chunk chunk) {
        rooms.remove(chunk);
    }

    private RoomData(final Chunk chunk) {
        this.chunk = chunk;
        getBlock(chunk, defaultY).setMetadata(metaString, new FixedMetadataValue(Main.main, this));
        rooms.put(chunk, this);

        setup();
    }

    private void setup() {
        final Block targetBlock = getBlock(this.chunk, defaultY);
        final String targetLine = WorldManager.getCommandString(targetBlock);
        if (!"".equals(targetLine)) {
            final Location targetSpawn = getBlock(this.chunk, defaultY + 2).getLocation().add(0.5, 0, 0.5);
            this.target = EnemyUnit.fromString(targetLine, targetSpawn, this::onKill);
        }

        for (final Slot slot : Slot.values()) {
            final Block block = slot.getBlock(this.chunk);
            final String line = WorldManager.getCommandString(block);
            if (!"".equals(line)) {
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

        this.respawn = new MyBukkitRunnable(this::forRunnable).runTaskLater(Main.main, 40L);
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
        if (Objects.nonNull(this.respawn) && !this.respawn.isCancelled()) {
            this.respawn.cancel();
        }
        WorldManager.setCommand(getBlock(this.chunk, defaultY), "");
        stopShooting();
        this.target.remove();
        this.target = null;
        return this.target.toItem();
    }

    public void setTarget(final ItemStack item) {
        final Location targetSpawn = getBlock(this.chunk, defaultY +2).getLocation().add(0.5, 0, 0.5);
        final EnemyUnit target = EnemyUnit.fromItem(item, targetSpawn, this::onKill);
        if (Objects.isNull(target)) {
            return;
        }
        WorldManager.setCommand(getBlock(this.chunk, defaultY), target.toString());
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
        if (Objects.isNull(ally)) {
            return;
        }
        WorldManager.setCommand(slot.getBlock(this.chunk), ally.toString());
        this.allies.put(slot, ally);
        ally.spawn();
        if (Objects.nonNull(this.target) && !this.target.isDead() && ally instanceof ShooterUnit) {
            ((ShooterUnit) ally).startShooting();
        }
    }

    public void joinRoom(final Player player) {
        if (Objects.nonNull(this.target) && !this.target.isDead()) {
            this.target.addToHealthbar(player);
        }
    }

    public void leaveRoom(final Player player) {
        if (this.target != null) {
            this.target.removeHealthbar(player);
        }
    }

    private void forRunnable()    {
        if (Objects.nonNull(target)) {
            target.spawn();
            startShooting();
        }
    }
}
