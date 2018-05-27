/*
 * Copyright (c) 2016-2018 dvargas135, DoNotSpamPls and contributors. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package com.github.donotspampls.ezprotector;

import org.bukkit.configuration.file.FileConfiguration;

class ModLogger {

    private static FileConfiguration config = Main.getPlugin().getConfig();

    static void logMods() {
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

    private static void logMod(String mod) {
       Main.getPlugin().getLogger().info(mod + " mod blocking activated.");
    }

}
