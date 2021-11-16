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

package net.elytrium.ezprotector.waterfall.listeners;

import java.nio.charset.StandardCharsets;
import net.elytrium.ezprotector.shared.Settings;
import net.elytrium.ezprotector.shared.utils.PacketUtil;
import net.elytrium.ezprotector.waterfall.utilities.ExecutionUtil;
import net.elytrium.ezprotector.waterfall.utilities.MessageUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ByteMessageListener implements Listener {

  private final ExecutionUtil execUtil;
  private final MessageUtil msgUtil;

  public ByteMessageListener(ExecutionUtil execUtil, MessageUtil msgUtil) {
    this.execUtil = execUtil;
    this.msgUtil = msgUtil;
  }

  @EventHandler
  public void onPluginMessage(PluginMessageEvent event) {
    if (!(event.getSender() instanceof ProxiedPlayer)) {
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) event.getSender();

    if (Settings.IMP.MODS.FIVE_ZIG.BLOCK) {
      this.block5Zig(player, event);
    }
    if (Settings.IMP.MODS.BETTERSPRINTING.BLOCK) {
      this.blockBSM(player, event);
    }

    if (event.getTag().equalsIgnoreCase("MC|Brand") || event.getTag().equalsIgnoreCase("minecraft:brand")) {
      // Converts the byte array to a string called "brand"
      String brand = new String(event.getData(), StandardCharsets.UTF_8);

      if (Settings.IMP.MODS.FABRIC.BLOCK) {
        this.blockFabric(player, brand);
      }
      if (Settings.IMP.MODS.FORGE.BLOCK) {
        this.blockForge(player, brand);
      }
      if (Settings.IMP.MODS.LITELOADER.BLOCK) {
        this.blockLiteLoader(player, brand);
      }
      if (Settings.IMP.MODS.RIFT.BLOCK) {
        this.blockRift(player, brand);
      }
    }

    if (Settings.IMP.MODS.SCHEMATICA.BLOCK) {
      this.blockSchematica(player, event);
    }
    if (Settings.IMP.MODS.WDL.BLOCK) {
      this.blockWDL(player, event);
    }
  }

  private void block5Zig(ProxiedPlayer player, PluginMessageEvent e) {
    if (!player.hasPermission("ezprotector.bypass.mod.5zig")) {
      if (e.getTag().equalsIgnoreCase("5zig_Set") || e.getTag().equalsIgnoreCase("the5zigmod:5zig_set")) {
        if (player.getPendingConnection().getVersion() <= 340) {
          player.sendData("5zig_Set", new byte[] {0x1 | 0x2 | 0x4 | 0x8 | 0x16 | 0x32});
        } else {
          player.sendData("the5zigmod:5zig_set", new byte[] {0x1 | 0x2 | 0x4 | 0x8 | 0x16 | 0x32});
        }
      }
    }
  }

  private void blockBSM(ProxiedPlayer player, PluginMessageEvent e) {
    if (!player.hasPermission("ezprotector.bypass.mod.bettersprinting")) {
      if (e.getTag().equalsIgnoreCase("BSM") || e.getTag().equalsIgnoreCase("bsm:settings")) {
        if (player.getPendingConnection().getVersion() <= 340) {
          player.sendData("BSM", new byte[] {1});
        } else {
          player.sendData("bsm:settings", new byte[] {1});
        }
      }
    }
  }

  private void blockFabric(ProxiedPlayer player, String brand) {
    if (brand.contains("fabric") && !player.hasPermission("ezprotector.bypass.mod.fabric")) {
      String punishCommand = Settings.IMP.MODS.FABRIC.PUNISH_COMMAND;
      this.execUtil.executeConsoleCommand(this.msgUtil.placeholders(punishCommand, player, null, null));

      String notifyMessage = this.msgUtil.placeholders(Settings.IMP.MODS.FABRIC.WARNING_MESSAGE, player, null, null);
      this.execUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.fabric");
    }
  }

  private void blockForge(ProxiedPlayer player, String brand) {
    if ((brand.contains("fml") || brand.contains("forge")) && !player.hasPermission("ezprotector.bypass.mod.forge")) {
      String punishCommand = Settings.IMP.MODS.FORGE.PUNISH_COMMAND;
      this.execUtil.executeConsoleCommand(this.msgUtil.placeholders(punishCommand, player, null, null));

      String notifyMessage = this.msgUtil.placeholders(Settings.IMP.MODS.FORGE.WARNING_MESSAGE, player, null, null);
      this.execUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.forge");
    }
  }

  private void blockLiteLoader(ProxiedPlayer player, String brand) {
    if ((brand.equalsIgnoreCase("LiteLoader") || brand.contains("Lite")) && !player.hasPermission("ezprotector.bypass.mod.liteloader")) {
      String punishCommand = Settings.IMP.MODS.LITELOADER.PUNISH_COMMAND;
      this.execUtil.executeConsoleCommand(this.msgUtil.placeholders(punishCommand, player, null, null));

      String notifyMessage = this.msgUtil.placeholders(Settings.IMP.MODS.LITELOADER.WARNING_MESSAGE, player, null, null);
      this.execUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.liteloader");
    }
  }

  private void blockRift(ProxiedPlayer player, String brand) {
    if (brand.contains("rift") && !player.hasPermission("ezprotector.bypass.mod.rift")) {
      String punishCommand = Settings.IMP.MODS.RIFT.PUNISH_COMMAND;
      this.execUtil.executeConsoleCommand(this.msgUtil.placeholders(punishCommand, player, null, null));

      String notifyMessage = this.msgUtil.placeholders(Settings.IMP.MODS.RIFT.WARNING_MESSAGE, player, null, null);
      this.execUtil.notifyAdmins(notifyMessage, "ezprotector.notify.mod.rift");
    }
  }

  private void blockSchematica(ProxiedPlayer player, PluginMessageEvent e) {
    if (!player.hasPermission("ezprotector.bypass.mod.schematica")) {
      if (player.getPendingConnection().getVersion() <= 340 && e.getTag().equalsIgnoreCase("schematica")) {
        player.sendData("schematica", PacketUtil.createSchematicaPacket());
      }
    }
  }

  private void blockWDL(ProxiedPlayer player, PluginMessageEvent e) {
    if (!player.hasPermission("ezprotector.bypass.mod.wdl")) {
      if (e.getTag().equalsIgnoreCase("WDL|INIT") || e.getTag().equalsIgnoreCase("wdl:init")) {
        byte[][] packets = new byte[2][];
        packets[0] = PacketUtil.createWDLPacket0();
        packets[1] = PacketUtil.createWDLPacket1();

        for (byte[] packet : packets) {
          if (player.getPendingConnection().getVersion() <= 340) {
            player.sendData("WDL|CONTROL", packet);
          } else {
            player.sendData("wdl:control", packet);
          }
        }
      }
    }
  }
}
