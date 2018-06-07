/*
 * eZProtector - Copyright (C) 2018 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector;

import org.bukkit.configuration.file.FileConfiguration;

class ModLogger {

    // Get the plugin config
    private static final FileConfiguration config = Main.getPlugin().getConfig();

    static void logMods() {
        // If a mod block is enabled in the config, execute the logMod() method below
        if (config.getBoolean("mods.5zig.block")) logMod("5Zig");
        if (config.getBoolean("mods.betterpvp.block")) logMod("BetterPvP");
        if (config.getBoolean("mods.bettersprinting.block")) logMod("BetterSprinting");
        if (config.getBoolean("mods.damageindicators.block")) logMod("DamageIndicators");
        if (config.getBoolean("mods.forge.block")) logMod("Forge");
        if (config.getBoolean("mods.liteloader.block")) logMod("LiteLoader");
        if (config.getBoolean("mods.reiminimap.block")) logMod("Rei's Minimap");
        if (config.getBoolean("mods.schematica.block")) logMod("Schematica");
        if (config.getBoolean("mods.smartmoving.block")) logMod("SmartMoving");
        if (config.getBoolean("mods.voxelmap.block")) logMod("VoxelMap");
    }

    /**
     * Log the block of various mods in the console.
     *
     * @param mod The mod's name.
     */
    private static void logMod(String mod) {
        // Send message to console
        Main.getPlugin().getLogger().info(mod + " mod blocking activated.");
    }

}
