/*
 * eZProtector - Copyright (C) 2018-2020 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.paper.listeners;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.util.List;

public class BrigadierListener implements Listener {

    private final FileConfiguration config;
    public BrigadierListener(FileConfiguration config) {
        this.config = config;
    }

    /**
     * Removes forbidden commands from Brigadier's command tree (1.13)
     *
     * @param event The event which removes the tab completions from the client.
     */
    @EventHandler
    public void onCommandSend(PlayerCommandSendEvent event) {
        if (config.getBoolean("tab-completion.blocked")) {
            Player player = event.getPlayer();
            List<String> blocked = config.getStringList("tab-completion.commands");

            if (!config.getBoolean("tab-completion.whitelist"))
                event.getCommands().removeIf(cmd ->
                        !player.hasPermission("ezprotector.bypass.command.tabcomplete." + cmd) && blocked.contains(cmd));
            else
                event.getCommands().removeIf(cmd ->
                        !player.hasPermission("ezprotector.bypass.command.tabcomplete." + cmd) && !blocked.contains(cmd));
        }
    }

}
