package ez.plugins.dan.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import ez.plugins.dan.Main;
import ez.plugins.dan.ModBlockage.Schematica;

public class IPlayerLoginEvent implements Listener {
    @EventHandler
    public void onPlayerLogin(final PlayerLoginEvent event) {
    	final Player player = event.getPlayer();
    	final byte[] payload = Schematica.getPayload(player);
    	if (payload != null) {
    		Schematica.sendCheatyPluginMessage(Main.getPlugin(), player, Main.SCHEMATICA, payload);
    	}
    }
}
