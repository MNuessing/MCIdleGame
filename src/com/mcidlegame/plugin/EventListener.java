package com.mcidlegame.plugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.md_5.bungee.api.ChatColor;

public class EventListener implements Listener{
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		event.getPlayer().sendMessage(ChatColor.GREEN + "Welcome.");
	}

}
