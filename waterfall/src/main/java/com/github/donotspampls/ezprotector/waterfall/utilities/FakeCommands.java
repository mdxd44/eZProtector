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

    /**
     * Intercepts the command and swaps the output with a fake one
     *
     * @param event The command event from which other information is gathered.
     */
    public static void executeCustom(ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer)) return;

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        String command = event.getMessage();
        Configuration config = Main.getConfig();

        if (command.split(" ")[0].matches("(?i)/ver|/version|/bungee") && !player.hasPermission("ezprotector.bypass.command.version")
                && config.getBoolean("custom-version.enabled")) {
            event.setCancelled(true);
            String version = config.getString("custom-version.version");
            player.sendMessage(color("This server is running server version " + version));
        } else if (command.split(" ")[0].matches("(?i)/pl|/plugins") && !player.hasPermission("ezprotector.bypass.command.plugins")
                && config.getBoolean("custom-plugins.enabled")) {
            event.setCancelled(true);

            String[] plugins = config.getString("custom-plugins.plugins").split(", ");
            String pluginsList = String.join(ChatColor.WHITE + ", " + ChatColor.GREEN, plugins);

            // Create a fake /plugins output message using the string array above.
            String customPlugins = ChatColor.WHITE + "Plugins (" + plugins.length + "): " + ChatColor.GREEN + pluginsList;

            player.sendMessage(customPlugins);
        }
    }

    public static void executeBlock(ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer)) return;

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        String command = event.getMessage();
        Configuration config = Main.getConfig();

        if (command.split(" ")[0].matches("(?i)/ver|/version|/bungee") && !player.hasPermission("ezprotector.bypass.command.version")
                && !config.getBoolean("custom-version.enabled")) {
            event.setCancelled(true);
            // Replace placeholder with the error message in the config
            String errorMessage = config.getString("custom-version.error-message");

            if (!errorMessage.trim().isEmpty())
                player.sendMessage(MessageUtil.placeholders(errorMessage, player, null, command));

            if (config.getBoolean("custom-version.punish-player.enabled")) {
                String punishCommand = config.getString("custom-version.punish-player.command");
                ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, errorMessage, command));
            }

            if (config.getBoolean("custom-version.notify-admins.enabled")) {
                String notifyMessage = MessageUtil.placeholders(config.getString("custom-version.notify-admins.message"), player, null, command);
                ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.version");
            }
        } else if (command.split(" ")[0].matches("(?i)/pl|/plugins") && !player.hasPermission("ezprotector.bypass.command.plugins")
                && !config.getBoolean("custom-plugins.enabled")) {
            event.setCancelled(true);
            String errorMessage = config.getString("custom-plugins.error-message");

            if (!errorMessage.trim().isEmpty())
                player.sendMessage(MessageUtil.placeholders(errorMessage, player, errorMessage, command));

            if (config.getBoolean("custom-plugins.punish-player.enabled")) {
                String punishCommand = config.getString("custom-plugins.punish-player.command");
                ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, errorMessage, command));
            }

            if (config.getBoolean("custom-plugins.notify-admins.enabled")) {
                String notifyMessage = MessageUtil.placeholders(config.getString("custom-plugins.notify-admins.message"), player, null, command);
                ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.plugins");
            }
        }
    }
}
