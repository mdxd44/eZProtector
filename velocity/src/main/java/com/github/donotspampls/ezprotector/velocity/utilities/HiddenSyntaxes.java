/*
 * eZProtector - Copyright (C) 2018-2020 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.velocity.utilities;

import com.github.donotspampls.ezprotector.velocity.Main;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;

import java.util.List;

public class HiddenSyntaxes {

    public static void execute(CommandExecuteEvent event) {
        Toml config = Main.getConfig();

        if (event.getCommandSource() instanceof Player) {
            Player player = (Player) event.getCommandSource();
            String command = event.getCommand();
            List<String> whitelisted = config.getList("hidden-syntaxes.whitelisted");

            if (event.getResult() == CommandExecuteEvent.CommandResult.denied()) return;

            if (command.contains(":") && !whitelisted.contains(command) && !player.hasPermission("ezprotector.bypass.command.hiddensyntaxes")) {
                event.setResult(CommandExecuteEvent.CommandResult.denied());

                String errorMessage = config.getString("hidden-syntaxes.error-message");
                if (!errorMessage.trim().isEmpty())
                    player.sendMessage(MessageUtil.placeholdersText(errorMessage, player, null, command));

                MessageUtil.punishPlayers("hidden-syntaxes", player, errorMessage, command);
                MessageUtil.notifyAdmins("hidden-syntaxes", player, command, "command.hiddensyntaxes");
            }
        }
    }

}
