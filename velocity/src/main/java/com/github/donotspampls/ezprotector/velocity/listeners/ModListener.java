/*
 * eZProtector - Copyright (C) 2018-2020 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.velocity.listeners;

import com.github.donotspampls.ezprotector.velocity.Main;
import com.github.donotspampls.ezprotector.velocity.utilities.ExecutionUtil;
import com.github.donotspampls.ezprotector.velocity.utilities.MessageUtil;
import com.github.donotspampls.ezprotector.velocity.utilities.PacketUtil;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import net.kyori.text.TextComponent;

public class ModListener {

    @Subscribe
    public void onChannelRegister(PluginMessageEvent event) {
        if (event.getSource() instanceof Player) {
            Toml config = Main.getConfig();
            Player player = (Player) event.getSource();
            String channel = event.getIdentifier().getId();
            int version = player.getProtocolVersion().getProtocol();

            if (config.getBoolean("mods.5zig")) block5Zig(player, channel, version);
            if (config.getBoolean("mods.bettersprinting")) blockBSM(player, channel, version);

            if (config.getBoolean("mods.fabric.block")) blockFabric(player, channel, config);
            if (config.getBoolean("mods.forge.block")) blockForge(player, channel, config);
            if (config.getBoolean("mods.liteloader.block")) blockLiteLoader(player, channel, config);
            if (config.getBoolean("mods.rift.block")) blockRift(player, channel, config);

            if (config.getBoolean("mods.schematica") && !player.hasPermission("ezprotector.bypass.mod.schematica")
                    && channel.equalsIgnoreCase("schematica")) {
                if (version <= 340) player.sendPluginMessage(new LegacyChannelIdentifier("schematica"), PacketUtil.createSchematicaPacket());
            }

            if (config.getBoolean("mods.wdl")) blockWDL(player, channel, version);
        }
    }

    private void block5Zig(Player player, String channel, int version) {
        if (!player.hasPermission("ezprotector.bypass.mod.5zig")) {
            if (channel.equalsIgnoreCase("5zig_Set") || channel.equalsIgnoreCase("the5zigmod:5zig_set")) {
                if (version <= 340)
                    player.sendPluginMessage(new LegacyChannelIdentifier("5zig_Set"), new byte[]{0x1 | 0x2 | 0x4 | 0x8 | 0x16 | 0x32});
                else
                    player.sendPluginMessage(MinecraftChannelIdentifier.create("the5zigmod", "5zig_set"),
                            new byte[]{0x1 | 0x2 | 0x4 | 0x8 | 0x16 | 0x32});
            }
        }
    }

    private void blockBSM(Player player, String channel, int version) {
        if (!player.hasPermission("ezprotector.bypass.mod.bettersprinting")) {
            if (channel.equalsIgnoreCase("BSM") || channel.equalsIgnoreCase("bsm:settings")) {
                if (version <= 340)
                    player.sendPluginMessage(new LegacyChannelIdentifier("BSM"), new byte[]{1});
                else
                    player.sendPluginMessage(MinecraftChannelIdentifier.create("bsm", "settings"), new byte[]{1});
            }
        }
    }

    private void blockFabric(Player player, String brand, Toml config) {
        if (brand.contains("fabric") && !player.hasPermission("ezprotector.bypass.mod.fabric")) {
            String punishCommand = config.getString("mods.fabric.punish-command");
            ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, null, null));

            TextComponent notifyMessage = MessageUtil.placeholdersText(config.getString("mods.fabric.warning-message"), player, null, null);
            ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.fabric");
        }
    }

    private void blockForge(Player player, String brand, Toml config) {
        if ((brand.contains("fml") || brand.contains("forge")) && !player.hasPermission("ezprotector.bypass.mod.forge")) {
            String punishCommand = config.getString("mods.forge.punish-command");
            ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, null, null));

            TextComponent notifyMessage = MessageUtil.placeholdersText(config.getString("mods.forge.warning-message"), player, null, null);
            ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.forge");
        }
    }

    private void blockLiteLoader(Player player, String brand, Toml config) {
        if ((brand.equalsIgnoreCase("LiteLoader") || brand.contains("Lite")) && !player.hasPermission("ezprotector.bypass.mod.liteloader")) {
            String punishCommand = config.getString("mods.liteloader.punish-command");
            ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, null, null));

            TextComponent notifyMessage = MessageUtil.placeholdersText(config.getString("mods.liteloader.warning-message"), player, null, null);
            ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.liteloader");
        }
    }

    private void blockRift(Player player, String brand, Toml config) {
        if (brand.contains("rift") && !player.hasPermission("ezprotector.bypass.mod.rift")) {
            String punishCommand = config.getString("mods.rift.punish-command");
            ExecutionUtil.executeConsoleCommand(MessageUtil.placeholders(punishCommand, player, null, null));

            TextComponent notifyMessage = MessageUtil.placeholdersText(config.getString("mods.rift.warning-message"), player, null, null);
            ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.rift");
        }
    }

    private void blockWDL(Player player, String channel, int version) {
        if (!player.hasPermission("ezprotector.bypass.mod.wdl")) {
            if (channel.equalsIgnoreCase("WDL|INIT") || channel.equalsIgnoreCase("wdl:init")) {
                byte[][] packets = new byte[2][];
                packets[0] = PacketUtil.createWDLPacket0();
                packets[1] = PacketUtil.createWDLPacket1();

                for (byte[] packet : packets) {
                    if (version <= 340)
                        player.sendPluginMessage(new LegacyChannelIdentifier("WDL|CONTROL"), packet);
                    else
                        player.sendPluginMessage(MinecraftChannelIdentifier.create("wdl", "control"), packet);
                }
            }
        }
    }

}
