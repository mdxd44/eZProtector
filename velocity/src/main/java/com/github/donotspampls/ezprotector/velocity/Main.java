/*
 * eZProtector - Copyright (C) 2018-2020 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.velocity;

import com.github.donotspampls.ezprotector.velocity.listeners.*;
import com.github.donotspampls.ezprotector.velocity.utilities.ExecutionUtil;
import com.github.donotspampls.ezprotector.velocity.utilities.MessageUtil;
import com.google.inject.Inject;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(id = "ezprotector")
public class Main {

    @Inject private ProxyServer server;
    @Inject private Logger logger;

    @Inject
    @DataDirectory
    private Path configDir;

    private Toml config;

    @Subscribe
    @SuppressWarnings({"ResultOfMethodCallIgnored", "unused"})
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // Load config
        try {
            if (!configDir.toFile().exists()) configDir.toFile().mkdir();

            File configFile = new File(configDir.toFile(), "config.toml");
            if (!configFile.exists())
                Files.copy(Main.class.getResourceAsStream("/resources/config.toml"), configFile.toPath());
            config = new Toml().read(configFile);
        } catch (IOException e) {
            logger.error("Unable to load configuration!");
            logger.error(e.getMessage(), e);
        }

        ExecutionUtil execUtil = new ExecutionUtil(server);
        MessageUtil msgUtil = new MessageUtil(config, execUtil);

        // Register events
        server.getEventManager().register(this, new BrigadierListener(config));
        server.getEventManager().register(this, new CustomCommands(config, msgUtil));
        server.getEventManager().register(this, new FakeCommands(config, msgUtil));
        server.getEventManager().register(this, new HiddenSyntaxes(config, msgUtil));
        server.getEventManager().register(this, new ModListener(config, execUtil, msgUtil));
        server.getEventManager().register(this, new PlayerJoinListener(config));
        server.getEventManager().register(this, new TabCompletionListener(config));

        // Register channel identifiers
        server.getChannelRegistrar().register(
                MinecraftChannelIdentifier.create("the5zigmod", "5zig_set"),
                MinecraftChannelIdentifier.create("bsm", "settings"),
                MinecraftChannelIdentifier.create("minecraft", "brand"),
                MinecraftChannelIdentifier.create("wdl", "init"),
                MinecraftChannelIdentifier.create("wdl", "control"),

                new LegacyChannelIdentifier("5zig_Set"),
                new LegacyChannelIdentifier("BSM"),
                new LegacyChannelIdentifier("MC|Brand"),
                new LegacyChannelIdentifier("schematica"),
                new LegacyChannelIdentifier("WDL|INIT"),
                new LegacyChannelIdentifier("WDL|CONTROL")
        );

    }

}
