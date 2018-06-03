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

public class CustomVersion {

    public static void executeCustom(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage();

        String[] ver = new String[]{"ver", "version"};
        for (String aList : ver) {
            Main.playerCommand = aList;
            if (command.split(" ")[0].equalsIgnoreCase("/" + Main.playerCommand) && !player.hasPermission("ezprotector.bypass.command.version")) {
                event.setCancelled(true);
                String version = Main.getPlugin().getConfig().getString("custom-version.version");
                player.sendMessage("This server is running server version " + version.replace("&", "§"));
            }
        }
    }

    public static void executeBlock(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        Main.player = player.getName();
        FileConfiguration config = Main.getPlugin().getConfig();
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        String punishCommand;
        String notifyMessage;

        if (!player.hasPermission("ezprotector.bypass.command.version")) {
            String[] ver = new String[]{"ver", "version"};
            for (String aList : ver) {
                Main.playerCommand = aList;
                if (event.getMessage().split(" ")[0].equalsIgnoreCase("/" + Main.playerCommand)) {
                    event.setCancelled(true);
                    Main.errorMessage = config.getString("custom-version.error-message");
                    if (!Main.errorMessage.trim().equals("")) player.sendMessage(Main.placeholders(Main.errorMessage));

                    if (config.getBoolean("custom-version.punish-player.enabled")) {
                        punishCommand = config.getString("custom-version.punish-player.command");
                        Main.errorMessage = config.getString("custom-version.error-message");
                        Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
                    }

                    if (config.getBoolean("custom-version.notify-admins.enabled")) {
                        notifyMessage = config.getString("custom-version.notify-admins.message");
                        ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.version");
                    }
                }
            }
        }
    }

}
