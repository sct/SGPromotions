package com.sgcraft.SGPromotions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.sgcraft.SGPromotions.commands.PromotionCommands;

public class SGPromotions extends JavaPlugin {
	
	public static SGPromotions plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	public static FileConfiguration config;
	public static Permission permission = null;
	public File playerDirectory;
	
	@Override
	public void onDisable() {
		PluginDescriptionFile pdf = this.getDescription();
		logger.info("[" + pdf.getName() + "] is now disabled.");
	}
	
	@Override
	public void onEnable() {
		PluginDescriptionFile pdf = this.getDescription();
		
		
		// Load config / Create default config
		config = getConfig();
        config.options().copyDefaults(true);
		saveConfig();
		
		playerDirectory = new File(getDataFolder(), "players");
		// Create player data folder if it does not already exist
		if (!playerDirectory.exists()) {
			playerDirectory.mkdir();
		}
		
		setupPermissions();
		startListeners();
		addCommands();
		
		logger.info("[" + pdf.getName() + "] v" + pdf.getVersion() + " is now enabled!");
	}
	
	private void startListeners() {
		getServer().getPluginManager().registerEvents(new PromotionPlayerListener(this), this);
	}
	
	private void addCommands() {
		getCommand("sgp").setExecutor(new PromotionCommands(this));
	}
	
	private Boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
	
	public Date getPlayerDate(Player player) {
		String group = permission.getPrimaryGroup(player);
		Date playerDate = null;
		
		if (group.equalsIgnoreCase(config.getString("config.trial-group-name"))) {
			// Let's load the players promotion date and get that saved
			File playerDataFile = new File(playerDirectory, player.getName() + ".txt");
			if (playerDataFile.exists()) {
				try {
					FileInputStream fis = new FileInputStream(playerDataFile);
					BufferedReader br = new BufferedReader(new InputStreamReader(fis));
					
					String dateFromFile = br.readLine();
					playerDate = new SimpleDateFormat("yyyy-MM-dd H:m").parse(dateFromFile);
					
					br.close();
					fis.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		return playerDate;
	}
	
	public Date getPromotionDate(Date playerDate) {
		Date promotionDate = new Date(playerDate.getTime() + (1000 * 60 * 60 * 24 * config.getInt("config.delay-promote")));
		return promotionDate;
	}
	
	public boolean checkPromotion(Date playerDate) {
		Date promotionDate = getPromotionDate(playerDate);
		Date currentDate = new Date();
		if (currentDate.getTime() >= promotionDate.getTime())
			return true;
		else
			return false;
	}

	public String formatPromotion(Date playerDate) {
		Date promotionDate = getPromotionDate(playerDate);
		String formatPromotionDate = new SimpleDateFormat("MM-dd-yyyy 'at' hh:mm a").format(promotionDate);
		return formatPromotionDate;
	}

}
