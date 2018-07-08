/*
 * eZProtector - Copyright (C) 2018 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector;

import com.github.donotspampls.ezprotector.commands.EZPCommand;
import com.github.donotspampls.ezprotector.listeners.IPacketEvent;
import com.github.donotspampls.ezprotector.listeners.IPlayerCommandPreprocessEvent;
import com.github.donotspampls.ezprotector.listeners.IPlayerJoinEvent;
import com.github.donotspampls.ezprotector.listeners.IPluginMessageListener;
import org.apache.commons.lang.StringEscapeUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class Main extends JavaPlugin {

    public static final ArrayList<String> plugins;
    public static final String ZIG;
    public static final String BSM;
    public static final String MCBRAND;
    public static final String SCHEMATICA;
    public static String player;
    public static String playerCommand;
    public static String errorMessage;
    public static Plugin plugin;
    private static String prefix;

    // Fill variables with information
    static {
        plugins = new ArrayList<>();
        ZIG = "5zig_Set";
        BSM = "BSM";
        MCBRAND = "MC|Brand";
        SCHEMATICA = "schematica";
        player = "";
        playerCommand = "";
        errorMessage = "";
    }

    private IPluginMessageListener pluginMessageListener;
    public Main() {
        this.pluginMessageListener = new IPluginMessageListener(this);
    }

    /**
     * Gets the plugin variable from the main class.
     *
     * @return The plugin variable.
     */
    public static Plugin getPlugin() {
        return plugin;
    }

    /**
     * Replaces placeholders with actual information in a given string.
     *
     * @param args The string that should be filtered.
     * @return The new string with replaced placeholders.
     */
    public static String placeholders(String args) {
        return StringEscapeUtils.unescapeJava(args
                .replace("%prefix%", prefix)
                .replace("%player%", player)
                .replace("%errormessage%", errorMessage)
                .replace("%command%", "/" + playerCommand));
    }

    public void onEnable() {
        plugin = this;
        prefix = getConfig().getString("prefix");

        // Save the default config
        saveDefaultConfig();
        reloadConfig();

        // Check if ProtocolLib is on the server and register the packet listener
        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            IPacketEvent.protocolLibHook();
        } else {
            plugin.getLogger().severe("This plugin requires ProtocolLib in order to work. Please download ProtocolLib and try again.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getMessenger().registerIncomingPluginChannel(this, ZIG, this.pluginMessageListener);
        getServer().getMessenger().registerIncomingPluginChannel(this, BSM, this.pluginMessageListener);
        getServer().getMessenger().registerIncomingPluginChannel(this, MCBRAND, this.pluginMessageListener);
        getServer().getMessenger().registerIncomingPluginChannel(this, SCHEMATICA, this.pluginMessageListener);

        getServer().getMessenger().registerOutgoingPluginChannel(this, ZIG);
        getServer().getMessenger().registerOutgoingPluginChannel(this, BSM);
        getServer().getMessenger().registerOutgoingPluginChannel(this, MCBRAND);
        getServer().getMessenger().registerOutgoingPluginChannel(this, SCHEMATICA);

        getServer().getPluginManager().registerEvents(new IPlayerCommandPreprocessEvent(), this);
        getServer().getPluginManager().registerEvents(new IPlayerJoinEvent(), this);
        getCommand("ezp").setExecutor(new EZPCommand());

        // Add custom plugin list to the internal ArrayList
        plugins.addAll(Arrays.asList(getConfig().getString("custom-plugins.plugins").split(", ")));

        // Register the metrics class and add custom charts
        registerMetrics();

        // Initiate a (very) simple check to see if the plugin has an update!
        if (getConfig().getBoolean("updater")) checkVersion();

    }

    public void onDisable() {
        getServer().getMessenger().unregisterIncomingPluginChannel(this, ZIG, this.pluginMessageListener);
        getServer().getMessenger().unregisterIncomingPluginChannel(this, BSM, this.pluginMessageListener);
        getServer().getMessenger().unregisterIncomingPluginChannel(this, MCBRAND, this.pluginMessageListener);
        getServer().getMessenger().unregisterIncomingPluginChannel(this, SCHEMATICA, this.pluginMessageListener);

        getServer().getMessenger().unregisterOutgoingPluginChannel(this, ZIG);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, BSM);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, MCBRAND);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, SCHEMATICA);
    }

    private void registerMetrics() {
        Metrics metrics = new Metrics(this);

        Boolean updater = getConfig().getBoolean("updater");
        Boolean tabcompletion = getConfig().getBoolean("tab-completion.blocked");
        Boolean hiddensyntaxes = getConfig().getBoolean("hidden-syntaxes.blocked");
        Boolean customplugins = getConfig().getBoolean("custom-plugins.enabled");
        Boolean customversion = getConfig().getBoolean("custom-version.enabled");
        Boolean customcommands = getConfig().getBoolean("custom-commands.blocked");

        metrics.addCustomChart(new Metrics.SimplePie("updater_enabled", updater::toString));
        metrics.addCustomChart(new Metrics.SimplePie("tab_completion", tabcompletion::toString));
        metrics.addCustomChart(new Metrics.SimplePie("hidden_syntaxes", hiddensyntaxes::toString));
        metrics.addCustomChart(new Metrics.SimplePie("custom_plugins", customplugins::toString));
        metrics.addCustomChart(new Metrics.SimplePie("custom_version", customversion::toString));
        metrics.addCustomChart(new Metrics.SimplePie("custom_commands", customcommands::toString));
    }

    private void checkVersion() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                HttpsURLConnection con = (HttpsURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=12663").openConnection();
                con.setRequestMethod("GET");
                String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();

                if (!(version.equals(this.getDescription().getVersion()))) {
                    plugin.getLogger().info("An update for eZProtector is available! Download it now at https://bit.ly/eZProtector");
                }
            } catch (IOException ignored) {
                // For some reason the update check failed, this is very rare so the exception isn't printed.
                plugin.getLogger().warning("Failed to check for an update!");
            }
        });
    }
}
