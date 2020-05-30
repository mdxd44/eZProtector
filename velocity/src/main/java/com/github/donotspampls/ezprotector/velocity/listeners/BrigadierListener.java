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

import com.github.donotspampls.ezprotector.velocity.Main;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.PlayerAvailableCommandsEvent;

import java.util.List;

public class BrigadierListener {

    /**
     * Removes forbidden commands from Brigadier's command tree (1.13)
     *
     * @param event The event which removes the tab completions from the client.
     */
    @Subscribe
    public void onCommandSend(PlayerAvailableCommandsEvent event) {
        Toml config = Main.getConfig();
        final List<String> blocked = config.getList("tab-completion.commands");

        System.out.println(event.getRootNode().getExamples());

        if (config.getBoolean("tab-completion.blocked") && !event.getPlayer().hasPermission("ezprotector.bypass.command.tabcomplete")) {
            if (!config.getBoolean("tab-completion.whitelist"))
                event.getRootNode().getChildren().stream()
                        .filter(commandNode -> blocked.contains(commandNode.toString()))
                        .iterator().remove();
            else
                event.getRootNode().getExamples().removeIf(cmd -> !blocked.contains(cmd));
        }
    }

}
