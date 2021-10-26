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

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

public class PlayerJoinListener implements Listener {

  private final Configuration config;

  public PlayerJoinListener(Configuration config) {
    this.config = config;
  }

  @EventHandler
  public void onPlayerJoin(PostLoginEvent event) {
    ProxiedPlayer p = event.getPlayer();

    if (this.config.getBoolean("mods.betterpvp.block") && !p.hasPermission("ezprotector.bypass.mod.betterpvp")) {
      p.sendMessage(" §c §r§5 §r§1 §r§f §r§0 ");
    }
    if (this.config.getBoolean("mods.voxelmap.block") && !p.hasPermission("ezprotector.bypass.mod.voxelmap")) {
      p.sendMessage(" §3 §6 §3 §6 §3 §6 §e §3 §6 §3 §6 §3 §6 §d ");
    }
  }
}
