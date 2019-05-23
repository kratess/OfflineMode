package me.kratess.OfflineMode.PremiumLock;

import me.kratess.OfflineMode.Main;
import me.kratess.OfflineMode.Utils.FilesManager;
import me.kratess.OfflineMode.Utils.checkMethods;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import static com.google.common.net.HttpHeaders.USER_AGENT;

public class onJoin implements Listener {

    @EventHandler
    public void onJoin(PreLoginEvent e) {
        if (FilesManager.Config.getBoolean("PremiumLock.use")) {

            if (e.isCancelled()) return;

            String uuid = checkMethods.isPremiumAccount(e.getConnection().getName());

            if (FilesManager.Config.getBoolean("PremiumLock.all_users")) {
                if (uuid != null) {
                    e.getConnection().setOnlineMode(true);
                }
            } else {
                if (uuid != null) {
                    for (String d : FilesManager.PremiumUsers.getSection("users").getKeys()) {
                        if (d.replaceAll("-", "").equalsIgnoreCase(uuid)) {
                            if (FilesManager.PremiumUsers.getString("users."+d+".ip") != "") {
                                String ip = e.getConnection().toString().split("\\[/")[1].split(":")[0];
                                if (!ip.equalsIgnoreCase(FilesManager.PremiumUsers.getString("users."+d+".ip"))) {
                                    e.setCancelled(true);
                                    e.setCancelReason(new TextComponent("§4There was an error. Please contact an administrator"));
                                    return;
                                }
                            }
                            e.getConnection().setOnlineMode(true);
                            break;
                        }
                    }
                }
            }

        }
    }

    @EventHandler
    public void connectEvent(ServerConnectEvent e) {
        if (!FilesManager.Config.getString("PremiumLock.switch_server.server").isEmpty()) {

            ServerInfo server = ProxyServer.getInstance().getServerInfo(FilesManager.Config.getString("PremiumLock.switch_server.server"));
            ProxiedPlayer p = e.getPlayer();

            if (p.getPendingConnection().isOnlineMode()) {
                if (e.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY)) {
                    e.setTarget(server);
                }
            }
        }
    }

}
