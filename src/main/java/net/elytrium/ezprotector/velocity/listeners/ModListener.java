/*
 * Copyright (C) 2021 Elytrium, DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.elytrium.ezprotector.velocity.listeners;

import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import net.elytrium.ezprotector.velocity.utilities.ExecutionUtil;
import net.elytrium.ezprotector.velocity.utilities.MessageUtil;
import net.elytrium.ezprotector.velocity.utilities.PacketUtil;
import net.kyori.adventure.text.TextComponent;

public class ModListener {

  private final Toml config;
  private final ExecutionUtil execUtil;
  private final MessageUtil msgUtil;

  public ModListener(Toml config, ExecutionUtil execUtil, MessageUtil msgUtil) {
    this.config = config;
    this.execUtil = execUtil;
    this.msgUtil = msgUtil;
  }

  @Subscribe
  @SuppressWarnings("unused")
  public void onChannelRegister(PluginMessageEvent event) {
    if (event.getSource() instanceof Player) {
      Player player = (Player) event.getSource();
      String channel = event.getIdentifier().getId().toLowerCase();
      int version = player.getProtocolVersion().getProtocol();

      if (this.config.getBoolean("mods.5zig")) {
        this.block5Zig(player, channel, version);
      }
      if (this.config.getBoolean("mods.bettersprinting")) {
        this.blockBSM(player, channel, version);
      }

      if (this.config.getBoolean("mods.fabric.block")) {
        this.blockFabric(player, channel, this.config);
      }
      if (this.config.getBoolean("mods.forge.block")) {
        this.blockForge(player, channel, this.config);
      }
      if (this.config.getBoolean("mods.liteloader.block")) {
        this.blockLiteLoader(player, channel, this.config);
      }
      if (this.config.getBoolean("mods.rift.block")) {
        this.blockRift(player, channel, this.config);
      }

      if (this.config.getBoolean("mods.schematica") && !player.hasPermission("ezprotector.bypass.mod.schematica")
          && channel.equalsIgnoreCase("schematica")) {
        if (version <= 340) {
          player.sendPluginMessage(
              new LegacyChannelIdentifier("schematica"), PacketUtil.createSchematicaPacket()
          );
        }
      }

      if (this.config.getBoolean("mods.wdl")) {
        this.blockWDL(player, channel, version);
      }
    }
  }

  private void block5Zig(Player player, String channel, int version) {
    if (!player.hasPermission("ezprotector.bypass.mod.5zig")) {
      if (channel.matches("(?i)5zig_Set|the5zigmod:5zig_set")) {
        if (version <= 340) {
          player.sendPluginMessage(new LegacyChannelIdentifier("5zig_Set"),
              new byte[] {0x1 | 0x2 | 0x4 | 0x8 | 0x16 | 0x32});
        } else {
          player.sendPluginMessage(MinecraftChannelIdentifier.create("the5zigmod", "5zig_set"),
              new byte[] {0x1 | 0x2 | 0x4 | 0x8 | 0x16 | 0x32});
        }
      }
    }
  }

  private void blockBSM(Player player, String channel, int version) {
    if (!player.hasPermission("ezprotector.bypass.mod.bettersprinting")) {
      if (channel.matches("(?i)BSM|bsm:settings")) {
        if (version <= 340) {
          player.sendPluginMessage(new LegacyChannelIdentifier("BSM"), new byte[] {1});
        } else {
          player.sendPluginMessage(MinecraftChannelIdentifier.create("bsm", "settings"),
              new byte[] {1});
        }
      }
    }
  }

  private void blockFabric(Player player, String brand, Toml config) {
    if (brand.contains("fabric") && !player.hasPermission("ezprotector.bypass.mod.fabric")) {
      String punishCommand = config.getString("mods.fabric.punish-command");
      this.execUtil.executeConsoleCommand(
          this.msgUtil.placeholders(punishCommand, player, null, null));

      TextComponent notifyMessage = this.msgUtil.placeholdersText(
          config.getString("mods.fabric.warning-message"), player, null, null);
      this.execUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.fabric");
    }
  }

  private void blockForge(Player player, String brand, Toml config) {
    if ((brand.matches("(?i)fml|forge")) && !player.hasPermission("ezprotector.bypass.mod.forge")) {
      String punishCommand = config.getString("mods.forge.punish-command");
      this.execUtil.executeConsoleCommand(
          this.msgUtil.placeholders(punishCommand, player, null, null));

      TextComponent notifyMessage = this.msgUtil.placeholdersText(
          config.getString("mods.forge.warning-message"), player, null, null);
      this.execUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.forge");
    }
  }

  private void blockLiteLoader(Player player, String brand, Toml config) {
    if ((brand.matches("(?i)LiteLoader|Lite")) && !player.hasPermission("ezprotector.bypass.mod.liteloader")) {
      String punishCommand = config.getString("mods.liteloader.punish-command");
      this.execUtil.executeConsoleCommand(
          this.msgUtil.placeholders(punishCommand, player, null, null));

      TextComponent notifyMessage = this.msgUtil.placeholdersText(
          config.getString("mods.liteloader.warning-message"), player, null, null);
      this.execUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.liteloader");
    }
  }

  private void blockRift(Player player, String brand, Toml config) {
    if (brand.contains("rift") && !player.hasPermission("ezprotector.bypass.mod.rift")) {
      String punishCommand = config.getString("mods.rift.punish-command");
      this.execUtil.executeConsoleCommand(
          this.msgUtil.placeholders(punishCommand, player, null, null));

      TextComponent notifyMessage = this.msgUtil.placeholdersText(
          config.getString("mods.rift.warning-message"), player, null, null);
      this.execUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.rift");
    }
  }

  private void blockWDL(Player player, String channel, int version) {
    if (!player.hasPermission("ezprotector.bypass.mod.wdl")) {
      if (channel.equalsIgnoreCase("WDL|INIT") || channel.equalsIgnoreCase("wdl:init")) {
        byte[][] packets = new byte[2][];
        packets[0] = PacketUtil.createWDLPacket0();
        packets[1] = PacketUtil.createWDLPacket1();

        for (byte[] packet : packets) {
          if (version <= 340) {
            player.sendPluginMessage(
                new LegacyChannelIdentifier("WDL|CONTROL"), packet);
          } else {
            player.sendPluginMessage(
                MinecraftChannelIdentifier.create("wdl", "control"), packet);
          }
        }
      }
    }
  }

}
