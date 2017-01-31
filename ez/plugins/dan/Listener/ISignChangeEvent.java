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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import ez.plugins.dan.Main;

public class ISignChangeEvent implements Listener {
	private Main plugin;
	public ISignChangeEvent (Main plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSignChange(SignChangeEvent event) {
		
		Player player = event.getPlayer();
		
		if(plugin.getConfig().getBoolean("sign-hack.blocked")) {
			if (!player.hasPermission("ezprotector.bypass.signhack")) {
				for (Player admin : Bukkit.getOnlinePlayers()) {
					for (int i = 0; i < 4; i++)
					if (event.getLine(i).matches("^[a-zA-Z0-9_]*$")) {
						if (event.getLine(i).toString().length() > 16) {
							event.setCancelled(true);
							String errorMessage = plugin.getConfig().getString("sign-hack.error-message");
							if (!errorMessage.trim().equals("")) {
								player.sendMessage(Main.placeholders(errorMessage));
							}
							if (plugin.getConfig().getBoolean("sign-hack.punish-player.enabled")) {
								String punishCommand = plugin.getConfig().getString("sign-hack.punish-player.command");
								Main.errorMessage = plugin.getConfig().getString("sign-hack.error-message");
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Main.placeholders(punishCommand));
							}
							if (plugin.getConfig().getBoolean("sign-hack.notify-admins.enabled")) {
								if (admin.hasPermission("ezprotector.notify.signhack")) {
									String notifyMessage = plugin.getConfig().getString("sign-hack.notify-admins.message");
									if (!notifyMessage.trim().equals("")) {
										admin.sendMessage(Main.placeholders(notifyMessage));
									}
								}	
							}
						}
					} else if (event.getLine(i).matches("^[a-zA-Z0-9_]*$")) {
						if (event.getLine(i).length() > 20) {
							event.setCancelled(true);
							String errorMessage = plugin.getConfig().getString("sign-hack.error-message");
							if (!errorMessage.trim().equals("")) {
								player.sendMessage(Main.placeholders(errorMessage));
							}
							if (plugin.getConfig().getBoolean("sign-hack.punish-player.enabled")) {
								String punishCommand = plugin.getConfig().getString("sign-hack.punish-player.command");
								Main.errorMessage = plugin.getConfig().getString("sign-hack.error-message");
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Main.placeholders(punishCommand));
							}
							if (plugin.getConfig().getBoolean("sign-hack.notify-admins.enabled")) {
								if (admin.hasPermission("ezprotector.notify.signhack")) {
									String notifyMessage = plugin.getConfig().getString("sign-hack.notify-admins.message");
									if (!notifyMessage.trim().equals("")) {
										admin.sendMessage(Main.placeholders(notifyMessage));
									}
								}	
							}
						}
					} else if (event.getLine(i).length() > 50) {
						event.setCancelled(true);
						String errorMessage = plugin.getConfig().getString("sign-hack.error-message");
						if (!errorMessage.trim().equals("")) {
							player.sendMessage(Main.placeholders(errorMessage));
						}
						if (plugin.getConfig().getBoolean("sign-hack.punish-player.enabled")) {
							String punishCommand = plugin.getConfig().getString("sign-hack.punish-player.command");
							Main.errorMessage = plugin.getConfig().getString("sign-hack.error-message");
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Main.placeholders(punishCommand));
						}
						if (plugin.getConfig().getBoolean("sign-hack.notify-admins.enabled")) {
							if (admin.hasPermission("ezprotector.notify.signhack")) {
								String notifyMessage = plugin.getConfig().getString("sign-hack.notify-admins.message");
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
