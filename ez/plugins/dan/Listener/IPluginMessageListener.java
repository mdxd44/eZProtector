/*
Copyright (c) 2016-2017 dvargas135

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
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import ez.plugins.dan.Main;

public class IPluginMessageListener implements PluginMessageListener {
	private Main plugin;
	public IPluginMessageListener (Main plugin) {
		this.plugin = plugin;
	}
	public void onPluginMessageReceived(String channel, Player player, byte[] value) {
		Main.player = player.getName();
    	FileConfiguration config = plugin.getConfig();
    	ConsoleCommandSender console = Bukkit.getConsoleSender();
    	String punishCommand;
    	String notifyMessage;
		  
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
    			if ((channel.equalsIgnoreCase(Main.WDLINIT)) || (channel.equalsIgnoreCase(Main.WDLCONTROL)) ||
    					(channel.equalsIgnoreCase(Main.WDLREQ)) || (channel.contains("WDL"))) {
    				punishCommand = config.getString("mods.wdl.punish-command");
    				Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
    				for (Player admin : Bukkit.getOnlinePlayers()) {
    					if (admin.hasPermission("ezprotector.notify.mod.wdl")) {
    						notifyMessage = config.getString("mods.wdl.warning-message");
    						if (!notifyMessage.trim().equals("")) {
								admin.sendMessage(Main.placeholders(notifyMessage));
							}
    					}
    				}
    			}
    		}
    	}
/*    	if (channel.equalsIgnoreCase("REGISTER")) {
    		String msg;
    		msg = "REGISTER CHANNEL";
    		Bukkit.broadcastMessage(msg);
    	}
    	if (channel.equalsIgnoreCase("UNREGISTER")) {
    		String msg;
    		msg = "UNREGISTER CHANNEL";
    		Bukkit.broadcastMessage(msg);
    	}
*/    	if (config.getBoolean("mods.forge.block")) {
    		if (channel.equalsIgnoreCase(Main.FML) || (channel.equalsIgnoreCase(Main.FMLHS))) {
    			if (!player.hasPermission("ezprotector.bypass.mod.forge")) {
    				punishCommand = config.getString("mods.forge.punish-command");
    				Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
    				for (Player admin : Bukkit.getOnlinePlayers()) {
    					if (admin.hasPermission("ezprotector.notify.mod.forge")) {
    						notifyMessage = config.getString("mods.forge.warning-message");
    						if (!notifyMessage.trim().equals("")) {
    							admin.sendMessage(Main.placeholders(notifyMessage));
    						}
        				}
        			}
        		}
    		}
    	}
    	if (channel.equalsIgnoreCase(Main.MCBRAND)) {
    		String brand;
    		try {
    			brand = new String(value, "UTF-8");
    		} catch (UnsupportedEncodingException e) {
    			throw new Error(e);
    		}
    		if (config.getBoolean("mods.wdl.block")) {
    			if (!player.hasPermission("ezprotector.bypass.mod.wdl")) {
    				if (brand.equalsIgnoreCase("worlddownloader-vanilla")) {
    					punishCommand = config.getString("mods.wdl.punish-command");
            			Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
            			for (Player admin : Bukkit.getOnlinePlayers()) {
        					if (admin.hasPermission("ezprotector.notify.mod.wdl")) {
        						notifyMessage = config.getString("mods.wdl.warning-message");
        						if (!notifyMessage.trim().equals("")) {
    								admin.sendMessage(Main.placeholders(notifyMessage));
    							}
        					}
        				}
    				}
    			}
    		}
    		if (config.getBoolean("mods.forge.block")) {
    			if (!player.hasPermission("ezprotector.bypass.mod.forge")) {
    				if ((brand.equalsIgnoreCase("fml,forge")) || (brand.contains("fml")) || (brand.contains("forge"))) {
    					punishCommand = config.getString("mods.forge.punish-command");
            			Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
            			for (Player admin : Bukkit.getOnlinePlayers()) {
        					if (admin.hasPermission("ezprotector.notify.mod.forge")) {
        						notifyMessage = config.getString("mods.forge.warning-message");
        						if (!notifyMessage.trim().equals("")) {
    								admin.sendMessage(Main.placeholders(notifyMessage));
    							}
        					}
        				}
    				}
    			}
    		}
    		if (config.getBoolean("mods.liteloader.block")) {
    			if (!player.hasPermission("ezprotector.bypass.mod.liteloader")) {
    				if ((brand.contains("Lite")) || (brand.equalsIgnoreCase("LiteLoader"))) {
    					punishCommand = config.getString("mods.liteloader.punish-command");
            			Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
            			for (Player admin : Bukkit.getOnlinePlayers()) {
        					if (admin.hasPermission("ezprotector.notify.mod.liteloader")) {
        						notifyMessage = config.getString("mods.liteloader.warning-message");
        						if (!notifyMessage.trim().equals("")) {
    								admin.sendMessage(Main.placeholders(notifyMessage));
    							}
        					}
        				}
    				}
    			}
    		}
    	}
    }
}
