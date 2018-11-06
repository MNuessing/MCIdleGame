package com.mcidlegame.plugin.units.spawner;

import org.bukkit.metadata.Metadatable;

public interface Spawner {

	Metadatable spawn();

	void kill();

	void remove();

}
