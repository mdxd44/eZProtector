/*
 * eZProtector - Copyright (C) 2018-2019 DoNotSpamPls
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
import com.github.donotspampls.ezprotector.waterfall.utilities.WDLPackets;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.nio.charset.StandardCharsets;

public class ByteMessageListener implements Listener {

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) {
        if (!(e.getSender() instanceof ProxiedPlayer)) return;

        ProxiedPlayer player = (ProxiedPlayer) e.getSender();
        Configuration config = Main.getConfig();

        if (config.getBoolean("mods.5zig.block") && e.getTag().equalsIgnoreCase("5zig_Set")) block5Zig(player);
        if (config.getBoolean("mods.bettersprinting.block") && e.getTag().equalsIgnoreCase("BSM")) blockBSM(player);

        if (e.getTag().equalsIgnoreCase("MC|Brand") || e.getTag().equalsIgnoreCase("minecraft:brand")) {
            // Converts the byte array to a string called "brand"
            String brand = new String(e.getData(), StandardCharsets.UTF_8);

            if (config.getBoolean("mods.forge.block")) blockForge(player, brand, config);
            if (config.getBoolean("mods.liteloader.block")) blockLiteLoader(player, brand, config);
        }

        if (config.getBoolean("mods.schematica.block") && !player.hasPermission("ezprotector.bypass.mod.schematica"))
            player.sendData("schematica", getSchematicaPayload());

        if (config.getBoolean("mods.wdl.block")) blockWDL(player);
    }

    private void block5Zig(ProxiedPlayer player) {
        if (!player.hasPermission("ezprotector.bypass.mod.5zig")) {
            /*
             * 0x1 = Potion HUD
             * 0x2 = Potion Indicator
             * 0x4 = Armor HUD
             * 0x8 = Saturation
             * 0x16 = Unused
             * 0x32 = Auto Reconnect
             */
            player.sendData("5zig_Set", new byte[] {0x1|0x2|0x4|0x8|0x16|0x32});
        }
    }

    private void blockBSM(ProxiedPlayer player) {
        if (!player.hasPermission("ezprotector.bypass.mod.bettersprinting")) {
            if (player.getPendingConnection().getVersion() <= 340)
                player.sendData("BSM", new byte[] {1});
            player.sendData("bsm:settings", new byte[] {1});
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

    @SuppressWarnings("UnstableApiUsage")
    private static byte[] getSchematicaPayload() {
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeByte(0);
        output.writeBoolean(false);
        output.writeBoolean(false);
        output.writeBoolean(false);

        return output.toByteArray();
    }

    private void blockWDL(ProxiedPlayer player) {
        if (!player.hasPermission("ezprotector.bypass.mod.wdl")) {
            byte[][] packets = new byte[2][];
            packets[0] = WDLPackets.createWDLPacket0();
            packets[1] = WDLPackets.createWDLPacket1();

            for (byte[] packet : packets) {
                if (player.getPendingConnection().getVersion() <= 340)
                    player.sendData("WDL|CONTROL", packet);
                player.sendData("wdl:control", packet);
            }
        }
    }

}
