package com.mcidlegame.plugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkLoadEvent;

public class EventListener implements Listener {

	@EventHandler
	public void onJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		player.sendMessage(ChatColor.GREEN + "Welcome.");
		player.setGameMode(GameMode.ADVENTURE);
		player.setCollidable(false);
	}

	@EventHandler
	public void onMobSpawn(final CreatureSpawnEvent event) {
		if (event.getSpawnReason() != SpawnReason.CUSTOM) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamage(final EntityDamageByEntityEvent event) {
		final Entity damager = event.getDamager();
		if (damager instanceof Player) {
			damager.sendMessage("You hit the zombie.");
		} else if (damager instanceof Projectile && ((Projectile) damager).getShooter() instanceof Snowman) {
			Bukkit.broadcastMessage("The snowman indirectly hit the zombie.");
		} else {
			Bukkit.broadcastMessage("The " + damager.getClass().getName() + " hit the zombie.");
		}
		// TODO: listen hit event here
		event.setCancelled(true);
	}

	@EventHandler
	public void onHunger(final FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onWeather(final WeatherChangeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onChunkLoad(final ChunkLoadEvent event) {
		for (final Entity entity : event.getChunk().getEntities()) {
			if (!(entity instanceof Player)) {
				entity.remove();
			}
		}
	}

}
