/*
 * eZProtector - Copyright (C) 2018-2020 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.sponge.listeners;

import com.github.donotspampls.ezprotector.sponge.Main;
import com.github.donotspampls.ezprotector.sponge.utilities.CustomCommands;
import com.github.donotspampls.ezprotector.sponge.utilities.HiddenSyntaxes;
import com.moandjiezana.toml.Toml;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.SendCommandEvent;

public class CommandEventListener {

    @Listener
    public void onCommand(SendCommandEvent event) {
        Toml config = Main.getConfig();

        if (config.getBoolean("custom-commands.blocked")) CustomCommands.execute(event);
        if (config.getBoolean("hidden-syntaxes.blocked")) HiddenSyntaxes.execute(event);
    }

}
