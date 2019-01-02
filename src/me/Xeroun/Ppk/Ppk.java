package me.Xeroun.Ppk;

import java.util.HashMap;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Ppk extends JavaPlugin {

	public HashMap<Player, PlayerData> Data = new HashMap<Player, PlayerData>();
	public static Economy econ = null;
	public final PpkListener listener = new PpkListener(this);

	public void info(String info) {
		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println(pdfFile.getName() + " " + "v" + pdfFile.getVersion() + " " + info);
	}

	public void onEnable() {

		if (!setupEconomy()) {
			System.out.println("[PayPerKill] This plugin requires Vault to function!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		info("Enabled!");
		getServer().getPluginManager().registerEvents(listener, this);
		getConfig().options().copyDefaults(true);
		saveConfig();

	}

	public void onDisable() {
		info("Disabled!");
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

}
