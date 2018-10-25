package com.github.donotspampls.ezprotector.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.Collections;
import java.util.List;

public class EZPTabCompleter implements TabCompleter {

    /**
     * Handles tab completion logic for the /ezp command
     *
     * @param sender The player who sent the command
     * @param command The command which was sent
     * @param alias Pretty much command.getAlias(). Not used in the code below
     * @param args The arguments after the command (/command <args>)
     * @return Collection with the tab completion results
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1 && StringUtil.startsWithIgnoreCase("reload", args[0])) {
            return Collections.singletonList("reload");
        }
        return Collections.emptyList();
    }

}
