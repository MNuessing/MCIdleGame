package com.mcidlegame.plugin.units.spawner;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.metadata.Metadatable;

public class BlockSpawner implements Spawner {

	private final String name;
	private final Block block;
	private final Material type;

	public BlockSpawner(final String name, final Location loc, final Material type) {
		this.name = name;
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
	public Location getLoacation() {
		return this.block.getLocation().add(0.5, 0, 0.5);
	}

}
