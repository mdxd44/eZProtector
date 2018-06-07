/*
 * eZProtector - Copyright (C) 2018 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
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

    /**
     * Blocks the Smart Moving mod for a certain player.
     *
     * @param player The player to execute the block on.
     */
    public static void set(Player player) {
        if (!player.hasPermission("ezprotector.bypass.mod.smartmoving")) {
            /*
            try {
                // Get the ProtocolLib manager
                ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

                // JSON string that will be sent to the player
                String json = "{\"text\":\"\",\"extra\":[{\"text\":\"§0§1§0§1§2§f§f\"},{\"text\":\"§0§1§3§4§f§f\"},{\"text\":\"§0§1§5§f§f\"},{\"text\":\"§0§1§6§f§f\"},{\"text\":\"§0§1§7§f§f\"},{\"text\":\"§0§1§8§9§a§b§f§f\"}]}";
                // Create a new chat packet container
                PacketContainer motd = new PacketContainer(PacketType.Play.Server.CHAT);
                // Write the JSON string from above to the packet container
                motd.getChatComponents().write(0, WrappedChatComponent.fromJson(json));
                // Send the packet to the user in question
                protocolManager.sendServerPacket(player, motd);
            } catch (InvocationTargetException ignored) {}
            */
            System.out.println("[eZProtector] The SmartMoving block is currently not available."); // The feature is not working at the moment.
        }
    }

}
