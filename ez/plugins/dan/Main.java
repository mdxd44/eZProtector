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

package ez.plugins.dan;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import ez.plugins.dan.CommandExecutor.ICommandExecutor;
import ez.plugins.dan.Extras.ModLogger;
import ez.plugins.dan.Extras.Setup;
import ez.plugins.dan.Listener.IPacketEvent;
import ez.plugins.dan.Listener.IPlayerCommandPreprocessEvent;
import ez.plugins.dan.Listener.IPlayerJoinEvent;
import ez.plugins.dan.Listener.IPlayerLoginEvent;
import ez.plugins.dan.Listener.IPluginMessageListener;
import ez.plugins.dan.Listener.ISignChangeEvent;
import ez.plugins.dan.Metrics.MetricsChecker;
import ez.plugins.dan.SpigotUpdater.UpdateChecker;

public class Main extends JavaPlugin implements Listener {
	public static Plugin plugin;
	private static JavaPlugin JavaPlugin;
	public boolean setup = new Setup().setupEZP();
	private IPluginMessageListener pluginMessageListener = new IPluginMessageListener(this);
	private List<String> blocked = new ArrayList<String>();
	public static ArrayList<String> plugins = new ArrayList<String>();
	public static String prefix;
	public static String player = "";
	public static String oppedPlayer = "";
	public static String playerCommand = "";
	public static String errorMessage = "";
	public static String opCommand = "";
	public static String ZIG = "5zig_Set";
	public static String BSPRINT = "BSprint";
	public static String BSM = "BSM";
	public static String WDLINIT = "WDL|INIT";
	public static String WDLCONTROL = "WDL|CONTROL";
	public static String MCBRAND = "MC|Brand";
	public static String WDLREQ = "WDL|REQUEST";
	public static String SCHEMATICA = "schematica";
	public static String FML = "FML";
	public static String FMLHS = "FMLHS";
	private static Logger log;
	public void onEnable() {
		plugin = this;
		log = getLogger();
		prefix = this.getConfig().getString("prefix");
		if (setup == true) {
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
			registerEvents(this, new IPlayerCommandPreprocessEvent(this), new IPlayerJoinEvent(this)
					, new IPlayerLoginEvent(this), new ISignChangeEvent(this));
			getCommand("ezp").setExecutor(new ICommandExecutor());
			MetricsChecker.tryMetrics();
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
					log.warning("ProtocolLib not found.");
					log.warning("The plugin needs ProtocolLib to work.");
					log.warning("Disabling.");
					Bukkit.getPluginManager().disablePlugin(this);
				}
			}
			for (String s : this.getConfig().getString("custom-plugins.plugins").split(", ")) {
				Main.plugins.add(s);
			}
			if (this.getConfig().getBoolean("log-blocked-mods")) {
				ModLogger.logMods();
			}
			log.info("Your server version is compatible with eZProtector.");
			log.info("The plugin has enabled successfully. Version: " + getDescription().getVersion());
		} else {
			log.warning("Your server version is not compatible with eZProtector.");
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}
	public void onDisable() {
		log = getLogger();
		saveDefaultConfig();
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
		log.info("Plugin disabled.");
		plugin = null;
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
		log = getLogger();
		this.getConfig().options().copyDefaults(true);
		this.saveDefaultConfig();
		log.info("Reloading config...");
	}
	public static String placeholders(String args) {
		String newString = StringEscapeUtils.unescapeJava(args.replace("%prefix%", prefix)
				.replace("%player%", player).replace("%player%", oppedPlayer).replace("%errormessage%", errorMessage)
				.replace("%command%",new StringBuilder("/").append(playerCommand).toString())
				.replace("%command%",new StringBuilder("/").append(opCommand).toString()).replaceAll("&", "§"));
		return newString;
	}
}
