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

package net.elytrium.ezprotector.waterfall.listeners;

import net.elytrium.ezprotector.shared.Settings;
import net.elytrium.ezprotector.waterfall.utilities.MessageUtil;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class CustomCommands implements Listener {

  private final MessageUtil msgUtil;

  public CustomCommands(MessageUtil msgUtil) {
    this.msgUtil = msgUtil;
  }

  @EventHandler
  public void execute(ChatEvent event) {
    if (!(event.getSender() instanceof ProxiedPlayer)) {
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) event.getSender();
    String command = event.getMessage().split(" ")[0];

    if (Settings.IMP.CUSTOM_COMMANDS.BLOCKED && !player.hasPermission("ezprotector.bypass.command.custom")) {
      for (String message : Settings.IMP.CUSTOM_COMMANDS.COMMANDS) {
        if (command.equalsIgnoreCase("/" + message)) {
          event.setCancelled(true);

          String errorMessage = Settings.IMP.CUSTOM_COMMANDS.ERROR_MESSAGE;
          if (!errorMessage.trim().isEmpty()) {
            player.sendMessage(TextComponent.fromLegacyText(this.msgUtil.placeholders(errorMessage, player, null, command)));
          }

          /*
          this.msgUtil.punishPlayers(Settings.IMP.CUSTOM_COMMANDS.PUNISH_PLAYER, player, errorMessage, command);
          this.msgUtil.notifyAdmins(Settings.IMP.CUSTOM_COMMANDS, player, command, "command.custom");
          */

          break;
        }
      }
    }
  }
}
