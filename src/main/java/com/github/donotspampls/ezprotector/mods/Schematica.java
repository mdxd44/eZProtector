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

    /**
     * Blocks the Schematica mod for a certain player (only used on 1.7.x)
     *
     * @param player The player to execute the block on.
     */
    public static void set(Player player) {
        if (!player.hasPermission("ezprotector.bypass.mod.schematica") && Main.getPlugin().getServer().getVersion().contains("1.7")) {
            try {
                // Get the ProtocolLib manager
                ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

                // JSON string that will be sent to the player
                String json = "{\"text\":\"\",\"extra\":[{\"text\":\"§0§2§0§0§e§f\"},{\"text\":\"§0§2§1§0§e§f\"},{\"text\":\"§0§2§1§1§e§f\"}]}";
                // Create a new chat packet container
                PacketContainer motd = new PacketContainer(PacketType.Play.Server.CHAT);
                // Write the JSON string from above to the packet container
                motd.getChatComponents().write(0, WrappedChatComponent.fromJson(json));
                // Send the packet to the user in question
                protocolManager.sendServerPacket(player, motd);
            } catch (InvocationTargetException ignored) {}
        }
    }

    /**
     * Creates a byte array with options that tell Schematica which features to block (used for 1.8+)
     *
     * @return The byte array containing the Schematica block configuration.
     */
    public static byte[] getPayload() {
        // Create the byte array and data stream
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            // Add bytes to data stream
            dataOutputStream.writeByte(0);
            dataOutputStream.writeBoolean(false);
            dataOutputStream.writeBoolean(false);
            dataOutputStream.writeBoolean(false);

            // Convert the data stream to a byte array and return it
            return byteArrayOutputStream.toByteArray();
        } catch (IOException ignored) {
            // If failed, return null
            return null;
        }
    }
}
