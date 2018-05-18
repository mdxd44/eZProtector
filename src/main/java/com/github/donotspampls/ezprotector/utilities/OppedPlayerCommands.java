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

public class OppedPlayerCommands {

    public static void execute(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        Main.oppedPlayer = player.getName();
        String command = event.getMessage();
        FileConfiguration config = Main.getPlugin().getConfig();
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        String punishCommand;
        String notifyMessage;

        Main.errorMessage = config.getString("opped-player-commands.error-message");
        if (player.isOp()) {
            for (int i2 = 0; i2 < config.getStringList("opped-player-commands.bypassed-players").size(); i2++) {
                String opped = config.getStringList("opped-player-commands.bypassed-players").get(i2);
                if (!opped.contains(Main.oppedPlayer)) {
                    for (int i = 0; i < config.getStringList("opped-player-commands.commands").size(); i++) {
                        Main.playerCommand = config.getList("opped-player-commands.commands").get(i).toString();

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
                                    notifyMessage = config.getString("opped-player-commands.notify-bypassed-players.message");
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

}
