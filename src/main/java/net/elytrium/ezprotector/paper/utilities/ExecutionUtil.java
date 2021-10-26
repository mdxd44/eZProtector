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

package net.elytrium.ezprotector.paper.utilities;

import org.bukkit.ChatColor;
import org.bukkit.Server;

public class ExecutionUtil {

  private final Server server;

  public ExecutionUtil(Server server) {
    this.server = server;
  }

  public void notifyAdmins(String message, String permission) {
    if (message.trim().isEmpty()) {
      return;
    }

    this.server.getOnlinePlayers().stream()
        .filter(admin -> admin.hasPermission(permission))
        .forEach(admin -> admin.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
  }

  public void executeConsoleCommand(String command) {
    this.server.dispatchCommand(this.server.getConsoleSender(), command);
  }
}
