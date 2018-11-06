package com.mcidlegame.plugin.data;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

public enum Slot {
	NORTH(11, 8, 180f), EAST(8, 11, -90f), SOUTH(5, 8, 0f), WEST(8, 5, 90f);

	private BlockHandler blockHandler;
	private final float pitch;

	private Slot(final int x, final int z, final float pitch) {
		this.blockHandler = new BlockHandler(x, 64, z);
		this.pitch = pitch;
	}

	protected Block getBlock(final Chunk chunk) {
		return this.blockHandler.getBlock(chunk);
	}

	protected Location getSpawnLocation(final Chunk chunk) {
		final Location location = this.blockHandler.getLocation(chunk);
		location.setPitch(this.pitch);
		return location;
	}
}
