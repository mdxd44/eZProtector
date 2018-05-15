/*
 * Copyright (c) 2016-2018 dvargas135, DoNotSpamPls and contributors. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package com.github.donotspampls.ezprotector.listeners;

import com.github.donotspampls.ezprotector.Main;
import com.github.donotspampls.ezprotector.utilities.*;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class IPlayerCommandPreprocessEvent implements Listener {

    public IPlayerCommandPreprocessEvent(Main plugin) {}

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {

        FileConfiguration config = Main.getPlugin().getConfig();
        Player player = event.getPlayer();
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        String punishCommand;
        String notifyMessage;

        if (config.getBoolean("custom-commands.blocked")) {
            CustomCommands.execute(event);
        }

        if (config.getBoolean("hidden-syntaxes.blocked")) {
           HiddenSyntaxes.execute(event);
        }

        if (config.getBoolean("opped-player-commands.blocked")) {
            OppedPlayerCommands.execute(event);
        }

        if (config.getBoolean("custom-plugins.enabled")) {
            CustomPlugins.execute(event);
        } else if (!player.hasPermission("ezprotector.bypass.command.plugins")) {
            String[] plu = new String[]{"pl", "plugins"};
            for (String aList : plu) {
                Main.playerCommand = aList;
                if (event.getMessage().split(" ")[0].toLowerCase().equals("/" + Main.playerCommand)) {
                    event.setCancelled(true);
                    Main.errorMessage = config.getString("custom-plugins.error-message");
                    if (!Main.errorMessage.trim().equals("")) {
                        player.sendMessage(Main.placeholders(Main.errorMessage));
                    }

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

        if (config.getBoolean("custom-version.enabled")) {
            CustomVersion.execute(event);
        } else if (!player.hasPermission("ezprotector.bypass.command.version")) {
            String[] ver = new String[]{"ver", "version"};
            for (String aList : ver) {
                Main.playerCommand = aList;
                if (event.getMessage().split(" ")[0].toLowerCase().equals("/" + Main.playerCommand)) {
                    event.setCancelled(true);
                    Main.errorMessage = config.getString("custom-version.error-message");
                    if (!Main.errorMessage.trim().equals("")) {
                        player.sendMessage(Main.placeholders(Main.errorMessage));
                    }

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
