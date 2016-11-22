/*
Copyright (c) 2016 dvargas135

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package ez.plugins.dan;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
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
	private static Logger log;
	public static final String ZIG = "5zig_Set";
	public static final String BSPRINT = "BSprint";
	public static final String BSM = "BSM";
	public static final String WDLINIT = "WDL|INIT";
	public static final String WDLCONTROL = "WDL|CONTROL";
	public static final String MCBRAND = "MC|Brand";
	public static final String WDLREQ = "WDL|REQUEST";
	public static final String SCHEMATICA = "schematica";
	public static ArrayList<String> plugins = new ArrayList<String>();
	boolean setup = new Setup().setupEZP();
	public static Plugin plugin;
	private static JavaPlugin JavaPlugin;
	private IPluginMessageListener pluginMessageListener = new IPluginMessageListener();
	private List<String> blocked = new ArrayList<String>();
	public static String prefix;
	public static String player = "";
	public static String oppedPlayer = "";
	public static String playerCommand = "";
	public static String errorMessage = "";
	public static String opCommand = "";
	
	public void onEnable() {
		plugin = this;
		log = getLogger();
		prefix = plugin.getConfig().getString("prefix");
		
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
			getServer().getMessenger().registerOutgoingPluginChannel(this, ZIG);
			getServer().getMessenger().registerOutgoingPluginChannel(this, WDLCONTROL);
			getServer().getMessenger().registerOutgoingPluginChannel(this, BSM);
			getServer().getMessenger().registerOutgoingPluginChannel(this, SCHEMATICA);
			
			registerEvents(this, new IPlayerCommandPreprocessEvent(this), new IPlayerJoinEvent(this), new IPlayerLoginEvent(), new ISignChangeEvent(this));
			
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
			
			if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
				setupProtocolLibHooks(this.blocked);
			} else {
				log.warning("ProtocolLib not found.");
				log.warning("The plugin needs ProtocolLib to work.");
				log.warning("Disabling.");
				Bukkit.getPluginManager().disablePlugin(this);
			}
			for (String s : this.getConfig().getString("custom-plugins.plugins").split(", ")) {
				Main.plugins.add(s);
			}
			if (this.getConfig().getBoolean("log-blocked-mods")) {
				ModLogger.logMods();
			}
			log.info("Your server version is compatible with eZProtector.");
			log.info("Setting up...");
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
		FileConfiguration cfg = this.getConfig();
		cfg.options().copyDefaults(true);
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
