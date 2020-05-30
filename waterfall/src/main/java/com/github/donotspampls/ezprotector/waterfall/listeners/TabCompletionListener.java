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

import com.github.donotspampls.ezprotector.waterfall.Main;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class TabCompletionListener implements Listener {

    /**
     * Checks if a player is tab completing a forbidden command. (1.12)
     *
     * @param event The tab complete event from which other information is gathered.
     */
    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {
        Configuration config = Main.getConfig();
        final List<String> blocked = config.getStringList("tab-completion.commands");

        if (config.getBoolean("tab-completion.blocked") && event.getSender() instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) event.getSender();
            String cmd = event.getCursor().split(" ")[0].replace("/", "");
            List<String> completions = event.getSuggestions();

            if (completions.isEmpty()) return;

            if (!player.hasPermission("ezprotector.bypass.command.tabcomplete." + cmd)) {
                if (!config.getBoolean("tab-completion.whitelist")) {
                    completions.removeIf(lcmd -> blocked.contains(lcmd.replace("/", "")));
                    if (blocked.contains(cmd))
                        event.setCancelled(true);
                } else {
                    if (completions.get(0).startsWith("/")) {
                        completions.removeIf(lcmd -> !blocked.contains(lcmd.replace("/", "")));
                    } else if (!blocked.contains(cmd))
                        event.setCancelled(true);
                }
            }

        }
    }

}
