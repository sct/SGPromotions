package com.sgcraft.SGPromotions;

import java.io.File;
import java.util.logging.Logger;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.configuration.file.FileConfiguration;
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
	

}
