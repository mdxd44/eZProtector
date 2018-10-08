/*
 * eZProtector - Copyright (C) 2018 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.github.donotspampls.ezprotector.Main;
import com.github.donotspampls.ezprotector.utilities.ExecutionUtil;
import com.github.donotspampls.ezprotector.utilities.MessageUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class PacketEventListener {

    public static void protocolLibHook() {
        FileConfiguration config = Main.getPlugin().getConfig();
        final List<String> blocked = config.getStringList("tab-completion.blacklisted");

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Main.getPlugin(), PacketType.Play.Client.TAB_COMPLETE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                // If tab blocking is enabled and the player has tried to autocomplete, continue
                if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE && config.getBoolean("tab-completion.blocked")) {
                        Player player = event.getPlayer();
                        PacketContainer packet = event.getPacket();
                        String cmd = packet.getSpecificModifier(String.class).read(0).toLowerCase(); // Get the first argument of the message

                        // Try all the commands in the blacklisted config section to see if there is a match
                        for (String command : blocked) {
                            if ((cmd.equals(command) || (cmd.startsWith("/") && !cmd.contains(" "))) && !player.hasPermission("ezprotector.bypass.command.tabcomplete")) {
                                event.setCancelled(true);

                                String errorMessage = config.getString("tab-completion.warn.message");

                                if (config.getBoolean("tab-completion.warn.enabled") && !errorMessage.trim().isEmpty()) {
                                    player.sendMessage(MessageUtil.placeholders(errorMessage, player, null, cmd));
                                }

                                if (plugin.getConfig().getBoolean("tab-completion.punish-player.enabled")) {
                                    String punishCommand = config.getString("tab-completion.punish-player.command");
                                    // Replace placeholder with the error message in the config
                                    ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, errorMessage, cmd));
                                }

                                if (plugin.getConfig().getBoolean("tab-completion.notify-admins.enabled")) {
                                    String notifyMessage = MessageUtil.placeholders(config.getString("tab-completion.notify-admins.message"), player, null, command);
                                    ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.tabcomplete");
                                }
                                break;
                            }
                        }
                }
            }
        });
    }
}
