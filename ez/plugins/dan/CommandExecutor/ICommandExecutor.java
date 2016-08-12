package ez.plugins.dan.CommandExecutor;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ez.plugins.dan.Main;

public class ICommandExecutor implements CommandExecutor {
	private FileConfiguration config = Main.getPlugin().getConfig();
	private String currentVersion = Main.getPlugin().getDescription().getVersion();
	
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	String prefix = config.getString("prefix").replaceAll("&", "§");
    	Player p = null;
    	if (sender instanceof Player) {
    		p = (Player)sender;
    	}
    	if (command.getName().equalsIgnoreCase("ezp")) {
    		if (p != null) {
    			if (args.length == 0) {
    				if (p.hasPermission("ezprotector.info")) {
    					p.sendMessage("" + ChatColor.DARK_BLUE + ChatColor.STRIKETHROUGH +
    						"-----------------------------------------------------");
    					p.sendMessage(ChatColor.AQUA + "eZ" + ChatColor.DARK_AQUA + "Protector " +
    						ChatColor.AQUA + "Version: " + ChatColor.WHITE + this.currentVersion);
    					p.sendMessage(ChatColor.GREEN + "There's only one simple command:");
    					p.sendMessage(ChatColor.YELLOW + "/ezp reload - Reloads the plugin");
    					p.sendMessage(ChatColor.AQUA + "The best server protector ever made.");
    					p.sendMessage("" + ChatColor.DARK_BLUE + ChatColor.STRIKETHROUGH +
    						"-----------------------------------------------------");
    					return true;
    				} else {
    					p.sendMessage(ChatColor.RED + "No permission.");
    					return true;
    				}
    			}
    			if(args[0].equalsIgnoreCase("reload")) {
    				if (p.hasPermission("ezprotector.reload")) {
    					Main.getPlugin().reloadConfig();
    					p.sendMessage(prefix + "Config reloaded. Plugin version: " + this.currentVersion);
    					return true;
    				} else {
    					p.sendMessage(ChatColor.RED + "No permission.");
    					return true;
    				}
    			}
    			if((args.length > 0) && !(args[0].equalsIgnoreCase("reload"))) {
    				sender.sendMessage(ChatColor.RED + "Wrong arguments. /ezp");
    				return true;
    			}
    		} else {
    			if (args.length == 0) {
    				sender.sendMessage(ChatColor.DARK_BLUE + "-----------------------------------------------------");
    				sender.sendMessage(ChatColor.AQUA + "eZ" + ChatColor.DARK_AQUA + "Protector " + ChatColor.AQUA +
    						"Version: " + ChatColor.WHITE + this.currentVersion);
    				sender.sendMessage(ChatColor.GREEN + "There's only one simple command:");
    				sender.sendMessage(ChatColor.YELLOW + "/ezp reload - Reloads the plugin");
    				sender.sendMessage(ChatColor.AQUA + "The best server protector ever made.");
    				sender.sendMessage(ChatColor.DARK_BLUE + "-----------------------------------------------------");
    				return true;
    			}
    			if(args[0].equalsIgnoreCase("reload")) {
    				Main.getPlugin().reloadConfig();
    				sender.sendMessage(ChatColor.GREEN + "[" + Main.getPlugin().getDescription().getName() + "]" + " Config reloaded. Version: " 
    					+ this.currentVersion);
    				return true;
    			}
    			if ((args.length > 0) && (!args[0].equalsIgnoreCase("reload"))) {
    				sender.sendMessage(ChatColor.RED + "Wrong arguments. /ezp");
    				return true;
    			}
    		}
    	}
    	return false;
    }
}
