/*
 * eZProtector - Copyright (C) 2018-2020 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.velocity.utilities;

import com.google.inject.Inject;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;

public class ExecutionUtil {

    @Inject
    private static ProxyServer server;

    public static void notifyAdmins(TextComponent message, String permission) {
        if (message.isEmpty()) return;

        server.getAllPlayers().stream()
                .filter(admin -> admin.hasPermission(permission))
                .forEach(admin -> admin.sendMessage(message));
    }

    public static void executeConsoleCommand(String command) {
        server.getCommandManager().executeAsync(server.getConsoleCommandSource(), command);
    }
}