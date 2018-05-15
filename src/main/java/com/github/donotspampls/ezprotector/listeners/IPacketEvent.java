/*
 * Copyright (c) 2016-2018 dvargas135, DoNotSpamPls and contributors. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package com.github.donotspampls.ezprotector.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.github.donotspampls.ezprotector.Main;
import com.github.donotspampls.ezprotector.utilities.ExecutionUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class IPacketEvent {

    private static FileConfiguration config = Main.getPlugin().getConfig();

    public static void protocolLibHook() {
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(Main.plugin, PacketType.Play.Client.TAB_COMPLETE) {
                    public void onPacketReceiving(PacketEvent event) {
                        if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
                            if (config.getBoolean("tab-completion.blocked")) {
                                Player player = event.getPlayer();
                                PacketContainer packet = event.getPacket();
                                String message = packet.getSpecificModifier(String.class).read(0).toLowerCase();

                                if (!event.getPlayer().hasPermission("ezprotector.bypass.command.tabcomplete")) {
                                    if ((((message.startsWith("/")) || ((!message.contains(" ")) || (message.contains(":")))))) {
                                        event.setCancelled(true);
                                        if (config.getBoolean("tab-completion.warn.enabled")) {
                                            String errorMessage = plugin.getConfig().getString("tab-completion.warn.message");
                                            if (!errorMessage.trim().equals("")) {
                                                player.sendMessage(Main.placeholders(errorMessage));
                                            }
                                        }

                                        if (plugin.getConfig().getBoolean("tab-completion.punish-player.enabled")) {
                                            String punishCommand = plugin.getConfig().getString("tab-completion.punish-player.command");
                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Main.placeholders(punishCommand));
                                        }

                                        if (plugin.getConfig().getBoolean("tab-completion.notify-admins.enabled")) {
                                            String notifyMessage = plugin.getConfig().getString("tab-completion.notify-admins.message");
                                            ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.tabcomplete");
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
    }
}
