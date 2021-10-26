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
import java.util.List;
import net.elytrium.ezprotector.velocity.utilities.MessageUtil;

public class HiddenSyntaxes {

  private final Toml config;
  private final MessageUtil msgUtil;

  public HiddenSyntaxes(Toml config, MessageUtil msgUtil) {
    this.config = config;
    this.msgUtil = msgUtil;
  }

  @Subscribe
  @SuppressWarnings("unused")
  public void execute(final CommandExecuteEvent event) {
    if (this.config.getBoolean("hidden-syntaxes.blocked")) {
      if (event.getCommandSource() instanceof Player) {
        final Player player = (Player) event.getCommandSource();
        final String command = event.getCommand();
        final List<String> whitelisted = this.config.getList("hidden-syntaxes.whitelisted");

        if (event.getResult() == CommandExecuteEvent.CommandResult.denied()) {
          return;
        }

        if (command.contains(":") && !whitelisted.contains(command) && !player.hasPermission("ezprotector.bypass.command.hiddensyntaxes")) {
          event.setResult(CommandExecuteEvent.CommandResult.denied());

          String errorMessage = this.config.getString("hidden-syntaxes.error-message");
          if (!errorMessage.trim().isEmpty()) {
            player.sendMessage(this.msgUtil.placeholdersText(errorMessage, player, null, command));
          }

          this.msgUtil.punishPlayers("hidden-syntaxes", player, errorMessage, command);
          this.msgUtil.notifyAdmins("hidden-syntaxes", player, command, "command.hiddensyntaxes");
        }
      }
    }
  }
}
