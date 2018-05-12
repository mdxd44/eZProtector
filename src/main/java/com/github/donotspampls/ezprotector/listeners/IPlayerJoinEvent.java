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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class IPlayerJoinEvent implements Listener {

    private Main plugin;

    public IPlayerJoinEvent(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        DamageIndicators damageIndicators = new DamageIndicators();
        ReiMinimap reiMinimap = new ReiMinimap();
        Schematica schematica = new Schematica();
        SmartMoving smartMoving = new SmartMoving();

        if (plugin.getConfig().getBoolean("mods.betterpvp.block")) {
            if (!p.hasPermission("ezprotector.bypass.mod.betterpvp")) {
                p.sendMessage(" §c §r§5 §r§1 §r§f §r§0 ");
            }
        }

        if (plugin.getConfig().getBoolean("mods.schematica.block")) {
            if (!p.hasPermission("ezprotector.bypass.mod.schematica")) {
                if (Main.getPlugin().getServer().getVersion().contains("1.7")) {
                    schematica.set(p);
                } else return;
            }
        }

        if (plugin.getConfig().getBoolean("mods.reiminimap.block")) {
            if (!p.hasPermission("ezprotector.bypass.mod.reiminimap")) {
                if (Main.getPlugin().getServer().getVersion().contains("1.7")) {
                    reiMinimap.set(p);
                } else return;
            }
        }

        if (plugin.getConfig().getBoolean("mods.damageindicators.block")) {
            if (!p.hasPermission("ezprotector.bypass.mod.damageindicators")) {
                if (Main.getPlugin().getServer().getVersion().contains("1.7")) {
                    damageIndicators.set(p);
                } else return;

            }
        }

        if (plugin.getConfig().getBoolean("mods.voxelmap.block")) {
            if (!p.hasPermission("ezprotector.bypass.mod.voxelmap")) {
                p.sendMessage(" §3 §6 §3 §6 §3 §6 §e ");
            }
        }

        if (plugin.getConfig().getBoolean("mods.smartmoving.block")) {
            if (!p.hasPermission("ezprotector.bypass.mod.smartmoving")) {
                smartMoving.set(p);
            }
        }
    }

}
