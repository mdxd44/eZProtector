/*
Copyright (c) 2016 dvargas135

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package ez.plugins.dan.Compatibility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import ez.plugins.dan.ModBlockage.LabyMod.EnumLabyModFeature;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.PacketDataSerializer;
import net.minecraft.server.v1_8_R2.PacketPlayOutChat;
import net.minecraft.server.v1_8_R2.PacketPlayOutCustomPayload;

public class R1_8_R2 implements Compatibility {
	
	public void sendBetterPvP(Player p) {
		try {
			String json = "{\"text\":\"\",\"extra\":[{\"text\":\"§c §r§5 §r§1 §r§f §r§0\"}]}";
			IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a(json);
			PacketPlayOutChat chat = new PacketPlayOutChat(icbc, (byte)0);
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(chat);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void sendSchematica(Player p) {
		try {
			String json = "{\"text\":\"\",\"extra\":[{\"text\":\"§0§2§0§0§e§f\"},{\"text\":\"§0§2§1§0§e§f\"},{\"text\":\"§0§2§1§1§e§f\"}]}";
			IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a(json);
			PacketPlayOutChat chat = new PacketPlayOutChat(icbc, (byte)0);
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(chat);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void sendReiMiniMap(Player p) {
		try {
			String json = "{\"text\":\"\",\"extra\":[{\"text\":\"§0§0§1§2§3§5§e§f\"},{\"text\":\"§0§0§2§3§4§5§6§7§e§f\"},{\"text\":\"§A§n§t§i§M§i§n§i§m§a§p\"}]}";
			IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a(json);
			PacketPlayOutChat chat = new PacketPlayOutChat(icbc, (byte)0);
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(chat);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void sendDamageIndicators(Player p) {
		try {
			String json = "{\"text\":\"\",\"extra\":[{\"text\":\"§0§0§c§d§e§f\"}]}";
			IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a(json);
			PacketPlayOutChat chat = new PacketPlayOutChat(icbc, (byte)0);
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(chat);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void sendVoxelMap(Player p) {
		try {
			String json = "{\"text\":\"\",\"extra\":[{\"text\":\"§3 §6 §3 §6 §3 §6 §d\"},{\"text\":\"§3 §6 §3 §6 §3 §6 §e\"}]}";
			IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a(json);
			PacketPlayOutChat chat = new PacketPlayOutChat(icbc, (byte)0);
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(chat);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    public void setLabyModFeature(Player p, HashMap<EnumLabyModFeature, Boolean> list) {
        try {
            HashMap<String, Boolean> nList = new HashMap<String, Boolean>();
            for (EnumLabyModFeature f : list.keySet()) {
                nList.put(f.name(), list.get(f));
            }
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(nList);
            ByteBuf a = Unpooled.copiedBuffer(byteOut.toByteArray());
            PacketDataSerializer b = new PacketDataSerializer(a);
            PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("LABYMOD", b);
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
	public void sendSmartMove(Player p) {
		try {
			String json = "{\"text\":\"\",\"extra\":[{\"text\":\"§0§1§0§1§2§f§f\"},{\"text\":\"§0§1§3§4§f§f\"},{\"text\":\"§0§1§5§f§f\"},{\"text\":\"§0§1§6§f§f\"},{\"text\":\"§0§1§8§9§a§b§f§f\"}]}";
			IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a(json);
			PacketPlayOutChat chat = new PacketPlayOutChat(icbc);
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(chat);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
