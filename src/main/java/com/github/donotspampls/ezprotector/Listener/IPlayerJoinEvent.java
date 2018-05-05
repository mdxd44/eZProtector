/*
Copyright (c) 2016-2017 dvargas135

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.github.donotspampls.ezprotector.Listener;

import com.github.donotspampls.ezprotector.Main;
import com.github.donotspampls.ezprotector.ModBlockage.DamageIndicators;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class IPlayerJoinEvent implements Listener {

	private Main plugin;
	public IPlayerJoinEvent (Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
	    Player p = e.getPlayer();
		DamageIndicators damageIndicators = new DamageIndicators();

        if (plugin.getConfig().getBoolean("mods.betterpvp.block")) {
            if (!p.hasPermission("ezprotector.bypass.mod.betterpvp")) {
                p.sendMessage(" §c §r§5 §r§1 §r§f §r§0 ");
            }
        }

        /*
        if (plugin.getConfig().getBoolean("mods.schematica.block")) {
            if (!p.hasPermission("ezprotector.bypass.mod.schematica")) {
                Setup.IeZP.sendSchematica(p);
            }
        }

        if (plugin.getConfig().getBoolean("mods.reiminimap.block")) {
            if (!p.hasPermission("ezprotector.bypass.mod.reiminimap")) {
                Setup.IeZP.sendReiMiniMap(p);
            }
        }
        */

        if (plugin.getConfig().getBoolean("mods.damageindicators.block")) {
            if (!p.hasPermission("ezprotector.bypass.mod.damageindicators")) {
                damageIndicators.set(p);
            }
        }

        /*
        if (plugin.getConfig().getBoolean("mods.voxelmap.block")) {
            if (!p.hasPermission("ezprotector.bypass.mod.voxelmap")) {
                Setup.IeZP.sendVoxelMap(p);
            }
        }

        if (plugin.getConfig().getBoolean("mods.smartmoving.block")) {
            if (!p.hasPermission("ezprotector.bypass.mod.smartmoving")) {
                Setup.IeZP.sendSmartMove(p);
            }
        }
        */
    }

}
