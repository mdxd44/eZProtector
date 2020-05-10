/*
 * eZProtector - Copyright (C) 2018-2020 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.paper.utilities;

import com.github.donotspampls.ezprotector.paper.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CustomCommands {

    public static void execute(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage().split(" ")[0];
        FileConfiguration config = Main.getPlugin().getConfig();

        if (!player.hasPermission("ezprotector.bypass.command.custom")) {
            for (String message : config.getStringList("custom-commands.commands")) {
                if (command.equalsIgnoreCase("/" + message)) {
                    event.setCancelled(true);

                    String errorMessage = config.getString("custom-commands.error-message");
                    if (!errorMessage.trim().isEmpty())
                        player.sendMessage(MessageUtil.placeholders(errorMessage, player, null, command));

                    MessageUtil.punishPlayers("custom-commands", player, errorMessage, command);
                    MessageUtil.notifyAdmins("custom-commands", player, command, "command.custom");

                    break;
                }
            }
        }
    }

}
