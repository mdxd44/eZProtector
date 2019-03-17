/*
 * eZProtector - Copyright (C) 2018-2019 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.sponge.utilities;

import com.github.donotspampls.ezprotector.sponge.Main;
import com.google.inject.Inject;
import org.spongepowered.api.Game;
import org.spongepowered.api.text.Text;

import static com.github.donotspampls.ezprotector.sponge.utilities.MessageUtil.color;

public class ExecutionUtil {

    @Inject
    private static Game server;

    /**
     * Sends a notification message to all online admins
     *
     * @param message    The notification message sent to the admins
     * @param permission The required permission to recieve the notification
     */
    public static void notifyAdmins(String message, String permission) {
        if (message.trim().isEmpty()) return;

        server.getServer().getOnlinePlayers().stream()
                .filter(admin -> admin.hasPermission(permission))
                .forEach(admin -> admin.sendMessage(Text.of(color(message))));
    }

    public static void executeConsoleCommand(String command) {
        server.getCommandManager().process(server.getServer().getConsole(), command);
    }
}