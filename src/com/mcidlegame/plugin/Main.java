package com.mcidlegame.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mcidlegame.plugin.data.RoomData;
import com.mcidlegame.plugin.units.ally.PlayerUnit;

public class Main extends JavaPlugin {

	public static Main main;

	@Override
	public void onEnable() {
		main = this;
		Bukkit.getPluginManager().registerEvents(new EventListener(), this);
		for (final World world : Bukkit.getWorlds()) {
			for (final Chunk chunk : world.getLoadedChunks()) {
				RoomData.checkChunk(chunk);
			}
		}
		for (final Player player : Bukkit.getOnlinePlayers()) {
			PlayerUnit.apply(player);
		}
	}

	@Override
	public void onDisable() {
		for (final Entity entity : Bukkit.getWorld("world").getEntities()) {
			if (!(entity instanceof Player)) {
				entity.remove();
			} else {
				final Player player = (Player) entity;
				RoomData.getRoom(player.getLocation().getChunk()).leaveRoom(player);
			}
		}
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
