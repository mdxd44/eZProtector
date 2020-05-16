/*
 * eZProtector - Copyright (C) 2018-2020 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.waterfall.utilities;

import com.github.donotspampls.ezprotector.waterfall.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.config.Configuration;

import static com.github.donotspampls.ezprotector.waterfall.utilities.MessageUtil.color;

public class FakeCommands {

    public static void execute(ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer)) return;

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        String command = event.getMessage().split(" ")[0];
        Configuration config = Main.getConfig();

        if (event.isCancelled()) return;

        if (!player.hasPermission("ezprotector.bypass.command.fake")) {
            if (config.getBoolean("custom-version.enabled")) {
                String version = config.getString("custom-version.version");

                if (command.matches("(?i)/ver|/version")) {
                    event.setCancelled(true);
                    player.sendMessage(color("This server is running server version " + version));
                }

                if (command.equalsIgnoreCase("/bungee")) {
                    event.setCancelled(true);
                    player.sendMessage(color("&9This server is running server version " + version));
                }

                MessageUtil.notifyAdmins("custom-version", player, command, "command.version");
            }

            if (command.matches("(?i)/pl|/plugins") && config.getBoolean("custom-plugins.enabled")) {
                event.setCancelled(true);

                String[] plugins = config.getString("custom-plugins.plugins").split(", ");
                String pluginsList = String.join(ChatColor.WHITE + ", " + ChatColor.GREEN, plugins);

                // Create a fake /plugins output message using the string array above.
                String customPlugins = ChatColor.WHITE + "Plugins (" + plugins.length + "): " + ChatColor.GREEN + pluginsList;

                player.sendMessage(customPlugins);

                MessageUtil.notifyAdmins("custom-plugins", player, command, "command.plugins");
            }

        }
    }

}
