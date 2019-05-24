package me.kratess.OfflineMode.Utils;

import me.kratess.OfflineMode.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SpigotChecker {

    private final int projectID = 67561;
    private URL checkURL;

    public SpigotChecker() {
        try {
            this.checkURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + projectID);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }

    public int checkForUpdates() {
        try {
            URLConnection con = checkURL.openConnection();
            String newVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if (Main.instance.getDescription().getVersion().equalsIgnoreCase(newVersion)) {
                // SAME VERSION
                return 0x01;
            } else {
                if (newVersion.contains("N")) {
                    // NECESSARY
                    return 0x02;
                } else if (newVersion.contains("S")) {
                    // SHOULD BE DOWNLOADED
                    return 0x03;
                } else {
                    // BAISC BUGS
                    return 0x04;
                }
            }
        } catch (IOException ex) {
            // NO INTERNET
            return 0x00;
        }
    }

}
