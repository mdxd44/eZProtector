/*
 * Copyright (c) 2016-2018 dvargas135, DoNotSpamPls and contributors. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package com.github.donotspampls.ezprotector.commands;

import com.github.donotspampls.ezprotector.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class EZPCommand implements CommandExecutor {

    /**
     * Checks if the /ezp command was executed
     * If yes, this also handles the entire command logic
     *
     * @param sender The player who sent the command
     * @param command The command which was sent
     * @param label Pretty much command.getName(). Not used in the code below
     * @param args The arguments after the command (/command <args>)
     * @return true if the command got executed successfully, otherwise false
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // TODO: Remove this debug code
        System.out.println(command);
        System.out.println(label);
        if (command.getName().equalsIgnoreCase("ezp")) {
            // Command variable matches the command defined in plugin.yml, check argument length
            if (args.length == 0) {
                // There are no arguments, return the plugin name, version and commands (if the player has the required permissions)
                if (!sender.hasPermission("ezprotector.bypass.command.ezprotector")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&leZProtector &7version &r") + Main.getPlugin().getDescription().getVersion());
                    return true;
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&leZProtector &7version &r") + Main.getPlugin().getDescription().getVersion());
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/ezp reload &7- &rReloads the plugin configuration."));
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                // Argument is reload, check if player has permission, then execute
                if (!sender.hasPermission("ezprotector.reload")) {
                    sender.sendMessage(ChatColor.RED + "You don't have the required permission to run this command.");
                    return true;
                }

                Main.getPlugin().reloadConfig();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("prefix")) + " The config was reloaded!");
                return true;
            }
            // The argument provided was invalid (currently only "reload" is valid), return error message.
            sender.sendMessage(ChatColor.RED + "You have typed an invalid argument. Type /ezp to see a list of available commands.");
            return true;
        }
        return false;
    }
}
