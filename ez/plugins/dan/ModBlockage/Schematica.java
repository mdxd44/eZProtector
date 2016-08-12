package ez.plugins.dan.ModBlockage;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import ez.plugins.dan.Main;

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
