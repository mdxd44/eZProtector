/*
 * eZProtector - Copyright (C) 2018-2019 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.waterfall.utilities;

import com.github.donotspampls.ezprotector.waterfall.Main;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import static com.github.donotspampls.ezprotector.waterfall.utilities.MessageUtil.color;

public class FakeCommands {

    /**
     * Intercepts the command and swaps the output with a fake one
     *
     * @param event The command event from which other information is gathered.
     */
    public static void executeCustom(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage();
        FileConfiguration config = Main.getPlugin().getConfig();

        if (command.split(" ")[0].matches("/ver|/version") && !player.hasPermission("ezprotector.bypass.command.version")
                && config.getBoolean("custom-version.enabled")) {
            event.setCancelled(true);
            String version = Main.getPlugin().getConfig().getString("custom-version.version");
            player.sendMessage(color("This server is running server version " + version));
        } else if (command.split(" ")[0].matches("/pl|/plugins") && !player.hasPermission("ezprotector.bypass.command.plugins")
                && config.getBoolean("custom-plugins.enabled")) {
            event.setCancelled(true);

            String[] plugins = Main.getPlugin().getConfig().getString("custom-plugins.plugins").split(", ");
            String pluginsList = String.join(ChatColor.WHITE + ", " + ChatColor.GREEN, plugins);

            // Create a fake /plugins output message using the string array above.
            String customPlugins = ChatColor.WHITE + "Plugins (" + plugins.length + "): " + ChatColor.GREEN + pluginsList;

            player.sendMessage(customPlugins);
        }
    }

    public static void executeBlock(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage();
        FileConfiguration config = Main.getPlugin().getConfig();

        if (command.split(" ")[0].matches("/ver|/version") && !player.hasPermission("ezprotector.bypass.command.version")
                && config.getBoolean("custom-version.enabled")) {
            event.setCancelled(true);
            // Replace placeholder with the error message in the config
            String errorMessage = config.getString("custom-version.error-message");

            if (!errorMessage.trim().isEmpty()) player.sendMessage(MessageUtil.placeholders(errorMessage, player, null, command));

            if (config.getBoolean("custom-version.punish-player.enabled")) {
                String punishCommand = config.getString("custom-version.punish-player.command");
                // Replace placeholder with the error message in the config
                errorMessage = config.getString("custom-version.error-message");
                ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, errorMessage, command));
            }

            if (config.getBoolean("custom-version.notify-admins.enabled")) {
                String notifyMessage = MessageUtil.placeholders(config.getString("custom-version.notify-admins.message"), player, null, command);
                ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.version");
            }
        } else if (command.split(" ")[0].matches("/pl|/plugins") && player.hasPermission("ezprotector.bypass.command.plugins")
                && config.getBoolean("custom-plugins.enabled")) {
            event.setCancelled(true);
            String errorMessage = config.getString("custom-plugins.error-message");

            if (!errorMessage.trim().isEmpty()) player.sendMessage(MessageUtil.placeholders(errorMessage, player, errorMessage, command));

            if (config.getBoolean("custom-plugins.punish-player.enabled")) {
                String punishCommand = config.getString("custom-plugins.punish-player.command");
                // Replace placeholder with the error message in the config
                errorMessage = config.getString("custom-version.error-message");
                ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, errorMessage, command));
            }

            if (config.getBoolean("custom-plugins.notify-admins.enabled")) {
                String notifyMessage = MessageUtil.placeholders(config.getString("custom-plugins.notify-admins.message"), player, null, command);
                ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.plugins");
            }
        }
    }
}
