/*
 * eZProtector - Copyright (C) 2018-2020 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.velocity.utilities;

import com.github.donotspampls.ezprotector.velocity.Main;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;

public class MessageUtil {

    private static final Toml config = Main.getConfig();

    public static String color(String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i+1]) > -1) {
                b[i] = '\u00A7';
                b[i+1] = Character.toLowerCase(b[i+1]);
            }
        }
        return new String(b);
    }

    public static String placeholders(String args, Player player, String errorMessage, String command) {
        return color(args
                .replace("%player%", player.getUsername())
                .replace("%errormessage%", errorMessage == null ? "" : errorMessage)
                .replace("%command%", command == null ? "" : command)
                .replace("%prefix%", Main.getPrefix()));
    }

    public static TextComponent placeholdersText(String args, Player player, String errorMessage, String command) {
        return LegacyComponentSerializer.legacy().deserialize(args
                .replace("%player%", player.getUsername())
                .replace("%errormessage%", errorMessage == null ? "" : color(errorMessage))
                .replace("%command%", command == null ? "" : color(command))
                .replace("%prefix%", Main.getPrefix())
        );
    }

    public static void punishPlayers(String module, Player player, String errorMessage, String command) {
        if (config.getBoolean(module + ".punish-player.enabled")) {
            String punishCommand = config.getString(module + ".punish-player.command");
            ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, errorMessage, command));
        }
    }

    public static void notifyAdmins(String module, Player player, String command, String perm) {
        if (config.getBoolean(module + ".notify-admins.enabled")) {
            String msg = config.getString(module + ".notify-admins.message");

            TextComponent notifyMessage =  MessageUtil.placeholdersText(msg, player, null, command);
            ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify." + perm);
        }
    }

}
