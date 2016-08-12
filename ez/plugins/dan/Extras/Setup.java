package ez.plugins.dan.Extras;

import java.util.logging.Logger;

import org.bukkit.Bukkit;

import ez.plugins.dan.Compatibility.Compatibility;
import ez.plugins.dan.Compatibility.R1_10_R1;
import ez.plugins.dan.Compatibility.R1_7_R1;
import ez.plugins.dan.Compatibility.R1_7_R2;
import ez.plugins.dan.Compatibility.R1_7_R3;
import ez.plugins.dan.Compatibility.R1_7_R4;
import ez.plugins.dan.Compatibility.R1_8_R1;
import ez.plugins.dan.Compatibility.R1_8_R2;
import ez.plugins.dan.Compatibility.R1_8_R3;
import ez.plugins.dan.Compatibility.R1_9_R1;
import ez.plugins.dan.Compatibility.R1_9_R2;

public class Setup {
	public static Compatibility IeZP;
    public boolean setupEZP() {

    	String version;

    	try {
    		version = Bukkit.getServer().getClass().getPackage().getName().replace(".",  ",").split(",")[3];
    	} catch (ArrayIndexOutOfBoundsException whatVersionAreYouUsingException) {
    		return false;
    	}
    	Logger.getLogger("Minecraft").info("[eZProtector] Your server is running version " + version);
    	
    	if (version.equals("v1_7_R1")) {
    		IeZP = new R1_7_R1();
    	} else if (version.equals("v1_7_R2")) {
    		IeZP = new R1_7_R2();
    	} else if (version.equals("v1_7_R3")) {
    		IeZP = new R1_7_R3();
    	} else if (version.equals("v1_7_R4")) {
    		IeZP = new R1_7_R4();
    	} else if (version.equals("v1_8_R1")) {
    		IeZP = new R1_8_R1();
    	} else if (version.equals("v1_8_R2")) {
    		IeZP = new R1_8_R2();
    	} else if (version.equals("v1_8_R3")) {
    		IeZP = new R1_8_R3();
    	} else if (version.equals("v1_9_R1")) {
    		IeZP = new R1_9_R1();
    	} else if (version.equals("v1_9_R2")) {
    		IeZP = new R1_9_R2();
    	} else if (version.equals("v1_10_R1")) {
    		IeZP = new R1_10_R1();
    	}
    	// This will return true if the server version was compatible with one of our NMS classes
    	// because if it is, our IeZP would not be null
    	return IeZP != null;
    }
}
