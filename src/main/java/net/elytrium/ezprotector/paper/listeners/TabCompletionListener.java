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
import net.elytrium.ezprotector.paper.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

public class TabCompletionListener implements Listener {

  private final Main plugin;

  public TabCompletionListener(Main plugin) {
    this.plugin = plugin;
  }

  /**
   * Checks if a player is tab completing a forbidden command. (1.12)
   *
   * @param event The tab complete event from which other information is gathered.
   */
  @EventHandler
  public void onTabComplete(TabCompleteEvent event) {
    FileConfiguration config = this.plugin.getConfig();

    if (config.getBoolean("tab-completion.blocked") && event.getSender() instanceof Player) {
      Player player = (Player) event.getSender();
      String cmd = event.getBuffer().split(" ")[0].replace("/", "");
      List<String> completions = event.getCompletions();
      List<String> blocked = config.getStringList("tab-completion.commands");

      if (completions.isEmpty()) {
        return;
      }

      if (!player.hasPermission("ezprotector.bypass.command.tabcomplete." + cmd)) {
        if (!config.getBoolean("tab-completion.whitelist")) {
          completions.removeIf(lcmd -> blocked.contains(lcmd.replace("/", "")));
          if (blocked.contains(cmd)) {
            event.setCancelled(true);
          }
        } else {
          if (completions.get(0).startsWith("/")) {
            completions.removeIf(lcmd -> !blocked.contains(lcmd.replace("/", "")));
          } else if (!blocked.contains(cmd)) {
            event.setCancelled(true);
          }
        }
        event.setCompletions(completions);
      }
    }
  }
}
