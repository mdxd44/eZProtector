/*
 * eZProtector - Copyright (C) 2018-2020 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.waterfall.listeners;

import com.github.donotspampls.ezprotector.waterfall.Main;
import com.github.donotspampls.ezprotector.waterfall.utilities.ExecutionUtil;
import com.github.donotspampls.ezprotector.waterfall.utilities.MessageUtil;
import com.github.donotspampls.ezprotector.waterfall.utilities.PacketUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.nio.charset.StandardCharsets;

public class ByteMessageListener implements Listener {

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer)) return;

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        Configuration config = Main.getConfig();

        if (config.getBoolean("mods.5zig.block")) block5Zig(player, event);
        if (config.getBoolean("mods.bettersprinting.block")) blockBSM(player, event);

        if (event.getTag().equalsIgnoreCase("MC|Brand") || event.getTag().equalsIgnoreCase("minecraft:brand")) {
            // Converts the byte array to a string called "brand"
            String brand = new String(event.getData(), StandardCharsets.UTF_8);

            if (config.getBoolean("mods.fabric.block")) blockFabric(player, brand, config);
            if (config.getBoolean("mods.forge.block")) blockForge(player, brand, config);
            if (config.getBoolean("mods.liteloader.block")) blockLiteLoader(player, brand, config);
            if (config.getBoolean("mods.rift.block")) blockRift(player, brand, config);
        }

        if (config.getBoolean("mods.schematica.block") && !player.hasPermission("ezprotector.bypass.mod.schematica")) {
            if (player.getPendingConnection().getVersion() <= 340) player.sendData("schematica", PacketUtil.getSchematicaPayload());
        }

        if (config.getBoolean("mods.wdl.block")) blockWDL(player, event);
    }

    private void block5Zig(ProxiedPlayer player, PluginMessageEvent e) {
        if (!player.hasPermission("ezprotector.bypass.mod.5zig")) {
            if (e.getTag().equalsIgnoreCase("5zig_Set") || e.getTag().equalsIgnoreCase("the5zigmod:5zig_set")) {
                if (player.getPendingConnection().getVersion() <= 340)
                    player.sendData("5zig_Set", new byte[]{0x1 | 0x2 | 0x4 | 0x8 | 0x16 | 0x32});
                else
                    player.sendData("the5zigmod:5zig_set", new byte[]{0x1 | 0x2 | 0x4 | 0x8 | 0x16 | 0x32});
            }
        }
    }

    private void blockBSM(ProxiedPlayer player, PluginMessageEvent e) {
        if (!player.hasPermission("ezprotector.bypass.mod.bettersprinting")) {
            if (e.getTag().equalsIgnoreCase("BSM") || e.getTag().equalsIgnoreCase("bsm:settings")) {
                if (player.getPendingConnection().getVersion() <= 340)
                    player.sendData("BSM", new byte[]{1});
                else
                    player.sendData("bsm:settings", new byte[]{1});
            }
        }
    }

    private void blockFabric(ProxiedPlayer player, String brand, Configuration config) {
        if (brand.contains("fabric") && !player.hasPermission("ezprotector.bypass.mod.fabric")) {
            String punishCommand = config.getString("mods.fabric.punish-command");
            ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, null, null));

            String notifyMessage = MessageUtil.placeholders(config.getString("mods.fabric.warning-message"), player, null, null);
            ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.fabric");
        }
    }

    private void blockForge(ProxiedPlayer player, String brand, Configuration config) {
        if ((brand.equalsIgnoreCase("fml,forge") || brand.contains("fml") || brand.contains("forge")) && !player.hasPermission("ezprotector.bypass.mod.forge")) {
            String punishCommand = config.getString("mods.forge.punish-command");
            ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, null, null));

            String notifyMessage = MessageUtil.placeholders(config.getString("mods.forge.warning-message"), player, null, null);
            ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.forge");
        }
    }

    private void blockLiteLoader(ProxiedPlayer player, String brand, Configuration config) {
        if ((brand.equalsIgnoreCase("LiteLoader") || brand.contains("Lite")) && !player.hasPermission("ezprotector.bypass.mod.liteloader")) {
            String punishCommand = config.getString("mods.liteloader.punish-command");
            ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, null, null));

            String notifyMessage = MessageUtil.placeholders(config.getString("mods.liteloader.warning-message"), player, null, null);
            ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.liteloader");
        }
    }

    private void blockRift(ProxiedPlayer player, String brand, Configuration config) {
        if (brand.contains("rift") && !player.hasPermission("ezprotector.bypass.mod.rift")) {
            String punishCommand = config.getString("mods.rift.punish-command");
            ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, null, null));

            String notifyMessage = MessageUtil.placeholders(config.getString("mods.rift.warning-message"), player, null, null);
            ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.rift");
        }
    }

    private void blockWDL(ProxiedPlayer player, PluginMessageEvent event) {
        if (!player.hasPermission("ezprotector.bypass.mod.wdl")) {
            if (event.getTag().equalsIgnoreCase("WDL|INIT") && event.getTag().equalsIgnoreCase("wdl:init")) {
                byte[][] packets = new byte[2][];
                packets[0] = PacketUtil.createWDLPacket0();
                packets[1] = PacketUtil.createWDLPacket1();

                for (byte[] packet : packets) {
                    if (player.getPendingConnection().getVersion() <= 340)
                        player.sendData("WDL|CONTROL", packet);
                    else
                        player.sendData("wdl:control", packet);
                }
            }
        }
    }

}
