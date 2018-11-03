package com.mcidlegame.plugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

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
			Game.spawnMonster();
			return true;
		}
		return false;
	}

}
