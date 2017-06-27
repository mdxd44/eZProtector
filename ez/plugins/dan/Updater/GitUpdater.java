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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONException;
import org.json.JSONObject;

import ez.plugins.dan.Main;
import ez.plugins.dan.Updater.Results.UpdateResult;

@SuppressWarnings("all")
public class GitUpdater {
	public static boolean updateAvailable = false;
	private final String HOST = "https://api.github.com/repos/dvargas135/eZProtector/releases/latest";
   
	private String version;
	private String oldVersion;
   
	private UpdateResult result = UpdateResult.DISABLED;
   
	private HttpURLConnection connection;
	
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
		  sb.append((char) cp);
		}
		return sb.toString();
	  }

	  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
		  BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		  String jsonText = readAll(rd);
		  JSONObject json = new JSONObject(jsonText);
		  return json;
		} finally {
		  is.close();
		}
	  }
   
	public GitUpdater(JavaPlugin plugin, boolean disabled) {
		plugin = Main.getJavaPlugin();
		oldVersion = Main.getPlugin().getDescription().getVersion();
	   
		if (disabled) {
			result = UpdateResult.DISABLED;
			return;
		}

		try {
			connection = (HttpURLConnection) new URL(HOST).openConnection();
		} catch (IOException e) {
			result = UpdateResult.FAIL_GITHUB;
			return;
		}
		run();
	}

	private void run() {
		String version = null;
		try {
			JSONObject json = GitUpdater.readJsonFromUrl(HOST);
			version = json.get("tag_name").toString();
		} catch (IOException e) {
			result = UpdateResult.FAIL_GITHUB;
		} catch (JSONException e) {
			result = UpdateResult.FAIL_GITHUB;
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
		return result;
	}
   
	public String getVersion() {
		return version;
	}
}
