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

import com.github.donotspampls.ezprotector.velocity.utilities.MessageUtil;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;

public class CustomCommands {

    private final Toml config;
    private final MessageUtil msgUtil;

    public CustomCommands(Toml config, MessageUtil msgUtil) {
        this.config = config;
        this.msgUtil = msgUtil;
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void execute(CommandExecuteEvent event) {
        if (config.getBoolean("custom-commands.blocked")) {
            if (event.getCommandSource() instanceof Player) {
                Player player = (Player) event.getCommandSource();
                String command = event.getCommand();

                if (!player.hasPermission("ezprotector.bypass.command.custom")) {
                    for (Object message : config.getList("custom-commands.commands")) {
                        if (command.equalsIgnoreCase((String) message)) {
                            event.setResult(CommandExecuteEvent.CommandResult.denied());

                            String errorMessage = config.getString("custom-commands.error-message");
                            if (!errorMessage.trim().isEmpty())
                                player.sendMessage(msgUtil.placeholdersText(errorMessage, player, null, command));

                            msgUtil.punishPlayers("custom-commands", player, errorMessage, command);
                            msgUtil.notifyAdmins("custom-commands", player, command, "command.custom");

                            break;
                        }
                    }
                }
            }
        }
    }
}
