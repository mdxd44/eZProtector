/*
 * Copyright (c) 2016-2018 dvargas135, DoNotSpamPls and contributors. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package com.github.donotspampls.ezprotector.listeners;

import com.github.donotspampls.ezprotector.Main;
import com.github.donotspampls.ezprotector.mods.DamageIndicators;
import com.github.donotspampls.ezprotector.mods.ReiMinimap;
import com.github.donotspampls.ezprotector.mods.Schematica;
import com.github.donotspampls.ezprotector.mods.SmartMoving;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class IPlayerJoinEvent implements Listener {

    /**
     * Listener to check for potential mods installed by the player.
     *
     * @param event The join event from which other information is gathered.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Get the player who joined and the plugin config
        Player p = event.getPlayer();
        FileConfiguration config = Main.getPlugin().getConfig();

        // Check if mod blockings are enabled, and if yes - execute them on the player in question.
        if (config.getBoolean("mods.betterpvp.block") && !p.hasPermission("ezprotector.bypass.mod.betterpvp")) p.sendMessage(" §c §r§5 §r§1 §r§f §r§0 ");
        if (config.getBoolean("mods.damageindicators.block")) DamageIndicators.set(p);
        if (config.getBoolean("mods.reiminimap.block")) ReiMinimap.set(p);
        if (config.getBoolean("mods.schematica.block")) Schematica.set(p);
        if (config.getBoolean("mods.smartmoving.block")) SmartMoving.set(p);
        if (config.getBoolean("mods.voxelmap.block") && !p.hasPermission("ezprotector.bypass.mod.voxelmap")) p.sendMessage(" §3 §6 §3 §6 §3 §6 §e ");
    }

}
