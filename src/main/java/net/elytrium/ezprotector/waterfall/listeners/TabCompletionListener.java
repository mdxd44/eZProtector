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
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

public class TabCompletionListener implements Listener {

  private final Configuration config;

  public TabCompletionListener(Configuration config) {
    this.config = config;
  }

  @EventHandler
  public void onTabComplete(TabCompleteEvent event) {
    final List<String> blocked = this.config.getStringList("tab-completion.commands");

    if (this.config.getBoolean("tab-completion.blocked") && event.getSender() instanceof ProxiedPlayer) {
      ProxiedPlayer player = (ProxiedPlayer) event.getSender();
      String cmd = event.getCursor().split(" ")[0].replace("/", "");
      List<String> completions = event.getSuggestions();

      if (completions.isEmpty()) {
        return;
      }

      if (!player.hasPermission("ezprotector.bypass.command.tabcomplete." + cmd)) {
        if (!this.config.getBoolean("tab-completion.whitelist")) {
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
      }
    }
  }
}
