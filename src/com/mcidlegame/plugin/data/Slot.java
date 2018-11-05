package com.mcidlegame.plugin.data;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

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

	protected Block getBlock(final Chunk chunk) {
		return chunk.getBlock(this.x, 64, this.z);
	}

	protected Location getSpawnLocation(final Chunk chunk) {
		final Location location = chunk.getBlock(this.x, 66, this.z).getLocation().add(0.5, 0, 0.5);
		location.setPitch(this.pitch);
		return location;
	}
}
