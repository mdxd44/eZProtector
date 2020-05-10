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

import java.util.List;

public class HiddenSyntaxes {

    public static void execute(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage().split(" ")[0].toLowerCase();
        FileConfiguration config = Main.getPlugin().getConfig();
        List<String> whitelisted = config.getStringList("hidden-syntaxes.whitelisted");

        if (event.isCancelled()) return;

        if (command.contains(":") && !whitelisted.contains(command.replace("/", ""))
                && !player.hasPermission("ezprotector.bypass.command.hiddensyntaxes")) {
            event.setCancelled(true);

            String errorMessage = config.getString("hidden-syntaxes.error-message");
            if (!errorMessage.trim().isEmpty())
                player.sendMessage(MessageUtil.placeholders(errorMessage, player, null, command));

            MessageUtil.punishPlayers("hidden-syntaxes", player, errorMessage, command);
            MessageUtil.notifyAdmins("hidden-syntaxes", player, command, "command.hiddensyntaxes");
        }
    }

}
