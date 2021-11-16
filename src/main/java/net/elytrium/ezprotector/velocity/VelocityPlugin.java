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
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import java.io.File;
import java.nio.file.Path;
import net.elytrium.ezprotector.BuildConstants;
import net.elytrium.ezprotector.shared.Platform;
import net.elytrium.ezprotector.shared.PluginImpl;
import net.elytrium.ezprotector.shared.Settings;
import net.elytrium.ezprotector.velocity.listeners.BrigadierListener;
import net.elytrium.ezprotector.velocity.utilities.ExecutionUtil;
import net.elytrium.ezprotector.velocity.utilities.MessageUtil;
import org.slf4j.Logger;

@Plugin(id = "ezprotector", name = "eZProtector", version = BuildConstants.VERSION,
    description = "Securing your server the easy way!",
    url = "https://github.com/Elytrium/eZProtector",
    authors = {"DoNotSpamPls", "mdxd44", "hevav"})
public class VelocityPlugin implements Platform {

  private final ProxyServer server;
  private final Logger logger;
  private final Path configDir;

  @Inject
  public VelocityPlugin(ProxyServer server, Logger logger, @DataDirectory Path configDir) {
    this.server = server;
    this.logger = logger;
    this.configDir = configDir;
  }

  @Subscribe
  public void onProxyInitialization(ProxyInitializeEvent event) {
    PluginImpl.setInstance(this);

    Settings.IMP.reload(new File(this.configDir.toFile(), "config.yml"));

    ExecutionUtil execUtil = new ExecutionUtil(this.server);
    MessageUtil msgUtil = new MessageUtil(execUtil);

    // Register events
    this.server.getEventManager().register(this, new BrigadierListener());
    /*
    this.server.getEventManager().register(this, new CustomCommands(msgUtil));
    this.server.getEventManager().register(this, new FakeCommands(msgUtil));
    this.server.getEventManager().register(this, new HiddenSyntaxes(msgUtil));
    this.server.getEventManager().register(this, new ModListener(execUtil, msgUtil));
    this.server.getEventManager().register(this, new PlayerJoinListener());
    */
    //new TabCompletionListener(this.server);

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

  @Override
  public Logger getPluginLogger() {
    return this.logger;
  }
}
