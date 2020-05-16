/*
 * eZProtector - Copyright (C) 2018-2020 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.paper.utilities;

import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

// The code in this class is copied as-is from PaperLib, all rights go to the PaperMC team
// https://github.com/PaperMC/PaperLib/blob/master/src/main/java/io/papermc/lib/PaperLib.java#L266
public class PaperLib {

    public static void suggestPaper(Plugin plugin) {
        if (isPaper()) return;

        final String benefitsProperty = "paperlib.shown-benefits";
        final String pluginName = plugin.getDescription().getName();
        final Logger logger = plugin.getLogger();
        logger.warning("====================================================");
        logger.warning(" " + pluginName + " works better if you use Paper ");
        logger.warning(" as your server software. ");
        if (System.getProperty(benefitsProperty) == null) {
            System.setProperty(benefitsProperty, "1");
            logger.warning("  ");
            logger.warning(" Paper offers significant performance improvements,");
            logger.warning(" bug fixes, security enhancements and optional");
            logger.warning(" features for server owners to enhance their server.");
            logger.warning("  ");
            logger.warning(" Paper includes Timings v2, which is significantly");
            logger.warning(" better at diagnosing lag problems over v1.");
            logger.warning("  ");
            logger.warning(" All of your plugins should still work, and the");
            logger.warning(" Paper community will gladly help you fix any issues.");
            logger.warning("  ");
            logger.warning(" Join the Paper Community @ https://papermc.io");
        }
        logger.warning("====================================================");
    }

    private static boolean isPaper() {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
