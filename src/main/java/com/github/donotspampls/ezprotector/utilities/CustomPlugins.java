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
        Player player = event.getPlayer();
        String command = event.getMessage();

        String[] plu = new String[]{"pl", "plugins"};
        if (!player.hasPermission("ezprotector.bypass.command.plugins")) {
            for (String aList : plu) {
                // The command that is being tested at the moment
                Main.playerCommand = aList;
                if (command.split(" ")[0].equalsIgnoreCase("/" + Main.playerCommand)) {
                    event.setCancelled(true);

                    // Put all the fake plugins defined in a string array
                    StringBuilder defaultMessage = new StringBuilder("§a");
                    for (String plugin : Main.plugins) defaultMessage.append(plugin).append(", ");
                    defaultMessage = new StringBuilder(defaultMessage.substring(0, defaultMessage.lastIndexOf(", ")));

                    // Create a fake /plugins output message using the string array above.
                    String customPlugins = ChatColor.WHITE + "Plugins (" + Main.plugins.size() + "): " + ChatColor.GREEN + defaultMessage.toString().replaceAll(", ", String.valueOf(ChatColor.WHITE) + ", " + ChatColor.GREEN);

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
        FileConfiguration config = Main.getPlugin().getConfig();
        Player player = event.getPlayer();
        Main.player = player.getName();
        ConsoleCommandSender console = Bukkit.getConsoleSender();

        if (!player.hasPermission("ezprotector.bypass.command.plugins")) {
            String[] plu = new String[]{"pl", "plugins"};
            for (String aList : plu) {
                // The command that is being tested at the moment
                Main.playerCommand = aList;
                if (event.getMessage().split(" ")[0].equalsIgnoreCase("/" + Main.playerCommand)) {
                    event.setCancelled(true);
                    // Replace placeholder with the error message in the config
                    Main.errorMessage = config.getString("custom-plugins.error-message");

                    if (!Main.errorMessage.trim().equals("")) player.sendMessage(Main.placeholders(Main.errorMessage));

                    if (config.getBoolean("custom-plugins.punish-player.enabled")) {
                        String punishCommand = config.getString("custom-plugins.punish-player.command");
                        // Replace placeholder with the error message in the config
                        Main.errorMessage = config.getString("custom-version.error-message");
                        Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
                    }

                    if (config.getBoolean("custom-plugins.notify-admins.enabled")) {
                        String notifyMessage = config.getString("custom-plugins.notify-admins.message");
                        ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.plugins");
                    }
                }
            }
        }
    }

}
