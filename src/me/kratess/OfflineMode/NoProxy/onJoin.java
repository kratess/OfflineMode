package me.kratess.OfflineMode.NoProxy;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jdk.nashorn.internal.parser.JSONParser;
import me.kratess.OfflineMode.Utils.BotAttack;
import me.kratess.OfflineMode.Utils.checkMethods;

import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class onJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PreLoginEvent e) {
        if (e.isCancelled()) return;

        String ip = e.getConnection().toString().split("\\[/")[1].split(":")[0];

        if (ip.equalsIgnoreCase("127.0.0.1")) {
            String systemipaddress = "";
            try
            {
                URL url_name = new URL("http://bot.whatismyipaddress.com");

                BufferedReader sc =
                        new BufferedReader(new InputStreamReader(url_name.openStream()));

                // reads system IPAddress
                systemipaddress = sc.readLine().trim();
            }
            catch (Exception ex)
            {
                systemipaddress = "Cannot Execute Properly";
            }
            ip = systemipaddress;
        }

        String proxyCheck = checkMethods.getProxyCheck(ip);

        JsonObject json = new JsonParser().parse(proxyCheck).getAsJsonObject();

        boolean proxy = json.get(ip).getAsJsonObject().get("proxy").toString().equalsIgnoreCase("yes");
        String type = proxyCheck.contains("\"type\": \"") ? json.get(ip).getAsJsonObject().get("type").toString() : null;
        int risk = json.get(ip).getAsJsonObject().get("risk").getAsInt();
        int port = proxyCheck.contains("\"port\": \"") ? json.get(ip).getAsJsonObject().get("port").getAsInt() : -1;

        BotAttack.addBotIP(ip, proxy, type, risk, port);

        if (checkMethods.isPremiumAccount(e.getConnection().getName()) == null && BotAttack.UNDER_ATTACK) {
            e.setCancelled(true);
        }
    }
}
