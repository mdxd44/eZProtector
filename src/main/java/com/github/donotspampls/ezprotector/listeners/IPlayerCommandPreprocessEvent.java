/*
 * Copyright (c) 2016-2018 dvargas135, DoNotSpamPls and contributors. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package com.github.donotspampls.ezprotector.listeners;

import com.github.donotspampls.ezprotector.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class IPlayerCommandPreprocessEvent implements Listener {

    private Main plugin;

    public IPlayerCommandPreprocessEvent(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {

        Player player = event.getPlayer();
        String command = event.getMessage();
        FileConfiguration config = plugin.getConfig();
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        String punishCommand;
        String notifyMessage;

        if (config.getBoolean("custom-commands.blocked")) {
            for (int i = 0; i < config.getList("custom-commands.commands").size(); i++) {
                Main.playerCommand = config.getList("custom-commands.commands").get(i).toString();
                if ((command.split(" ")[0].toLowerCase().equals("/" + Main.playerCommand)) || command
                        .toLowerCase().equals("/" + Main.playerCommand)) {

                    if (!player.hasPermission("ezprotector.bypass.command.custom")) {
                        event.setCancelled(true);
                        Main.errorMessage = config.getString("custom-commands.error-message");

                        if (!Main.errorMessage.trim().equals("")) {
                            player.sendMessage(Main.placeholders(Main.errorMessage));
                        }

                        if (config.getBoolean("custom-commands.punish-player.enabled")) {
                            punishCommand = config.getString("custom-commands.punish-player.command");
                            Main.errorMessage = config.getString("custom-commands.error-message");
                            Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
                        }

                        if (config.getBoolean("custom-commands.notify-admins.enabled")) {
                            for (Player admin : Bukkit.getOnlinePlayers()) {
                                if (admin.hasPermission("ezprotector.notify.command.custom")) {
                                    notifyMessage = config.getString("custom-commands.notify-admins.message");
                                    if (!notifyMessage.trim().equals("")) {
                                        admin.sendMessage(Main.placeholders(notifyMessage));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (config.getBoolean("hidden-syntaxes.blocked")) {
            Main.errorMessage = config.getString("hidden-syntaxes.error-message");
            Main.playerCommand = command.replace("/", "");
            notifyMessage = config.getString("hidden-syntaxes.notify-admins.message");
            punishCommand = config.getString("hidden-syntaxes.punish-player.command");
            if (command.split(" ")[0].contains(":")) {

                if (!player.hasPermission("ezprotector.bypass.command.hiddensyntax")) {

                    event.setCancelled(true);
                    if (!Main.errorMessage.trim().equals("")) {
                        player.sendMessage(Main.placeholders(Main.errorMessage));
                    }

                    if (config.getBoolean("hidden-syntaxes.punish-player.enabled")) {
                        Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
                    }

                    if (config.getBoolean("hidden-syntaxes.notify-admins.enabled")) {
                        for (Player admin : Bukkit.getOnlinePlayers()) {
                            if (admin.hasPermission("ezprotector.notify.command.hiddensyntax")) {
                                if (!notifyMessage.trim().equals("")) {
                                    admin.sendMessage(Main.placeholders(notifyMessage));
                                }
                            }
                        }
                    }
                }
            }
        }
        if (config.getBoolean("opped-player-commands.blocked")) {
            Main.errorMessage = config.getString("opped-player-commands.error-message");
            if (player.isOp()) {
                for (int i2 = 0; i2 < config.getStringList("opped-player-commands.bypassed-players").size();
                     i2++) {
                    String opped = config.getStringList("opped-player-commands.bypassed-players").get(i2);
                    if (!opped.contains(Main.oppedPlayer)) {
                        for (int i = 0; i < config.getStringList("opped-player-commands.commands").size();
                             i++) {
                            Main.playerCommand = config.getList("opped-player-commands.commands").get(i)
                                    .toString();

                            if (command.split(" ")[0].toLowerCase().equals("/" + Main.playerCommand)) {

                                event.setCancelled(true);
                                if (!Main.errorMessage.trim().equals("")) {
                                    player.sendMessage(Main.placeholders(Main.errorMessage));
                                }

                                if (config.getBoolean("opped-player-commands.punish-player.enabled")) {
                                    punishCommand = config.getString("opped-player-commands.punish-player.command");
                                    Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
                                }

                                Player ops = Bukkit.getPlayer(opped);
                                if (config.getBoolean("opped-player-commands.notify-bypassed-players.enabled")) {
                                    if (ops != null) {
                                        notifyMessage = config
                                                .getString("opped-player-commands.notify-bypassed-players.message");
                                        if (!notifyMessage.trim().equals("")) {
                                            ops.sendMessage(Main.placeholders(notifyMessage));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        String[] plu = new String[]{"pl", "plugins"};
        for (String aList : plu) {
            Main.playerCommand = aList;
            if (command.split(" ")[0].toLowerCase().equals("/" + Main.playerCommand)) {
                if (config.getBoolean("custom-plugins.enabled")) {
                    if (!player.hasPermission("ezprotector.bypass.command.plugins")) {
                        event.setCancelled(true);
                        StringBuilder defaultMessage = new StringBuilder("§a");
                        for (String plugin : Main.plugins) {
                            defaultMessage.append(plugin).append(", ");
                        }
                        defaultMessage = new StringBuilder(
                                defaultMessage.substring(0, defaultMessage.lastIndexOf(", ")));
                        String customPlugins = ChatColor.WHITE + "Plugins (" + Main.plugins.size() + "): "
                                + ChatColor.GREEN + defaultMessage.toString()
                                .replaceAll(", ", String.valueOf(ChatColor.WHITE) + ", " + ChatColor.GREEN);
                        player.sendMessage(customPlugins);
                        if (config.getBoolean("custom-plugins.punish-player.enabled")) {
                            punishCommand = config.getString("custom-plugins.punish-player.command");
                            Main.errorMessage = config.getString("custom-plugins.error-message");
                            Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
                        }
                        if (config.getBoolean("custom-plugins.notify-admins.enabled")) {
                            for (Player admin : Bukkit.getOnlinePlayers()) {
                                if (admin.hasPermission("ezprotector.notify.command.plugins")) {
                                    notifyMessage = config.getString("custom-plugins.notify-admins.message");
                                    if (!notifyMessage.trim().equals("")) {
                                        admin.sendMessage(Main.placeholders(notifyMessage));
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (!player.hasPermission("ezprotector.bypass.command.plugins")) {

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
                            for (Player admin : Bukkit.getOnlinePlayers()) {
                                if (admin.hasPermission("ezprotector.notify.command.plugins")) {
                                    notifyMessage = config.getString("custom-plugins.notify-admins.message");
                                    if (!notifyMessage.trim().equals("")) {
                                        admin.sendMessage(Main.placeholders(notifyMessage));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
