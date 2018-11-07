package com.mcidlegame.plugin;

import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.projectiles.ProjectileSource;

import com.mcidlegame.plugin.data.RoomData;
import com.mcidlegame.plugin.units.ally.Damager;
import com.mcidlegame.plugin.units.ally.PlayerUnit;
import com.mcidlegame.plugin.units.enemy.EnemyUnit;

public class EventListener implements Listener {

	@EventHandler
	public void onJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		player.sendMessage(ChatColor.GREEN + "Welcome.");
		player.setGameMode(GameMode.ADVENTURE);
		player.setCollidable(false);
		WorldUtils.enterWorld(player, Bukkit.getWorld("world"));

		// TODO: maybe do this differently, maybe not
		if (player.getLevel() == 0) {
			player.setLevel(1);

			final ItemStack spawner = new ItemStack(Material.SKULL_ITEM, 1, (byte) 2);
			final ItemMeta spawnerMeta = spawner.getItemMeta();
			spawnerMeta.setDisplayName("Zombie");
			spawnerMeta.setLore(Collections.singletonList("Level: 1"));
			spawner.setItemMeta(spawnerMeta);

			final ItemStack ally = new ItemStack(Material.SNOW_BLOCK);
			final ItemMeta allyMeta = ally.getItemMeta();
			allyMeta.setDisplayName("Snowman");
			allyMeta.setLore(Collections.singletonList("Level: 1"));
			ally.setItemMeta(allyMeta);

			player.getInventory().addItem(spawner);
			player.getInventory().addItem(ally);
		}

		PlayerUnit.apply(player);
		final RoomData data = RoomData.getRoom(player.getLocation().getChunk());
		if (data != null) {
			data.joinRoom(player);
		}
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

		final EnemyUnit enemy = (EnemyUnit) getMetadataValue(event.getEntity(), EnemyUnit.metaString);
		if (enemy == null) {
			return;
		}

		final Entity attacker = getDamager(event);
		final Damager damager = (Damager) getMetadataValue(attacker, Damager.metaString);
		if (damager == null) {
			return;
		}

		enemy.hit(damager);
	}

	private Entity getDamager(final EntityDamageByEntityEvent event) {
		final Entity damager = event.getDamager();
		if (damager instanceof Projectile) {
			final ProjectileSource source = ((Projectile) damager).getShooter();
			if (source instanceof Entity) {
				return (Entity) source;
			} else {
				// TODO: maybe enable support for Dispenser as allies / if so refactor this
				return null;
			}
		}
		return damager;
	}

	private Object getMetadataValue(final Entity entity, final String type) {
		for (final MetadataValue value : entity.getMetadata(type)) {
			if (value.getOwningPlugin() == Main.main) {
				return value.value();
			}
		}
		return null;
	}

	@EventHandler
	public void onDeath(final EntityDeathEvent event) {
		event.setDroppedExp(0);
		event.getDrops().clear();
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
		final Chunk chunk = event.getChunk();
		for (final Entity entity : chunk.getEntities()) {
			if (!(entity instanceof Player)) {
				entity.remove();
			}
		}
		RoomData.checkChunk(chunk);
	}

	@EventHandler
	public void onChunkUnload(final ChunkUnloadEvent event) {
		RoomData.unloadRoom(event.getChunk());
	}

	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		final Chunk chunkfrom = event.getFrom().getChunk();
		final Chunk chunkto = event.getTo().getChunk();
		if (chunkfrom != chunkto) {
			final Player player = event.getPlayer();
			final RoomData from = RoomData.getRoom(chunkfrom);
			final RoomData to = RoomData.getRoom(chunkto);
			if (from != null) {
				from.leaveRoom(player);
			}
			if (to != null) {
				to.joinRoom(player);
			}

		}
	}

	@EventHandler
	public void onInteract(final PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		if (event.getHand() != EquipmentSlot.OFF_HAND) {
			return;
		}
		final Block block = event.getClickedBlock();
		final RoomData data = getRoomData(block);
		if (data != null) {
			data.interact(event.getPlayer(), block);
			return;
		}
		if (block.getType() != Material.JACK_O_LANTERN) {
			return;
		}
		if (event.getPlayer().getInventory().getItemInMainHand().getType() != Material.JACK_O_LANTERN) {
			return;
		}
		WorldUtils.addRoom(block.getChunk());
		PlayerUtils.decreaseItem(event.getPlayer());
	}

	private RoomData getRoomData(final Block block) {
		for (final MetadataValue value : block.getMetadata(RoomData.metaString)) {
			if (value.getOwningPlugin() == Main.main) {
				return (RoomData) value.value();
			}
		}
		return null;
	}

}
