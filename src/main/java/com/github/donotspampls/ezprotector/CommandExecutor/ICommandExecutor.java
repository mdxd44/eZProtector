/*
Copyright (c) 2016-2017 dvargas135

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.github.donotspampls.ezprotector.CommandExecutor;

import com.github.donotspampls.ezprotector.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ICommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		String prefix = Main.getPlugin().getConfig().getString("prefix").replaceAll("&", "§");
    	if (command.getName().equalsIgnoreCase("ezp")) {

    		if (args.length == 0) {
				if (!sender.hasPermission("ezprotector.bypass.command.ezprotector")) {
					sender.sendMessage(ChatColor.GREEN + "eZProtector |" + ChatColor.AQUA + "Version " + ChatColor.WHITE + Main.getPlugin().getDescription().getVersion());
					return true;
				} else {
                    sender.sendMessage(ChatColor.GREEN + "eZProtector |" + ChatColor.AQUA + "Version " + ChatColor.WHITE + Main.getPlugin().getDescription().getVersion());
                    sender.sendMessage(ChatColor.GREEN + "/ezp reload" + ChatColor.WHITE + "- " + ChatColor.AQUA + " Reloads the plugin configuration");
                }
				return true;
			} else if(args[0].equalsIgnoreCase("reload")) {
    			if (!sender.hasPermission("ezprotector.reload")) {
    				sender.sendMessage(ChatColor.RED + "You don't have the required permission to run this command.");
    				return true;
    			}

    			Main.getPlugin().reloadConfig();
    			sender.sendMessage(prefix + " The config was reloaded!");
    			return true;
    		}

    		sender.sendMessage(ChatColor.RED + "You have typed an invalid argument. Type /ezp to see a list of available commands.");
    		return true;
    	}
    	return false;
	}
}
