/*
 * Copyright (c) 2016-2017 dvargas135
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.donotspampls.ezprotector;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.donotspampls.ezprotector.CommandExecutor.ICommandExecutor;
import com.github.donotspampls.ezprotector.Extras.ModLogger;
import com.github.donotspampls.ezprotector.Listener.*;
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
    private IPluginMessageListener pluginMessageListener;
    private List<String> blocked;
    public static ArrayList<String> plugins;
    private static String prefix;
    public static String player;
    public static String oppedPlayer;
    public static String playerCommand;
    public static String errorMessage;
    private static String opCommand;
    public static String ZIG;
    public static String BSM;
    public static String MCBRAND;
    public static String SCHEMATICA;
    public static String FML;
    public static String FMLHS;
    private static Logger log;
    private ProtocolManager protocolManager;

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
        FML = "FML";
        FMLHS = "FMLHS";
    }

    public Main() {
        this.pluginMessageListener = new IPluginMessageListener(this);
        this.blocked = new ArrayList<>();
        log = this.getLogger();
    }

	public void onEnable() {
        plugin = this;
        prefix = this.getConfig().getString("prefix");
        protocolManager = ProtocolLibrary.getProtocolManager();

        // Check if ProtocolLib is on the server.
        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            setupProtocolLibHooks(this.blocked);
        } else {
            log.severe("This plugin requires ProtocolLib in order to work. Please download ProtocolLib and try again.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // A (very) simple check if the plugin has an update!
        if (plugin.getDescription().getVersion().equals(checkVersion())) return;
        else log.info("An update for eZProtector is available! Download it now at https://bit.ly/eZProtector");

        // Save the default config
        saveDefaultConfig();
        reloadConfig();

        // Register plugin channels
        getServer().getMessenger().registerIncomingPluginChannel(this, ZIG, this.pluginMessageListener);
        getServer().getMessenger().registerIncomingPluginChannel(this, BSM, this.pluginMessageListener);
        getServer().getMessenger().registerIncomingPluginChannel(this, MCBRAND, this.pluginMessageListener);
        getServer().getMessenger().registerIncomingPluginChannel(this, SCHEMATICA, this.pluginMessageListener);
        getServer().getMessenger().registerIncomingPluginChannel(this, FML, this.pluginMessageListener);
        getServer().getMessenger().registerIncomingPluginChannel(this, FMLHS, this.pluginMessageListener);

        getServer().getMessenger().registerOutgoingPluginChannel(this, MCBRAND);
        getServer().getMessenger().registerOutgoingPluginChannel(this, FML);
        getServer().getMessenger().registerOutgoingPluginChannel(this, FMLHS);
        getServer().getMessenger().registerOutgoingPluginChannel(this, ZIG);
        getServer().getMessenger().registerOutgoingPluginChannel(this, BSM);
        getServer().getMessenger().registerOutgoingPluginChannel(this, SCHEMATICA);

        // Register events and commands
        registerEvents(this, new IPlayerCommandPreprocessEvent(this), new IPlayerJoinEvent(this), new IPlayerLoginEvent(this));
        this.getCommand("ezp").setExecutor(new ICommandExecutor());

        // Initiate metrics
        Metrics metrics = new Metrics(this);

        // Add blocked commands and custom plugin list to the internal ArrayLists
        blocked.addAll(getConfig().getStringList("block-commands.commands"));
        plugins.addAll(Arrays.asList(this.getConfig().getString("custom-plugins.plugins").split(", ")));

        // Log blocked mods (if enabled)
        if (this.getConfig().getBoolean("log-blocked-mods")) ModLogger.logMods();

	}
	public void onDisable() {
        getServer().getMessenger().unregisterIncomingPluginChannel(this, ZIG, this.pluginMessageListener);
        getServer().getMessenger().unregisterIncomingPluginChannel(this, BSM, this.pluginMessageListener);
        getServer().getMessenger().unregisterIncomingPluginChannel(this, MCBRAND, this.pluginMessageListener);
        getServer().getMessenger().unregisterIncomingPluginChannel(this, SCHEMATICA,  this.pluginMessageListener);
        getServer().getMessenger().unregisterIncomingPluginChannel(this, FML, this.pluginMessageListener);
        getServer().getMessenger().unregisterIncomingPluginChannel(this, FMLHS, this.pluginMessageListener);

        getServer().getMessenger().unregisterOutgoingPluginChannel(this, MCBRAND);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, FML);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, FMLHS);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, ZIG);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, BSM);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, SCHEMATICA);
	}

	private static void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners) {
		for (Listener listener : listeners) {
			Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
		}
	}

	public static Plugin getPlugin() {
		return plugin;
	}

	private void setupProtocolLibHooks(List<String> protocolList) {
		IPacketEvent.protocolLibHook(protocolList);
	}

	public static String placeholders(String args) {
        return StringEscapeUtils.unescapeJava(args.replace("%prefix%", prefix)
                .replace("%player%", player).replace("%player%", oppedPlayer).replace("%errormessage%", errorMessage)
                .replace("%command%", "/" + playerCommand)
                .replace("%command%", "/" + opCommand).replaceAll("&", "§"));
	}

    private String checkVersion() {
        try {
            HttpsURLConnection con = (HttpsURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=12663").openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("GET");
            String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if (version.length() <= 13) return version;
        } catch (Exception e) {
            log.info("Failed to check for an update.");
        }
        return getDescription().getVersion();
    }

}
