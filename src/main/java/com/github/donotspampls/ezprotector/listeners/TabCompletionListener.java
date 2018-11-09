/*
 * eZProtector - Copyright (C) 2018 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.listeners;

import com.github.donotspampls.ezprotector.Main;
import com.github.donotspampls.ezprotector.utilities.ExecutionUtil;
import com.github.donotspampls.ezprotector.utilities.MessageUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.List;

public class TabCompletionListener implements Listener {

    /**
     * Checks if a player is tab completing a forbidden command. (1.8-1.12)
     *
     * @param event The tab complete event from which other information is gathered.
     */
    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {
        FileConfiguration config = Main.getPlugin().getConfig();
        final List<String> blocked = config.getStringList("tab-completion.blacklisted");

        // If tab blocking is enabled and the player has tried to autocomplete, continue
        if (config.getBoolean("tab-completion.blocked") && event.getSender() instanceof Player) {
            Player player = (Player) event.getSender();
            String cmd = event.getBuffer().replaceAll("[/ ]", "");

            // Try all the commands in the blacklisted config section to see if there is a match
            for (String command : blocked) {
                if (command.equalsIgnoreCase(cmd) && !player.hasPermission("ezprotector.bypass.command.tabcomplete")) {
                    event.setCancelled(true);

                    String errorMessage = config.getString("tab-completion.warn.message");

                    if (config.getBoolean("tab-completion.warn.enabled") && !errorMessage.trim().isEmpty()) {
                        player.sendMessage(MessageUtil.placeholders(errorMessage, player, null, cmd));
                    }

                    if (Main.getPlugin().getConfig().getBoolean("tab-completion.punish-player.enabled")) {
                        String punishCommand = config.getString("tab-completion.punish-player.command");
                        // Replace placeholder with the error message in the config
                        ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, errorMessage, cmd));
                    }

                    if (Main.getPlugin().getConfig().getBoolean("tab-completion.notify-admins.enabled")) {
                        String notifyMessage = MessageUtil.placeholders(config.getString("tab-completion.notify-admins.message"), player, null, command);
                        ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.tabcomplete");
                    }
                    break;
                }
            }
        }
    }

}
