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

import java.util.function.Predicate;
import net.elytrium.ezprotector.shared.handlers.tab.PlatformTabCompletion;
import net.elytrium.ezprotector.shared.handlers.tab.TabCompletionHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

@SuppressWarnings("unchecked")
public class TabCompletionListener implements Listener, PlatformTabCompletion {

  private final TabCompletionHandler handler = new TabCompletionHandler(this);

  @EventHandler
  public void onTabComplete(TabCompleteEvent event) {
    if (!(event.getSender() instanceof Player) || event.getCompletions().isEmpty()) {
      return;
    }

    this.handler.handle(event);
  }

  @Override
  public <E, C> Object blockCommand(E event, Predicate<? super C> filter) {
    TabCompleteEvent event0 = (TabCompleteEvent) event;
    Predicate<? super String> filter0 = (Predicate<? super String>) filter;
    if (event0.getCompletions().removeIf(filter0) || filter0.test(event0.getBuffer())) {
      event0.setCancelled(true);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public <C> String getCommandName(C command) {
    return ((String) command).replace("/", "").split(" ")[0];
  }

  @Override
  public <E> boolean hasPermission(E event, String permission) {
    return ((TabCompleteEvent) event).getSender().hasPermission(permission);
  }
}
