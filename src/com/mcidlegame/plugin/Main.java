package com.mcidlegame.plugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin {

	private static boolean started = false;

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new EventListener(), this);
	}

	@Override
	public void onDisable() {

	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		if (label.equals("start")) {
			if (started) {
				sender.sendMessage(ChatColor.RED + "The game has already started.");
			} else {
				Game.spawnMonster();
			}
			return true;
		}
		return false;
	}

}
