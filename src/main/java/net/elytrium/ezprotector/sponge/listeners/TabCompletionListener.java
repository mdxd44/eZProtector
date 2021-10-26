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

package net.elytrium.ezprotector.sponge.listeners;

import com.moandjiezana.toml.Toml;
import java.util.List;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.TabCompleteEvent;

public class TabCompletionListener {

  private final Toml config;

  public TabCompletionListener(Toml config) {
    this.config = config;
  }

  @Listener
  public void onTabComplete(TabCompleteEvent.Command event) {
    final List<String> blocked = this.config.getList("tab-completion.commands");

    if (this.config.getBoolean("tab-completion.blocked") && event.getSource() instanceof Player) {
      Player player = (Player) event.getSource();
      String cmd = event.getRawMessage().replace(" ", "");
      List<String> completions = event.getTabCompletions();

      if (completions.isEmpty()) {
        return;
      }

      if (!player.hasPermission("ezprotector.bypass.command.tabcomplete")) {
        if (!this.config.getBoolean("tab-completion.whitelist")) {
          completions.removeIf(blocked::contains);
          if (blocked.contains(cmd)) {
            event.setCancelled(true);
          }
        } else {
          completions.removeIf(lcmd -> !blocked.contains(lcmd));
          for (String lcmd : blocked) {
            if (lcmd.equalsIgnoreCase(cmd)) {
              return; // subcommand tab completion
            }
          }
        }
      }
    }
  }

}
