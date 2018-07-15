/*
 * eZProtector - Copyright (C) 2018 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.commands;

import com.github.donotspampls.ezprotector.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import static com.github.donotspampls.ezprotector.utilities.MessageUtil.color;

public class EZPCommand implements CommandExecutor {

    /**
     * Checks if the /ezp command was executed and handles command logic.
     *
     * @param sender The player who sent the command
     * @param command The command which was sent
     * @param label Pretty much command.getName(). Not used in the code below
     * @param args The arguments after the command (/command <args>)
     * @return true if the command got executed successfully, otherwise false
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ezp") && args.length == 0) {
            sender.sendMessage(color( "&a&leZProtector &7version &r") + Main.getPlugin().getDescription().getVersion());
            sender.sendMessage(color("&b/ezp reload &7- &rReloads the plugin configuration."));
            return true;
        } else if (args[0].equalsIgnoreCase("reload")) {
            Main.getPlugin().reloadConfig();
            sender.sendMessage(color(Main.getPlugin().getConfig().getString("prefix")) + " The config was reloaded!");
            return true;
        }
        sender.sendMessage(color("&4You have typed an invalid argument. Type /ezp to see a list of available commands."));
        return true;
    }
}
