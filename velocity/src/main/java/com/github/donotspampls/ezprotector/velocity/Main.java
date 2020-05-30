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
import com.google.inject.Inject;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.github.donotspampls.ezprotector.velocity.utilities.MessageUtil.color;

@Plugin(id="ezprotector", name = "${parent.name}",
        description = "${parent.description}",
        version = "${parent.version}",
        authors = "DoNotSpamPls"
)
public class Main {

    private final ProxyServer server;
    private final Logger logger;
    private final Path configDir;

    private static Toml config;
    private static String prefix;

    @Inject
    public Main(ProxyServer server, Logger logger, @DataDirectory Path configDir) {
        this.server = server;
        this.logger = logger;
        this.configDir = configDir;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // Load config
        try {
            Files.copy(Main.class.getResourceAsStream("/resources/config.toml"), configDir.toFile().toPath());
            File configFile = new File(configDir.toFile(), "config.toml");
            config = new Toml().read(configFile);
        } catch (IOException e) {
            logger.error("Unable to load configuration!");
            logger.error(e.getMessage(), e);
        }

        prefix = color(getConfig().getString("prefix"));

        server.getEventManager().register(this, new BrigadierListener());
        server.getEventManager().register(this, new CommandEventListener());
        server.getEventManager().register(this, new ModListener());
        server.getEventManager().register(this, new PlayerJoinListener());
        server.getEventManager().register(this, new TabCompletionListener());
    }

    public static Toml getConfig() {
        return config;
    }

    public static String getPrefix() {
        return prefix;
    }

}
