/*
Copyright (c) 2016 dvargas135

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

package ez.plugins.dan.Listener;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import ez.plugins.dan.Main;
import ez.plugins.dan.Extras.Setup;
import ez.plugins.dan.ModBlockage.LabyMod.EnumLabyModFeature;

public class IPlayerJoinEvent implements Listener {
	private FileConfiguration config = Main.getPlugin().getConfig();
	
	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
    	Player p = e.getPlayer();
    	final Player p2 = e.getPlayer();
    	
    	if (config.getBoolean("mods.betterpvp.block")) {
    		if (!p.hasPermission("ezprotector.bypass.mod.betterpvp")) {
    			Setup.IeZP.sendBetterPvP(p);
    		}
    	}
    	if (config.getBoolean("mods.schematica.block")) {
    		if (!p.hasPermission("ezprotector.bypass.mod.schematica")) {
    			Setup.IeZP.sendSchematica(p);
    		}
    	}
    	if (config.getBoolean("mods.reiminimap.block")) {
    		if (!p.hasPermission("ezprotector.bypass.mod.reiminimap")) {
    			Setup.IeZP.sendReiMiniMap(p);
    		}
    	}
    	if (config.getBoolean("mods.damageindicators.block")) {
    		if (!p.hasPermission("ezprotector.bypass.mod.damageindicators")) {
    			Setup.IeZP.sendDamageIndicators(p);
    		}
    	}
    	if (config.getBoolean("mods.voxelmap.block")) {
    		if (!p.hasPermission("ezprotector.bypass.mod.voxelmap")) {
    			Setup.IeZP.sendVoxelMap(p);
    		}
    	}
    	if (config.getBoolean("mods.labymod.block")) {
    		if (!p.hasPermission("ezprotector.bypass.mod.labymod")) {
    			HashMap<EnumLabyModFeature, Boolean> list = new HashMap<EnumLabyModFeature, Boolean>();
    			list.put(EnumLabyModFeature.ANIMATIONS, Boolean.valueOf(false));
    			list.put(EnumLabyModFeature.ARMOR , Boolean.valueOf(false));
    			list.put(EnumLabyModFeature.BLOCKBUILD , Boolean.valueOf(false));
    			list.put(EnumLabyModFeature.CHAT , Boolean.valueOf(false));
    			list.put(EnumLabyModFeature.DAMAGEINDICATOR , Boolean.valueOf(false));
    			list.put(EnumLabyModFeature.EXTRAS , Boolean.valueOf(false));
    			list.put(EnumLabyModFeature.FOOD , Boolean.valueOf(false));
    			list.put(EnumLabyModFeature.GUI , Boolean.valueOf(false));
    			list.put(EnumLabyModFeature.MINIMAP_RADAR , Boolean.valueOf(false));
    			list.put(EnumLabyModFeature.NICK , Boolean.valueOf(false));
    			list.put(EnumLabyModFeature.POTIONS , Boolean.valueOf(false));
    			Setup.IeZP.setLabyModFeature(p, list);
    		}
    	}
    	if (config.getBoolean("mods.smartmoving.block")) {
    		if (!p.hasPermission("ezprotector.bypass.mod.smartmoving")) {
    			Setup.IeZP.sendSmartMove(p);
    		}
    	}
    }
}
