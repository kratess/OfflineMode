package me.kratess.OfflineMode.Utils;

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
import java.util.Scanner;

public class SpigotChecker {
    private static final String USER_AGENT  = "PluginAgent";// Change this!
    private static final String REQUEST_URL = "https://www.spigotmc.org/resources/offlinemode.67561/history";

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

            Scanner s = new Scanner(inputStream).useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";

            ArrayList<String> splitteds = new ArrayList<>();
            for (String d : result.split("<table class=\"dataTable resourceHistory\">")[1].split("</table>")[0].split("<tr class=\"dataRow  \">")) {
                if (d.contains("<td class=\"version\">")) {
                    splitteds.add(d.split("<td class=\"version\">")[1].split("</td>")[0]);
                }
            }

            ArrayList<String> newest_versions = new ArrayList<>();

            String actual_version = Main.instance.getDescription().getVersion();

            for (String d : splitteds) {
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
