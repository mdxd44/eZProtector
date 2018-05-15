/*
 * Copyright (c) 2016-2018 dvargas135, DoNotSpamPls and contributors. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package com.github.donotspampls.ezprotector.listeners;

import com.github.donotspampls.ezprotector.Main;
import com.github.donotspampls.ezprotector.mods.Schematica;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class IPlayerLoginEvent implements Listener {

    private Main plugin;
    public IPlayerLoginEvent(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(final PlayerLoginEvent event) {
        if (plugin.getConfig().getBoolean("mods.schematica.block")) {
            Player player = event.getPlayer();
            byte[] payload = Schematica.getPayload(player);
            if (payload != null) {
                player.sendPluginMessage(plugin, Main.SCHEMATICA, payload);
            }
        }
    }
}
