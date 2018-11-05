package com.mcidlegame.plugin.units.friend;

import java.util.function.IntUnaryOperator;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.mcidlegame.plugin.Main;
import com.mcidlegame.plugin.units.Damager;

public abstract class ShooterUnit extends AllyUnit implements Damager {

	// TODO: find an appropriate growth value
	private static final IntUnaryOperator damageGrowth = n -> n;
	// TODO: write lvl / wave in file
	private final int damage;
	private final long attackrate;
	private final Class<? extends Projectile> projectile;
	private BukkitTask shooting = null;

	public ShooterUnit(final String name, final EntityType type, final Location location, final int level,
			final long attackrate, final Class<? extends Projectile> projectile, final double damageModifier) {
		super(name, type, location, level);
		this.attackrate = attackrate;
		this.projectile = projectile;
		this.damage = (int) (damageGrowth.applyAsInt(level) * damageModifier);
	}

	@Override
	public void onSpawn() {
		this.entity.setMetadata(metaString, new FixedMetadataValue(Main.main, this));
	}

	@Override
	public void onRemove() {
		if (!this.shooting.isCancelled()) {
			this.shooting.cancel();
		}
	}

	public void startShooting() {
		this.shooting = new BukkitRunnable() {
			@Override
			public void run() {
				ShooterUnit.this.entity.launchProjectile(ShooterUnit.this.projectile);
			}
		}.runTaskTimer(Main.main, this.attackrate, this.attackrate);
	}

	public void stopShooting() {
		if (this.shooting != null) {
			if (!this.shooting.isCancelled()) {
				this.shooting.cancel();
			}
		}
	}

	@Override
	public int getDamage() {
		return this.damage;
	}

}
