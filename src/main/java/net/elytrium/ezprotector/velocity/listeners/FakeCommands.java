/*
 * Copyright (C) 2021 Elytrium, DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.elytrium.ezprotector.velocity.listeners;

import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;
import net.elytrium.ezprotector.velocity.utilities.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class FakeCommands {

  private Toml config;
  private final MessageUtil msgUtil;

  public FakeCommands(MessageUtil msgUtil) {
    this.msgUtil = msgUtil;
  }

  @Subscribe
  public void execute(CommandExecuteEvent event) {
    if (event.getCommandSource() instanceof Player) {
      Player player = (Player) event.getCommandSource();
      String command = event.getCommand();

      if (event.getResult() == CommandExecuteEvent.CommandResult.denied()) {
        return;
      }

      if (!player.hasPermission("ezprotector.bypass.command.fake")) {
        if (command.matches("(?i)ver|version") && this.config.getBoolean("custom-version.enabled")) {
          event.setResult(CommandExecuteEvent.CommandResult.denied());

          String version = this.config.getString("custom-version.version");
          player.sendMessage(
              LegacyComponentSerializer.legacyAmpersand().deserialize("This server is running server version " + version)
          );

          this.msgUtil.notifyAdmins("custom-version", player, command, "command.version");
        }

        if (command.matches("(?i)pl|plugins") && this.config.getBoolean("custom-plugins.enabled")) {
          event.setResult(CommandExecuteEvent.CommandResult.denied());

          String[] plugins = this.config.getString("custom-plugins.plugins").split(", ");
          int pluginCount = plugins.length;

          TextComponent.Builder output = Component.text().append(Component.text("Plugins (" + pluginCount + "): "));
          for (int i = 0; i < pluginCount; i++) {
            output.append(Component.text(plugins[i], NamedTextColor.GREEN));
            if (i + 1 < pluginCount) {
              output.append(Component.text(", ", NamedTextColor.WHITE));
            }
          }

          player.sendMessage(output.build());

          this.msgUtil.notifyAdmins("custom-plugins", player, command, "command.plugins");
        }
      }
    }
  }
}
