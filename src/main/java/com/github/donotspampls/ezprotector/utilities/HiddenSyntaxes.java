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

    public static void execute(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage();
        FileConfiguration config = Main.getPlugin().getConfig();
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        String punishCommand;
        String notifyMessage;

        Main.errorMessage = config.getString("hidden-syntaxes.error-message");
        Main.playerCommand = command.replace("/", "");
        List<String> whitelisted = config.getStringList("hidden-syntaxes.whitelisted");
        notifyMessage = config.getString("hidden-syntaxes.notify-admins.message");
        punishCommand = config.getString("hidden-syntaxes.punish-player.command");
        if (command.split(" ")[0].contains(":")) {
            if (!player.hasPermission("ezprotector.bypass.command.hiddensyntax")) {
                if (!whitelisted.contains(command.toLowerCase().replace("/", ""))) {
                    event.setCancelled(true);
                    if (!Main.errorMessage.trim().equals("")) {
                        player.sendMessage(Main.placeholders(Main.errorMessage));
                    }

                    if (config.getBoolean("hidden-syntaxes.punish-player.enabled")) {
                        Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
                    }

                    if (config.getBoolean("hidden-syntaxes.notify-admins.enabled")) {
                        ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.hiddensyntax");
                    }
                }
            }
        }
    }

}
