/*
 * Copyright (c) 2016-2018 dvargas135, DoNotSpamPls and contributors. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package com.github.donotspampls.ezprotector.utilities;

import com.github.donotspampls.ezprotector.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CustomVersion {

    public static void execute(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage();

        String[] ver = new String[]{"ver", "version"};
        for (String aList : ver) {
            Main.playerCommand = aList;
            if (command.split(" ")[0].toLowerCase().equals("/" + Main.playerCommand)) {
                if (!player.hasPermission("ezprotector.bypass.command.version")) {
                    event.setCancelled(true);
                    String version = Main.getPlugin().getConfig().getString("custom-version.version");
                    player.sendMessage("This server is running server version " + version);
                }
            }
        }
    }

}
