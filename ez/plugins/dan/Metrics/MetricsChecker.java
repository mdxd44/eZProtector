package ez.plugins.dan.Metrics;

import java.io.IOException;
import java.util.logging.Logger;

import ez.plugins.dan.Main;

public class MetricsChecker {
	private static Logger log;
	
	public static void tryMetrics() {
		log = Main.getPlugin().getLogger();
		
		try {
			Metrics metrics = new Metrics(Main.getPlugin());
			metrics.start();
		} catch (IOException e) {
			log.warning("Could not start Metrics");
		}
	}
}
