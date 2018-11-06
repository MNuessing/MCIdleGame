package com.mcidlegame.plugin.units.spawner;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Dispenser;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.Metadatable;

public class BlockSpawner implements Spawner {

	private final Block block;
	private final BlockFace face;
	private final Material type;

	public BlockSpawner(final Location loc, final Material type) {
		this.block = loc.getBlock();
		this.type = type;
		switch ((int) loc.getYaw()) {
		case 180:
			this.face = BlockFace.SOUTH;
			break;
		case 90:
			this.face = BlockFace.WEST;
			break;
		case -90:
			this.face = BlockFace.EAST;
			break;
		default:
			this.face = BlockFace.NORTH;
		}
	}

	@Override
	public Metadatable spawn() {
		this.block.setType(this.type);
		final MaterialData data = this.block.getState().getData();
		if (data instanceof Dispenser) {
			((Dispenser) data).setFacingDirection(this.face);
		}
		return this.block;
	}

	@Override
	public void kill() {
		this.block.setType(Material.AIR);
	}

	@Override
	public void remove() {
		this.block.setType(Material.AIR);
	}

}
