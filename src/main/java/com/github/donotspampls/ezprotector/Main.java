/*
 * Copyright (c) 2016-2018 dvargas135, DoNotSpamPls and contributors. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package com.github.donotspampls.ezprotector;

import com.github.donotspampls.ezprotector.commands.EZPCommand;
import com.github.donotspampls.ezprotector.listeners.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Main extends JavaPlugin implements Listener {

    public static Plugin plugin;
    public static ArrayList<String> plugins;
    public static String player;
    public static String oppedPlayer;
    public static String playerCommand;
    public static String errorMessage;
    public static String ZIG;
    public static String BSM;
    public static String MCBRAND;
    public static String SCHEMATICA;
    private static String prefix;
    private static String opCommand;
    private static Logger log;

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
    private List<String> blocked;

    public Main() {
        this.pluginMessageListener = new IPluginMessageListener(this);
        this.blocked = new ArrayList<>();
        log = this.getLogger();
    }

    private static void registerEvents(Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
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
        prefix = this.getConfig().getString("prefix");

        // Save the default config
        this.saveDefaultConfig();
        this.reloadConfig();

        // Check if ProtocolLib is on the server.
        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            setupProtocolLibHooks(this.blocked);
        } else {
            log.severe("This plugin requires ProtocolLib in order to work. Please download ProtocolLib and try again.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Register plugin channels
        getServer().getMessenger().registerIncomingPluginChannel(this, ZIG, this.pluginMessageListener);
        getServer().getMessenger().registerIncomingPluginChannel(this, BSM, this.pluginMessageListener);
        getServer().getMessenger().registerIncomingPluginChannel(this, MCBRAND, this.pluginMessageListener);
        getServer().getMessenger().registerIncomingPluginChannel(this, SCHEMATICA, this.pluginMessageListener);

        getServer().getMessenger().registerOutgoingPluginChannel(this, MCBRAND);
        getServer().getMessenger().registerOutgoingPluginChannel(this, ZIG);
        getServer().getMessenger().registerOutgoingPluginChannel(this, BSM);
        getServer().getMessenger().registerOutgoingPluginChannel(this, SCHEMATICA);

        // Register events and commands
        registerEvents(this, new IPlayerCommandPreprocessEvent(this), new IPlayerJoinEvent(this), new IPlayerLoginEvent(this));
        this.getCommand("ezp").setExecutor(new EZPCommand());

        // Add blocked commands and custom plugin list to the internal ArrayLists
        blocked.addAll(getConfig().getStringList("block-commands.commands"));
        plugins.addAll(Arrays.asList(this.getConfig().getString("custom-plugins.plugins").split(", ")));

        // Log blocked mods (if enabled)
        if (this.getConfig().getBoolean("log-blocked-mods")) { ModLogger.logMods(); }

        // A (very) simple check if the plugin has an update!
        checkVersion();

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

    public void onDisable() {
        getServer().getMessenger().unregisterIncomingPluginChannel(this, ZIG, this.pluginMessageListener);
        getServer().getMessenger().unregisterIncomingPluginChannel(this, BSM, this.pluginMessageListener);
        getServer().getMessenger().unregisterIncomingPluginChannel(this, MCBRAND, this.pluginMessageListener);
        getServer().getMessenger().unregisterIncomingPluginChannel(this, SCHEMATICA, this.pluginMessageListener);

        getServer().getMessenger().unregisterOutgoingPluginChannel(this, MCBRAND);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, ZIG);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, BSM);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, SCHEMATICA);
    }

    private void setupProtocolLibHooks(List<String> protocolList) {
        IPacketEvent.protocolLibHook(protocolList);
    }

    private void checkVersion() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                HttpsURLConnection con = (HttpsURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=12663").openConnection();
                con.setRequestMethod("GET");
                String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();

                if (!(version.equals(this.getDescription().getVersion()))) {
                    log.info("An update for eZProtector is available! Download it now at https://bit.ly/eZProtector");
                }
            } catch (Exception ex) {
                plugin.getLogger().info("Failed to check for updates on spigot.");
            }
        });
    }
}
