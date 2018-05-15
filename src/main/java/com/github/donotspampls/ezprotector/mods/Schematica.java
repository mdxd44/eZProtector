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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

public class Schematica {

    public static void set(Player p) {
        if (!p.hasPermission("ezprotector.bypass.mod.schematica")) {
            if (Main.getPlugin().getServer().getVersion().contains("1.7")) {
                try {
                    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

                    String json = "{\"text\":\"\",\"extra\":[{\"text\":\"§0§2§0§0§e§f\"},{\"text\":\"§0§2§1§0§e§f\"},{\"text\":\"§0§2§1§1§e§f\"}]}";
                    PacketContainer motd = new PacketContainer(PacketType.Play.Server.CHAT);
                    motd.getChatComponents().write(0, WrappedChatComponent.fromJson(json));
                    protocolManager.sendServerPacket(p, motd);
                } catch (InvocationTargetException ignored) {
                }
            }
        }
    }

    public static byte[] getPayload(Player player) {
        Logger log = Main.getPlugin().getLogger();
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeByte(0);
            dataOutputStream.writeBoolean(player.hasPermission("schematica.printer"));
            dataOutputStream.writeBoolean(player.hasPermission("schematica.save"));
            dataOutputStream.writeBoolean(player.hasPermission("schematica.load"));

            return byteArrayOutputStream.toByteArray();
        } catch (final IOException ioe) {
            log.throwing(Main.class.getName(), "getPayload", ioe);
            return null;
        }
    }

    /*
    public static void sendCheatyPluginMessage(final Player player, final String channel, final byte[] payload) {
        Plugin plugin = Main.getPlugin();
        log = Main.getPlugin().getLogger();
        try {
            final Class<? extends Player> playerClass = player.getClass();
            if (playerClass.getSimpleName().equals("CraftPlayer")) {
                final Method addChannel = playerClass.getDeclaredMethod("addChannel", String.class);
                final Method removeChannel = playerClass.getDeclaredMethod("removeChannel", String.class);

                addChannel.invoke(player, channel);
                player.sendPluginMessage(plugin, channel, payload);
                removeChannel.invoke(player, channel);
            }
        } catch (final Exception e) {
            log.throwing(Main.class.getName(), "sendCheatyPluginMessage", e);
        }
    }
    */
}
