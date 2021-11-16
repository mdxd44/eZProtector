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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

@SuppressWarnings("unchecked")
public class BrigadierListener implements Listener, PlatformTabCompletion {

  private final TabCompletionHandler handler = new TabCompletionHandler(this);

  @EventHandler
  public void onCommandSend(PlayerCommandSendEvent event) {
    if (event.getCommands().isEmpty()) {
      return;
    }

    this.handler.handle(event);
  }

  @Override
  public <E, C> Object blockCommand(E event, Predicate<? super C> filter) {
    return ((PlayerCommandSendEvent) event).getCommands().removeIf((Predicate<? super String>) filter);
  }

  @Override
  public <C> String getCommandName(C command) {
    return (String) command;
  }

  @Override
  public <E> boolean hasPermission(E event, String permission) {
    return ((PlayerCommandSendEvent) event).getPlayer().hasPermission(permission);
  }
}
