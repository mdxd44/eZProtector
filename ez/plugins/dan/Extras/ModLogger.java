package ez.plugins.dan.Extras;

import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;

import ez.plugins.dan.Main;

public class ModLogger {
	private static FileConfiguration config = Main.getPlugin().getConfig();
	private static Logger log;
	
	public static void logMods() {
		log = Main.getPlugin().getLogger();
		
		if (config.getBoolean("mods.liteloader.block")) {
			log.info("Blockage for the mod \"LiteLoader\" activated.");
		} if (config.getBoolean("mods.betterpvp.block")) {
			log.info("Blockage for the mod \"BetterPvP\" activated.");
		} if (config.getBoolean("mods.schematica.block")) {
			log.info("Blockage for the mod \"Schematica\" activated.");
		} if (config.getBoolean("mods.reiminimap.block")) {
			log.info("Blockage for the mod \"Rei's Minimap\" activated.");
		} if (config.getBoolean("mods.damageindicators.block")) {
			log.info("Blockage for the mod \"Damage Indicators\" activated.");
		} if (config.getBoolean("mods.voxelmap.block")) {
			log.info("Blockage for the mod \"VoxelMap\" activated.");
		} if (config.getBoolean("mods.5zig.block")) {
			log.info("Blockage for the mod \"5zig\" activated.");
		} if (config.getBoolean("mods.forge.block")) {
			log.info("Blockage for the mod \"Forge\" activated.");
		} if (config.getBoolean("mods.wdl.block")) {
			log.info("Blockage for the mod \"World Downloader\" activated.");
		} if (config.getBoolean("mods.betterpvp.block")) {
			log.info("Blockage for the mod \"Better PvP\" activated.");
		} if (config.getBoolean("mods.labymod.block")) {
			log.info("Blockage for the mod \"LabyMod\" activated.");
			log.warning("Warning: for some servers the LabyMod blockage may throw errors.");	
			log.warning("LabyMod blockage won't work in 1.7 versions");
			log.warning("The reason of this is because LabyMod for 1.7 "
					+ "didn't implement the blockage features as it did for 1.8 and above.");
		}
    		 
		if ((config.getBoolean("mods.liteloader.block", false)) && 
			(config.getBoolean("mods.betterpvp.block", false)) &&
    			(config.getBoolean("mods.schematica.block", false)) &&
    				(config.getBoolean("mods.reiminimap.block", false)) &&
    					(config.getBoolean("mods.damageindicators.block", false)) &&
    						(config.getBoolean("mods.voxelmap.block", false)) &&
    							(config.getBoolean("mods.5zig.block", false)) &&
    								(config.getBoolean("mods.forge.block", false)) &&
    									(config.getBoolean("mods.wdl.block", false)) &&
    										(config.getBoolean("mods.betterpvp.block", false)) &&
    											(config.getBoolean("mods.labymod.block", false))) {
			log.info("You don't have any mods blocked.");										 
		}												 
	}
}
