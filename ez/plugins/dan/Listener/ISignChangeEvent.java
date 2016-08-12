package ez.plugins.dan.Listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import ez.plugins.dan.Main;

public class ISignChangeEvent implements Listener {
	private FileConfiguration config = Main.getPlugin().getConfig();

	@EventHandler(priority = EventPriority.HIGHEST)
    public void onSignChange(SignChangeEvent e) {
    	if(config.getBoolean("block-anti-sign-hack.enabled")){
			
    		Player p = e.getPlayer();
    		String pName = p.getName();
			  
    		for (int i = 0; i < 4; i++)
    			if (e.getLine(i).matches("^[a-zA-Z0-9_]*$")) {
    				if (e.getLine(i).toString().length() > 16) {
    					e.setCancelled(true);
    					if (!p.hasPermission("ezprotector.bypass.signhack")) {
    						ConsoleCommandSender console = Main.getPlugin().getServer().getConsoleSender();
    						Bukkit.getServer().broadcast(ChatColor.translateAlternateColorCodes('&', config.getString
    								("block-anti-sign-hack.warning-message").replace("%player%", pName)), "ezprotector.notify.signhack");
							  
    						Bukkit.getServer().dispatchCommand(console, ChatColor.translateAlternateColorCodes('&', config.getString
    								("block-anti-sign-hack.punish-command")).replace("%player%", pName));
    					}
    				}
    			} else if (e.getLine(i).matches("^[a-zA-Z0-9_]*$")) {
    				if (e.getLine(i).length() > 20) {
    					e.setCancelled(true);
    					if (!p.hasPermission("ezprotector.bypass.signhack")) {
    						ConsoleCommandSender console = Main.getPlugin().getServer().getConsoleSender();
    						Bukkit.getServer().broadcast(ChatColor.translateAlternateColorCodes('&', config.getString
    								("block-anti-sign-hack.warning-message").replace("%player%", pName)), "ezprotector.notify.signhack");
							  
    						Bukkit.getServer().dispatchCommand(console, ChatColor.translateAlternateColorCodes('&', config.getString
    								("block-anti-sign-hack.punish-command")).replace("%player%", pName));
    					}
    				}
    			} else if (e.getLine(i).length() > 50) {
    				e.setCancelled(true);
    				if (!p.hasPermission("ezprotector.bypass.signhack")) {
    					ConsoleCommandSender console = Main.getPlugin().getServer().getConsoleSender();
    					Bukkit.getServer().broadcast(ChatColor.translateAlternateColorCodes('&', config.getString
    							("block-anti-sign-hack.warning-message").replace("%player%", pName)), "ezprotector.notify.signhack");
    					
    					Bukkit.getServer().dispatchCommand(console, ChatColor.translateAlternateColorCodes('&', config.getString
    							("block-anti-sign-hack.punish-command")).replace("%player%", pName));
    				}
    			}
    	}
    }
}
