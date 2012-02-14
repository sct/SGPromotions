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
        
        if (promotionCommand("promote",args,sender) && (args.length > 1)) {
        	
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
        		SGPromotions.permission.playerRemoveGroup(target, SGPromotions.config.getString("config.guest-group-name"));
        		// Create file for player here
        		
        		File playerDataFile = new File(plugin.playerDirectory, target.getName() + ".txt");
        		
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
        	target.sendMessage(ChatColor.RED + "[SGPromotions] " + ChatColor.WHITE + "You have been promoted to Trial status by " +
        						ChatColor.BLUE + sender.getName() + ChatColor.WHITE + "!");
        	sender.sendMessage(ChatColor.RED + "[SGPromotions] " + ChatColor.BLUE + 
        						target.getName() + ChatColor.WHITE + " has been promoted to Trial status!");
        	return true;
        }
        
        if (promotionCommand("demote",args,sender) && (args.length > 1)) {
        	// Get Target Player
        	Player target = (Bukkit.getServer().getPlayer(args[1]));
        	if (target == null) {
        		sender.sendMessage(ChatColor.RED + "[SGPromotions] User does not exist or is offline!");
        		return true;
        	}
        	
        	if (SGPromotions.permission.getPrimaryGroup(target).equalsIgnoreCase(SGPromotions.config.getString("config.trial-group-name"))) {
        		SGPromotions.permission.playerAddGroup(target, SGPromotions.config.getString("config.guest-group-name"));
        		SGPromotions.permission.playerRemoveGroup(target, SGPromotions.config.getString("config.trial-group-name"));
        		sender.sendMessage(ChatColor.RED + "[SGPromotions] " + ChatColor.BLUE + 
						target.getName() + ChatColor.WHITE + " has been demoted back to a guest!");
        		File playerDataFile = new File(plugin.playerDirectory, target.getName() + ".txt");
        		playerDataFile.delete();
        	} else {
        		sender.sendMessage(ChatColor.RED + "[SGPromotions] User is not in the trial group!");
        	}
        	return true;
        }
        
        if (promotionCommand("vouch",args,sender) && (args.length > 1)) {
        	Player target = (Bukkit.getServer().getPlayer(args[1]));
        	if (target == null) {
        		sender.sendMessage(ChatColor.RED + "[SGPromotions] User does not exist or is offline!");
        		return true;
        	}
        	
        	if (SGPromotions.permission.getPrimaryGroup(target).equalsIgnoreCase(SGPromotions.config.getString("config.trial-group-name"))) {
        		SGPromotions.permission.playerAddGroup(target, SGPromotions.config.getString("config.member-group-name"));
        		SGPromotions.permission.playerRemoveGroup(target, SGPromotions.config.getString("config.trial-group-name"));
        		sender.sendMessage(ChatColor.RED + "[SGPromotions] " + ChatColor.WHITE + "You have vouched for " + ChatColor.BLUE + 
						target.getName() + ChatColor.WHITE + "! They are now a full member!");
        		target.sendMessage(ChatColor.RED + "[SGPromotions] " + ChatColor.BLUE + sender.getName() + ChatColor.WHITE + " vouched for you!"
        				+ " You are now a full member!");
        		File playerDataFile = new File(plugin.playerDirectory, target.getName() + ".txt");
        		playerDataFile.delete();
        	}
        	return true;
        }
        
        if (promotionCommand("status",args,sender) && (args.length >= 1)) {
        	Player player = null;
        	Boolean self = false;
        	if (args.length > 1) {
        		player = (Bukkit.getServer().getPlayer(args[1]));
        	} else {
        		player = (Bukkit.getServer().getPlayer(sender.getName()));
        		self = true;
        	}
        	
        	if (player == null) {
        		sender.sendMessage(ChatColor.RED + "[SGPromotions] User does not exist or is offline!");
        		return true;
        	}
        	
        	if (SGPromotions.permission.getPrimaryGroup(player).equalsIgnoreCase(SGPromotions.config.getString("config.trial-group-name"))) {
        		Date playerDate = plugin.getPlayerDate(player);
        		if (self == true)
        			sender.sendMessage(ChatColor.RED + "[SGPromotions] " + ChatColor.WHITE + "Promotion Date: " + ChatColor.BLUE +
        				plugin.formatPromotion(playerDate));
        		else
        			sender.sendMessage(ChatColor.RED + "[SGPromotions] " + ChatColor.BLUE + player.getName() + ChatColor.WHITE + "'s promotion Date: " + ChatColor.BLUE +
            				plugin.formatPromotion(playerDate));
        	} else {
        		sender.sendMessage(ChatColor.RED + "[SGPromotions] This command is only for Trial members!");
        	}
        	return true;
        }
		
		return false;
	}
	
	private boolean promotionCommand(String label,String[] args, CommandSender sender) {
		if (args[0].equalsIgnoreCase(label) && (sender.isOp() || sender.hasPermission("sgpromotions." + label)))
			return true;
		else
			return false;
	}
	
}
