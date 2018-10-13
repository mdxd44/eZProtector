package com.github.donotspampls.ezprotector.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.Collections;
import java.util.List;

public class TabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1 && StringUtil.startsWithIgnoreCase("reload", args[0])) {
            return Collections.singletonList("reload");
        }
        return Collections.emptyList();
    }
}
