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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.github.donotspampls.ezprotector.Updater.UpdateChecker;
import org.apache.commons.lang.StringEscapeUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.donotspampls.ezprotector.CommandExecutor.ICommandExecutor;
import com.github.donotspampls.ezprotector.Extras.ModLogger;
import com.github.donotspampls.ezprotector.Extras.Setup;
import com.github.donotspampls.ezprotector.Listener.IPacketEvent;
import com.github.donotspampls.ezprotector.Listener.IPlayerCommandPreprocessEvent;
import com.github.donotspampls.ezprotector.Listener.IPlayerJoinEvent;
import com.github.donotspampls.ezprotector.Listener.IPlayerLoginEvent;
import com.github.donotspampls.ezprotector.Listener.IPluginMessageListener;
import com.github.donotspampls.ezprotector.Listener.ISignChangeEvent;

public class Main extends JavaPlugin implements Listener {

    public static Plugin plugin;
    private static JavaPlugin JavaPlugin;
    public boolean setup = new Setup().setupEZP();
    private IPluginMessageListener pluginMessageListener;
    private List<String> blocked;
    public static ArrayList<String> plugins;
    public static String prefix;
    public static String player;
    public static String oppedPlayer;
    public static String playerCommand;
    public static String errorMessage;
    public static String opCommand;
    public static String ZIG;
    public static String BSPRINT;
    public static String BSM;
    public static String WDLINIT;
    public static String WDLCONTROL;
    public static String MCBRAND;
    public static String WDLREQ;
    public static String SCHEMATICA;
    public static String FML;
    public static String FMLHS;
    private static Logger log;

    static {
        plugins = new ArrayList();
        player = "";
        oppedPlayer = "";
        playerCommand = "";
        errorMessage = "";
        opCommand = "";
        ZIG = "5zig_Set";
        BSPRINT = "BSprint";
        BSM = "BSM";
        WDLINIT = "WDL|INIT";
        WDLCONTROL = "WDL|CONTROL";
        MCBRAND = "MC|Brand";
        WDLREQ = "WDL|REQUEST";
        SCHEMATICA = "schematica";
        FML = "FML";
        FMLHS = "FMLHS";
    }

    public Main() {
        this.pluginMessageListener = new IPluginMessageListener(this);
        this.blocked = new ArrayList<String>();
        log = this.getLogger();
    }
	
	public void onEnable() {
        plugin = this;
        prefix = this.getConfig().getString("prefix");

		if (this.setup) {

			if (this.getConfig().getBoolean("updater")) {
				getServer().getScheduler().runTaskLaterAsynchronously(this, new Runnable() {
					public void run() {
						UpdateChecker.checkUpdate();
					}
				}
				, 20L);
			}

			loadConfig();
			getServer().getMessenger().registerIncomingPluginChannel(this, ZIG, this.pluginMessageListener);
            getServer().getMessenger().registerIncomingPluginChannel(this, BSM, this.pluginMessageListener);
            getServer().getMessenger().registerIncomingPluginChannel(this, WDLINIT, this.pluginMessageListener);
            getServer().getMessenger().registerIncomingPluginChannel(this, MCBRAND, this.pluginMessageListener);
            getServer().getMessenger().registerIncomingPluginChannel(this, WDLREQ, this.pluginMessageListener);
            getServer().getMessenger().registerIncomingPluginChannel(this, SCHEMATICA,  this.pluginMessageListener);
            getServer().getMessenger().registerIncomingPluginChannel(this, FML, this.pluginMessageListener);
            getServer().getMessenger().registerIncomingPluginChannel(this, FMLHS, this.pluginMessageListener);
            getServer().getMessenger().registerOutgoingPluginChannel(this, MCBRAND);
            getServer().getMessenger().registerOutgoingPluginChannel(this, FML);
            getServer().getMessenger().registerOutgoingPluginChannel(this, FMLHS);
            getServer().getMessenger().registerOutgoingPluginChannel(this, ZIG);
            getServer().getMessenger().registerOutgoingPluginChannel(this, WDLCONTROL);
            getServer().getMessenger().registerOutgoingPluginChannel(this, BSM);
            getServer().getMessenger().registerOutgoingPluginChannel(this, SCHEMATICA);
			Main.registerEvents(this, new IPlayerCommandPreprocessEvent(this), new IPlayerJoinEvent(this)
					, new IPlayerLoginEvent(this), new ISignChangeEvent(this));
			this.getCommand("ezp").setExecutor(new ICommandExecutor());

			Metrics metrics = new Metrics(this);

			for (String string : getConfig().getStringList("block-commands.commands")) {
				this.blocked.add(string);
			}

			this.blocked.add("all");
			this.blocked.add("/plugins");
			this.blocked.add("/pl");
			this.blocked.add("/icanhasbukkit");
			this.blocked.add("/?");
			this.blocked.add("/version");
			this.blocked.add("/ver");
			this.blocked.add("/about");
			this.blocked.add("/a");
			this.blocked.add("/help");
			
			if (this.getConfig().getBoolean("tab-completion.blocked")){
				if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
					setupProtocolLibHooks(this.blocked);
				} else {
					log.severe("This plugin requires ProtocolLib in order to work. Please download ProtocolLib and try again.");
					Bukkit.getPluginManager().disablePlugin(this);
				}
			}

            for (String s : this.getConfig().getString("custom-plugins.plugins").split(", ")) {
                Main.plugins.add(s);
            }

			if (this.getConfig().getBoolean("log-blocked-mods")) {
				ModLogger.logMods();
			}

		} else {
			log.severe("Your server version is not compatible with eZProtector. For more information, please read https://git.io/vpRGf");
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}
	public void onDisable() {
		// saveDefaultConfig();
        getServer().getMessenger().unregisterIncomingPluginChannel(this, ZIG, this.pluginMessageListener);
        getServer().getMessenger().unregisterIncomingPluginChannel(this, BSM, this.pluginMessageListener);
        getServer().getMessenger().unregisterIncomingPluginChannel(this, WDLINIT, this.pluginMessageListener);
        getServer().getMessenger().unregisterIncomingPluginChannel(this, MCBRAND, this.pluginMessageListener);
        getServer().getMessenger().unregisterIncomingPluginChannel(this, WDLREQ, this.pluginMessageListener);
        getServer().getMessenger().unregisterIncomingPluginChannel(this, SCHEMATICA,  this.pluginMessageListener);
        getServer().getMessenger().unregisterIncomingPluginChannel(this, FML, this.pluginMessageListener);
        getServer().getMessenger().unregisterIncomingPluginChannel(this, FMLHS, this.pluginMessageListener);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, MCBRAND);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, FML);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, FMLHS);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, ZIG);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, WDLCONTROL);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, BSM);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, SCHEMATICA);
	}

	public static void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners) {
		for (Listener listener : listeners) {
			Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
		}
	}

	public static Plugin getPlugin() {
		return plugin;
	}

	public static JavaPlugin getJavaPlugin() {
		return JavaPlugin;
	}

	public void setupProtocolLibHooks(List<String> protocolList) {
		IPacketEvent.protocolLibHook(protocolList);
	}

	private void loadConfig() {
		this.getConfig().options().copyDefaults(true);
		this.saveDefaultConfig();
		log.info("Reloaded config!");
	}

	public static String placeholders(String args) {
		String newString = StringEscapeUtils.unescapeJava(args.replace("%prefix%", prefix)
				.replace("%player%", player).replace("%player%", oppedPlayer).replace("%errormessage%", errorMessage)
				.replace("%command%",new StringBuilder("/").append(playerCommand).toString())
				.replace("%command%",new StringBuilder("/").append(opCommand).toString()).replaceAll("&", "§"));
		return newString;
	}

}
