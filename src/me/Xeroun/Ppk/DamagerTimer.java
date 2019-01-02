package me.Xeroun.Ppk;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DamagerTimer extends TimerHandler {

	private Player player;
	private Ppk main;

	public DamagerTimer(Player player, Plugin plugin) {
		super(plugin);
		this.main = (Ppk) plugin;
		this.player = player;
	}

	@Override
	public void onEnd(Object obj) {

		if (!(obj instanceof Player)) return;

		Player damager = (Player) obj;

		if (main.Data.containsKey(player)) {
			if (main.Data.get(player).getDamagers().contains(damager)) {
				main.Data.get(player).removeDamager(damager);
			}
		}
	}
}
