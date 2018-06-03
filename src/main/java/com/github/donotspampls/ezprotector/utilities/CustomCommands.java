/*
 * Copyright (c) 2016-2018 dvargas135, DoNotSpamPls and contributors. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package com.github.donotspampls.ezprotector.utilities;

import com.github.donotspampls.ezprotector.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CustomCommands {

    /**
     * Intercepts a command if it's found to be blocked by the server admin.
     *
     * @param event The command event from which other information is gathered.
     */
    public static void execute(PlayerCommandPreprocessEvent event) {
        // Get information from the event, get the config and console and register strings.
        Player player = event.getPlayer();
        Main.player = player.getName();
        String command = event.getMessage();
        FileConfiguration config = Main.getPlugin().getConfig();
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        String punishCommand;
        String notifyMessage;

        // Start a for loop to check if the executed command is blocked in the config
        for (int i = 0; i < config.getList("custom-commands.commands").size(); i++) {
            // Replace placeholder with the command executed by the player
            Main.playerCommand = config.getList("custom-commands.commands").get(i).toString();
            // If the executed command is matching and the player doesn't have a bypass permission, continue
            if (((command.split(" ")[0].equalsIgnoreCase("/" + Main.playerCommand)) /*|| command.equalsIgnoreCase("/" + Main.playerCommand)*/) && !player.hasPermission("ezprotector.bypass.command.custom")) {
                // Intercept the command event and cancel it
                event.setCancelled(true);
                // Replace placeholder with the error message in the config
                Main.errorMessage = config.getString("custom-commands.error-message");

                // Send an error message to the player in question.
                if (!Main.errorMessage.trim().equals("")) player.sendMessage(Main.placeholders(Main.errorMessage));

                // Check if punishment is enabled
                if (config.getBoolean("custom-commands.punish-player.enabled")) {
                    // Get the punishment command to dispatch
                    punishCommand = config.getString("custom-commands.punish-player.command");
                    // Replace placeholder with the error message in the config
                    Main.errorMessage = config.getString("tab-completion.warn.message");
                    // Dispatch punishment command to player
                    Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
                }

                // Check if admin notifications are enabled.
                if (config.getBoolean("custom-commands.notify-admins.enabled")) {
                    // Get the notification message to send to online admins
                    notifyMessage = config.getString("custom-commands.notify-admins.message");
                    // Send the notification message to all online admins
                    ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.custom");
                }
            }
        }
    }

}
