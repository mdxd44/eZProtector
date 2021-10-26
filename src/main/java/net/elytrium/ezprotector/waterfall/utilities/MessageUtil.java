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

package net.elytrium.ezprotector.waterfall.utilities;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

public class MessageUtil {

  private final Configuration config;
  private final ExecutionUtil execUtil;

  public MessageUtil(Configuration config, ExecutionUtil execUtil) {
    this.config = config;
    this.execUtil = execUtil;
  }

  public String placeholders(String args, ProxiedPlayer player, String errorMessage, String command) {
    String cargs =
        args.replace("%player%", player.getName())
            .replace("%errormessage%", errorMessage == null ? "" : errorMessage)
            .replace("%command%", command == null ? "" : command);

    return ChatColor.translateAlternateColorCodes('&', cargs);
  }

  public void punishPlayers(String module, ProxiedPlayer player, String errorMessage, String command) {
    if (this.config.getBoolean(module + ".punish-player.enabled")) {
      String punishCommand = this.config.getString(module + ".punish-player.command");
      this.execUtil.executeConsoleCommand(this.placeholders(punishCommand, player, errorMessage, command));
    }
  }

  public void notifyAdmins(String module, ProxiedPlayer player, String command, String perm) {
    if (this.config.getBoolean(module + ".notify-admins.enabled")) {
      String msg = this.config.getString(module + ".notify-admins.message");

      String notifyMessage = this.placeholders(msg, player, null, command);
      this.execUtil.notifyAdmins(notifyMessage, "ezprotector.notify." + perm);
    }
  }
}
