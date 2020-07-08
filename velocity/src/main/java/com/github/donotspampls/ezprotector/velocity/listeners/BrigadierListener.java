/*
 * eZProtector - Copyright (C) 2018-2020 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.velocity.listeners;

import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.PlayerAvailableCommandsEvent;
import com.velocitypowered.api.proxy.Player;

import java.util.List;

public class BrigadierListener {

    private final Toml config;
    public BrigadierListener(Toml config) {
        this.config = config;
    }

    /**
     * Removes forbidden commands from Brigadier's command tree (1.13)
     *
     * @param event The event which removes the tab completions from the client.
     */
    @Subscribe
    @SuppressWarnings({"unused", "UnstableApiUsage"})
    public void onCommandSend(final PlayerAvailableCommandsEvent event) {
        if (config.getBoolean("tab-completion.blocked")) {
            final Player player = event.getPlayer();
            final List<String> blocked = config.getList("tab-completion.commands");

            if (!config.getBoolean("tab-completion.whitelist")) {
                event.getRootNode().getChildren().removeIf(cmd ->
                        !player.hasPermission("ezprotector.bypass.command.tabcomplete." + cmd.getName()) && blocked.contains(cmd.getName()));
            } else {
                event.getRootNode().getChildren().removeIf(cmd ->
                        !player.hasPermission("ezprotector.bypass.command.tabcomplete." + cmd.getName()) && !blocked.contains(cmd.getName()));
            }
        }
    }

}
