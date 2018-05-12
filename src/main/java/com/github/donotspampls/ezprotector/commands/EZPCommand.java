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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ezp")) {
            if (args.length == 0) {
                if (!sender.hasPermission("ezprotector.bypass.command.ezprotector")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&leZProtector &7version &r") + Main.getPlugin().getDescription().getVersion());
                    return true;
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&leZProtector &7version &r") + Main.getPlugin().getDescription().getVersion());
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b/ezp reload &7- &rReloads the plugin configuration."));
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("ezprotector.reload")) {
                    sender.sendMessage(ChatColor.RED + "You don't have the required permission to run this command.");
                    return true;
                }

                Main.getPlugin().reloadConfig();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("prefix")) + " The config was reloaded!");
                return true;
            }
            sender.sendMessage(ChatColor.RED + "You have typed an invalid argument. Type /ezp to see a list of available commands.");
            return true;
        }
        return false;
    }
}
