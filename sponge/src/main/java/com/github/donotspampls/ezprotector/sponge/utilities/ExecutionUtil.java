/*
 * eZProtector - Copyright (C) 2018-2020 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.sponge.utilities;

import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;

public class ExecutionUtil {

    private static Server server = Sponge.getServer();

    /**
     * Sends a notification message to all online admins
     *
     * @param message    The notification message sent to the admins
     * @param permission The required permission to recieve the notification
     */
    public static void notifyAdmins(Text message, String permission) {
        if (message.trim().isEmpty()) return;

        server.getOnlinePlayers().stream()
                .filter(admin -> admin.hasPermission(permission))
                .forEach(admin -> admin.sendMessage(message));
    }

    public static void executeConsoleCommand(String command) {
        Sponge.getCommandManager().process(server.getConsole(), command);
    }
}