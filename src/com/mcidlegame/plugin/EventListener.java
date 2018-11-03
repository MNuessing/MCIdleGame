package com.mcidlegame.plugin;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.projectiles.ProjectileSource;

import com.mcidlegame.plugin.enemy.EnemyUnit;
import com.mcidlegame.plugin.friend.AllyUnit;
import com.mcidlegame.plugin.friend.PlayerUnit;

public class EventListener implements Listener {

	@EventHandler
	public void onJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		player.sendMessage(ChatColor.GREEN + "Welcome.");
		player.setGameMode(GameMode.ADVENTURE);
		player.setCollidable(false);
		player.setMetadata("allyUnit", new FixedMetadataValue(Main.main, new PlayerUnit(1)));
	}

	@EventHandler
	public void onMobSpawn(final CreatureSpawnEvent event) {
		if (event.getSpawnReason() != SpawnReason.CUSTOM) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamage(final EntityDamageByEntityEvent event) {
		event.setCancelled(true);

		EnemyUnit enemy = null;
		for (final MetadataValue value : event.getEntity().getMetadata("enemyUnit")) {
			if (value.getOwningPlugin() == Main.main) {
				enemy = (EnemyUnit) value.value();
			}
		}
		if (enemy == null) {
			return;
		}

		Entity damager = event.getDamager();
		if (damager instanceof Projectile) {
			final ProjectileSource source = ((Projectile) damager).getShooter();
			if (source instanceof Entity) {
				damager = (Entity) source;
			} else {
				// TODO: maybe enable support for Dispenser as allies
				return;
			}
		}

		AllyUnit ally = null;
		for (final MetadataValue value : damager.getMetadata("allyUnit")) {
			if (value.getOwningPlugin() == Main.main) {
				ally = (AllyUnit) value.value();
			}
		}
		if (ally == null) {
			return;
		}

		enemy.hit(ally.getDamage());
	}

	@EventHandler
	public void onDeath(final EntityDeathEvent event) {
		event.setDroppedExp(0);
		event.getDrops().clear();
		Game.checkDeath(event.getEntity());
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
	public void onBlockForm(final BlockFormEvent event) {
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
