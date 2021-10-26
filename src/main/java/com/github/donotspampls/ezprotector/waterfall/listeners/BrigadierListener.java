/*
 * eZProtector - Copyright (C) 2018-2020 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.waterfall.listeners;

import io.github.waterfallmc.waterfall.event.ProxyDefineCommandsEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class BrigadierListener implements Listener {

    private final Configuration config;
    public BrigadierListener(Configuration config) {
        this.config = config;
    }

    /**
     * Removes forbidden commands from Brigadier's command tree (1.13)
     *
     * @param event The event which removes the tab completions from the client.
     */
    @EventHandler
    public void onCommandSend(ProxyDefineCommandsEvent event) {
        if (!(event.getReceiver() instanceof ProxiedPlayer)) return;

        if (config.getBoolean("tab-completion.blocked")) {
            ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();
            List<String> blocked = config.getStringList("tab-completion.commands");

            if (!config.getBoolean("tab-completion.whitelist"))
                event.getCommands().values().removeIf(cmd ->
                        !player.hasPermission("ezprotector.bypass.command.tabcomplete." + cmd.getName()) && blocked.contains(cmd.getName()));
            else
                event.getCommands().values().removeIf(cmd ->
                        !player.hasPermission("ezprotector.bypass.command.tabcomplete." + cmd.getName()) && !blocked.contains(cmd.getName()));
        }
    }

}
