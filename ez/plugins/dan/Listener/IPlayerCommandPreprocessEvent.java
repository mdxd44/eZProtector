package ez.plugins.dan.Listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
		
		boolean plugins = command.split(" ")[0].toLowerCase().equals("/plugins");
		boolean pl = command.split(" ")[0].toLowerCase().equals("/pl");
		
		
		if (plugin.getConfig().getBoolean("custom-commands.blocked")) {
			for (int i = 0; i < plugin.getConfig().getList("custom-commands.commands").size(); i++) {
				Main.playerCommand = plugin.getConfig().getList("custom-commands.commands").get(i).toString();
				if (command.split(" ")[0].toLowerCase().equals("/" + Main.playerCommand)) {
					if(!player.hasPermission("ezprotector.bypass.custom-commands")) {
						event.setCancelled(true);
						String errorMessage = plugin.getConfig().getString("custom-commands.error-message");
						if (!errorMessage.trim().equals("")) {
							player.sendMessage(Main.placeholders(errorMessage));
						}
						if (plugin.getConfig().getBoolean("custom-commands.punish-player.enabled")) {
							String punishCommand = plugin.getConfig().getString("custom-commands.punish-player.command");
							Main.errorMessage = plugin.getConfig().getString("custom-commands.error-message");
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Main.placeholders(punishCommand));
						}
						if (plugin.getConfig().getBoolean("custom-commands.notify-admins.enabled")) {
							for (Player admin : Bukkit.getOnlinePlayers()) {
								if (admin.hasPermission("ezprotector.notify.custom-commands")) {
									String notifyMessage = plugin.getConfig().getString("custom-commands.notify-admins.message");
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
		if(plugin.getConfig().getBoolean("hidden-syntaxes.blocked")) {
			if (command.split(" ")[0].contains(":")) {
				if(!player.hasPermission("ezprotector.bypass.hiddensyntaxes")) {
					event.setCancelled(true);
					String errorMessage = plugin.getConfig().getString("hidden-syntaxes.error-message");
					if (!errorMessage.trim().equals("")) {
						player.sendMessage(Main.placeholders(errorMessage));
					}
					if (plugin.getConfig().getBoolean("hidden-syntaxes.punish-player.enabled")) {
						String punishCommand = plugin.getConfig().getString("hidden-syntaxes.punish-player.command");
						Main.errorMessage = plugin.getConfig().getString("hidden-syntaxes.error-message");
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Main.placeholders(punishCommand));
					}
					if (plugin.getConfig().getBoolean("hidden-syntaxes.notify-admins.enabled")) {
						for (Player admin : Bukkit.getOnlinePlayers()) {
							if (admin.hasPermission("ezprotector.notify.hidden-syntaxes")) {
								String notifyMessage = plugin.getConfig().getString("hidden-syntaxes.notify-admins.message");
								if (!notifyMessage.trim().equals("")) {
									admin.sendMessage(Main.placeholders(notifyMessage));
								}
							}
						}
					}
				}
			}
		}
		if (plugin.getConfig().getBoolean("opped-player-commands.blocked")) {
			if (player.isOp()) {
				for (int i2 = 0; i2 < plugin.getConfig().getList("opped-player-commands.bypassed-players").size(); i2++) {
					String opped = plugin.getConfig().getList("opped-player-commands.bypassed-players").get(i2).toString();
					if(!opped.contains(Main.oppedPlayer)) {
						for (int i = 0; i < plugin.getConfig().getStringList("opped-player-commands.commands").size(); i++) {
							Main.playerCommand = plugin.getConfig().getList("opped-player-commands.commands").get(i).toString();
							if (command.split(" ")[0].toLowerCase().equals("/" + Main.playerCommand)) {
								event.setCancelled(true);
								String errorMessage = plugin.getConfig().getString("opped-player-commands.error-message");
								if (!errorMessage.trim().equals("")) {
									player.sendMessage(Main.placeholders(errorMessage));
								}
								if (plugin.getConfig().getBoolean("opped-player-commands.punish-player.enabled")) {
									String punishCommand = plugin.getConfig().getString("opped-player-commands.punish-player.command");
									Main.errorMessage = plugin.getConfig().getString("opped-player-commands.error-message");
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Main.placeholders(punishCommand));
								}
								Player ops = Bukkit.getPlayer(opped);
								if (plugin.getConfig().getBoolean("opped-player-commands.notify-bypassed-players.enabled")) {
									if (ops != null) {
										String notifyMessage = plugin.getConfig().getString("opped-player-commands.notify-bypassed-players.message");
										if (!notifyMessage.trim().equals("")) {
											for (Player admin : Bukkit.getOnlinePlayers()) {
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
		if (plugin.getConfig().getBoolean("custom-plugins.enabled")) {
			if ((plugins) || (pl)) {
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
					if (plugin.getConfig().getBoolean("custom-plugins.punish-player.enabled")) {
						String punishCommand = plugin.getConfig().getString("custom-plugins.punish-player.command");
						Main.errorMessage = plugin.getConfig().getString("custom-plugins.error-message");
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Main.placeholders(punishCommand));
					}
					if (plugin.getConfig().getBoolean("custom-plugins.notify.enabled")){
						for (Player admin : Bukkit.getOnlinePlayers()) {
							if (admin.hasPermission("ezprotector.notify.command.plugins")){
								String notifyMessage = plugin.getConfig().getString("custom-plugins.notify-admins.message");
								if (!notifyMessage.trim().equals("")) {
									admin.sendMessage(Main.placeholders(notifyMessage));
								}
							}
						}
					}
				}
			}
		} else {
			if ((plugins) || (pl)) {
				if(!player.hasPermission("ezprotector.bypass.command.plugins")) {
					event.setCancelled(true);
					String errorMessage = plugin.getConfig().getString("custom-plugins.error-message");
					if (!errorMessage.trim().equals("")) {
						player.sendMessage(Main.placeholders(errorMessage));
					}
					if (plugin.getConfig().getBoolean("custom-plugins.punish-player.enabled")) {
						String punishCommand = plugin.getConfig().getString("custom-plugins.punish-player.command");
						Main.errorMessage = plugin.getConfig().getString("custom-plugins.error-message");
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Main.placeholders(punishCommand));
					}
					if (plugin.getConfig().getBoolean("custom-plugins.notify-admins.enabled")) {
						for (Player admin : Bukkit.getOnlinePlayers()) {
							if (admin.hasPermission("ezprotector.notify.custom-plugins")) {
								String notifyMessage = plugin.getConfig().getString("custom-plugins.notify-admins.message");
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
