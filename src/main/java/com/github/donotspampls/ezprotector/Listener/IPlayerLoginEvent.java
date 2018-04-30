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
import com.github.donotspampls.ezprotector.ModBlockage.Schematica;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class IPlayerLoginEvent implements Listener {
	private Main plugin;
	public IPlayerLoginEvent (Main plugin) {
		this.plugin = plugin;
	}
	
    @EventHandler
    public void onPlayerLogin(final PlayerLoginEvent event) {
    	if (plugin.getConfig().getBoolean("mods.schematica.block")) {
    		final Player player = event.getPlayer();
    		final byte[] payload = Schematica.getPayload(player);
    		if (payload != null) {
    			Schematica.sendCheatyPluginMessage(Main.getPlugin(), player, Main.SCHEMATICA, payload);
    			Schematica.sendCheatyPluginMessage(Main.plugin, player, Main.SCHEMATICA, payload);
    		}
    		player.sendPluginMessage(Main.plugin, Main.SCHEMATICA, payload);
    		player.sendPluginMessage(Main.getPlugin(), Main.SCHEMATICA, payload);
    	}
    }
}
