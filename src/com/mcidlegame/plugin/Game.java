package com.mcidlegame.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;

public class Game {

	public static void spawnMonster() {
		final World world = Bukkit.getWorld("world");
		final Zombie zombie = (Zombie) world.spawnEntity(new Location(world, 0, 66, 0), EntityType.ZOMBIE);
		zombie.setAI(false);
	}

}
