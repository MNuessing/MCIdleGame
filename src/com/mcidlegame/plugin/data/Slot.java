package com.mcidlegame.plugin.data;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

public enum Slot {
	NORTH(7, 10, 180f), EAST(10, 7, 90f), SOUTH(7, 4, 0f), WEST(4, 7, -90f);

	private final int x;
	private final int z;
	private final float yaw;

	private Slot(final int x, final int z, final float yaw) {
		this.x = x;
		this.z = z;
		this.yaw = yaw;
	}

	protected Block getBlock(final Chunk chunk) {
		return chunk.getBlock(this.x, 64, this.z);
	}

	protected Location getSpawnLocation(final Chunk chunk) {
		final Location location = chunk.getBlock(this.x, 66, this.z).getLocation().add(0.5, 0, 0.5);
		location.setYaw(this.yaw);
		location.setPitch(20f);
		return location;
	}
}
