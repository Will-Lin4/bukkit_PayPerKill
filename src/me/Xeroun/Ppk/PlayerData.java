package me.Xeroun.Ppk;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerData {

	public PlayerData(Ppk plugin, Player player) {
		this.main = plugin;
		this.player = player;
		dt = new DamagerTimer(player, main);
	}

	private Ppk main;
	private Player player;
	DamagerTimer dt = null;
	public HashMap<Player, Integer> damagers = new HashMap<Player, Integer>();

	public List<Player> getDamagers() {
		List<Player> damagers = new ArrayList<Player>();
		damagers.addAll(this.damagers.keySet());
		return damagers;
	}

	public Player getPlayer() {
		return player;
	}

	public Integer getDamage(Player damager) {
		return damagers.get(damager);
	}

	public Integer getTotalDamage() {
		int total = 0;

		for (Player p : getDamagers()) {
			total += getDamage(p);
		}

		return total;
	}

	public void addDamager(Player damager, int damage) {

		if (damagers.containsKey(damager)) {
			damagers.put(damager, getDamage(damager) + damage);
		} else {
			damagers.put(damager, damage);
		}

		if (dt.getTimer(damager) == null) {
			dt.addTimer(damager);
		}

		dt.getTimer(damager).setDuration(main.getConfig().getInt("Configuration.Reset Timer Duration"));

		if (!dt.getTimer(damager).isRunning()) {
			dt.getTimer(damager).run();
		}

	}

	public void removeDamager(Player damager) {
		if (damagers.containsKey(damager)) {
			damagers.remove(damager);
		}
	}

	public void pay() {

		if (getDamagers() != null) {

			for (Player damager : getDamagers()) {

				if (damager != player) {
					int max;
					double multiplier = 1.0;
					double percent = (double) getDamage(damager) / (double) getTotalDamage();

					NumberFormat format = NumberFormat.getNumberInstance();
					format.setMinimumFractionDigits(1);
					format.setMaximumFractionDigits(1);

					double maxMultiplier = Double.parseDouble(format.format(main.getConfig().getInt("Configuration.Maximum Multiplier Value")));
					Double i = 1.0;

					while (i <= maxMultiplier) {
						i = Double.parseDouble(format.format(i));

						if (damager.hasPermission("ppk.multiplier." + i.toString().split("\\.")[0] + "." + i.toString().split("\\.")[1])) {
							multiplier = i;
						}

						i += 0.1;

					}

					if (Ppk.econ.getBalance(damager.getName()) >= main.getConfig().getInt("Configuration.Maximum Pay")) {
						max = main.getConfig().getInt("Configuration.Maximum Pay");
					} else {
						max = (int) Ppk.econ.getBalance(damager.getName());
					}

					Double amount = (double) (percent * max);
					int amountAfter = (int) (amount.intValue() * multiplier);

					if (multiplier == 1.0)
						damager.sendMessage(ChatColor.AQUA + "You've gained " + ChatColor.GOLD + "$" + amount.intValue() + ChatColor.AQUA + " for damaging " + ChatColor.GOLD + player.getName() + ChatColor.AQUA + ".");
					else
						damager.sendMessage(ChatColor.AQUA + "You've gained " + ChatColor.GOLD + "$" + amountAfter + ChatColor.AQUA + " (Multiplier: " + ChatColor.GOLD + multiplier + "x" + ChatColor.AQUA + ") for damaging " + ChatColor.GOLD + player.getName() + ChatColor.AQUA + ".");
					Ppk.econ.depositPlayer(damager.getName(), amountAfter);
				}

			}
		}

	}

	public void reset() {
		damagers.clear();
	}

}
