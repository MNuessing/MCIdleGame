package com.mcidlegame.plugin.data;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class BlockHandler {
	private final int x;
	private final int y;
	private final int z;

	public BlockHandler(final int x, final int y, final int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Block getBlock(final Chunk chunk) {
		return getBlock(chunk, this.y);
	}

	public Location getLocation(final Chunk chunk) {
		return getBlock(chunk, this.y + 2).getLocation().add(0.5, 0, 0.5);
	}

	private Block getBlock(final Chunk chunk, final int y) {
		return chunk.getBlock(this.x, y, this.z);
	}
}
