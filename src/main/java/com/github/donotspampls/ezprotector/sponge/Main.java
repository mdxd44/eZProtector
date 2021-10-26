/*
 * eZProtector - Copyright (C) 2018-2020 DoNotSpamPls
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
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Plugin(id="ezprotector")
public class Main {

    // Plugin Channels
    public static ChannelBinding.RawDataChannel ZIG;
    public static ChannelBinding.RawDataChannel BSM;
    public static ChannelBinding.RawDataChannel SCHEMATICA;
    public static ChannelBinding.RawDataChannel WDLCONTROL;

    private PluginContainer plugin;
    private Toml config;

    @Inject private PluginManager pluginManager;
    @Inject private Game server;
    @Inject private Logger logger;

    @Inject
    @ConfigDir(sharedRoot = true)
    private Path configDir;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        plugin = pluginManager.getPlugin("ezprotector").orElse(null);

        // Save the default config
        loadConfig();

        MessageUtil msgUtil = new MessageUtil(config);

        // Register mod channels
        ZIG = server.getChannelRegistrar().createRawChannel(this, "5zig_Set");
        BSM = server.getChannelRegistrar().createRawChannel(this, "BSM");
        SCHEMATICA = server.getChannelRegistrar().createRawChannel(this, "schematica");
        WDLCONTROL = server.getChannelRegistrar().createRawChannel(this, "WDL|CONTROL");

        // Register listeners
        server.getEventManager().registerListeners(this, new CustomCommands(config, msgUtil));
        server.getEventManager().registerListeners(this, new HiddenSyntaxes(config, msgUtil));
        server.getEventManager().registerListeners(this, new ModListener(config, msgUtil));
        server.getEventManager().registerListeners(this, new PlayerJoinListener(config));
        server.getEventManager().registerListeners(this, new TabCompletionListener(config));
    }

    private void loadConfig() {
        try {
            plugin.getAsset("ezprotector.toml").ifPresent(asset -> {
                try {
                    asset.copyToDirectory(configDir);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });

            File configFile = new File(configDir.toFile(), "ezprotector.toml");

            config = new Toml().read(configFile);
        } catch (Exception e) {
            logger.error("Unable to load configuration!");
            logger.error(e.getMessage(), e);
        }
    }

}
