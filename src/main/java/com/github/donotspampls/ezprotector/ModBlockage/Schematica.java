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

package com.github.donotspampls.ezprotector.ModBlockage;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.donotspampls.ezprotector.Main;

public class Schematica {

    private static final String PERM_PRINTER = "schematica.printer";
    private static final String PERM_SAVE = "schematica.save";
    private static final String PERM_LOAD = "schematica.load";
    private static Logger log;
    
    public static byte[] getPayload(final Player player) {
    	log = Main.getPlugin().getLogger();
    	final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    	final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
    	try {
    		dataOutputStream.writeByte(0);
    		dataOutputStream.writeBoolean(player.hasPermission(PERM_PRINTER));
    		dataOutputStream.writeBoolean(player.hasPermission(PERM_SAVE));
    		dataOutputStream.writeBoolean(player.hasPermission(PERM_LOAD));

    		return byteArrayOutputStream.toByteArray();
    	} catch (final IOException ioe) {
    		log.throwing(Main.class.getName(), "getPayload", ioe);
    	}
    	return null;
    }

    public static void sendCheatyPluginMessage(Plugin plugin, final Player player, final String channel, final byte[] payload) {
    	plugin = Main.getPlugin();
    	log = Main.getPlugin().getLogger();
    	try {
    		final Class<? extends Player> playerClass = player.getClass();
    		if (playerClass.getSimpleName().equals("CraftPlayer")) {
    			final Method addChannel = playerClass.getDeclaredMethod("addChannel", String.class);
    			final Method removeChannel = playerClass.getDeclaredMethod("removeChannel", String.class);

    			addChannel.invoke(player, channel);
    			player.sendPluginMessage(plugin, channel, payload);
    			removeChannel.invoke(player, channel);
    		}
    	} catch (final NoSuchMethodException nsme) {
    		log.throwing(Main.class.getName(), "sendCheatyPluginMessage", nsme);
    	} catch (final InvocationTargetException ite) {
    		log.throwing(Main.class.getName(), "sendCheatyPluginMessage", ite);
    	} catch (final IllegalAccessException iae) {
    		log.throwing(Main.class.getName(), "sendCheatyPluginMessage", iae);
    	} catch (final Exception e) {
    		log.throwing(Main.class.getName(), "sendCheatyPluginMessage", e);
    	}
    }
}
