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

package com.github.donotspampls.ezprotector.Listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.github.donotspampls.ezprotector.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class IPacketEvent {
	private static FileConfiguration config = Main.getPlugin().getConfig();
	public static void protocolLibHook(final List<String> list) {
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter
			(Main.plugin, ListenerPriority.HIGHEST, PacketType.Play.Client.TAB_COMPLETE) {
			public void onPacketReceiving(PacketEvent event) {
				if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
					if (config.getBoolean("tab-completion.blocked")) {
						Player player = event.getPlayer();
						PacketContainer packet = event.getPacket();
						String message = packet.getSpecificModifier(String.class).read(0).toLowerCase();
						if (!event.getPlayer().hasPermission("ezprotector.bypass.command.tabcomplete")) {
							for (String command : list) {
								if (message.startsWith(command)) {
									if (config.getBoolean("tab-completion.warn.enabled")) {
										String errorMessage = plugin.getConfig().getString("tab-completion.warn.message");
										if (!errorMessage.trim().equals("")) {
											player.sendMessage(Main.placeholders(errorMessage));
										}
										if (plugin.getConfig().getBoolean("tab-completion.punish-player.enabled")) {
											String punishCommand = plugin.getConfig().getString("tab-completion.punish-player.command");
											Main.errorMessage = plugin.getConfig().getString("tab-completion.error-message");
											Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Main.placeholders(punishCommand));
										}
										if (plugin.getConfig().getBoolean("tab-completion.notify-admins.enabled")) {
											for (Player admin : Bukkit.getOnlinePlayers()) {
												if (admin.hasPermission("ezprotector.notify.command.tabcomplete")) {
													String notifyMessage = plugin.getConfig().getString("tab-completion.notify-admins.message");
													if (!notifyMessage.trim().equals("")) {
														admin.sendMessage(Main.placeholders(notifyMessage));
													}
												}
											}
										}
									}
								}
								if ((message.startsWith(command)) || ((message.startsWith("/")) && ((!message.contains(" ")) || 
										(message.contains(":"))))) {
									event.setCancelled(true);
								}
							}
						}
					}
				}
			}
		});
	}
}
