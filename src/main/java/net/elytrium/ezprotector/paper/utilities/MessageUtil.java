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

import me.clip.placeholderapi.PlaceholderAPI;
import net.elytrium.ezprotector.paper.PaperPlugin;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MessageUtil {

  private final PaperPlugin plugin;
  private final ExecutionUtil execUtil;
  private final boolean papi;

  public MessageUtil(PaperPlugin plugin, ExecutionUtil execUtil, boolean papi) {
    this.plugin = plugin;
    this.execUtil = execUtil;
    this.papi = papi;
  }

  public String placeholders(String args, Player player, String errorMessage, String command) {
    String cargs = args.replace("%player%", player.getName())
        .replace("%errormessage%", errorMessage == null ? "" : errorMessage)
        .replace("%command%", command == null ? "" : command);

    if (this.papi) {
      return PlaceholderAPI.setPlaceholders(player, ChatColor.translateAlternateColorCodes('&', cargs));
    } else {
      return ChatColor.translateAlternateColorCodes('&', cargs);
    }
  }

  public void punishPlayers(String module, Player player, String errorMessage, String command) {
    FileConfiguration config = this.plugin.getConfig();
    if (config.getBoolean(module + ".punish-player.enabled")) {
      String punishCommand = config.getString(module + ".punish-player.command");
      this.execUtil.executeConsoleCommand(this.placeholders(punishCommand, player, errorMessage, command));
    }
  }

  public void notifyAdmins(String module, Player player, String command, String perm) {
    FileConfiguration config = this.plugin.getConfig();
    if (config.getBoolean(module + ".notify-admins.enabled")) {
      String msg = config.getString(module + ".notify-admins.message");

      String notifyMessage = this.placeholders(msg, player, null, command);
      this.execUtil.notifyAdmins(notifyMessage, "ezprotector.notify." + perm);
    }
  }
}
