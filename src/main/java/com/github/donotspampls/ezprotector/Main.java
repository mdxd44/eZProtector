/*
 * Copyright (c) 2016-2018 dvargas135, DoNotSpamPls and contributors. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
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
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class Main extends JavaPlugin {

    public static Plugin plugin;
    public static final ArrayList<String> plugins;
    public static String player;
    public static String oppedPlayer;
    public static String playerCommand;
    public static String errorMessage;
    public static final String ZIG;
    public static final String BSM;
    public static final String MCBRAND;
    public static final String SCHEMATICA;
    private static String prefix;
    private static final String opCommand;

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

    private IPluginMessageListener pluginMessageListener;

    public Main() {
        this.pluginMessageListener = new IPluginMessageListener(this);
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static String placeholders(String args) {
        return StringEscapeUtils.unescapeJava(args
                .replace("%prefix%", prefix)
                .replace("%player%", player)
                .replace("%player%", oppedPlayer)
                .replace("%errormessage%", errorMessage)
                .replace("%command%", "/" + playerCommand)
                .replace("%command%", "/" + opCommand).replaceAll("&", "§"));
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

        // Register plugin channels
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
        getServer().getPluginManager().registerEvents(new IPlayerJoinEvent(this), this);
        getCommand("ezp").setExecutor(new EZPCommand());

        // Add custom plugin list to the internal ArrayList
        plugins.addAll(Arrays.asList(getConfig().getString("custom-plugins.plugins").split(", ")));

        // Log blocked mods (if enabled)
        if (getConfig().getBoolean("log-blocked-mods")) ModLogger.logMods();

        // Register the metrics class and add custom charts
        registerMetrics();

        // A (very) simple check if the plugin has an update!
        checkVersion();

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

    private void checkVersion() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                HttpsURLConnection con = (HttpsURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=12663").openConnection();
                con.setRequestMethod("GET");
                String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();

                if (!(version.equals(this.getDescription().getVersion()))) {
                    plugin.getLogger().info("An update for eZProtector is available! Download it now at https://bit.ly/eZProtector");
                }
            } catch (Exception ignored) {
                plugin.getLogger().warning("Failed to check for an update!");
            }
        });
    }
}
