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

import io.github.waterfallmc.waterfall.event.ProxyDefineCommandsEvent;
import java.util.function.Predicate;
import net.elytrium.ezprotector.shared.handlers.tab.PlatformTabCompletion;
import net.elytrium.ezprotector.shared.handlers.tab.TabCompletionHandler;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

// TODO: Block commands from internal servers via reflection without waterfall event.
@SuppressWarnings("unchecked")
public class BrigadierListener implements Listener, PlatformTabCompletion {

  private final TabCompletionHandler handler = new TabCompletionHandler(this);

  @EventHandler
  public void onCommandSend(ProxyDefineCommandsEvent event) {
    if (!(event.getReceiver() instanceof ProxiedPlayer) || event.getCommands().isEmpty()) {
      return;
    }

    this.handler.handle(event);
  }

  @Override
  public <E, C> Object blockCommand(E event, Predicate<? super C> filter) {
    return ((ProxyDefineCommandsEvent) event).getCommands().values().removeIf((Predicate<? super Command>) filter);
  }

  @Override
  public <C> String getCommandName(C command) {
    return ((Command) command).getName();
  }

  @Override
  public <E> boolean hasPermission(E event, String permission) {
    return ((ProxiedPlayer) ((ProxyDefineCommandsEvent) event).getReceiver()).hasPermission(permission);
  }
}
