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

package net.elytrium.ezprotector.waterfall;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import net.elytrium.ezprotector.waterfall.listeners.BrigadierListener;
import net.elytrium.ezprotector.waterfall.listeners.ByteMessageListener;
import net.elytrium.ezprotector.waterfall.listeners.CustomCommands;
import net.elytrium.ezprotector.waterfall.listeners.FakeCommands;
import net.elytrium.ezprotector.waterfall.listeners.HiddenSyntaxes;
import net.elytrium.ezprotector.waterfall.listeners.PlayerJoinListener;
import net.elytrium.ezprotector.waterfall.listeners.TabCompletionListener;
import net.elytrium.ezprotector.waterfall.utilities.ExecutionUtil;
import net.elytrium.ezprotector.waterfall.utilities.MessageUtil;
import net.md_5.bungee.api.event.ProxyReloadEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

public class Main extends Plugin implements Listener {

  private Configuration config;

  @Override
  public void onEnable() {
    // eZProtector is only compatible with Waterfall!
    try {
      Class.forName("io.github.waterfallmc.waterfall.event.ProxyDefineCommandsEvent");
    } catch (ClassNotFoundException e) {
      this.getLogger().severe(
          "eZProtector is incompatible with this version of BungeeCord! Please make sure to use Waterfall(b262+), Travertine(b89+) or a compatible fork"
      );
      return;
    }

    // Load the configuration
    this.loadConfig();

    ExecutionUtil execUtil = new ExecutionUtil(getProxy());
    MessageUtil msgUtil = new MessageUtil(this.config, execUtil);

    // Register listeners
    getProxy().getPluginManager().registerListener(this, new BrigadierListener(this.config));
    getProxy().getPluginManager().registerListener(this, new ByteMessageListener(this.config, execUtil, msgUtil));
    getProxy().getPluginManager().registerListener(this, new CustomCommands(this.config, msgUtil));
    getProxy().getPluginManager().registerListener(this, new FakeCommands(this.config, msgUtil));
    getProxy().getPluginManager().registerListener(this, new HiddenSyntaxes(this.config, msgUtil));
    getProxy().getPluginManager().registerListener(this, new PlayerJoinListener(this.config));
    getProxy().getPluginManager().registerListener(this, new TabCompletionListener(this.config));
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private void loadConfig() {
    // Copy default config
    if (!getDataFolder().exists()) {
      getDataFolder().mkdir();
    }
    File file = new File(getDataFolder(), "config.yml");

    if (!file.exists()) {
      try (InputStream in = getResourceAsStream("config.yml")) {
        Files.copy(in, file.toPath());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    // Load configuration
    try {
      this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
    } catch (IOException ignored) {
      //
    }
  }

  @EventHandler
  public void onReload(ProxyReloadEvent event) {
    this.loadConfig();
  }
}
