package com.mcidlegame.plugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Game {

    private static LivingEntity monster = null;
    private static final List<LivingEntity> allies = new ArrayList<>();

    public static void startGame() {
        final World world = Bukkit.getWorld("world");
        for (final Entity entity : world.getEntities()) {
            if (!(entity instanceof Player)) {
                entity.remove();
            }
        }

        /*
         * final Snowman snowman = (Snowman) world.spawnEntity(new Location(world, 4.5,
         * 66, 0.5), EntityType.SNOWMAN); snowman.addPotionEffect(new
         * PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 30));
         * snowman.setCollidable(false); snowman.setMetadata(AllyUnit.roleString, new
         * FixedMetadataValue(Main.main, new SnowmanUnit(1))); allies.add(snowman);
         *
         * snowman.setCustomName("Snowman"); snowman.setCustomNameVisible(true);
         *
         * spawnMonster(new Location(world, 0.5, 66, 0.5), 1);
         */

    }

    public static void checkDeath(final Entity entity) {
        if (entity.equals(monster)) {
            monster = null;
        }
    }

}
