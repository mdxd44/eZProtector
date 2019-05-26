/*
 * eZProtector - Copyright (C) 2018-2019 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.paper;

import com.github.donotspampls.ezprotector.paper.listeners.*;
import com.github.donotspampls.ezprotector.paper.utilities.MessageUtil;

import io.papermc.lib.PaperLib;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    // Variables
    public static String ZIG;
    public static String BSM;
    public static String MCBRAND;
    public static String SCHEMATICA;
    public static String WDLINIT;
    public static String WDLCONTROL;

    private static String prefix;
    private static Main plugin;

    /**
     * Gets the plugin variable from the main class.
     *
     * @return The plugin variable.
     */
    public static Main getPlugin() {
        return plugin;
    }

    /**
     * Gets the plugin's message prefix from the main class.
     *
     * @return The plugin variable.
     */
    public static String getPrefix() {
        return prefix;
    }

    @Override
    public void onEnable() {
        plugin = this;
        prefix = MessageUtil.color(getConfig().getString("prefix"));
        String version = getServer().getVersion();

        //if (!version.matches("1\\.1[2-9](.\\d)?")) {
        //    getLogger().severe("eZProtector is not supported on versions lower than 1.12.2!");
        //    getServer().getPluginManager().disablePlugin(this);
        //} else {
            // Save the default config
            saveDefaultConfig();

            ByteMessageListener bml = new ByteMessageListener();

            // Check if the server is 1.13 or above
            boolean newerversion;
            try {
                Class.forName("org.bukkit.entity.Dolphin");
                newerversion = true;
            } catch (ClassNotFoundException ignored) {
                newerversion = false;
            }

            // Set mod channels (Forge 1.13 doesn't exist yet so we don't bother with most 1.13 mods)
            if (!newerversion) {
                ZIG = "5zig_Set";
                BSM = "BSM";
                MCBRAND = "MC|Brand";
                SCHEMATICA = "schematica";
                WDLINIT = "WDL|INIT";
                WDLCONTROL = "WDL|CONTROL";

                getServer().getMessenger().registerIncomingPluginChannel(this, ZIG, bml);
                getServer().getMessenger().registerIncomingPluginChannel(this, BSM, bml);
                getServer().getMessenger().registerIncomingPluginChannel(this, MCBRAND, bml);
                getServer().getMessenger().registerIncomingPluginChannel(this, SCHEMATICA, bml);
                getServer().getMessenger().registerIncomingPluginChannel(this, WDLINIT, bml);

                getServer().getMessenger().registerOutgoingPluginChannel(this, ZIG);
                getServer().getMessenger().registerOutgoingPluginChannel(this, BSM);
                getServer().getMessenger().registerOutgoingPluginChannel(this, SCHEMATICA);
                getServer().getMessenger().registerOutgoingPluginChannel(this, WDLCONTROL);

                getServer().getPluginManager().registerEvents(new TabCompletionListener(), this);
            } else {
                ZIG = "dev:null";
                BSM = "bsm:settings";
                MCBRAND = "minecraft:brand";
                SCHEMATICA = "dev:null";
                WDLINIT = "wdl:init";
                WDLCONTROL = "wdl:control";

                getServer().getMessenger().registerIncomingPluginChannel(this, ZIG, bml);
                getServer().getMessenger().registerIncomingPluginChannel(this, BSM, bml);
                getServer().getMessenger().registerIncomingPluginChannel(this, MCBRAND, bml);
                getServer().getMessenger().registerIncomingPluginChannel(this, WDLINIT, bml);

                getServer().getMessenger().registerOutgoingPluginChannel(this, ZIG);
                getServer().getMessenger().registerOutgoingPluginChannel(this, WDLCONTROL);

                getServer().getPluginManager().registerEvents(new BrigadierListener(), this);
            }

            getServer().getPluginManager().registerEvents(new CommandEventListener(), this);
            getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

            // Suggest Paper to unsuspecting server owners
            PaperLib.suggestPaper(this);
        //}
    }

}
