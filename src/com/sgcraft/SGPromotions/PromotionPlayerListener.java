package com.sgcraft.SGPromotions;

import java.io.File;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PromotionPlayerListener implements Listener {
	
	public static SGPromotions plugin;
	
	public PromotionPlayerListener(SGPromotions instance) {
		plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String group = SGPromotions.permission.getPrimaryGroup(player);
		
		if (group.equalsIgnoreCase(SGPromotions.config.getString("config.trial-group-name"))) {
			// Let's load the players promotion date and get that saved
			File playerDataFile = new File(plugin.playerDirectory, player.getName() + ".txt");
			if (playerDataFile.exists()) {
				
				Date playerDate = plugin.getPlayerDate(player);
				
				if (plugin.checkPromotion(playerDate)) {
					SGPromotions.permission.playerAddGroup(player, SGPromotions.config.getString("config.member-group-name"));
	        		SGPromotions.permission.playerRemoveGroup(player, SGPromotions.config.getString("config.trial-group-name"));
					player.sendMessage(ChatColor.RED + "[SGPromotions] " + ChatColor.WHITE + "You have been promoted! You are now a full Member!");
					playerDataFile.delete();
					plugin.logger.info("[SGPromotions] " + player.getName() + " has been automatically promoted to a Member.");
				} else {
					player.sendMessage(ChatColor.RED + "[SGPromotions] " + ChatColor.WHITE + "You are currently still a " + ChatColor.RED + "Trial"
							+ ChatColor.WHITE + " Member");
					player.sendMessage(ChatColor.RED + "[SGPromotions] " + ChatColor.WHITE + "You will be promoted to a full member on "
							+ ChatColor.BLUE + plugin.formatPromotion(playerDate));
				}
			}
		}
	}

}
