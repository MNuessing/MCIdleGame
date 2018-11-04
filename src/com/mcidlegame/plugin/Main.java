package com.mcidlegame.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public static Main main;

	@Override
	public void onEnable() {
		main = this;
		Bukkit.getPluginManager().registerEvents(new EventListener(), this);
	}

	@Override
	public void onDisable() {
		Game.removeHealthbar();
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args) {

		if (label.equals("test")) {
			final World world = Bukkit.getWorld("world");
			for (int x = -9; x < 11; x++) {
				for (int y = 64; y < 72; y++) {
					for (int z = -9; z < 11; z++) {
						final Block block = world.getBlockAt(x, y, z);
						if (block.getType() != Material.AIR) {
							sender.sendMessage(block.getType() + " " + block.getData());
						}
					}
				}
			}

			return true;
		}

		if (label.equals("start")) {
			Game.startGame();
			return true;
		}
		return false;
	}

}
