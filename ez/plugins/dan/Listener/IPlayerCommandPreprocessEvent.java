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

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import ez.plugins.dan.Main;

public class IPlayerCommandPreprocessEvent implements Listener {
	private Main plugin;
	public IPlayerCommandPreprocessEvent (Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onCommand(PlayerCommandPreprocessEvent event) {
		
		Main.player = event.getPlayer().getName();
		Main.oppedPlayer = event.getPlayer().getName();
		Player player = event.getPlayer();
		String command = event.getMessage();
		FileConfiguration config = plugin.getConfig();
		ConsoleCommandSender console = Bukkit.getConsoleSender();
		String punishCommand;
		String notifyMessage;
		
		if (config.getBoolean("custom-commands.blocked")) {
			for (int i = 0; i < config.getList("custom-commands.commands").size(); i++) {
				Main.playerCommand = config.getList("custom-commands.commands").get(i).toString();
				if ((command.split(" ")[0].toLowerCase().equals("/" + Main.playerCommand)) || 
						command.toLowerCase().equals("/" + Main.playerCommand)) {
					if(!player.hasPermission("ezprotector.bypass.command.custom")) {
						event.setCancelled(true);
						Main.errorMessage = config.getString("custom-commands.error-message");
						if (!Main.errorMessage.trim().equals("")) {
							player.sendMessage(Main.placeholders(Main.errorMessage));
						}
						if (config.getBoolean("custom-commands.punish-player.enabled")) {
							punishCommand = config.getString("custom-commands.punish-player.command");
							Main.errorMessage = config.getString("custom-commands.error-message");
							Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
						}
						if (config.getBoolean("custom-commands.notify-admins.enabled")) {
							for (Player admin : Bukkit.getOnlinePlayers()) {
								if (admin.hasPermission("ezprotector.notify.command.custom")) {
									notifyMessage = config.getString("custom-commands.notify-admins.message");
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
		if(config.getBoolean("hidden-syntaxes.blocked")) {
			Main.errorMessage = config.getString("hidden-syntaxes.error-message");
			Main.playerCommand = command.replace("/", "");
			notifyMessage = config.getString("hidden-syntaxes.notify-admins.message");
			punishCommand = config.getString("hidden-syntaxes.punish-player.command");
			if (command.split(" ")[0].contains(":")) {
				if(!player.hasPermission("ezprotector.bypass.command.hiddensyntax")) {
					event.setCancelled(true);
					if (!Main.errorMessage.trim().equals("")) {
						player.sendMessage(Main.placeholders(Main.errorMessage));
					}
					if (config.getBoolean("hidden-syntaxes.punish-player.enabled")) {
						Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
					}
					if (config.getBoolean("hidden-syntaxes.notify-admins.enabled")) {
						for (Player admin : Bukkit.getOnlinePlayers()) {
							if (admin.hasPermission("ezprotector.notify.command.hiddensyntax")) {
								if (!notifyMessage.trim().equals("")) {
									admin.sendMessage(Main.placeholders(notifyMessage));
								}
							}
						}
					}
				}
			}
		}
		if (config.getBoolean("opped-player-commands.blocked")) {
			Main.errorMessage = config.getString("opped-player-commands.error-message");
			if (player.isOp()) {
				for (int i2 = 0; i2 < config.getStringList("opped-player-commands.bypassed-players").size(); i2++) {
					String opped = config.getStringList("opped-player-commands.bypassed-players").get(i2).toString();
					if(!opped.contains(Main.oppedPlayer)) {
						for (int i = 0; i < config.getStringList("opped-player-commands.commands").size(); i++) {
							Main.playerCommand = config.getList("opped-player-commands.commands").get(i).toString();
							if (command.split(" ")[0].toLowerCase().equals("/" + Main.playerCommand)) {
								event.setCancelled(true);
								if (!Main.errorMessage.trim().equals("")) {
									player.sendMessage(Main.placeholders(Main.errorMessage));
								}
								if (config.getBoolean("opped-player-commands.punish-player.enabled")) {
									punishCommand = config.getString("opped-player-commands.punish-player.command");
									Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
								}
								Player ops = Bukkit.getPlayer(opped);
								if (config.getBoolean("opped-player-commands.notify-bypassed-players.enabled")) {
									if (ops != null) {
										notifyMessage = config.getString("opped-player-commands.notify-bypassed-players.message");
										if (!notifyMessage.trim().equals("")) {
											ops.sendMessage(Main.placeholders(notifyMessage));
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		String[] plu = new String[] {"pl", "plugins"};
		List<String> list = (List<String>) Arrays.asList(plu);
		for (int i = 0; i < list.size(); i++) {
			Main.playerCommand = list.get(i).toString();
			if (command.split(" ")[0].toLowerCase().equals("/" + Main.playerCommand)) {
				if (config.getBoolean("custom-plugins.enabled")) {
					if(!player.hasPermission("ezprotector.bypass.command.plugins")) {
						event.setCancelled(true);
						String defaultMessage = "§a";
						for (String plugin : Main.plugins) {
							defaultMessage = defaultMessage + plugin + ", ";
						}	
						defaultMessage = defaultMessage.substring(0, defaultMessage.lastIndexOf(", "));
						String customPlugins = ChatColor.WHITE + "Plugins (" + Main.plugins.size() + "): " 
								+ ChatColor.GREEN + defaultMessage.replaceAll(", ", new StringBuilder()
										.append(ChatColor.WHITE).append(", ").append(ChatColor.GREEN).toString());
						player.sendMessage(customPlugins);
						if (config.getBoolean("custom-plugins.punish-player.enabled")) {
							punishCommand = config.getString("custom-plugins.punish-player.command");
							Main.errorMessage = config.getString("custom-plugins.error-message");
							Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
						}
						if (config.getBoolean("custom-plugins.notify-admins.enabled")){
							for (Player admin : Bukkit.getOnlinePlayers()) {
								if (admin.hasPermission("ezprotector.notify.command.plugins")) {
									notifyMessage = config.getString("custom-plugins.notify-admins.message");
									if (!notifyMessage.trim().equals("")) {
										admin.sendMessage(Main.placeholders(notifyMessage));
									}
								}
							}
						}
					}
				} else {
					if(!player.hasPermission("ezprotector.bypass.command.plugins")) {
						event.setCancelled(true);
						Main.errorMessage = config.getString("custom-plugins.error-message");
						if (!Main.errorMessage.trim().equals("")) {
							player.sendMessage(Main.placeholders(Main.errorMessage));
						}
						if (config.getBoolean("custom-plugins.punish-player.enabled")) {
							punishCommand = config.getString("custom-plugins.punish-player.command");
							Main.errorMessage = config.getString("custom-plugins.error-message");
							Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
						}
						if (config.getBoolean("custom-plugins.notify-admins.enabled")) {
							for (Player admin : Bukkit.getOnlinePlayers()) {
								if (admin.hasPermission("ezprotector.notify.command.plugins")) {
									notifyMessage = config.getString("custom-plugins.notify-admins.message");
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
}
