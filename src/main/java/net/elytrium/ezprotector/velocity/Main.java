/*
 * Copyright (C) 2021 Elytrium, DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.elytrium.ezprotector.velocity;

import com.google.inject.Inject;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.elytrium.ezprotector.velocity.listeners.BrigadierListener;
import net.elytrium.ezprotector.velocity.listeners.CustomCommands;
import net.elytrium.ezprotector.velocity.listeners.FakeCommands;
import net.elytrium.ezprotector.velocity.listeners.HiddenSyntaxes;
import net.elytrium.ezprotector.velocity.listeners.ModListener;
import net.elytrium.ezprotector.velocity.listeners.PlayerJoinListener;
import net.elytrium.ezprotector.velocity.listeners.TabCompletionListener;
import net.elytrium.ezprotector.velocity.utilities.ExecutionUtil;
import net.elytrium.ezprotector.velocity.utilities.MessageUtil;
import org.slf4j.Logger;

@Plugin(id = "ezprotector")
public class Main {

  @Inject
  private ProxyServer server;
  @Inject
  private Logger logger;

  @Inject
  @DataDirectory
  private Path configDir;

  private Toml config;

  @Subscribe
  @SuppressWarnings({"ResultOfMethodCallIgnored", "unused"})
  public void onProxyInitialization(ProxyInitializeEvent event) {
    // Load config
    try {
      if (!this.configDir.toFile().exists()) {
        this.configDir.toFile().mkdir();
      }

      File configFile = new File(this.configDir.toFile(), "config.toml");
      if (!configFile.exists()) {
        Files.copy(Main.class.getResourceAsStream("/resources/config.toml"), configFile.toPath());
      }
      this.config = new Toml().read(configFile);
    } catch (IOException e) {
      this.logger.error("Unable to load configuration!");
      this.logger.error(e.getMessage(), e);
    }

    ExecutionUtil execUtil = new ExecutionUtil(this.server);
    MessageUtil msgUtil = new MessageUtil(this.config, execUtil);

    // Register events
    this.server.getEventManager().register(this, new BrigadierListener(this.config));
    this.server.getEventManager().register(this, new CustomCommands(this.config, msgUtil));
    this.server.getEventManager().register(this, new FakeCommands(this.config, msgUtil));
    this.server.getEventManager().register(this, new HiddenSyntaxes(this.config, msgUtil));
    this.server.getEventManager().register(this, new ModListener(this.config, execUtil, msgUtil));
    this.server.getEventManager().register(this, new PlayerJoinListener(this.config));
    this.server.getEventManager().register(this, new TabCompletionListener(this.config));

    // Register channel identifiers
    this.server.getChannelRegistrar().register(
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
