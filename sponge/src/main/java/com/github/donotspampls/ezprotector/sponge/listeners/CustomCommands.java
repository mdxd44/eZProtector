/*
 * eZProtector - Copyright (C) 2018-2019 DoNotSpamPls
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
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.text.Text;

public class CustomCommands {

    /**
     * Intercepts a command if it's found to be blocked by the server admin.
     *
     * @param event The command event from which other information is gathered.
     */
    @Listener
    public void execute(SendCommandEvent event) {
        Toml config = Main.getConfig();
        if (event.getSource() instanceof Player && config.getBoolean("custom-commands.blocked")) {
            Player player = (Player) event.getSource();
            String command = event.getCommand();

            for (Object message : config.getList("custom-commands.commands")) {
                // Replace placeholder with the command executed by the player
                if (command.equalsIgnoreCase(message.toString()) && !player.hasPermission("ezprotector.bypass.command.custom")) {
                    event.setCancelled(true);
                    // Replace placeholder with the error message in the config
                    String errorMessage = config.getString("custom-commands.error-message");

                    if (!errorMessage.trim().isEmpty())
                        player.sendMessage(MessageUtil.placeholdersText(errorMessage, player, null, "/" + message));

                    if (config.getBoolean("custom-commands.punish-player.enabled")) {
                        String punishCommand = config.getString("custom-commands.punish-player.command");
                        ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, errorMessage, "/" + message));
                    }

                    if (config.getBoolean("custom-commands.notify-admins.enabled")) {
                        Text notifyMessage = MessageUtil.placeholdersText(config.getString("custom-commands.notify-admins.message"), player, null, command);
                        ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.custom");
                    }
                }
            }
        }
    }
}
