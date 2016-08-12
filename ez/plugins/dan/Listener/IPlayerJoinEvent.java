package ez.plugins.dan.Listener;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import ez.plugins.dan.Main;
import ez.plugins.dan.Extras.Setup;
import ez.plugins.dan.ModBlockage.LabyMod.EnumLabyModFeature;
import ez.plugins.dan.SpigotUpdater.SpigotUpdater;
import ez.plugins.dan.SpigotUpdater.UpdateChecker;

public class IPlayerJoinEvent implements Listener {
	private FileConfiguration config = Main.getPlugin().getConfig();
	
	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
    	Player p = e.getPlayer();
    	final Player p2 = e.getPlayer();
    	
    	if (config.getBoolean("mods.betterpvp.block")) {
    		if (!p.hasPermission("ezprotector.bypass.mod.betterpvp")) {
    			Setup.IeZP.sendBetterPvP(p);
    		}
    	}
    	if (config.getBoolean("mods.schematica.block")) {
    		if (!p.hasPermission("ezprotector.bypass.mod.schematica")) {
    			Setup.IeZP.sendSchematica(p);
    		}
    	}
    	if (config.getBoolean("mods.reiminimap.block")) {
    		if (!p.hasPermission("ezprotector.bypass.mod.reiminimap")) {
    			Setup.IeZP.sendReiMiniMap(p);
    		}
    	}
    	if (config.getBoolean("mods.damageindicators.block")) {
    		if (!p.hasPermission("ezprotector.bypass.mod.damageindicators")) {
    			Setup.IeZP.sendDamageIndicators(p);
    		}
    	}
    	if (config.getBoolean("mods.voxelmap.block")) {
    		if (!p.hasPermission("ezprotector.bypass.mod.voxelmap")) {
    			Setup.IeZP.sendVoxelMap(p);
    		}
    	}
    	if (config.getBoolean("mods.labymod.block")) {
    		if (!p.hasPermission("ezprotector.bypass.mod.labymod")) {
    			HashMap<EnumLabyModFeature, Boolean> list = new HashMap<EnumLabyModFeature, Boolean>();
    			list.put(EnumLabyModFeature.ANIMATIONS, Boolean.valueOf(false));
    			list.put(EnumLabyModFeature.ARMOR , Boolean.valueOf(false));
    			list.put(EnumLabyModFeature.BLOCKBUILD , Boolean.valueOf(false));
    			list.put(EnumLabyModFeature.CHAT , Boolean.valueOf(false));
    			list.put(EnumLabyModFeature.DAMAGEINDICATOR , Boolean.valueOf(false));
    			list.put(EnumLabyModFeature.EXTRAS , Boolean.valueOf(false));
    			list.put(EnumLabyModFeature.FOOD , Boolean.valueOf(false));
    			list.put(EnumLabyModFeature.GUI , Boolean.valueOf(false));
    			list.put(EnumLabyModFeature.MINIMAP_RADAR , Boolean.valueOf(false));
    			list.put(EnumLabyModFeature.NICK , Boolean.valueOf(false));
    			list.put(EnumLabyModFeature.POTIONS , Boolean.valueOf(false));
    			Setup.IeZP.setLabyModFeature(p, list);
    		}
    	}
    	if (config.getBoolean("mods.smartmoving.block")) {
    		if (!p.hasPermission("ezprotector.bypass.mod.smartmoving")) {
    			Setup.IeZP.sendSmartMove(p);
    		}
    	}
		// UPDATER SEPARATOR - ON LATE JOIN
		if (config.getBoolean("updater")) {
			if(p.hasPermission("ezprotector.notify.update") && SpigotUpdater.updateAvailable) {
				Bukkit.getScheduler().runTaskLater((Plugin) this, new Runnable() {
					public void run() {
						p2.sendMessage(ChatColor.GREEN + "A new update for eZProtector is available. Version " + UpdateChecker.version);
						p2.sendMessage(ChatColor.GREEN + "- Current version: " + Main.getPlugin().getDescription().getVersion());
						p2.sendMessage(ChatColor.GREEN + "- Download it at: http://bit.ly/eZProtector");
					}
				}, 20L);
			}
		}
    }
}
