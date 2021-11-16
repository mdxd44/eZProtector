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

package net.elytrium.ezprotector.velocity.listeners;

import com.mojang.brigadier.tree.CommandNode;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.PlayerAvailableCommandsEvent;
import java.util.function.Predicate;
import net.elytrium.ezprotector.shared.handlers.tab.TabCompletionHandler;
import net.elytrium.ezprotector.shared.handlers.tab.PlatformTabCompletion;

@SuppressWarnings({"UnstableApiUsage", "unchecked"})
public class BrigadierListener implements PlatformTabCompletion {

  private final TabCompletionHandler handler = new TabCompletionHandler(this);

  @Subscribe
  public void onCommandSend(PlayerAvailableCommandsEvent event) {
    if (event.getRootNode().getChildren().isEmpty()) {
      return;
    }

    this.handler.handle(event);
  }

  @Override
  public <E, C> Object blockCommand(E event, Predicate<? super C> filter) {
    return ((PlayerAvailableCommandsEvent) event).getRootNode().getChildren().removeIf((Predicate<? super CommandNode<?>>) filter);
  }

  @Override
  public <C> String getCommandName(C command) {
    return ((CommandNode<?>) command).getName();
  }

  @Override
  public <E> boolean hasPermission(E event, String permission) {
    return ((PlayerAvailableCommandsEvent) event).getPlayer().hasPermission(permission);
  }
}
