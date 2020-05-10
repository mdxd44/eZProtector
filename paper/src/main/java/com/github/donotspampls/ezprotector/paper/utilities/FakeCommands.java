/*
 * eZProtector - Copyright (C) 2018-2020 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.paper.utilities;

import com.github.donotspampls.ezprotector.paper.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import static com.github.donotspampls.ezprotector.paper.utilities.MessageUtil.color;

public class FakeCommands {

    public static void execute(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage().split(" ")[0];
        FileConfiguration config = Main.getPlugin().getConfig();

        if (event.isCancelled()) return;

        if (!player.hasPermission("ezprotector.bypass.command.fake")) {
            if (command.matches("(?i)/ver|/version") && config.getBoolean("custom-version.enabled")) {
                event.setCancelled(true);

                String version = config.getString("custom-version.version");
                player.sendMessage(color("This server is running server version " + version));

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
