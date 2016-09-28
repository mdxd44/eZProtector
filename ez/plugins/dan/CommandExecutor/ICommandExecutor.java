package ez.plugins.dan.CommandExecutor;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ez.plugins.dan.Main;

public class ICommandExecutor implements CommandExecutor {
	private String currentVersion = Main.getPlugin().getDescription().getVersion();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String prefix = Main.getPlugin().getConfig().getString("prefix").replaceAll("&", "§");
    	if (command.getName().equalsIgnoreCase("ezp")) {
    		if (args.length == 0) {
    			if (!sender.hasPermission("ezprotector.info")) {
    				sender.sendMessage(ChatColor.RED + "No permission.");
    				return true;
    			}
    			sender.sendMessage("" + ChatColor.DARK_BLUE + ChatColor.STRIKETHROUGH +
    				"-----------------------------------------------------");
    			sender.sendMessage(ChatColor.AQUA + "eZ" + ChatColor.DARK_AQUA + "Protector " +
    				ChatColor.AQUA + "Version: " + ChatColor.WHITE + this.currentVersion);
    			sender.sendMessage(ChatColor.GREEN + "There's only one simple command:");
    			sender.sendMessage(ChatColor.YELLOW + "/ezp reload - Reloads the plugin");
    			sender.sendMessage(ChatColor.AQUA + "The best server protector ever made.");
    			sender.sendMessage("" + ChatColor.DARK_BLUE + ChatColor.STRIKETHROUGH +
    				"-----------------------------------------------------");
    			return true;
    		}
    		if (args.length > 0) {
    			if(args[0].equalsIgnoreCase("reload")) {
    				if (!sender.hasPermission("ezprotector.reload")) {
    					sender.sendMessage(ChatColor.RED + "No permission.");
    					return true;
    				}
    				Main.getPlugin().reloadConfig();
					sender.sendMessage(prefix + "Config reloaded. Plugin version: " + this.currentVersion);
					return true;
    			}
    			sender.sendMessage(ChatColor.RED + "Invalid argument. /ezp");
				return true;
    		}
    		return false;
    	}
    	return false;
	}
}
