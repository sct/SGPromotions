package com.sgcraft.SGPromotions.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.command.ColouredConsoleSender;
import org.bukkit.entity.Player;

import com.sgcraft.SGPromotions.SGPromotions;

public class PromotionCommands implements CommandExecutor {
	public static SGPromotions plugin;
	
	public PromotionCommands (SGPromotions instance) {
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender instanceof ColouredConsoleSender)
        {
			sender.sendMessage("Sorry, you can not run these commands from the console!");
            return true;
        }
		
        if (args.length == 0) {
            return false;
        }
        
        if (args[0].equalsIgnoreCase("promote") && args[1].length() > 0 && (sender.isOp() || sender.hasPermission("sgpromotions.promote"))) {
        	// Get Target Player
        	Player target = (Bukkit.getServer().getPlayer(args[1]));
        	if (target == null) {
        		sender.sendMessage(ChatColor.RED + "[SGPromotions] User does not exist or is offline!");
        		return true;
        	}
        	if (!SGPromotions.permission.playerInGroup(target,SGPromotions.config.getString("config.guest-group-name"))) {
        		sender.sendMessage(ChatColor.RED + "[SGPromotions] This user is not a Guest!");
        		return true;
        	} else {
        		SGPromotions.permission.playerAddGroup(target, SGPromotions.config.getString("config.trial-group-name"));
        		// Create file for player here
        		
        		File playerDataFile = new File(plugin.playerDirectory, target.getName() + ".yml");
        		
        		if (!playerDataFile.exists()) {
        			try {
        				playerDataFile.createNewFile();
        			} catch (IOException e) {
        				e.printStackTrace();
        			}
        		}
        		
        		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m");
        		Date date = new Date();
        		String data = dateFormat.format(date);
        		
        		try {
        			FileWriter fw = new FileWriter(playerDataFile);
        			BufferedWriter out = new BufferedWriter(fw);
        			out.write(data);
        			out.close();
        		} catch (FileNotFoundException e) {
        			e.printStackTrace();
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
        	}
        	sender.sendMessage(ChatColor.RED + "[SGPromotions] " + ChatColor.BLUE + 
        						target.getName() + ChatColor.WHITE + " has been promoted to Trial status!");
        	return true;
        }
		
		return false;
	}
	

}
