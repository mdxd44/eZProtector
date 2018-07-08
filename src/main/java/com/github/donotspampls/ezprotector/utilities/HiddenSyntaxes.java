/*
 * eZProtector - Copyright (C) 2018 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.utilities;

import com.github.donotspampls.ezprotector.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class HiddenSyntaxes {

    /**
     * Intercepts a command containing the ":" character and blocks it.
     *
     * @param event The command event from which other information is gathered.
     */
    public static void execute(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        Main.player = player.getName();
        String command = event.getMessage();
        FileConfiguration config = Main.getPlugin().getConfig();
        ConsoleCommandSender console = Bukkit.getConsoleSender();

        // Get the commands which will not be filtered by this check
        List<String> whitelisted = config.getStringList("hidden-syntaxes.whitelisted");

        // Check if the command contains :. If that is true, check if the player hasn't got the bypass permission and that the command hasn't got any spaces in it
        if (command.split(" ")[0].contains(":") && !player.hasPermission("ezprotector.bypass.command.hiddensyntax") && !whitelisted.contains(command.split(" ")[0].toLowerCase().replace("/", ""))) {
            event.setCancelled(true);
            // Replace placeholder with the executed command
            Main.playerCommand = command.replace("/", "");

            if (!Main.errorMessage.trim().equals("")) player.sendMessage(Main.placeholders(Main.errorMessage));

            if (config.getBoolean("hidden-syntaxes.punish-player.enabled")) {
                String punishCommand = config.getString("hidden-syntaxes.punish-player.command");
                // Replace placeholder with the error message in the config
                Main.errorMessage = config.getString("hidden-syntaxes.error-message");
                Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
            }

            if (config.getBoolean("hidden-syntaxes.notify-admins.enabled")) {
                String notifyMessage = config.getString("hidden-syntaxes.notify-admins.message");
                ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.hiddensyntax");
            }
        }
    }

}
