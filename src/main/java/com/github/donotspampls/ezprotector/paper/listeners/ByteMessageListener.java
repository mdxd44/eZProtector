/*
 * eZProtector - Copyright (C) 2018-2020 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.paper.listeners;

import com.github.donotspampls.ezprotector.paper.Main;
import com.github.donotspampls.ezprotector.paper.utilities.ExecutionUtil;
import com.github.donotspampls.ezprotector.paper.utilities.MessageUtil;
import com.github.donotspampls.ezprotector.paper.utilities.PacketUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class ByteMessageListener implements PluginMessageListener {

    private final Plugin plugin;
    private final ExecutionUtil execUtil;
    private final MessageUtil msgUtil;

    public ByteMessageListener(Plugin plugin, ExecutionUtil execUtil, MessageUtil msgUtil) {
        this.plugin = plugin;
        this.execUtil = execUtil;
        this.msgUtil = msgUtil;
    }

    @Override
    public void onPluginMessageReceived(String ch, @NotNull Player player, byte[] value) {
        String channel = ch.toLowerCase();
        FileConfiguration config = plugin.getConfig();

        if (config.getBoolean("mods.5zig.block")) block5Zig(player, channel);
        if (config.getBoolean("mods.bettersprinting.block")) blockBSM(player, channel);

        if (channel.equalsIgnoreCase(Main.MCBRAND)) {
            // Converts the byte array to a string called "brand"
            String brand = new String(value, StandardCharsets.UTF_8).toLowerCase();

            if (config.getBoolean("mods.fabric.block")) blockFabric(player, brand, config);
            if (config.getBoolean("mods.forge.block")) blockForge(player, brand, config);
            if (config.getBoolean("mods.liteloader.block")) blockLiteLoader(player, brand, config);
            if (config.getBoolean("mods.rift.block")) blockRift(player, brand, config);
        }

        if (config.getBoolean("mods.schematica.block")) blockSchematica(player, channel);
        if (config.getBoolean("mods.wdl.block")) blockWDL(player, channel);
    }

    private void block5Zig(Player player, String channel) {
        if (channel.equalsIgnoreCase(Main.ZIG) && !player.hasPermission("ezprotector.bypass.mod.5zig"))
            player.sendPluginMessage(plugin, channel, new byte[] {0x1|0x2|0x4|0x8|0x16|0x32});
    }

    private void blockBSM(Player player, String channel) {
        if (channel.equalsIgnoreCase(Main.BSM) && !player.hasPermission("ezprotector.bypass.mod.bettersprinting")) {
            player.sendPluginMessage(plugin, channel, new byte[]{1});
        }
    }

    private void blockFabric(Player player, String brand, FileConfiguration config) {
        if (brand.contains("fabric") && !player.hasPermission("ezprotector.bypass.mod.fabric")) {
            String punishCommand = config.getString("mods.fabric.punish-command");
            execUtil.executeConsoleCommand(msgUtil.placeholders(punishCommand, player, null, null));

            String notifyMessage = msgUtil.placeholders(config.getString("mods.fabric.warning-message"), player, null, null);
            execUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.fabric");
        }
    }

    private void blockForge(Player player, String brand, FileConfiguration config) {
        if ((brand.contains("fml") || brand.contains("forge")) && !player.hasPermission("ezprotector.bypass.mod.forge")) {
            String punishCommand = config.getString("mods.forge.punish-command");
            execUtil.executeConsoleCommand(msgUtil.placeholders(punishCommand, player, null, null));

            String notifyMessage = msgUtil.placeholders(config.getString("mods.forge.warning-message"), player, null, null);
            execUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.forge");
        }
    }

    private void blockLiteLoader(Player player, String brand, FileConfiguration config) {
        if ((brand.equalsIgnoreCase("LiteLoader") || brand.contains("lite")) && !player.hasPermission("ezprotector.bypass.mod.liteloader")) {
            String punishCommand = config.getString("mods.liteloader.punish-command");
            execUtil.executeConsoleCommand(msgUtil.placeholders(punishCommand, player, null, null));

            String notifyMessage = msgUtil.placeholders(config.getString("mods.liteloader.warning-message"), player, null, null);
            execUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.liteloader");
        }
    }

    private void blockRift(Player player, String brand, FileConfiguration config) {
        if (brand.contains("rift") && !player.hasPermission("ezprotector.bypass.mod.rift")) {
            String punishCommand = config.getString("mods.rift.punish-command");
            execUtil.executeConsoleCommand(msgUtil.placeholders(punishCommand, player, null, null));

            String notifyMessage = msgUtil.placeholders(config.getString("mods.rift.warning-message"), player, null, null);
            execUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.rift");
        }
    }

    private void blockSchematica(Player player, String channel) {
        if (channel.equalsIgnoreCase(Main.SCHEMATICA) && !player.hasPermission("ezprotector.bypass.mod.schematica"))
            player.sendPluginMessage(plugin, channel, PacketUtil.getSchematicaPayload());
    }

    private void blockWDL(Player player, String channel) {
        if (channel.equalsIgnoreCase(Main.WDLINIT) && !player.hasPermission("ezprotector.bypass.mod.wdl")) {
            byte[][] packets = new byte[2][];
            packets[0] = PacketUtil.createWDLPacket0();
            packets[1] = PacketUtil.createWDLPacket1();

            for (byte[] packet : packets) player.sendPluginMessage(plugin, Main.WDLCONTROL, packet);
        }
    }

}
