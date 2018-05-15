/*
 * Copyright (c) 2016-2018 dvargas135, DoNotSpamPls and contributors. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package com.github.donotspampls.ezprotector.listeners;

import com.github.donotspampls.ezprotector.Main;
import com.github.donotspampls.ezprotector.utilities.ExecutionUtil;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.UnsupportedEncodingException;

public class IPluginMessageListener implements PluginMessageListener {

    private Main plugin;
    public IPluginMessageListener(Main plugin) {
        this.plugin = plugin;
    }

    public void onPluginMessageReceived(String channel, Player player, byte[] value) {

        Main.player = player.getName();
        FileConfiguration config = plugin.getConfig();
        ConsoleCommandSender console = Bukkit.getConsoleSender();

        if (config.getBoolean("mods.5zig.block")) {
            block5Zig(player, channel);
        }

        if (config.getBoolean("mods.bettersprinting.block")) {
            blockBSM(player, channel);
        }

        if (channel.equalsIgnoreCase(Main.MCBRAND)) {
            String brand;
            try {
                brand = new String(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new Error(e);
            }

            if (config.getBoolean("mods.forge.block")) {
                blockForge(player, brand, config, console);
            }

            if (config.getBoolean("mods.liteloader.block")) {
                blockLiteLoader(player, brand, config, console);
            }
        }
    }

    private void block5Zig(Player player, String channel) {
        if (!player.hasPermission("ezprotector.bypass.mod.5zig")) {
            if ((channel.equalsIgnoreCase(Main.ZIG)) || (channel.contains("5zig"))) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeByte(0x01 | 0x02 | 0x04 | 0x08 | 0x010);
                player.sendPluginMessage(Main.getPlugin(), Main.ZIG, out.toByteArray());
            }
        }
    }

    private void blockBSM(Player player, String channel) {
        if (!player.hasPermission("ezprotector.bypass.mod.bettersprinting")) {
            if (channel.equalsIgnoreCase(Main.BSM)) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeByte(1);
                player.sendPluginMessage(Main.getPlugin(), Main.BSM, out.toByteArray());
            }
        }
    }
    private void blockForge(Player player, String brand, FileConfiguration config, ConsoleCommandSender console) {
        String punishCommand;
        String notifyMessage;
        if (!player.hasPermission("ezprotector.bypass.mod.forge")) {
            if ((brand.equalsIgnoreCase("fml,forge")) || (brand.contains("fml")) || (brand.contains("forge"))) {
                punishCommand = config.getString("mods.forge.punish-command");
                Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
                notifyMessage = config.getString("mods.forge.warning-message");
                ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.forge");
            }
        }
    }

    private void blockLiteLoader(Player player, String brand, FileConfiguration config, ConsoleCommandSender console) {
        String punishCommand;
        String notifyMessage;
        if (!player.hasPermission("ezprotector.bypass.mod.liteloader")) {
            if ((brand.contains("Lite")) || (brand.equalsIgnoreCase("LiteLoader"))) {
                punishCommand = config.getString("mods.liteloader.punish-command");
                Bukkit.dispatchCommand(console, Main.placeholders(punishCommand));
                notifyMessage = config.getString("mods.liteloader.warning-message");
                ExecutionUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.liteloader");
            }
        }
    }
}
