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
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class SmartMoving {

    public void set(Player p) {
        try {
            ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

            String json = "{\"text\":\"\",\"extra\":[{\"text\":\"§0§1§0§1§2§f§f\"},{\"text\":\"§0§1§3§4§f§f\"},{\"text\":\"§0§1§5§f§f\"},{\"text\":\"§0§1§6§f§f\"},{\"text\":\"§0§1§7§f§f\"},{\"text\":\"§0§1§8§9§a§b§f§f\"}]}";
            PacketContainer motd = new PacketContainer(PacketType.Play.Server.CHAT);
            motd.getChatComponents().write(0, WrappedChatComponent.fromJson(json));
            protocolManager.sendServerPacket(p, motd);
        } catch (InvocationTargetException ignored) {}
    }

}
