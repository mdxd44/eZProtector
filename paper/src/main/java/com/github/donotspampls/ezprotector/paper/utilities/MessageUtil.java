/*
 * eZProtector - Copyright (C) 2018-2019 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.paper.utilities;

import com.github.donotspampls.ezprotector.paper.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageUtil {

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String placeholders(String args, Player player, String errorMessage, String command) {
        return color(args)
                .replace("%player%", player.getName())
                .replace("%errormessage%", errorMessage == null ? "" : color(errorMessage))
                .replace("%command%", command == null ? "" : command)
                .replace("%prefix%", Main.getPrefix());
    }

}
