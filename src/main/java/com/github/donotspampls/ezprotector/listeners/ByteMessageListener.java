/*
 * eZProtector - Copyright (C) 2018 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.listeners;

import com.github.donotspampls.ezprotector.Main;
import com.github.donotspampls.ezprotector.utilities.ExecutionUtil;
import com.github.donotspampls.ezprotector.utilities.MessageUtil;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.nio.charset.StandardCharsets;

import static com.github.donotspampls.ezprotector.utilities.WDLPackets.*;

public class ByteMessageListener implements PluginMessageListener {

    /**
     * Listen for plugin messages by various mods
     *
     * @param channel The plugin channel used by the mod.
     * @param player The player whose client sent the plugin message.
     * @param value The bytes included in the plugin message.
     */
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] value) {
        FileConfiguration config = Main.getPlugin().getConfig();

        if (config.getBoolean("mods.5zig.block")) block5Zig(player, channel);
        if (config.getBoolean("mods.bettersprinting.block")) blockBSM(player, channel);

        if (config.getBoolean("mods.schematica.block") && !player.hasPermission("ezprotector.bypass.mod.schematica"))
            player.sendPluginMessage(Main.getPlugin(), Main.SCHEMATICA, getSchematicaPayload());

        if (channel.equalsIgnoreCase(Main.MCBRAND)) {
            // Converts the byte array to a string called "brand"
            String brand = new String(value, StandardCharsets.UTF_8);

            if (config.getBoolean("mods.forge.block")) blockForge(player, brand, config);
            if (config.getBoolean("mods.liteloader.block")) blockLiteLoader(player, brand, config);
        }

        if (config.getBoolean("mods.wdl.block")) blockWDL(player, channel);
    }

    /**
     * Blocks the 5-Zig mod for a certain player.
     *
     * @param player The player to execute the block on.
     * @param channel The channel where the byte array should be sent.
     */
    private void block5Zig(Player player, String channel) {
        if (channel.equalsIgnoreCase(Main.ZIG) && !player.hasPermission("ezprotector.bypass.mod.5zig")) {
            /*
             * 0x1 = Potion HUD
             * 0x2 = Potion Indicator
             * 0x4 = Armor HUD
             * 0x8 = Saturation
             * 0x16 = Unused
             * 0x32 = Auto Reconnect
             */
            player.sendPluginMessage(Main.getPlugin(), channel, new byte[] {0x1|0x2|0x4|0x8|0x16|0x32});
        }
    }

    /**
     * Blocks the BetterSprinting mod for a certain player.
     *
     * @param player The player to execute the block on.
     * @param channel The channel where the byte array should be sent.
     */
    private void blockBSM(Player player, String channel) {
        if (channel.equalsIgnoreCase(Main.BSM) && !player.hasPermission("ezprotector.bypass.mod.bettersprinting")) {
            // Send the data output as a byte array to the player
            player.sendPluginMessage(Main.getPlugin(), channel, new byte[] {1});
        }
    }

    /**
     * Kicks a certain player if Forge is found on the client
     *
     * @param player The player to execute the block on.
     * @param brand The brand name recieved in the "MC|Brand" plugin message.
     * @param config The plugin configuration on the particular server.
     */
    private void blockForge(Player player, String brand, FileConfiguration config) {
        if ((brand.equalsIgnoreCase("fml,forge")) || (brand.contains("fml")) || (brand.contains("forge")) && !player.hasPermission("ezprotector.bypass.mod.forge")) {
            String punishCommand = config.getString("mods.forge.punish-command");
            ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, null, null));

            String notifyMessage = MessageUtil.placeholders(config.getString("mods.forge.warning-message"), player, null, null);
            ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.forge");
        }
    }

    /**
     * Kicks a certain player if LiteLoader is found on the client
     *
     * @param player The player to execute the block on.
     * @param brand The brand name recieved in the "MC|Brand" plugin message.
     * @param config The plugin configuration on the particular server.
     */
    private void blockLiteLoader(Player player, String brand, FileConfiguration config) {
        if ((brand.equalsIgnoreCase("LiteLoader") || brand.contains("Lite")) && !player.hasPermission("ezprotector.bypass.mod.liteloader")) {
            String punishCommand = config.getString("mods.liteloader.punish-command");
            ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, null, null));

            String notifyMessage = MessageUtil.placeholders(config.getString("mods.liteloader.warning-message"), player, null, null);
            ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.liteloader");
        }
    }

    /**
     * Creates a byte array with options that tell Schematica which features to block
     *
     * @return The byte array containing the Schematica block configuration.
     */
    @SuppressWarnings("UnstableApiUsage")
    private static byte[] getSchematicaPayload() {
        // Create the byte array and data stream
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();

        // Add bytes to data stream
        output.writeByte(0);
        output.writeBoolean(false);
        output.writeBoolean(false);
        output.writeBoolean(false);

        // Convert the data stream to a byte array and return it
        return output.toByteArray();
    }

    /**
     * Kicks a certain player if WorldDownloader is found on the client
     *
     * @param player The player to execute the block on.
     * @param channel The channel which the message was sent on.
     */
    private void blockWDL(Player player, String channel) {
        if (channel.equalsIgnoreCase(Main.WDLINIT) && !player.hasPermission("ezprotector.bypass.mod.wdl")) {
            byte[][] packets = new byte[2][];
            packets[0] = createWDLPacket0();
            packets[1] = createWDLPacket1();

            for (byte[] packet : packets) player.sendPluginMessage(Main.getPlugin(), Main.WDLCONTROL, packet);
        }
    }

}
