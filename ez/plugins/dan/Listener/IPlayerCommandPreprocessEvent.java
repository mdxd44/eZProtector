package ez.plugins.dan.Listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import ez.plugins.dan.Main;

public class IPlayerCommandPreprocessEvent implements Listener {
	private FileConfiguration config = Main.getPlugin().getConfig();
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onCommand(PlayerCommandPreprocessEvent event) {
		
		Player player = event.getPlayer();
		String prefix = config.getString("prefix").replaceAll("&", "§");
		String command = event.getMessage();
		
		boolean plugins = command.split(" ")[0].toLowerCase().equals("/plugins");
    		boolean pl = command.split(" ")[0].toLowerCase().equals("/pl");
		
		for (int i = 0; i < config.getList("block-commands.commands").size(); i++) {
			String playerCommand = config.getList("block-commands.commands").get(i).toString();
			if (command.split(" ")[0].toLowerCase().equals("/" + playerCommand.toUpperCase())) {
				if(!player.hasPermission("ezprotector.bypass.blocked-commands")) {
					event.setCancelled(true);
					player.sendMessage(config.getString("error-message").replaceAll("&", "§"));
					for (Player p : Bukkit.getOnlinePlayers()){
						if (p.hasPermission("ezprotector.notify.blocked-commands")) {
							p.sendMessage(prefix + config.getString("default-notify-message").replaceAll("&","§").replaceAll("%command%", new StringBuilder("/").append(playerCommand).toString()).replaceAll("%player%", player.getName()));
						}
					}
				}
			}
		}
    	if(config.getBoolean("block-hidden-syntaxes.enabled")) {
    		if (event.getMessage().split(" ")[0].contains(":")) {
    			if(!player.hasPermission("ezprotector.bypass.hiddensyntaxes")) {
    				event.setCancelled(true);
    				player.sendMessage(config.getString("block-hidden-syntaxes.message").replaceAll("&", "§"));
    				for (Player p : Bukkit.getOnlinePlayers()){
    					if (config.getBoolean("block-hidden-syntaxes.notify")) {
    						if (p.hasPermission("ezprotector.notify.hiddensyntaxes")) {
    							p.sendMessage(prefix + config.getString("block-hidden-syntaxes.notify-message").replaceAll("&", "§").replaceAll("%command%", command).replaceAll("%player%", player.getName()));
    						}
    					}
    				}
    			}
    		}
    	}
    	if (config.getBoolean("block-op-commands.enabled")) {
    		if(player.isOp()) {
    			for (int iop = 0; iop < config.getList("block-op-commands.bypassed-players").size(); iop++) {
    				String opped = config.getList("block-op-commands.bypassed-players").get(iop).toString();
    				if(!opped.contains(player.getName())) {
    					for (int i = 0; i < config.getList("block-op-commands.commands").size(); i++) {
    						String opcommand = config.getList("block-op-commands.commands").get(i).toString;
    						if (command.split(" ")[0].toLowerCase().equals("/" + opcommand.toUpperCase())) {
    							Player p = event.getPlayer();
    							p.sendMessage(config.getString("block-op-commands.message").replaceAll("&", "§"));
    							event.setCancelled(true);
    							for (int iops = 0; iops < config.getList("block-op-commands.bypassed-players").size(); iops++) {
    								String oppedPlayers = config.getList("block-op-commands.bypassed-players").get(iops).toString();
    								Player ops = Bukkit.getPlayer(oppedPlayers);
    								if (config.getBoolean("block-op-commands.notify")) {
    									if (ops != null) {
    										ops.sendMessage(prefix + config.getString("block-op-commands.notify-message").replaceAll("&", "§").replaceAll("%command%", new StringBuilder("/").append(opcommand).toString()).replaceAll("%player%", player.getName()));
    									}
    								}
    							}
    						}
    					}
    				}
    			}
    		}
    	}
		if (config.getBoolean("custom-plugins.enabled")) { 
			if ((plugins) || (pl)) {
				if(!player.hasPermission("ezprotector.bypass.command.plugins")) {
					event.setCancelled(true);
					String defaultMessage = "§a";
					for (String plugin : Main.plugins) {
						defaultMessage = defaultMessage + plugin + ", ";
					}
					defaultMessage = defaultMessage.substring(0, defaultMessage.lastIndexOf(", "));
					player.sendMessage(ChatColor.WHITE + "Plugins (" + Main.plugins.size() + "): " + ChatColor.GREEN + defaultMessage.replaceAll(", ", new StringBuilder().append(ChatColor.WHITE).append(", ").append(ChatColor.GREEN).toString()));
					for (Player p : Bukkit.getOnlinePlayers()){
						if (config.getBoolean("custom-plugins.notify")){
							if (p.hasPermission("ezprotector.notify.command.plugins")){
								p.sendMessage(prefix + config.getString("default-notify-message").replaceAll("&", "§").replaceAll("%command%", command).replaceAll("%player%", player.getName()));
							}
						}
					}
				} 	
			}
		} else {
			if ((plugins) || (pl)) {
				if(!player.hasPermission("ezprotector.bypass.command.plugins")) {
					event.setCancelled(true);
					player.sendMessage(config.getString("custom-plugins.message").replaceAll("&", "§"));
					for (Player p : Main.getPlugin().getServer().getOnlinePlayers()) {
						if (config.getBoolean("custom-plugins.notify")) {
							if (p.hasPermission("ezprotector.notify.command.plugins")) {
								p.sendMessage(prefix + config.getString("default-notify-message").replaceAll("&", "§").replaceAll("%command%", command).replaceAll("%player%", player.getName()));
							}
						}
					}
				}
			}
		}
	}
}
