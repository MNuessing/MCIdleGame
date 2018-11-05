package com.mcidlegame.plugin;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.Player;

public class WorldManager {

	public static void addRoom(final Chunk chunk) {
		final World world = chunk.getWorld();
		final Chunk north = world.getChunkAt(chunk.getX() + 1, chunk.getZ());
		final Chunk south = world.getChunkAt(chunk.getX() - 1, chunk.getZ());
		final Chunk east = world.getChunkAt(chunk.getX(), chunk.getZ() + 1);
		final Chunk west = world.getChunkAt(chunk.getX(), chunk.getZ() - 1);
		final Chunk[] relatives = { north, south, east, west };
		for (final Chunk relative : relatives) {
			if (!hasRoom(relative)) {
				createRoom(relative);
			}
		}
		unlockRoom(chunk);
	}

	@SuppressWarnings("deprecation")
	public static void createRoom(final Chunk chunk) {
		final World world = chunk.getWorld();
		final Chunk nullChunk = world.getChunkAt(0, 0);

		for (int i = 0; i < 16; i++) {
			for (int y = 64; y < 72; y++) {
				for (int z = 0; z < 16; z++) {
					final Block nullBlock = nullChunk.getBlock(i, y - 16, z);
					final Material type = nullBlock.getType();
					if (type != Material.AIR) {
						final Block block = chunk.getBlock(i, y, z);
						block.setType(type);
						// I know it's deprecated but there is no other way
						block.setData(nullBlock.getData());
					}
				}
			}
		}
		((CommandBlock) chunk.getBlock(8, 64, 8).getState()).setCommand("locked");
	}

	public static void unlockRoom(final Chunk chunk) {
		// TODO: do this smart
		final int[][] walls = { { 1, 7 }, { 7, 4 }, { 10, 7 }, { 7, 10 } };
		for (final int[] pair : walls) {
			for (int x = pair[0] - 1; x < pair[0] + 2; x++) {
				for (int y = 65; y < 68; y++) {
					for (int z = pair[1] - 1; z < pair[1] + 2; z++) {
						final Block block = chunk.getBlock(x, y, z);
						if (block.getType() == Material.WOOL) {
							block.setType(Material.AIR);
						}
					}
				}
			}
		}
		((CommandBlock) chunk.getBlock(8, 64, 8).getState()).setCommand("");
	}

	public static boolean hasRoom(final Chunk chunk) {
		if (chunk.getBlock(8, 64, 8).getType() == Material.AIR) {
			return false;
		}
		return true;
	}

	public static void enterWorld(final Player player, final World world) {
		player.teleport(new Location(world, 4, 65, 4));
	}

	public static void setCommand(final Block block, final String command) {
		final BlockState state = block.getState();
		if (!(state instanceof CommandBlock)) {
			return;
		}
		((CommandBlock) state).setCommand(command);
		state.update();
	}

}
