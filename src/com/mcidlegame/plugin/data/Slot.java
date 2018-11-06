package com.mcidlegame.plugin.data;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

public enum Slot {
	NORTH(7, 10, 180f), EAST(10, 7, 90f), SOUTH(7, 4, 0f), WEST(4, 7, -90f);

	private final float yaw;
	private final BlockHandler blockHandler;

	private Slot(final int x, final int z, final float yaw) {
		this.blockHandler = new BlockHandler(x, 64, z);
		this.yaw = yaw;
	}

	protected Block getBlock(final Chunk chunk) {
		return this.blockHandler.getBlock(chunk);
	}

	protected Location getSpawnLocation(final Chunk chunk) {
		final Location location = this.blockHandler.getLocation(chunk);
		location.setYaw(this.yaw);
		location.setPitch(20f);
		return location;
	}
}
