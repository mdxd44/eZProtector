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
import com.velocitypowered.api.event.player.TabCompleteEvent;
import com.velocitypowered.api.proxy.Player;

import java.util.List;

public class TabCompletionListener {

    /**
     * Checks if a player is tab completing a forbidden command. (1.12)
     *
     * @param event The tab complete event from which other information is gathered.
     */
    @Subscribe
    public void onTabComplete(TabCompleteEvent event) {
        Toml config = Main.getConfig();
        final List<String> blocked = config.getList("tab-completion.commands");

        if (config.getBoolean("tab-completion.blocked")) {
            Player player = event.getPlayer();
            String cmd = event.getPartialMessage().replace(" ", "");
            List<String> completions = event.getSuggestions();

            if (completions.isEmpty()) return;

            if (!player.hasPermission("ezprotector.bypass.command.tabcomplete")) {
                if (!config.getBoolean("tab-completion.whitelist")) {
                    completions.removeIf(blocked::contains);
                    if (blocked.contains(cmd)) completions.clear();
                } else {
                    // TODO: does velocity include / or not?
                    completions.removeIf(lcmd -> !blocked.contains(lcmd));
                    for (String lcmd : blocked) {
                        if (lcmd.equalsIgnoreCase(cmd)) completions.clear();
                    }
                }
            }
        }
    }

}
