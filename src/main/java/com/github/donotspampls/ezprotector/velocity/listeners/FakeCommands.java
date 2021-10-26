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
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;

public class FakeCommands {

    private final Toml config;
    private final MessageUtil msgUtil;

    public FakeCommands(Toml config, MessageUtil msgUtil) {
        this.config = config;
        this.msgUtil = msgUtil;
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void execute(CommandExecuteEvent event) {
        if (event.getCommandSource() instanceof Player) {
            Player player = (Player) event.getCommandSource();
            String command = event.getCommand();

            if (event.getResult() == CommandExecuteEvent.CommandResult.denied()) return;

            if (!player.hasPermission("ezprotector.bypass.command.fake")) {
                if (command.matches("(?i)ver|version") && config.getBoolean("custom-version.enabled")) {
                    event.setResult(CommandExecuteEvent.CommandResult.denied());

                    String version = config.getString("custom-version.version");
                    player.sendMessage(
                            LegacyComponentSerializer.legacy().deserialize("This server is running server version " + version, '&')
                    );

                    msgUtil.notifyAdmins("custom-version", player, command, "command.version");
                }

                if (command.matches("(?i)pl|plugins") && config.getBoolean("custom-plugins.enabled")) {
                    event.setResult(CommandExecuteEvent.CommandResult.denied());

                    String[] plugins = config.getString("custom-plugins.plugins").split(", ");
                    int pluginCount = plugins.length;

                    TextComponent.Builder output = TextComponent.builder("Plugins (" + pluginCount + "): ");
                    for (int i = 0; i < pluginCount; i++) {
                        output.append(plugins[i], TextColor.GREEN);
                        if (i + 1 < pluginCount) {
                            output.append(TextComponent.of(", ", TextColor.WHITE));
                        }
                    }

                    player.sendMessage(output.build());

                    msgUtil.notifyAdmins("custom-plugins", player, command, "command.plugins");
                }
            }
        }
    }

}
