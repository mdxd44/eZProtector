/*
 * Copyright (c) 2016-2018 dvargas135, DoNotSpamPls and contributors. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package com.github.donotspampls.ezprotector.utilities;

import com.github.donotspampls.ezprotector.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ExecutionUtil {

    /**
     * Sends a notification message to all online admins
     *
     * @param message The notification message sent to the admins
     * @param permission The required permission to recieve the notification
     */
    public static void notifyAdmins(String message, String permission) {
        // Start a for loop to check for players which have the required permission
        for (Player admin : Bukkit.getOnlinePlayers()) {
            // If a player has the required permission and the notify message isn't empty, send the message to the player
            if (admin.hasPermission(permission) && !message.trim().equals("")) admin.sendMessage(Main.placeholders(message));
        }
    }

}
