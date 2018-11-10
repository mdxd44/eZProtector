package com.github.donotspampls.ezprotector.listeners;

import com.github.donotspampls.ezprotector.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.util.List;

public class BrigadierListener implements Listener {

    /**
     * Removes forbidden commands from Brigadier's command tree (1.13)
     *
     * @param event The event which removes the tab completions from the client.
     */
    @EventHandler
    public void onPlayerTab(PlayerCommandSendEvent event) {
        FileConfiguration config = Main.getPlugin().getConfig();
        final List<String> blocked = config.getStringList("tab-completion.blacklisted");

        if (config.getBoolean("tab-completion.blocked") && !event.getPlayer().hasPermission("ezprotector.bypass.command.tabcomplete")) {
            event.getCommands().removeAll(blocked);
        }
    }

}
