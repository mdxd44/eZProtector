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

package net.elytrium.ezprotector.sponge;

import com.google.inject.Inject;
import com.moandjiezana.toml.Toml;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import net.elytrium.ezprotector.sponge.listeners.CustomCommands;
import net.elytrium.ezprotector.sponge.listeners.HiddenSyntaxes;
import net.elytrium.ezprotector.sponge.listeners.ModListener;
import net.elytrium.ezprotector.sponge.listeners.PlayerJoinListener;
import net.elytrium.ezprotector.sponge.listeners.TabCompletionListener;
import net.elytrium.ezprotector.sponge.utilities.MessageUtil;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.network.ChannelBinding;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.plugin.PluginManager;

@Plugin(id = "ezprotector")
public class Main {

  // Plugin Channels
  public static ChannelBinding.RawDataChannel ZIG;
  public static ChannelBinding.RawDataChannel BSM;
  public static ChannelBinding.RawDataChannel SCHEMATICA;
  public static ChannelBinding.RawDataChannel WDLCONTROL;

  private PluginContainer plugin;
  private Toml config;

  @Inject
  private PluginManager pluginManager;
  @Inject
  private Game server;
  @Inject
  private Logger logger;

  @Inject
  @ConfigDir(sharedRoot = true)
  private Path configDir;

  @Listener
  public void onServerStart(GameStartedServerEvent event) {
    this.plugin = this.pluginManager.getPlugin("ezprotector").orElse(null);

    // Save the default config
    this.loadConfig();

    // Register mod channels
    ZIG = this.server.getChannelRegistrar().createRawChannel(this, "5zig_Set");
    BSM = this.server.getChannelRegistrar().createRawChannel(this, "BSM");
    SCHEMATICA = this.server.getChannelRegistrar().createRawChannel(this, "schematica");
    WDLCONTROL = this.server.getChannelRegistrar().createRawChannel(this, "WDL|CONTROL");

    MessageUtil msgUtil = new MessageUtil(this.config);

    // Register listeners
    this.server.getEventManager().registerListeners(this, new CustomCommands(this.config, msgUtil));
    this.server.getEventManager().registerListeners(this, new HiddenSyntaxes(this.config, msgUtil));
    this.server.getEventManager().registerListeners(this, new ModListener(this.config, msgUtil));
    this.server.getEventManager().registerListeners(this, new PlayerJoinListener(this.config));
    this.server.getEventManager().registerListeners(this, new TabCompletionListener(this.config));
  }

  private void loadConfig() {
    try {
      this.plugin.getAsset("ezprotector.toml").ifPresent(asset -> {
        try {
          asset.copyToDirectory(this.configDir);
        } catch (IOException ioException) {
          ioException.printStackTrace();
        }
      });

      File configFile = new File(this.configDir.toFile(), "ezprotector.toml");

      this.config = new Toml().read(configFile);
    } catch (Exception e) {
      this.logger.error("Unable to load configuration!");
      this.logger.error(e.getMessage(), e);
    }
  }
}
