package me.kratess.OfflineMode.Utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.kratess.OfflineMode.Main;
import sun.misc.IOUtils;
import sun.nio.ch.IOUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class SpigotChecker {
    private static final String USER_AGENT  = "PluginAgent";
    private static final String REQUEST_URL = "https://api.spiget.org/v2/resources/67561/versions?size=100";

    private int version_behind = -1;
    // SAME VERSION
    private int status = 0x01;

    public SpigotChecker() {
        try {
            URL url = new URL(REQUEST_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", USER_AGENT);

            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);

            ArrayList<String> splitteds = new ArrayList<>();

            JsonElement element = new JsonParser().parse(reader);

            for (JsonElement js : element.getAsJsonArray()) {
                splitteds.add(js.getAsJsonObject().get("name").getAsString());
            }

            Collections.reverse(splitteds);

            ArrayList<String> newest_versions = new ArrayList<>();

            String actual_version = Main.instance.getDescription().getVersion();

            for (String d : splitteds) {
                d = d.split(" ")[0];
                if (d.equalsIgnoreCase(actual_version)) {
                    break;
                } else {
                    newest_versions.add(d);
                    if (d.contains("N")) {
                        // NECESSARY
                        status = 0x02;
                    } else if (d.contains("S") && status == 0x01) {
                        // SHOULD BE DOWNLOADED
                        status = 0x03;
                    } else if (status == 0x01) {
                        // BAISC BUGS
                        status = 0x04;
                    }
                }
            }

            version_behind = newest_versions.size();
        } catch (IOException e) {
            // NO INTERNET
            status = 0x00;
            e.printStackTrace();
        }
    }

    public int getVersion_behind() {
        return version_behind;
    }

    public int getStatus() {
        return status;
    }
}
