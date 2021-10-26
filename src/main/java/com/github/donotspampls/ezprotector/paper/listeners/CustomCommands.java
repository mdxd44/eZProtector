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

import com.github.donotspampls.ezprotector.paper.Main;
import com.github.donotspampls.ezprotector.paper.utilities.MessageUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CustomCommands implements Listener {

    private final Main plugin;
    public CustomCommands(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void execute(PlayerCommandPreprocessEvent event) {
        FileConfiguration config = plugin.getConfig();
        Player player = event.getPlayer();

        if (config.getBoolean("custom-commands.blocked") && !player.hasPermission("ezprotector.bypass.command.custom")) {
            String command = event.getMessage().split(" ")[0];
            MessageUtil msgUtil = plugin.getMsgUtil();
            for (String message : config.getStringList("custom-commands.commands")) {
                if (command.equalsIgnoreCase("/" + message)) {
                    event.setCancelled(true);

                    String errorMessage = config.getString("custom-commands.error-message");
                    if (errorMessage != null && !errorMessage.trim().isEmpty())
                        player.sendMessage(msgUtil.placeholders(errorMessage, player, null, command));

                    msgUtil.punishPlayers("custom-commands", player, errorMessage, command);
                    msgUtil.notifyAdmins("custom-commands", player, command, "command.custom");

                    break;
                }
            }
        }
    }

}
