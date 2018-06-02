/*
 * Copyright (c) 2016-2018 dvargas135, DoNotSpamPls and contributors. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package com.github.donotspampls.ezprotector.utilities;

import com.github.donotspampls.ezprotector.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ExecutionUtil {

    public static void notifyAdmins(String message, String permission) {
        for (Player admin : Bukkit.getOnlinePlayers()) {
            if (admin.hasPermission(permission) && !message.trim().equals("")) admin.sendMessage(Main.placeholders(message));
        }
    }

}
