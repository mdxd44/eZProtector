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

    // Static variables
    public static final ArrayList<String> plugins;
    public static final String ZIG;
    public static final String BSM;
    public static final String MCBRAND;
    public static final String SCHEMATICA;
    private static final String opCommand;
    public static Plugin plugin;
    public static String player;
    public static String oppedPlayer;
    public static String playerCommand;
    public static String errorMessage;
    private static String prefix;

    // Fill static variables with information
    static {
        plugins = new ArrayList<>();
        player = "";
        oppedPlayer = "";
        playerCommand = "";
        errorMessage = "";
        opCommand = "";
        ZIG = "5zig_Set";
        BSM = "BSM";
        MCBRAND = "MC|Brand";
        SCHEMATICA = "schematica";
    }

    // Register the plugin message listener
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
        // Replace placeholders in config messages with the required words
        return StringEscapeUtils.unescapeJava(args
                .replace("%prefix%", prefix)
                .replace("%player%", player)
                .replace("%player%", oppedPlayer)
                .replace("%errormessage%", errorMessage)
                .replace("%command%", "/" + playerCommand)
                .replace("%command%", "/" + opCommand).replaceAll("&", "§"));
    }

    // Code executed on each server startup
    public void onEnable() {
        // Set internal variables
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

        // Register incoming and outgoing plugin channels
        getServer().getMessenger().registerIncomingPluginChannel(this, ZIG, this.pluginMessageListener);
        getServer().getMessenger().registerIncomingPluginChannel(this, BSM, this.pluginMessageListener);
        getServer().getMessenger().registerIncomingPluginChannel(this, MCBRAND, this.pluginMessageListener);
        getServer().getMessenger().registerIncomingPluginChannel(this, SCHEMATICA, this.pluginMessageListener);

        getServer().getMessenger().registerOutgoingPluginChannel(this, ZIG);
        getServer().getMessenger().registerOutgoingPluginChannel(this, BSM);
        getServer().getMessenger().registerOutgoingPluginChannel(this, MCBRAND);
        getServer().getMessenger().registerOutgoingPluginChannel(this, SCHEMATICA);

        // Register events and commands
        getServer().getPluginManager().registerEvents(new IPlayerCommandPreprocessEvent(), this);
        getServer().getPluginManager().registerEvents(new IPlayerJoinEvent(), this);
        getCommand("ezp").setExecutor(new EZPCommand());

        // Add custom plugin list to the internal ArrayList
        plugins.addAll(Arrays.asList(getConfig().getString("custom-plugins.plugins").split(", ")));

        // Log blocked mods (if enabled)
        if (getConfig().getBoolean("log-blocked-mods")) ModLogger.logMods();

        // Register the metrics class and add custom charts
        registerMetrics();

        // Initiate a (very) simple check to see if the plugin has an update!
        checkVersion();

    }

    // Code executed on each server shutdown
    public void onDisable() {
        // Unregister incoming and outgoing plugin channels
        getServer().getMessenger().unregisterIncomingPluginChannel(this, ZIG, this.pluginMessageListener);
        getServer().getMessenger().unregisterIncomingPluginChannel(this, BSM, this.pluginMessageListener);
        getServer().getMessenger().unregisterIncomingPluginChannel(this, MCBRAND, this.pluginMessageListener);
        getServer().getMessenger().unregisterIncomingPluginChannel(this, SCHEMATICA, this.pluginMessageListener);

        getServer().getMessenger().unregisterOutgoingPluginChannel(this, ZIG);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, BSM);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, MCBRAND);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, SCHEMATICA);
    }

    // Registers the Metrics
    private void registerMetrics() {
        // Initiate metrics
        Metrics metrics = new Metrics(this);

        // Get config variables
        Boolean updater = getConfig().getBoolean("updater");
        Boolean mods = getConfig().getBoolean("log-blocked-mods");
        Boolean tabcompletion = getConfig().getBoolean("tab-completion.blocked");
        Boolean hiddensyntaxes = getConfig().getBoolean("hidden-syntaxes.blocked");
        Boolean customplugins = getConfig().getBoolean("custom-plugins.enabled");
        Boolean customversion = getConfig().getBoolean("custom-version.enabled");
        Boolean customcommands = getConfig().getBoolean("custom-commands.blocked");
        Boolean oppedcommands = getConfig().getBoolean("opped-player-commands.blocked");

        // Add custom charts
        metrics.addCustomChart(new Metrics.SimplePie("updater_enabled", updater::toString));
        metrics.addCustomChart(new Metrics.SimplePie("log_blocked_mods", mods::toString));
        metrics.addCustomChart(new Metrics.SimplePie("tab_completion", tabcompletion::toString));
        metrics.addCustomChart(new Metrics.SimplePie("hidden_syntaxes", hiddensyntaxes::toString));
        metrics.addCustomChart(new Metrics.SimplePie("custom_plugins", customplugins::toString));
        metrics.addCustomChart(new Metrics.SimplePie("custom_version", customversion::toString));
        metrics.addCustomChart(new Metrics.SimplePie("custom_commands", customcommands::toString));
        metrics.addCustomChart(new Metrics.SimplePie("opped_commands", oppedcommands::toString));
    }

    // Checks if the version used on the server is the latest and logs to console if not
    private void checkVersion() {
        // Start an async thread
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                // Contact the Spigot Resource API
                HttpsURLConnection con = (HttpsURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=12663").openConnection();
                con.setRequestMethod("GET");
                String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();

                // If version on the API doesn't equal the version in plugin.yml, print an update notification in console
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
