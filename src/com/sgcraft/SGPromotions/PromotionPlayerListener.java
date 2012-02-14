package com.sgcraft.SGPromotions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PromotionPlayerListener implements Listener {
	
	public static SGPromotions plugin;
	
	public PromotionPlayerListener(SGPromotions instance) {
		plugin = instance;
	}
	
	public void onPlayerJoin(final PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		String group = SGPromotions.permission.getPrimaryGroup(player);
		if (group.equalsIgnoreCase(SGPromotions.config.getString("config.trial-group-name"))) {
			// Let's load the players promotion date and get that saved
			File playerDataFile = new File(plugin.playerDirectory, player.getName() + ".txt");
			try {
				FileInputStream fis = new FileInputStream(playerDataFile);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				
				String dateFromFile = br.readLine();
				DateFormat dateformat = new SimpleDateFormat();
				Date playerDate = dateformat.parse(dateFromFile);
				Date currentDate = new Date();
				
				Date promotionDate = new Date();
				promotionDate.setTime(playerDate.getTime() + (1000 * 60 * 60 * 24 * 14));
				
				DateFormat promotionFormat = new SimpleDateFormat("yyyy-MM-dd H:m");
				String promoDate = promotionFormat.format(promotionDate);
				
				if (currentDate.getTime() >= promotionDate.getTime()) {
					SGPromotions.permission.playerAddGroup(player, SGPromotions.config.getString("config.member-group-name"));
	        		SGPromotions.permission.playerRemoveGroup(player, SGPromotions.config.getString("config.trial-group-name"));
					player.sendMessage(ChatColor.RED + "[SGPromotions] " + ChatColor.WHITE + "You have been promoted! You are now a full Member!");
				} else {
					player.sendMessage(ChatColor.RED + "[SGPromotions] " + ChatColor.WHITE + "You are currently still a " + ChatColor.RED + "Trial"
							+ ChatColor.WHITE + " Member");
					player.sendMessage(ChatColor.RED + "[SGPromotions] " + ChatColor.WHITE + "You will be promoted to a full member on "
							+ ChatColor.BLUE + promoDate);
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

}
