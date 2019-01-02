package me.Xeroun.Ppk;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PpkListener implements Listener {

	Ppk main;

	public PpkListener(Ppk ppk) {
		main = ppk;
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent event) {

		if (event.getEntity() instanceof Player) {
			
			Player player = (Player) event.getEntity();
			
			if (player.getNoDamageTicks() < player.getMaximumNoDamageTicks() / 2) {
				
				if (event.getDamager() instanceof Player) {
					
					Player damager = (Player) event.getDamager();
					int damage = (int) Math.round(event.getDamage());
					int health = (int) Math.round(player.getHealth());

					if (20 >= damage + 20 - health) {
						if (main.Data.containsKey(player)) {
							PlayerData pi = main.Data.get(player);
							pi.addDamager(damager, damage);
							main.Data.put(player, pi);
						} else if (!main.Data.containsKey(player)) {
							PlayerData pi = new PlayerData(main, player);
							pi.addDamager(damager, damage);
							main.Data.put(player, pi);
						}
					} else if (20 <= damage + 20 - health) {
						if (!main.Data.containsKey(player)) {
							PlayerData pi = new PlayerData(main, player);
							pi.addDamager(damager, health);
							main.Data.put(player, pi);
						} else if (main.Data.containsKey(player)) {
							PlayerData pi = main.Data.get(player);
							pi.addDamager(damager, health);

							main.Data.put(player, pi);
						}

					}
				} else if (event.getDamager() instanceof Projectile) {

					Projectile proj = (Projectile) event.getDamager();
					Entity shooterEntity = (Entity) proj.getShooter();

					if (shooterEntity instanceof Player) {

						int damage = (int) Math.round(event.getDamage());
						int health = (int) Math.round(player.getHealth());
						Player damager = (Player) shooterEntity;

						if (20 >= damage + 20 - health) {
							if (main.Data.containsKey(player)) {
								PlayerData pi = main.Data.get(player);
								pi.addDamager(damager, damage);
								main.Data.put(player, pi);
							} else if (!main.Data.containsKey(player)) {
								PlayerData pi = new PlayerData(main, player);
								pi.addDamager(damager, damage);
								main.Data.put(player, pi);
							}
						} else if (20 <= damage + 20 - health) {
							if (!main.Data.containsKey(player)) {
								PlayerData pi = new PlayerData(main, player);
								pi.addDamager(damager, health);
								main.Data.put(player, pi);
							} else if (main.Data.containsKey(player)) {
								PlayerData pi = main.Data.get(player);
								pi.addDamager(damager, health);

								main.Data.put(player, pi);
							}
						}
					}

				}
			}
		}

	}

	@EventHandler()
	public void onDeath(PlayerDeathEvent event) {

		Player player = event.getEntity();
		if (main.Data.containsKey(player)) {
			PlayerData data = main.Data.get(player);
			data.pay();
			data.reset();
		}

	}

}
