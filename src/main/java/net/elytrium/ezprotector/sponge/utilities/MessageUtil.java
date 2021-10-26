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

package net.elytrium.ezprotector.sponge.utilities;

import com.moandjiezana.toml.Toml;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class MessageUtil {

  private final Toml config;
  private final ExecutionUtil execUtil = new ExecutionUtil();

  public MessageUtil(Toml config) {
    this.config = config;
  }

  public static String color(String textToTranslate) {
    char[] b = textToTranslate.toCharArray();
    for (int i = 0; i < b.length - 1; i++) {
      if (b[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
        b[i] = '§';
        b[i + 1] = Character.toLowerCase(b[i + 1]);
      }
    }
    return new String(b);
  }

  public String placeholders(String args, Player player, String errorMessage, String command) {
    return color(args
        .replace("%player%", player.getName())
        .replace("%errormessage%", errorMessage == null ? "" : errorMessage)
        .replace("%command%", command == null ? "" : command));
  }

  public Text placeholdersText(String args, Player player, String errorMessage, String command) {
    return TextSerializers.FORMATTING_CODE.deserialize(args
        .replace("%player%", player.getName())
        .replace("%errormessage%", errorMessage == null ? "" : color(errorMessage))
        .replace("%command%", command == null ? "" : color(command)));
  }

  public void punishPlayers(String module, Player player, String errorMessage, String command) {
    if (this.config.getBoolean(module + ".punish-player.enabled")) {
      String punishCommand = this.config.getString(module + ".punish-player.command");
      this.execUtil.executeConsoleCommand(this.placeholders(punishCommand, player, errorMessage, command));
    }
  }

  public void notifyAdmins(String module, Player player, String command, String perm) {
    if (this.config.getBoolean(module + ".notify-admins.enabled")) {
      String msg = this.config.getString(module + ".notify-admins.message");

      Text notifyMessage = this.placeholdersText(msg, player, null, command);
      this.execUtil.notifyAdmins(notifyMessage, "ezprotector.notify." + perm);
    }
  }
}
