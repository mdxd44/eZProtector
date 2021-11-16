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

/*
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.proxy.command.VelocityCommandManager;
import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import net.elytrium.ezprotector.shared.handlers.tab.TabCompletionHandler;
import net.elytrium.ezprotector.shared.handlers.tab.PlatformTabCompletion;
*/

/*
 * Stupid shit.
 */
@SuppressWarnings("unchecked")
public class TabCompletionListener /*extends CommandContextBuilder<CommandSource> implements PlatformTabCompletion */{
/*
  private final TabCompletionHandler handler = new TabCompletionHandler(this);
  private SuggestionProvider<CommandSource> defaultProvider;

  public TabCompletionListener(ProxyServer server) {
    try {
      Field suggestionsProviderField = VelocityCommandManager.class.getDeclaredField("suggestionsProvider");
      suggestionsProviderField.setAccessible(true);
      this.defaultProvider = (SuggestionsProvider<CommandSource>) suggestionsProviderField.get(server.getCommandManager());
      suggestionsProviderField.set(server.getCommandManager(), this);

    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  @Override
  public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSource> ctx, SuggestionsBuilder builder) throws CommandSyntaxException {
    return (CompletableFuture<Suggestions>) this.handler.onCommandSend(this.defaultProvider.getSuggestions(ctx, builder), ctx);
  }

  @Override
  public <E, C> Object blockCommand(E event, Predicate<? super C> filter) {
    try {
      return ((CompletableFuture<Suggestions>) event).get().getList().removeIf((Predicate<? super Suggestion>) filter);
    } catch (InterruptedException | ExecutionException e) {
      return event;
    }
  }

  @Override
  public <C> String getCommandName(C command) {
    return ((Suggestion) command).getText();
  }

  @Override
  public <E> boolean hasPermission(E event, String permission) {
    return ((Player) ((CommandContext<CommandSource>) event).getSource()).hasPermission(permission);
  }
*/
}