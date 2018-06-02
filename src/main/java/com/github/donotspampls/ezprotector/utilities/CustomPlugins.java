/*
 * Copyright (c) 2016-2018 dvargas135, DoNotSpamPls and contributors. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
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

    public static void executeCustom(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage();

        String[] plu = new String[]{"pl", "plugins"};
        for (String aList : plu) {
            Main.playerCommand = aList;
            if (command.split(" ")[0].equalsIgnoreCase("/" + Main.playerCommand) && !player.hasPermission("ezprotector.bypass.command.plugins")) {
                event.setCancelled(true);
                StringBuilder defaultMessage = new StringBuilder("§a");
                for (String plugin : Main.plugins) defaultMessage.append(plugin).append(", ");
                defaultMessage = new StringBuilder(defaultMessage.substring(0, defaultMessage.lastIndexOf(", ")));
                String customPlugins = ChatColor.WHITE + "Plugins (" + Main.plugins.size() + "): " + ChatColor.GREEN + defaultMessage.toString().replaceAll(", ", String.valueOf(ChatColor.WHITE) + ", " + ChatColor.GREEN);
                player.sendMessage(customPlugins);
            }
        }
    }

    public static void executeBlock(PlayerCommandPreprocessEvent event) {
        FileConfiguration config = Main.getPlugin().getConfig();
        Player player = event.getPlayer();
        Main.player = player.getName();
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        String punishCommand;
        String notifyMessage;

        if (!player.hasPermission("ezprotector.bypass.command.plugins")) {
            String[] plu = new String[]{"pl", "plugins"};
            for (String aList : plu) {
                Main.playerCommand = aList;
                if (event.getMessage().split(" ")[0].equalsIgnoreCase("/" + Main.playerCommand)) {
                    event.setCancelled(true);
                    Main.errorMessage = config.getString("custom-plugins.error-message");
                    if (!Main.errorMessage.trim().equals("")) player.sendMessage(Main.placeholders(Main.errorMessage));

                    if (config.getBoolean("custom-plugins.punish-player.enabled")) {
                        punishCommand = config.getString("custom-plugins.punish-player.command");
                        Main.errorMessage = config.getString("custom-plugins.error-message");
                        Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
                    }

                    if (config.getBoolean("custom-plugins.notify-admins.enabled")) {
                        notifyMessage = config.getString("custom-plugins.notify-admins.message");
                        ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.plugins");
                    }
                }
            }
        }
    }

}
