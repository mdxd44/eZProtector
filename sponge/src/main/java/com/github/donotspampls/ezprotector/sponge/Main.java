/*
 * eZProtector - Copyright (C) 2018-2019 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.sponge;

import com.github.donotspampls.ezprotector.sponge.listeners.*;
import com.github.donotspampls.ezprotector.sponge.utilities.MessageUtil;
import com.google.inject.Inject;
import com.moandjiezana.toml.Toml;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.network.ChannelBinding;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(id="ezprotector")
public class Main {

    // Plugin Channels
    public static ChannelBinding.RawDataChannel ZIG;
    public static ChannelBinding.RawDataChannel BSM;
    public static ChannelBinding.RawDataChannel MCBRAND;
    public static ChannelBinding.RawDataChannel SCHEMATICA;
    public static ChannelBinding.RawDataChannel WDLINIT ;
    public static ChannelBinding.RawDataChannel WDLCONTROL;
    private static String prefix;

    @Inject
    private Game server;

    @Inject
    private Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;
    private static Toml config;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        // Save the default config
        loadConfig();

        prefix = MessageUtil.color(getConfig().getString("prefix"));

        // Register mod channels
        ZIG = server.getChannelRegistrar().createRawChannel(this, "5zig_Set");
        BSM = server.getChannelRegistrar().createRawChannel(this, "BSM");
        MCBRAND = server.getChannelRegistrar().createRawChannel(this, "MC|Brand");
        SCHEMATICA = server.getChannelRegistrar().createRawChannel(this, "schematica");
        WDLINIT = server.getChannelRegistrar().createRawChannel(this, "WDL|INIT");
        WDLCONTROL = server.getChannelRegistrar().createRawChannel(this, "WDL|CONTROL");

        // Register listeners
        server.getEventManager().registerListeners(this, new CustomCommands());
        server.getEventManager().registerListeners(this, new FakeCommands());
        server.getEventManager().registerListeners(this, new HiddenSyntaxes());
        server.getEventManager().registerListeners(this, new PlayerJoinListener());
        server.getEventManager().registerListeners(this, new TabCompletionListener());
    }

    private void loadConfig() {
        try {
            if (Files.notExists(configDir)) {
                server.getAssetManager().getAsset("config.toml").ifPresent(asset -> {
                    try {
                        asset.copyToDirectory(configDir);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

            File configFile = new File(configDir.toFile(), "config.toml");

            config = new Toml(
                    new Toml().read(Main.class.getResourceAsStream("/resources/assets/config.toml")))
                    .read(configFile);
        } catch (Exception e) {
            logger.error("Unable to load configuration!");
            logger.error(e.getMessage(), e);
        }
    }

    public static Toml getConfig() {
        return config;
    }

    /**
     * Gets the plugin's message prefix from the main class.
     *
     * @return The plugin variable.
     */
    public static String getPrefix() {
        return prefix;
    }

}
