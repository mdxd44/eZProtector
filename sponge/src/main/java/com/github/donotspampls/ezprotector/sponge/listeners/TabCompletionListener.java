/*
 * eZProtector - Copyright (C) 2018-2020 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.sponge.listeners;

import com.github.donotspampls.ezprotector.sponge.Main;
import com.github.donotspampls.ezprotector.sponge.utilities.ExecutionUtil;
import com.github.donotspampls.ezprotector.sponge.utilities.MessageUtil;
import com.moandjiezana.toml.Toml;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.TabCompleteEvent;
import org.spongepowered.api.text.Text;

import java.util.List;

public class TabCompletionListener {

    /**
     * Checks if a player is tab completing a forbidden command. (1.12)
     *
     * @param event The tab complete event from which other information is gathered.
     */
    @Listener
    public void onTabComplete(TabCompleteEvent.Command event) {
        Toml config = Main.getConfig();
        final List<String> blocked = config.getList("tab-completion.blacklisted");

        // If tab blocking is enabled and the player has tried to autocomplete, continue
        if (config.getBoolean("tab-completion.blocked") && event.getSource() instanceof Player) {
            Player player = (Player) event.getSource();
            String cmd = event.getRawMessage().replace(" ", "");

            // Try all the commands in the blacklisted config section to see if there is a match
            for (String command : blocked) {
                if (command.contains(cmd) && !player.hasPermission("ezprotector.bypass.command.tabcomplete")) {
                    event.setCancelled(true);

                    String errorMessage = config.getString("tab-completion.warn.message");

                    if (config.getBoolean("tab-completion.warn.enabled") && !errorMessage.trim().isEmpty())
                        player.sendMessage(MessageUtil.placeholdersText(errorMessage, player, null, cmd));

                    if (config.getBoolean("tab-completion.punish-player.enabled")) {
                        String punishCommand = config.getString("tab-completion.punish-player.command");
                        ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, errorMessage, cmd));
                    }

                    if (config.getBoolean("tab-completion.notify-admins.enabled")) {
                        Text notifyMessage = MessageUtil.placeholdersText(config.getString("tab-completion.notify-admins.message"), player, null, command);
                        ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.tabcomplete");
                    }
                    break;
                }
            }
        }
    }

}
