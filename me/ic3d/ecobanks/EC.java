package me.ic3d.ecobanks;

import java.util.logging.Logger;

import me.ic3d.eco.ECOP;
import me.ic3d.eco.ECO;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class EC implements CommandExecutor {
	private final EcoBanks plugin;

  	public EC(EcoBanks instance) {
	  plugin = instance;
  	}
  	//Get the logger
  	private static final Logger log = Logger.getLogger("Minecraft");
  	
  	//The main part of a CommandExecutor class
  	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
  		if(!(sender instanceof Player)) {
  			sender.sendMessage("[3coBanks] This command must be executed by a player!");
  			return true;
  		}
  		Player player = (Player) sender;
  		if(args.length == 0) {
  			Integer balance = plugin.getSavings(player);
  			player.sendMessage(ChatColor.GREEN + "Your savings account balance is: " + balance + " " + plugin.eco.getPluralCurrency());
  		}
  		if(args.length >= 1) {
  			String arg = args[0];
  			if(arg.equals("withdraw")) {
  				if(!plugin.hasPerm(player, "withdraw")) {
  					player.sendMessage(ChatColor.RED + "You don't have permission to do that!");
  					return true;
  				}
  				if(args.length == 1) {
  					player.sendMessage(ChatColor.GREEN + "/eb withdraw <amount>");
  					return true;
  				}
  				if(args.length == 2) {
  					Integer amount = Integer.parseInt(args[1]);
  					Boolean afford = plugin.hasSaved(player, amount);
  					if(!afford == true) {
  						player.sendMessage(ChatColor.RED + "You don't have enough money in your bank account!");
  						return true;
  					}
  					plugin.withdrawMoney(player, amount);
  					plugin.eco.giveMoney(player, amount);
  					Integer balance = plugin.getSavings(player);
  					player.sendMessage(ChatColor.GREEN + "Your savings account balance is: " + balance + " " + plugin.eco.getPluralCurrency());
  				}
  			}
  			if(arg.equals("deposit")) {
  				if(!plugin.hasPerm(player, "deposit")) {
  					player.sendMessage(ChatColor.RED + "You don't have permission to do that!");
  					return true;
  				}
  				if(args.length == 1) {
  					player.sendMessage(ChatColor.GREEN + "/eb deposit <amount>");
  					return true;
  				}
  				if(args.length == 2) {
  					Integer amount = Integer.parseInt(args[1]);
  					Boolean afford = plugin.eco.hasEnough(player, amount);
  					if(!afford == true) {
  						player.sendMessage(ChatColor.RED + "You dont have enough money!");
  						return true;
  					}
  					plugin.eco.takeMoney(player, amount);
  					plugin.depositMoney(player, amount);
  					Integer balance = plugin.getSavings(player);
  					player.sendMessage(ChatColor.GREEN + "Your savings account balance is: " + balance + " " + plugin.eco.getPluralCurrency());
  					return true;
  				}
  			}
  			
  			if(arg.equals("balance")) {
  				if(!plugin.hasPerm(player, "balance")) {
  					player.sendMessage(ChatColor.RED + "You don't have permission to do that!");
  					return true;
  				}
  	  			Integer balance = plugin.getSavings(player);
  	  			player.sendMessage(ChatColor.GREEN + "Your savings account balance is: " + balance + " " + plugin.eco.getPluralCurrency());
  	  			return true;
  			}
  		}
  		return false;
  	}
}