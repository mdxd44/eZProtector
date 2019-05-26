/*
 * eZProtector - Copyright (C) 2018-2019 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.sponge.listeners;

import com.github.donotspampls.ezprotector.sponge.Main;
import com.github.donotspampls.ezprotector.sponge.utilities.ExecutionUtil;
import com.github.donotspampls.ezprotector.sponge.utilities.MessageUtil;
import com.github.donotspampls.ezprotector.sponge.utilities.PacketUtil;
import com.moandjiezana.toml.Toml;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class ModListener {

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event) {
        Toml config = Main.getConfig();
        Player player = (Player) event.getSource();

        if (config.getBoolean("mods.5zig")) block5Zig(player);
        if (config.getBoolean("mods.bettersprinting")) blockBSM(player);

        /*
        if (channel.equalsIgnoreCase(Main.MCBRAND)) {
                // Converts the byte array to a string called "brand"
                String brand = new String(data, StandardCharsets.UTF_8);

                if (config.getBoolean("mods.forge.block")) blockForge(player, brand, config);
                if (config.getBoolean("mods.liteloader.block")) blockLiteLoader(player, brand, config);
            }
        */

        if (config.getBoolean("mods.schematica") && !player.hasPermission("ezprotector.bypass.mod.schematica"))
            Main.SCHEMATICA.sendTo(player, buf -> buf.writeByteArray(PacketUtil.createSchematicaPacket()));

        if (config.getBoolean("mods.wdl")) blockWDL(player);
    };

    private void block5Zig(Player player) {
        if (!player.hasPermission("ezprotector.bypass.mod.5zig")) {
            /*
             * 0x1 = Potion HUD
             * 0x2 = Potion Indicator
             * 0x4 = Armor HUD
             * 0x8 = Saturation
             * 0x16 = Unused
             * 0x32 = Auto Reconnect
             */
            Main.ZIG.sendTo(player, buf -> buf.writeByteArray(new byte[] {0x1|0x2|0x4|0x8|0x16|0x32}));
        }
    }

    private void blockBSM(Player player) {
        if (!player.hasPermission("ezprotector.bypass.mod.bettersprinting")) {
            // Send the data output as a byte array to the player
            Main.BSM.sendTo(player, buf -> buf.writeByteArray(new byte[] {1}));
        }
    }

    private void blockForge(Player player, String brand, Toml config) {
        if ((brand.equalsIgnoreCase("fml,forge")) || (brand.contains("fml")) || (brand.contains("forge")) && !player.hasPermission("ezprotector.bypass.mod.forge")) {
            String punishCommand = config.getString("mods.forge.punish-command");
            ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, null, null));

            String notifyMessage = MessageUtil.placeholders(config.getString("mods.forge.warning-message"), player, null, null);
            ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.forge");
        }
    }

    private void blockLiteLoader(Player player, String brand, Toml config) {
        if ((brand.equalsIgnoreCase("LiteLoader") || brand.contains("Lite")) && !player.hasPermission("ezprotector.bypass.mod.liteloader")) {
            String punishCommand = config.getString("mods.liteloader.punish-command");
            ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, null, null));

            String notifyMessage = MessageUtil.placeholders(config.getString("mods.liteloader.warning-message"), player, null, null);
            ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.liteloader");
        }
    }

    private void blockWDL(Player player) {
        if (!player.hasPermission("ezprotector.bypass.mod.wdl")) {
            byte[][] packets = new byte[2][];
            packets[0] = PacketUtil.createWDLPacket0();
            packets[1] = PacketUtil.createWDLPacket1();

            for (byte[] packet : packets) Main.WDLCONTROL.sendTo(player, buf-> buf.writeByteArray(packet));
        }
    }

}
