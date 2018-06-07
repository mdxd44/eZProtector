/*
 * eZProtector - Copyright (C) 2018 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.utilities;

import com.github.donotspampls.ezprotector.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CustomPlugins {

    /**
     * Intercepts the /plugins command and swaps the output with a fake one
     *
     * @param event The command event from which other information is gathered.
     */
    public static void executeCustom(PlayerCommandPreprocessEvent event) {
        // Get the player and the executed command
        Player player = event.getPlayer();
        String command = event.getMessage();

        // Create an internal string array with the possible plugin commands
        String[] plu = new String[]{"pl", "plugins"};
        // Check if the player hasn't got the bypass permission
        if (!player.hasPermission("ezprotector.bypass.command.plugins")) {
            // Try all the commands in the string array to see if there is a match
            for (String aList : plu) {
                Main.playerCommand = aList;
                // Check if the command matches one of the command in the string array
                if (command.split(" ")[0].equalsIgnoreCase("/" + Main.playerCommand)) {
                    // Cancel the command event
                    event.setCancelled(true);

                    // Puts all the fake plugins defined in a string array
                    StringBuilder defaultMessage = new StringBuilder("§a");
                    for (String plugin : Main.plugins) defaultMessage.append(plugin).append(", ");
                    defaultMessage = new StringBuilder(defaultMessage.substring(0, defaultMessage.lastIndexOf(", ")));

                    // Create a fake /plugins output message using the string array above.
                    String customPlugins = ChatColor.WHITE + "Plugins (" + Main.plugins.size() + "): " + ChatColor.GREEN + defaultMessage.toString().replaceAll(", ", String.valueOf(ChatColor.WHITE) + ", " + ChatColor.GREEN);

                    // Send the fake message to the player who executed the command
                    player.sendMessage(customPlugins);
                }
            }
        }
    }

    /**
     * Intercepts the /plugins command and blocks it for the player who executed it.
     *
     * @param event The command event from which other information is gathered.
     */
    public static void executeBlock(PlayerCommandPreprocessEvent event) {
        // Get information from the event, get the config and console and register strings
        FileConfiguration config = Main.getPlugin().getConfig();
        Player player = event.getPlayer();
        Main.player = player.getName();
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        String punishCommand;
        String notifyMessage;

        // Check if the player hasn't got the bypass permission
        if (!player.hasPermission("ezprotector.bypass.command.plugins")) {
            // Create an internal string array with the possible plugin commands
            String[] plu = new String[]{"pl", "plugins"};
            // Try all the commands in the string array to see if there is a match
            for (String aList : plu) {
                Main.playerCommand = aList;
                // If the command matches one of the command in the string array, continue.
                if (event.getMessage().split(" ")[0].equalsIgnoreCase("/" + Main.playerCommand)) {
                    // Cancel the command event
                    event.setCancelled(true);
                    // Replace placeholder with the error message in the config
                    Main.errorMessage = config.getString("custom-plugins.error-message");

                    // Send an error message to the player in question.
                    if (!Main.errorMessage.trim().equals("")) player.sendMessage(Main.placeholders(Main.errorMessage));

                    // Check if punishment is enabled
                    if (config.getBoolean("custom-plugins.punish-player.enabled")) {
                        // Get the punishment command to dispatch
                        punishCommand = config.getString("custom-plugins.punish-player.command");
                        // Replace placeholder with the error message in the config
                        Main.errorMessage = config.getString("custom-version.error-message");
                        // Dispatch punishment command to player
                        Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
                    }

                    // Check if admin notifications are enabled.
                    if (config.getBoolean("custom-plugins.notify-admins.enabled")) {
                        // Get the notification message to send to online admins
                        notifyMessage = config.getString("custom-plugins.notify-admins.message");
                        // Send the notification message to all online admins
                        ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.plugins");
                    }
                }
            }
        }
    }

}
