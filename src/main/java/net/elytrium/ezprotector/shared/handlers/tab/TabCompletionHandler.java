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

package net.elytrium.ezprotector.shared.handlers.tab;

import java.util.List;
import net.elytrium.ezprotector.shared.Settings;

public class TabCompletionHandler {

  private final PlatformTabCompletion platform;
  private final String permissionPrefix = "ezprotector.bypass.command.tabcomplete.";
  private final boolean enabled = Settings.IMP.TAB_COMPLETION.BLOCKED;
  private final List<String> blockedList = Settings.IMP.TAB_COMPLETION.COMMANDS;
  private final boolean whitelist = Settings.IMP.TAB_COMPLETION.WHITELIST;

  public TabCompletionHandler(PlatformTabCompletion platform) {
    this.platform = platform;
  }

  @SuppressWarnings("UnusedReturnValue")
  public <E> Object handle(E event) {
    if (this.enabled) {
      return this.platform.blockCommand(
          event,
          cmd -> {
            String command = this.platform.getCommandName(cmd);
            return !this.platform.hasPermission(event, this.permissionPrefix + command) && this.checkForWhitelist(this.blockedList.contains(command));
          }
      );
    }

    return event;
  }

  private boolean checkForWhitelist(boolean isContains) {
    if (this.whitelist) {
      return !isContains;
    } else {
      return isContains;
    }
  }
}
