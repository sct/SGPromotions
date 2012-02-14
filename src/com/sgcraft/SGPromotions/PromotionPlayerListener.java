package com.sgcraft.SGPromotions;

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
		
		// Here we will check files to see if player exists already in said files.
	}

}
