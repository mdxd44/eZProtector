/*
 * Copyright (c) 2016-2018 dvargas135, DoNotSpamPls and contributors. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package com.github.donotspampls.ezprotector.listeners;

import com.github.donotspampls.ezprotector.Main;
import com.github.donotspampls.ezprotector.utilities.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class IPlayerCommandPreprocessEvent implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        FileConfiguration config = Main.getPlugin().getConfig();

        if (config.getBoolean("custom-commands.blocked")) CustomCommands.execute(event);
        if (config.getBoolean("hidden-syntaxes.blocked")) HiddenSyntaxes.execute(event);
        if (config.getBoolean("opped-player-commands.blocked")) OppedPlayerCommands.execute(event);

        if (config.getBoolean("custom-plugins.enabled")) CustomPlugins.executeCustom(event);
        else CustomPlugins.executeBlock(event);

        if (config.getBoolean("custom-version.enabled")) CustomVersion.executeCustom(event);
        else CustomVersion.executeBlock(event);
    }

}
