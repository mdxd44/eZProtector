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

package net.elytrium.ezprotector.paper.listeners;

import java.util.List;
import net.elytrium.ezprotector.paper.PaperPlugin;
import net.elytrium.ezprotector.paper.utilities.MessageUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class HiddenSyntaxes implements Listener {

  private final PaperPlugin plugin;

  public HiddenSyntaxes(PaperPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void execute(PlayerCommandPreprocessEvent event) {
    FileConfiguration config = this.plugin.getConfig();
    if (config.getBoolean("hidden-syntaxes.blocked")) {
      Player player = event.getPlayer();
      String command = event.getMessage().split(" ")[0].toLowerCase();
      MessageUtil msgUtil = this.plugin.getMsgUtil();
      List<String> whitelisted = config.getStringList("hidden-syntaxes.whitelisted");

      if (event.isCancelled()) {
        return;
      }

      if (command.contains(":") && !whitelisted.contains(command.replace("/", "")) && !player.hasPermission("ezprotector.bypass.command.hiddensyntaxes")) {
        event.setCancelled(true);

        String errorMessage = config.getString("hidden-syntaxes.error-message");
        if (errorMessage != null && !errorMessage.trim().isEmpty()) {
          player.sendMessage(msgUtil.placeholders(errorMessage, player, null, command));
        }

        msgUtil.punishPlayers("hidden-syntaxes", player, errorMessage, command);
        msgUtil.notifyAdmins("hidden-syntaxes", player, command, "command.hiddensyntaxes");
      }
    }
  }
}
