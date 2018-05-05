/*
Copyright (c) 2016-2017 dvargas135

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

package com.github.donotspampls.ezprotector.Extras;

import com.github.donotspampls.ezprotector.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Logger;

public class ModLogger {

	private static FileConfiguration config = Main.getPlugin().getConfig();

	public static void logMods() {
		Logger log = Main.getPlugin().getLogger();
		
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
		} if (config.getBoolean("mods.smartmoving.block")) {
			log.info("Blockage for the mod \"SmartMoving\" activated.");
		} if (config.getBoolean("mods.bettersprinting.block")) {
			log.info("Blockage for the mod \"BetterSprinting\" activated.");
		}
	}
}
