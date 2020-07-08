/*
 * eZProtector - Copyright (C) 2018-2020 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.waterfall.utilities;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;

public class ExecutionUtil {

    private final ProxyServer server;
    public ExecutionUtil(ProxyServer server) {
        this.server = server;
    }

    public void notifyAdmins(String message, String permission) {
        if (message.trim().isEmpty()) return;

        server.getPlayers().stream()
                .filter(admin -> admin.hasPermission(permission))
                .forEach(admin -> admin.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
    }

    public void executeConsoleCommand(String command) {
        server.getPluginManager().dispatchCommand(server.getConsole(), command);
    }

}