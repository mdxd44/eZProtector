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

import java.util.List;

public class HiddenSyntaxes {

    /**
     * Intercepts a command containing the ":" character and blocks it.
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

        // Get the commands which will not be filtered by this check
        List<String> whitelisted = config.getStringList("hidden-syntaxes.whitelisted");

        // Check if the command contains :. If that is true, check if the player hasn't got the bypass permission and that the command hasn't got any spaces in it
        if (command.split(" ")[0].contains(":") && !player.hasPermission("ezprotector.bypass.command.hiddensyntax") && !whitelisted.contains(command.split(" ")[0].toLowerCase().replace("/", ""))) {
            // Cancel the command event
            event.setCancelled(true);
            // Replace placeholder with the executed command
            Main.playerCommand = command.replace("/", "");

            // Send an error message to the player in question.
            if (!Main.errorMessage.trim().equals("")) player.sendMessage(Main.placeholders(Main.errorMessage));

            // Check if punishment is enabled
            if (config.getBoolean("hidden-syntaxes.punish-player.enabled")) {
                // Get the punishment command to dispatch
                punishCommand = config.getString("hidden-syntaxes.punish-player.command");
                // Replace placeholder with the error message in the config
                Main.errorMessage = config.getString("hidden-syntaxes.error-message");
                // Dispatch punishment command to player
                Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
            }

            // Check if admin notifications are enabled.
            if (config.getBoolean("hidden-syntaxes.notify-admins.enabled")) {
                // Get the notification message to send to online admins
                notifyMessage = config.getString("hidden-syntaxes.notify-admins.message");
                // Send the notification message to all online admins
                ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.hiddensyntax");
            }
        }
    }

}
