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

    public static void execute(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        Main.player = player.getName();
        String command = event.getMessage();
        FileConfiguration config = Main.getPlugin().getConfig();
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        String punishCommand;
        String notifyMessage;

        for (int i = 0; i < config.getList("custom-commands.commands").size(); i++) {
            Main.playerCommand = config.getList("custom-commands.commands").get(i).toString();
            if (((command.split(" ")[0].equalsIgnoreCase("/" + Main.playerCommand)) || command.toLowerCase().equals("/" + Main.playerCommand)) && !player.hasPermission("ezprotector.bypass.command.custom")) {
                event.setCancelled(true);
                Main.errorMessage = config.getString("custom-commands.error-message");

                if (!Main.errorMessage.trim().equals("")) player.sendMessage(Main.placeholders(Main.errorMessage));

                if (config.getBoolean("custom-commands.punish-player.enabled")) {
                    punishCommand = config.getString("custom-commands.punish-player.command");
                    Main.errorMessage = config.getString("custom-commands.error-message");
                    Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
                }

                if (config.getBoolean("custom-commands.notify-admins.enabled")) {
                    notifyMessage = config.getString("custom-commands.notify-admins.message");
                    ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.custom");
                }
            }
        }
    }

}
