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

package com.github.donotspampls.ezprotector.Updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.donotspampls.ezprotector.Main;

public class SpigotUpdater {

	public static boolean updateAvailable = false;
	private final String API_KEY = "98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4";
	private final String REQUEST_METHOD = "POST";
	private String RESOURCE_ID = "";
	private final String HOST = "http://www.spigotmc.org";
	private final String QUERY = "/api/general.php";
	private String WRITE_STRING;
   
	private String version;
	private String oldVersion;
   
	private UpdateResult result = UpdateResult.DISABLED;
   
	private HttpURLConnection connection;
   
	public SpigotUpdater(JavaPlugin plugin, Integer resourceId, boolean disabled) {
		RESOURCE_ID = resourceId + "";
		plugin = Main.getJavaPlugin();
		oldVersion = Main.getPlugin().getDescription().getVersion();
	   
		if (disabled) {
			result = UpdateResult.DISABLED;
			return;
		}

		try {
			connection = (HttpURLConnection) new URL(HOST + QUERY).openConnection();
		} catch (IOException e) {
			result = UpdateResult.FAIL_SPIGOT;
			return;
		}

		WRITE_STRING = "key=" + API_KEY + "&resource=" + RESOURCE_ID;
		run();
	}

	private void run() {
		connection.setDoOutput(true);
		try {
			connection.setRequestMethod(REQUEST_METHOD);
			connection.getOutputStream().write(WRITE_STRING.getBytes("UTF-8"));
		} catch (ProtocolException e1) {
			result = UpdateResult.FAIL_SPIGOT;
		} catch (UnsupportedEncodingException e) {
			result = UpdateResult.FAIL_SPIGOT;
		} catch (IOException e) {
			result = UpdateResult.FAIL_SPIGOT;
		}
		String version;
		try {
			version = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
		} catch (Exception e) {
			result = UpdateResult.BAD_RESOURCEID;
			return;
		}
		if (version.length() <= 7) {
			this.version = version;
			version.replace("[^A-Za-z]", "").replace("|", "");
			versionCheck();
			return;
		}
		result = UpdateResult.BAD_RESOURCEID;
	}
   
	private void versionCheck() {
		if(shouldUpdate(oldVersion, version)) {
			result = UpdateResult.UPDATE_AVAILABLE;
		} else {
			result = UpdateResult.NO_UPDATE;
		}
	}

	public boolean shouldUpdate(String localVersion, String remoteVersion) {
		return !localVersion.equalsIgnoreCase(remoteVersion);
	}

    public UpdateResult getResult() {
        return this.result;
    }

	public String getVersion() {
		return version;
	}

	public enum UpdateResult {
        NO_UPDATE,
        DISABLED,
        FAIL_SPIGOT,
        FAIL_NOVERSION,
        BAD_RESOURCEID,
        UPDATE_AVAILABLE;
    }

}
