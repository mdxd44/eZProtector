/*
 * eZProtector - Copyright (C) 2018-2020 DoNotSpamPls
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
import org.spongepowered.api.event.network.ChannelRegistrationEvent;
import org.spongepowered.api.text.Text;

public class ModListener {

    @Listener
    public void onChannelRegister(ChannelRegistrationEvent.Register event) {
        Toml config = Main.getConfig();
        Player player = (Player) event.getSource();
        String channel = event.getChannel();

        if (config.getBoolean("mods.5zig")) block5Zig(player, channel);
        if (config.getBoolean("mods.bettersprinting") && event.getChannel().equals("BSM")) blockBSM(player, channel);

        if (config.getBoolean("mods.fabric.block")) blockFabric(player, channel, config);
        if (config.getBoolean("mods.forge.block")) blockForge(player, channel, config);
        if (config.getBoolean("mods.liteloader.block")) blockLiteLoader(player, channel, config);
        if (config.getBoolean("mods.rift.block")) blockRift(player, channel, config);

        if (config.getBoolean("mods.schematica") && !player.hasPermission("ezprotector.bypass.mod.schematica")
                && channel.equalsIgnoreCase("schematica"))
            Main.SCHEMATICA.sendTo(player, buf -> buf.writeBytes(PacketUtil.createSchematicaPacket()));

        if (config.getBoolean("mods.wdl")) blockWDL(player, channel);
    }

    private void block5Zig(Player player, String channel) {
        if (channel.equalsIgnoreCase("5Zig_Set") && !player.hasPermission("ezprotector.bypass.mod.5zig"))
            Main.ZIG.sendTo(player, buf -> buf.writeBytes(new byte[] {0x1|0x2|0x4|0x8|0x16|0x32}));
    }

    private void blockBSM(Player player, String channel) {
        if (channel.equalsIgnoreCase("BSM") && !player.hasPermission("ezprotector.bypass.mod.bettersprinting"))
            Main.BSM.sendTo(player, buf -> buf.writeBytes(new byte[] {1}));
    }

    private void blockFabric(Player player, String brand, Toml config) {
        if (brand.contains("fabric") && !player.hasPermission("ezprotector.bypass.mod.fabric")) {
            String punishCommand = config.getString("mods.fabric.punish-command");
            ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, null, null));

            Text notifyMessage = MessageUtil.placeholdersText(config.getString("mods.fabric.warning-message"), player, null, null);
            ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.fabric");
        }
    }

    private void blockForge(Player player, String brand, Toml config) {
        if ((brand.contains("fml") || brand.contains("forge")) && !player.hasPermission("ezprotector.bypass.mod.forge")) {
            String punishCommand = config.getString("mods.forge.punish-command");
            ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, null, null));

            Text notifyMessage = MessageUtil.placeholdersText(config.getString("mods.forge.warning-message"), player, null, null);
            ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.forge");
        }
    }

    private void blockLiteLoader(Player player, String brand, Toml config) {
        if ((brand.equalsIgnoreCase("LiteLoader") || brand.contains("Lite")) && !player.hasPermission("ezprotector.bypass.mod.liteloader")) {
            String punishCommand = config.getString("mods.liteloader.punish-command");
            ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, null, null));

            Text notifyMessage = MessageUtil.placeholdersText(config.getString("mods.liteloader.warning-message"), player, null, null);
            ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.liteloader");
        }
    }

    private void blockRift(Player player, String brand, Toml config) {
        if (brand.contains("rift") && !player.hasPermission("ezprotector.bypass.mod.rift")) {
            String punishCommand = config.getString("mods.rift.punish-command");
            ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, null, null));

            Text notifyMessage = MessageUtil.placeholdersText(config.getString("mods.rift.warning-message"), player, null, null);
            ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.rift");
        }
    }

    private void blockWDL(Player player, String channel) {
        if (channel.contains("WDL") && !player.hasPermission("ezprotector.bypass.mod.wdl")) {
            byte[][] packets = new byte[2][];
            packets[0] = PacketUtil.createWDLPacket0();
            packets[1] = PacketUtil.createWDLPacket1();

            for (byte[] packet : packets)
                Main.WDLCONTROL.sendTo(player, buf -> buf.writeBytes(packet));
        }
    }

}
