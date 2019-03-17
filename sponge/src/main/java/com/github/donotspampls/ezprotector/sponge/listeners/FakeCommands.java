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

import java.awt.*;

import static com.github.donotspampls.ezprotector.sponge.utilities.MessageUtil.color;

public class FakeCommands {

    /**
     * Intercepts the command and swaps the output with a fake one
     *
     * @param event The command event from which other information is gathered.
     */
    @Listener
    public void executeCustom(SendCommandEvent event) {
        if (event.getSource() instanceof Player) {
            Player player = (Player) event.getSource();
            String command = event.getCommand();
            Toml config = Main.getConfig();

            if (command.matches("/ver|/version") && !player.hasPermission("ezprotector.bypass.command.version") && config.getBoolean("custom-version.enabled")) {
                event.setCancelled(true);
                String version = Main.getConfig().getString("custom-version.version");
                player.sendMessage(Text.of(color("This server is running server version " + version)));
            } else if (command.matches("/pl|/plugins") && player.hasPermission("ezprotector.bypass.command.plugins") && config.getBoolean("custom-plugins.enabled")) {
                event.setCancelled(true);

                String[] plugins = Main.getConfig().getString("custom-plugins.plugins").split(", ");
                String pluginsList = String.join(Color.WHITE + ", " + Color.GREEN, plugins);

                // Create a fake /plugins output message using the string array above.
                String customPlugins = Color.WHITE + "Plugins (" + plugins.length + "): " + Color.GREEN + pluginsList;

                player.sendMessage(Text.of(customPlugins));
            }
        }
    }

    @Listener
    public void executeBlock(SendCommandEvent event) {
        if (event.getSource() instanceof Player) {
            Player player = (Player) event.getSource();
            String command = event.getCommand();
            Toml config = Main.getConfig();

            if (command.matches("/ver|/version") && !player.hasPermission("ezprotector.bypass.command.version") && config.getBoolean("custom-version.enabled")) {
                event.setCancelled(true);
                // Replace placeholder with the error message in the config
                String errorMessage = config.getString("custom-version.error-message");

                if (!errorMessage.trim().isEmpty())
                    player.sendMessage(Text.of(MessageUtil.placeholders(errorMessage, player, null, command)));

                if (config.getBoolean("custom-version.punish-player.enabled")) {
                    String punishCommand = config.getString("custom-version.punish-player.command");
                    // Replace placeholder with the error message in the config
                    errorMessage = config.getString("custom-version.error-message");
                    ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, errorMessage, command));
                }

                if (config.getBoolean("custom-version.notify-admins.enabled")) {
                    String notifyMessage = MessageUtil.placeholders(config.getString("custom-version.notify-admins.message"), player, null, command);
                    ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.version");
                }
            } else if (command.matches("/pl|/plugins") && player.hasPermission("ezprotector.bypass.command.plugins") && config.getBoolean("custom-plugins.enabled")) {
                event.setCancelled(true);
                String errorMessage = config.getString("custom-plugins.error-message");

                if (!errorMessage.trim().isEmpty())
                    player.sendMessage(Text.of(MessageUtil.placeholders(errorMessage, player, errorMessage, command)));

                if (config.getBoolean("custom-plugins.punish-player.enabled")) {
                    String punishCommand = config.getString("custom-plugins.punish-player.command");
                    // Replace placeholder with the error message in the config
                    errorMessage = config.getString("custom-version.error-message");
                    ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, errorMessage, command));
                }

                if (config.getBoolean("custom-plugins.notify-admins.enabled")) {
                    String notifyMessage = MessageUtil.placeholders(config.getString("custom-plugins.notify-admins.message"), player, null, command);
                    ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.plugins");
                }
            }
        }
    }
}
