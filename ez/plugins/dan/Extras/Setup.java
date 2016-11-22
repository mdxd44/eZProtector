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

package ez.plugins.dan.Extras;

import java.util.logging.Logger;

import org.bukkit.Bukkit;

import ez.plugins.dan.Compatibility.Compatibility;
import ez.plugins.dan.Compatibility.R1_7_R1;
import ez.plugins.dan.Compatibility.R1_7_R2;
import ez.plugins.dan.Compatibility.R1_7_R3;
import ez.plugins.dan.Compatibility.R1_7_R4;
import ez.plugins.dan.Compatibility.R1_8_R1;
import ez.plugins.dan.Compatibility.R1_8_R2;
import ez.plugins.dan.Compatibility.R1_8_R3;
import ez.plugins.dan.Compatibility.R1_9_R1;
import ez.plugins.dan.Compatibility.R1_9_R2;
import ez.plugins.dan.Compatibility.R1_10_R1;
import ez.plugins.dan.Compatibility.R1_11_R1;

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
    	} else if (version.equals("v1_11_R1")) {
    		IeZP = new R1_11_R1();
    	}
    	// This will return true if the server version was compatible with one of our NMS classes
    	// because if it is, our IeZP would not be null
    	return IeZP != null;
    }
}
