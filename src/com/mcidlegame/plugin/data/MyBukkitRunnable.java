package com.mcidlegame.plugin.data;

import org.bukkit.scheduler.BukkitRunnable;

public class MyBukkitRunnable extends BukkitRunnable {

    private final Runnable run;

    public MyBukkitRunnable(Runnable run) {
        this.run = run;
    }

    @Override
    public void run() {
        run.run();
    }
}
