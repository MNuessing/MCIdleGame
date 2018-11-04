package com.mcidlegame.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
			final Location location = world.getSpawnLocation();
			sender.sendMessage(location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ());

			return true;
		}

		if (label.equals("core")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("The console cannot move.");
				return true;
			}
			final Player player = (Player) sender;
			final World world = player.getWorld();
			player.teleport(new Location(world, 4, 65, 4));
			return true;
		}
		return false;
	}

}
