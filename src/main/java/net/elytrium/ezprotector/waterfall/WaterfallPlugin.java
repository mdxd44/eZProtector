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
import net.elytrium.ezprotector.shared.PluginImpl;
import net.elytrium.ezprotector.shared.IPlatform;
import net.elytrium.ezprotector.shared.config.Settings;
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
import net.md_5.bungee.event.EventHandler;
import org.slf4j.Logger;

public class WaterfallPlugin extends Plugin implements Listener, IPlatform {

  @Override
  public void onEnable() {
    // eZProtector is only compatible with Waterfall!
    try {
      Class.forName("io.github.waterfallmc.waterfall.event.ProxyDefineCommandsEvent");
    } catch (ClassNotFoundException e) {
      this.getPluginLogger().error(
          "eZProtector is incompatible with this version of BungeeCord! Please make sure to use Waterfall(b262+), Travertine(b89+) or a compatible fork"
      );
      return;
    }

    new PluginImpl(this);

    // Load the configuration
    this.reloadConfig();

    ExecutionUtil execUtil = new ExecutionUtil(getProxy());
    MessageUtil msgUtil = new MessageUtil(execUtil);

    // Register listeners
    this.getProxy().getPluginManager().registerListener(this, new BrigadierListener());
    this.getProxy().getPluginManager().registerListener(this, new ByteMessageListener(execUtil, msgUtil));
    this.getProxy().getPluginManager().registerListener(this, new CustomCommands(msgUtil));
    this.getProxy().getPluginManager().registerListener(this, new FakeCommands(msgUtil));
    this.getProxy().getPluginManager().registerListener(this, new HiddenSyntaxes(msgUtil));
    this.getProxy().getPluginManager().registerListener(this, new PlayerJoinListener());
    this.getProxy().getPluginManager().registerListener(this, new TabCompletionListener());
  }

  private void reloadConfig() {
    Settings.IMP.reload(new File(this.getDataFolder().getAbsoluteFile(), "config.yml"));
  }

  @EventHandler
  public void onReload(ProxyReloadEvent event) {
    this.reloadConfig();
  }

  @Override
  public Logger getPluginLogger() {
    return this.getSLF4JLogger();
  }
}
