/*
 * Copyright (c) 2016-2018 dvargas135, DoNotSpamPls and contributors. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package com.github.donotspampls.ezprotector.mods;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.github.donotspampls.ezprotector.Main;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class DamageIndicators {

    public static void set(Player p) {
        if (!p.hasPermission("ezprotector.bypass.mod.damageindicators")) {
            if (Main.getPlugin().getServer().getVersion().contains("1.7")) {
                try {
                    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

                    String json = "{\"text\":\"\",\"extra\":[{\"text\":\"\\u00a70\\u00a70\\u00a7c\\u00a7d\\u00a7e\\u00a7f\"}]}";
                    PacketContainer motd = new PacketContainer(PacketType.Play.Server.CHAT);
                    motd.getChatComponents().write(0, WrappedChatComponent.fromJson(json));
                    protocolManager.sendServerPacket(p, motd);
                } catch (InvocationTargetException ignored) {}
            }
        }
    }

}
