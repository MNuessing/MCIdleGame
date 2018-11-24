package com.mcidlegame.plugin.units.spawner;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.metadata.Metadatable;

public class BlockSpawner implements Spawner {

	private final String name;
	private int level;
	private final Block block;
	private final Material type;

	public BlockSpawner(final String name, final int level, final Location loc, final Material type) {
		this.name = name;
		this.level = level;
		this.block = loc.getBlock();
		this.type = type;
	}

	@Override
	public Metadatable spawn() {
		this.block.setType(this.type);
		// TODO: set name
		return this.block;
	}

	@Override
	public void kill() {
		// TODO: remove name
		this.block.setType(Material.AIR);
	}

	@Override
	public void remove() {
		// TODO: remove name
		this.block.setType(Material.AIR);
	}

	@Override
	public boolean isDead() {
		return this.block.getType() == Material.AIR;
	}

	@Override
	public Chunk getChunk() {
		return this.block.getChunk();
	}

	@Override
	public Location getDropLocation() {
		return this.block.getLocation().add(0.5, 0, 0.5);
	}

	@Override
	public void setLevel(final int level) {
		this.level = level;
		// TODO: update name
	}

	public Block getBlock() {
		return this.block;
	}

}
