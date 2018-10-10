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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import static com.github.donotspampls.ezprotector.utilities.MessageUtil.color;

public class CustomVersion {

    /**
     * Intercepts the /version command and swaps the output with a fake one
     *
     * @param event The command event from which other information is gathered.
     */
    public static void executeCustom(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage();

        String[] ver = new String[]{"/ver", "/version"};
        if (!player.hasPermission("ezprotector.bypass.command.version")) {
            for (String aList : ver) {
                // The command that is being tested at the moment
                if (command.split(" ")[0].equalsIgnoreCase(aList)) {
                    event.setCancelled(true);
                    String version = Main.getPlugin().getConfig().getString("custom-version.version");
                    player.sendMessage(color("This server is running server version " + version));
                }
            }
        }
    }

    /**
     * Intercepts the /version command and blocks it for the player who executed it.
     *
     * @param event The command event from which other information is gathered.
     */
    public static void executeBlock(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = Main.getPlugin().getConfig();

        if (!player.hasPermission("ezprotector.bypass.command.version")) {
            String[] ver = new String[]{"/ver", "/version"};
            for (String aList : ver) {
                // The command that is being tested at the moment
                if (event.getMessage().split(" ")[0].equalsIgnoreCase(aList)) {
                    event.setCancelled(true);
                    // Replace placeholder with the error message in the config
                    String errorMessage = config.getString("custom-version.error-message");

                    if (!errorMessage.trim().isEmpty()) player.sendMessage(MessageUtil.placeholders(errorMessage, player, null, aList));

                    if (config.getBoolean("custom-version.punish-player.enabled")) {
                        String punishCommand = config.getString("custom-version.punish-player.command");
                        // Replace placeholder with the error message in the config
                        errorMessage = config.getString("custom-version.error-message");
                        ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, errorMessage, aList));
                    }

                    if (config.getBoolean("custom-version.notify-admins.enabled")) {
                        String notifyMessage = MessageUtil.placeholders(config.getString("custom-version.notify-admins.message"), player, null, aList);
                        ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.command.version");
                    }
                }
            }
        }
    }

}
