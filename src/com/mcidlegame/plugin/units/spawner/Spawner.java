package com.mcidlegame.plugin.units.spawner;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.metadata.Metadatable;

public interface Spawner {

	Metadatable spawn();

	void kill();

	void remove();

	boolean isDead();

	Chunk getChunk();

	Location getDropLacation();

	void updateName(String name);
}
