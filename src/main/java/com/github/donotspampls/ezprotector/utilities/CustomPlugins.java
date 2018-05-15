/*
 * Copyright (c) 2016-2018 dvargas135, DoNotSpamPls and contributors. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package com.github.donotspampls.ezprotector.utilities;

import com.github.donotspampls.ezprotector.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CustomPlugins {

    public static void execute(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage();

        String[] plu = new String[]{"pl", "plugins"};
        for (String aList : plu) {
            Main.playerCommand = aList;
            if (command.split(" ")[0].toLowerCase().equals("/" + Main.playerCommand)) {
                if (!player.hasPermission("ezprotector.bypass.command.plugins")) {
                    event.setCancelled(true);
                    StringBuilder defaultMessage = new StringBuilder("§a");
                    for (String plugin : Main.plugins) {
                        defaultMessage.append(plugin).append(", ");
                    }
                    defaultMessage = new StringBuilder(defaultMessage.substring(0, defaultMessage.lastIndexOf(", ")));
                    String customPlugins = ChatColor.WHITE + "Plugins (" + Main.plugins.size() + "): " + ChatColor.GREEN + defaultMessage.toString().replaceAll(", ", String.valueOf(ChatColor.WHITE) + ", " + ChatColor.GREEN);
                    player.sendMessage(customPlugins);
                }
            }
        }
    }

}
