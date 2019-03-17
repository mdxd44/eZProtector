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

import java.util.List;

public class HiddenSyntaxes {

    /**
     * Intercepts a command containing the ":" character and blocks it.
     *
     * @param event The command event from which other information is gathered.
     */
    @Listener
    public void onHiddenSyntax(SendCommandEvent event) {
        Toml config = Main.getConfig();
        if (event.getSource() instanceof Player && config.getBoolean("custom-commands.blocked")) {
            Player player = (Player) event.getSource();
            String command = event.getCommand();

            // Get the commands which will not be filtered by this check
            List<String> whitelisted = config.getList("hidden-syntaxes.whitelisted");

            // Check if the command contains :. If that is true, check if the player hasn't got the bypass permission and that the command hasn't got any spaces in it
            if (command.contains(":") && !whitelisted.contains(command.replace("/", "")) && !player.hasPermission("ezprotector.bypass.command.hiddensyntax")) {
                event.setCancelled(true);

                // Replace placeholder with the executed command
                String errorMessage = config.getString("hidden-syntaxes.error-message");

                if (!errorMessage.trim().isEmpty())
                    player.sendMessage(Text.of(MessageUtil.placeholders(errorMessage, player, null, command)));

                if (config.getBoolean("hidden-syntaxes.punish-player.enabled")) {
                    String punishCommand = config.getString("hidden-syntaxes.punish-player.command");
                    // Replace placeholder with the error message in the config
                    errorMessage = config.getString("hidden-syntaxes.error-message");
                    ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, errorMessage, command));
                }

                if (config.getBoolean("hidden-syntaxes.notify-admins.enabled")) {
                    String notifyMessage = MessageUtil.placeholders(config.getString("hidden-syntaxes.notify-admins.message"), player, null, command);
                    ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.hiddensyntax");
                }
            }
        }
    }

}
