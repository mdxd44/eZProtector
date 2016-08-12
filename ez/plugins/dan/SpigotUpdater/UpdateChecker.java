package ez.plugins.dan.SpigotUpdater;

import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;

import ez.plugins.dan.Main;

public class UpdateChecker {
	private static Logger log = Logger.getLogger("Minecraft");
	private static FileConfiguration config = Main.getPlugin().getConfig();
	public static String version;
	
	public static void checkUpdate() {
    	if (config.getBoolean("updater")){
    		log.info("Checking update...");
    		final SpigotUpdater updater = new SpigotUpdater(Main.getJavaPlugin(), 12663, false);
    		final SpigotUpdater.UpdateResult result = updater.getResult();
    		switch (result) {
    			case FAIL_SPIGOT: {
    				log.info("The update manager could not contact Spigot.");
    				break;
    			}
    			case BAD_RESOURCEID: {
    				log.info("The update manager could not contact Spigot.");
    				break;
    			}
    			case NO_UPDATE: {
    				log.info("eZProtector is up to date.");
    				break;
    			}
    			case UPDATE_AVAILABLE: {
    				UpdateChecker.version = updater.getVersion();
    				log.info("--------------------------");
    				log.info("eZProtector Update Manager");
    				log.info("An update for eZProtector is available.");
    				log.info("Current: " + Main.getPlugin().getDescription().getVersion());
    				log.info("New: " + version);
    				log.info("Download it at: http://bit.ly/eZProtector");
    				log.info("--------------------------");
    				SpigotUpdater.updateAvailable = true;
    				break;
    			} default: {
    				log.info(result.toString());
    				break;
    			}
    		}
    	}
    }
}
