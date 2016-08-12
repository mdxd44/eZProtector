package ez.plugins.dan;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
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
	public static ArrayList<String> plugins = new ArrayList<String>();
    public static final String SCHEMATICA = "schematica";
    boolean setup = new Setup().setupEZP();
    public static Main instance;
    public static Plugin plugin;
    private static JavaPlugin JavaPlugin;
    private IPluginMessageListener pluginMessageListener = new IPluginMessageListener();
    private List<String> blocked = new ArrayList<String>();
    
    public void onEnable() {
    	plugin = this;
    	log = getLogger();
    	instance = this;
    	
    	if (setup == true) {
    		
    		getServer().getScheduler().runTaskLaterAsynchronously(this, new Runnable() {
    			public void run() {
    				UpdateChecker.checkUpdate();
    			}
    		}
    		, 20L);

    		loadConfig();
    		
    		getServer().getMessenger().registerIncomingPluginChannel(this, ZIG, this.pluginMessageListener);
    		getServer().getMessenger().registerIncomingPluginChannel(this, BSM, this.pluginMessageListener);
    		getServer().getMessenger().registerIncomingPluginChannel(this, WDLINIT, this.pluginMessageListener);
    		getServer().getMessenger().registerIncomingPluginChannel(this, MCBRAND,  this.pluginMessageListener);
    		getServer().getMessenger().registerIncomingPluginChannel(this, WDLREQ, this.pluginMessageListener);
    		getServer().getMessenger().registerOutgoingPluginChannel(this, ZIG);
    		getServer().getMessenger().registerOutgoingPluginChannel(this, WDLCONTROL);
    		getServer().getMessenger().registerOutgoingPluginChannel(this, BSM);
    		getServer().getMessenger().registerOutgoingPluginChannel(this, SCHEMATICA);
		  
    		registerEvents(this, new IPlayerJoinEvent(), new IPlayerLoginEvent(), new ISignChangeEvent());
    		getServer().getPluginManager().registerEvents(new IPlayerCommandPreprocessEvent(this), this);
    		
    		getCommand("ezp").setExecutor(new ICommandExecutor());
		  
    		MetricsChecker.tryMetrics();

    		if (this.getConfig().getBoolean("block-op-commands.op-bypassed-players-on-startup")) {
    			for (int iops = 0; iops < this.getConfig().getList("block-op-commands.bypassed-players").size(); iops++) {
    				String oppedPlayers = (String)this.getConfig().getList("block-op-commands.bypassed-players").get(iops);
    				Player ops = Bukkit.getPlayer(oppedPlayers);
    				ops.setOp(true);
    			}
    		}
    		
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
    	log.info("Plugin disabled.");
		getServer().getMessenger().unregisterIncomingPluginChannel(this, ZIG, this.pluginMessageListener);
		getServer().getMessenger().unregisterIncomingPluginChannel(this, BSM, this.pluginMessageListener);
		getServer().getMessenger().unregisterIncomingPluginChannel(this, WDLINIT, this.pluginMessageListener);
		getServer().getMessenger().unregisterIncomingPluginChannel(this, MCBRAND,  this.pluginMessageListener);
		getServer().getMessenger().unregisterIncomingPluginChannel(this, WDLREQ, this.pluginMessageListener);
		getServer().getMessenger().unregisterOutgoingPluginChannel(this, ZIG);
		getServer().getMessenger().unregisterOutgoingPluginChannel(this, WDLCONTROL);
		getServer().getMessenger().unregisterOutgoingPluginChannel(this, BSM);
		getServer().getMessenger().unregisterOutgoingPluginChannel(this, SCHEMATICA);
    	plugin = null;
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
    
    public static void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners) {
    	for (Listener listener : listeners) {
    		Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
    	}
    }
    private void loadConfig() {
    	log = getLogger();
    	FileConfiguration cfg = this.getConfig();
    	cfg.options().copyDefaults(true);
    	this.saveDefaultConfig();
    	log.info("Reloading config...");
    }
}
