package com.thecatbutt;

import java.time.LocalTime;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import net.kyori.adventure.text.Component;

public class DinnerBell extends JavaPlugin implements Listener {
	private int hour;
	@Override
	public void onEnable() {
		BukkitScheduler scheduler = this.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {
				int now = LocalTime.now().getHour();
				if (now != hour) { // hour has changed
					getServer().getLogger().log(Level.INFO, "Hour has changed");
					hour = now;
					int normalizedHour = hour;
					// only do it if there are players active
					if (Bukkit.getOnlinePlayers().size() > 0) {
						String ampm = " AM";
						if (normalizedHour >= 12) {
							ampm = " PM";
							if (normalizedHour > 12)
								normalizedHour = normalizedHour - 12;
						}
						String message = "It is now " + normalizedHour + ampm;
						for (Player player : Bukkit.getOnlinePlayers()) {
							try {
								player.playSound(player.getLocation(), Sound.BLOCK_BELL_RESONATE, 1.0f, 1.0f);
							} catch (Exception e) {
								// swallow, maybe the player logged out
							}
						}
						getServer().broadcast(Component.text(message));
					}
				}
			}
		}, 0, 20 * 5);
	}

	@Override
	public void onDisable() {
			this.getServer().getScheduler().cancelTasks(this);
	}
	
}
