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

public class OppedPlayerCommands {

    /**
     * Intercepts a command if it's blocked in the config for opped players and blocks it
     * This feature is very likely to be removed in the future
     * TODO: This has a lot of cleanup needed, but will probably be removed. We'll see
     *
     * @param event The command event from which other information is gathered
     */
    public static void execute(PlayerCommandPreprocessEvent event) {
        // Get information from the event, get the config and console and register strings
        Player player = event.getPlayer();
        Main.oppedPlayer = player.getName();
        String command = event.getMessage();
        FileConfiguration config = Main.getPlugin().getConfig();
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        String punishCommand;
        String notifyMessage;

        Main.errorMessage = config.getString("opped-player-commands.error-message");
        // Check if the player is OP (probably should check for a permission in the future)
        if (player.isOp()) {
            // Calculate the number of exempted players and run a for loop on them
            for (int i2 = 0; i2 < config.getStringList("opped-player-commands.bypassed-players").size(); i2++) {
                // Get the list of players that are exempt from this check
                String opped = config.getStringList("opped-player-commands.bypassed-players").get(i2);
                // Check if the player who executes the command is exempt, and if not - check if the command executed is blocked
                if (!opped.contains(Main.oppedPlayer)) {
                    // Run another for loop, calculating the size of the blocked commands and running for them
                    for (int i = 0; i < config.getStringList("opped-player-commands.commands").size(); i++) {
                        // Check if the executed command is blocked in the config
                        if (command.split(" ")[0].equalsIgnoreCase("/" + Main.playerCommand)) {
                            // Cancel the command event
                            event.setCancelled(true);
                            // Replace placeholder with the executed command
                            Main.playerCommand = config.getList("opped-player-commands.commands").get(i).toString();

                            // Send an error message to the player in question.
                            if (!Main.errorMessage.trim().equals("")) player.sendMessage(Main.placeholders(Main.errorMessage));

                            if (config.getBoolean("opped-player-commands.punish-player.enabled")) {
                                // Get the punishment command to dispatch
                                punishCommand = config.getString("opped-player-commands.punish-player.command");
                                // Dispatch punishment command to player
                                Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
                            }

                            // Get all the exempt players online in the server
                            Player ops = Bukkit.getPlayer(opped);
                            // Check if notifications to exempt players are enabled and that there are any online
                            if (config.getBoolean("opped-player-commands.notify-bypassed-players.enabled") && ops != null) {
                                // Get the notification message to send to online admins
                                notifyMessage = config.getString("opped-player-commands.notify-bypassed-players.message");
                                // Send the notification message to all online exempt players
                                if (!notifyMessage.trim().equals("")) ops.sendMessage(Main.placeholders(notifyMessage));
                            }
                        }
                    }
                }
            }
        }
    }

}
