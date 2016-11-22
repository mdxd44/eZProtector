/*
Copyright (c) 2016 dvargas135

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package ez.plugins.dan.Listener;

import java.io.UnsupportedEncodingException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import ez.plugins.dan.Main;

public class IPluginMessageListener implements PluginMessageListener {
	
	public void onPluginMessageReceived(String channel, Player player, byte[] value) {
    	String pName = player.getName();
    	FileConfiguration config = Main.getPlugin().getConfig();
    	ConsoleCommandSender console = Main.getPlugin().getServer().getConsoleSender();
		  
    	if (config.getBoolean("mods.bettersprinting.block")) {
    		if (!player.hasPermission("ezprotector.bypass.mod.bettersprinting")) {
    			if (config.getBoolean("mods.bettersprinting.block")) {
    				if (channel.equalsIgnoreCase(Main.BSM)) {
    					ByteArrayDataOutput out1 = ByteStreams.newDataOutput();
    					out1.writeByte(1);
    					player.sendPluginMessage(Main.getPlugin(), Main.BSM, out1.toByteArray());
    				}
    			}
    		}
    	}
    	if (config.getBoolean("mods.5zig.block")) {
    		if (!player.hasPermission("ezprotector.bypass.mod.5zig")) {
    			if ((channel.equalsIgnoreCase(Main.ZIG)) || (channel.contains("5zig"))) {
    				ByteArrayDataOutput out1 = ByteStreams.newDataOutput();
    				out1.writeByte(0x01);
    				ByteArrayDataOutput out2 = ByteStreams.newDataOutput();
    				out2.writeByte(0x02);
    				ByteArrayDataOutput out4 = ByteStreams.newDataOutput();
    				out4.writeByte(0x04);
    				ByteArrayDataOutput out8 = ByteStreams.newDataOutput();
    				out8.writeByte(0x08);
    				ByteArrayDataOutput out10 = ByteStreams.newDataOutput();
    				out10.writeByte(0x010);
					  
    				ByteArrayDataOutput all = ByteStreams.newDataOutput();
    				all.writeByte(0x01 | 0x02 | 0x04 | 0x08 | 0x010);
					  	
    				player.sendPluginMessage(Main.getPlugin(), Main.ZIG, out1.toByteArray());
    				player.sendPluginMessage(Main.getPlugin(), Main.ZIG, out2.toByteArray());
    				player.sendPluginMessage(Main.getPlugin(), Main.ZIG, out4.toByteArray());
    				player.sendPluginMessage(Main.getPlugin(), Main.ZIG, out8.toByteArray());
    				player.sendPluginMessage(Main.getPlugin(), Main.ZIG, out10.toByteArray());
    				player.sendPluginMessage(Main.getPlugin(), Main.ZIG, all.toByteArray());
    			}
    		}
    	}
		  
    	if (config.getBoolean("mods.wdl.block")) {
    		if (!player.hasPermission("ezprotector.bypass.mod.wdl")) {
    			if ((channel.equalsIgnoreCase("WDL|INIT")) || (channel.equalsIgnoreCase("WDL|CONTROL")) || (channel.contains("WDL"))) {
    				Bukkit.getServer().broadcast(ChatColor.translateAlternateColorCodes('&', config.getString
    						("mods.wdl.warning-message").replace("%player%", pName)), "ezprotector.notify.mod.wdl");
					  	
    				Bukkit.getServer().dispatchCommand(console, ChatColor.translateAlternateColorCodes('&', config.getString
    						("mods.wdl.punish-command")).replace("%player%", pName));
    			}
    		}
    	}
    	if (channel.equalsIgnoreCase("MC|Brand")) {
    		String brand;
    		try {
    			brand = new String(value, "UTF-8");
    		} catch (UnsupportedEncodingException e) {
    			throw new Error(e);
    		}
    		if (config.getBoolean("mods.wdl.block")) {
    			if (!player.hasPermission("ezprotector.bypass.mod.wdl")) {
    				if (brand.equalsIgnoreCase("worlddownloader-vanilla")) {
    					Bukkit.getServer().broadcast(ChatColor.translateAlternateColorCodes('&', config.getString
    							("mods.wdl.warning-message").replace("%player%", pName)), "ezprotector.notify.mod.wdl");
						  
    					Bukkit.getServer().dispatchCommand(console, ChatColor.translateAlternateColorCodes('&', config.getString
    							("mods.wdl.punish-command")).replace("%player%", pName));
    				}
    			}
    		}
    		if (config.getBoolean("mods.forge.block")) {
    			if (!player.hasPermission("ezprotector.bypass.mod.forge")) {
    				if ((brand.contains("fml")) || (brand.contains("forge")) || (brand.equalsIgnoreCase("fml,forge"))) {
    					Bukkit.getServer().broadcast(ChatColor.translateAlternateColorCodes('&', config.getString
    							("mods.forge.warning-message").replace("%player%", pName)), "ezprotector.notify.mod.forge");
						  	
    					Bukkit.getServer().dispatchCommand(console, ChatColor.translateAlternateColorCodes('&', config.getString
    							("mods.forge.punish-command")).replace("%player%", pName));
    				}
    			}
    		}
    		if (config.getBoolean("mods.liteloader.block")) {
    			if (!player.hasPermission("ezprotector.bypass.mod.liteloader")) {
    				if ((brand.contains("LiteLoader")) || (brand.equalsIgnoreCase("LiteLoader"))) {
    					Bukkit.getServer().broadcast(ChatColor.translateAlternateColorCodes('&', config.getString
    							("mods.liteloader.warning-message").replace("%player%", pName)), "ezprotector.notify.mod.liteloader");
				  
    					Bukkit.getServer().dispatchCommand(console, ChatColor.translateAlternateColorCodes('&', config.getString
    							("mods.liteloader.punish-command")).replace("%player%", pName));
    				}
    			}
    		}
    	}
    }
}
