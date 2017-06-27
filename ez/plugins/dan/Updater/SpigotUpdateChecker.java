/*
Copyright (c) 2016-2017 dvargas135

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

package ez.plugins.dan.Updater;

import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;

import ez.plugins.dan.Main;
import ez.plugins.dan.Updater.Results.UpdateResult;

public class SpigotUpdateChecker {
	private static Logger log = Logger.getLogger("Minecraft");
	private static FileConfiguration config = Main.getPlugin().getConfig();
	public static String version;
	public static String oldVersion = Main.getPlugin().getDescription().getVersion();
	
	public static void checkUpdate() {
		if (config.getBoolean("updater")) {
			log.info("Checking update...");
			final SpigotUpdater updater = new SpigotUpdater(Main.getJavaPlugin(), 12663, false);
			final UpdateResult result = updater.getResult();
			switch (result) {
				case FAIL_SPIGOT: {
					log.info("[eZProtector] The update manager could not contact Spigot.");
					break;
				}
				case BAD_RESOURCEID: {
					log.info("[eZProtector] The update manager could not contact Spigot.");
					break;
				}
				case NO_UPDATE: {
					log.info("[eZProtector] The plugin is up to date.");
					break;
				}
				case UPDATE_AVAILABLE: {
					SpigotUpdateChecker.version = updater.getVersion();
					log.info("--------------------------");
					log.info("eZProtector Update Manager");
					log.info("An update for eZProtector is available.");
					log.info("Current: " + oldVersion);
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
