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

import java.util.List;
import net.elytrium.ezprotector.waterfall.utilities.MessageUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

public class HiddenSyntaxes implements Listener {

  private final Configuration config;
  private final MessageUtil msgUtil;

  public HiddenSyntaxes(Configuration config, MessageUtil msgUtil) {
    this.config = config;
    this.msgUtil = msgUtil;
  }

  @EventHandler
  public void execute(ChatEvent event) {
    if (!(event.getSender() instanceof ProxiedPlayer)) {
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) event.getSender();
    String command = event.getMessage().split(" ")[0].toLowerCase();
    List<String> whitelisted = this.config.getStringList("hidden-syntaxes.whitelisted");

    if (event.isCancelled()) {
      return;
    }

    if (this.config.getBoolean("hidden-syntaxes.blocked")
        && command.startsWith("/") && command.contains(":")
        && !whitelisted.contains(command.replace("/", ""))
        && !player.hasPermission("ezprotector.bypass.command.hiddensyntaxes")) {
      event.setCancelled(true);

      String errorMessage = this.config.getString("hidden-syntaxes.error-message");
      if (!errorMessage.trim().isEmpty()) {
        player.sendMessage(this.msgUtil.placeholders(errorMessage, player, null, command));
      }

      this.msgUtil.punishPlayers("hidden-syntaxes", player, errorMessage, command);
      this.msgUtil.notifyAdmins("hidden-syntaxes", player, command, "command.hiddensyntaxes");
    }
  }
}
