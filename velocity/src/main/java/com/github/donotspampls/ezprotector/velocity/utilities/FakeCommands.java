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
import net.kyori.text.TextComponent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;

import java.awt.*;

public class FakeCommands {

    public static void execute(CommandExecuteEvent event) {
        Toml config = Main.getConfig();
        if (event.getCommandSource() instanceof Player) {
            Player player = (Player) event.getCommandSource();
            String command = event.getCommand();

            if (event.getResult() == CommandExecuteEvent.CommandResult.denied()) return;

            if (!player.hasPermission("ezprotector.bypass.command.fake")) {
                if (command.matches("(?i)/ver|/version") && config.getBoolean("custom-version.enabled")) {
                    event.setResult(CommandExecuteEvent.CommandResult.denied());

                    String version = config.getString("custom-version.version");
                    player.sendMessage(
                            LegacyComponentSerializer.legacy().deserialize("This server is running server version " + version, '&')
                    );

                    MessageUtil.notifyAdmins("custom-version", player, command, "command.version");
                }

                if (command.matches("(?i)/pl|/plugins") && config.getBoolean("custom-plugins.enabled")) {
                    event.setResult(CommandExecuteEvent.CommandResult.denied());

                    String[] plugins = config.getString("custom-plugins.plugins").split(", ");
                    String pluginsList = String.join(Color.WHITE + ", " + Color.GREEN, plugins);

                    // Create a fake /plugins output message using the string array above.
                    String customPlugins = "Plugins (" + plugins.length + "): " + Color.GREEN + pluginsList;

                    player.sendMessage(TextComponent.of(customPlugins));

                    MessageUtil.notifyAdmins("custom-plugins", player, command, "command.plugins");
                }
            }
        }
    }

}
