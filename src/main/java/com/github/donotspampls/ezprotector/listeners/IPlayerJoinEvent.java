/*
 * Copyright (c) 2016-2018 dvargas135, DoNotSpamPls and contributors. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package com.github.donotspampls.ezprotector.listeners;

import com.github.donotspampls.ezprotector.Main;
import com.github.donotspampls.ezprotector.mods.DamageIndicators;
import com.github.donotspampls.ezprotector.mods.ReiMinimap;
import com.github.donotspampls.ezprotector.mods.Schematica;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class IPlayerJoinEvent implements Listener {

    private final Main plugin;
    public IPlayerJoinEvent(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (plugin.getConfig().getBoolean("mods.betterpvp.block")) {
            if (!p.hasPermission("ezprotector.bypass.mod.betterpvp")) {
                p.sendMessage(" §c §r§5 §r§1 §r§f §r§0 ");
            }
        }

        if (plugin.getConfig().getBoolean("mods.damageindicators.block")) DamageIndicators.set(p);
        if (plugin.getConfig().getBoolean("mods.reiminimap.block")) ReiMinimap.set(p);
        if (plugin.getConfig().getBoolean("mods.schematica.block")) Schematica.set(p);
        if (plugin.getConfig().getBoolean("mods.smartmoving.block")) {/*SmartMoving.set(p);*/}

        if (plugin.getConfig().getBoolean("mods.voxelmap.block")) {
            if (!p.hasPermission("ezprotector.bypass.mod.voxelmap")) {
                p.sendMessage(" §3 §6 §3 §6 §3 §6 §e ");
            }
        }
    }

}
