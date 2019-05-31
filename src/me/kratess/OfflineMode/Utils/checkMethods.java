package me.kratess.OfflineMode.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class checkMethods {

    public static String isPremiumAccount(String name) {
        try {
            String url = "https://api.mojang.com/users/profiles/minecraft/" + name;

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            if (responseCode == 204 || responseCode == 404) {
                return null;
            } else {
                return response.toString().split("id\":\"")[1].split("\",")[0];
            }
        } catch (IOException ex) {
            return null;
        }
    }

    public static String getProxyCheck(String ip) {
        try {
            String url = "https://proxycheck.io/v2/"+ip+"?asn=1&node=1&time=1&inf=0&risk=1&port=1";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            if (responseCode == 204 || responseCode == 404) {
                return null;
            } else {
                return response.toString();
            }
        } catch (IOException ex) {
            return null;
        }
    }

}
