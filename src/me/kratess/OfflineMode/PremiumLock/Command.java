package me.kratess.OfflineMode.PremiumLock;

import me.kratess.OfflineMode.Utils.FilesManager;
import me.kratess.OfflineMode.Utils.checkMethods;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Command extends net.md_5.bungee.api.plugin.Command {

    public Command() {
        super(FilesManager.Config.getString("PremiumLock.command"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if (checkMethods.isPremiumAccount(sender.getName()) != null) {
                String uuid = checkMethods.isPremiumAccount(sender.getName());
                if (FilesManager.PremiumUsers.getString("users." + stringToUUID(uuid) + ".name") == null) {
                    FilesManager.PremiumUsers.set("users." + stringToUUID(uuid) + ".name", p.getName());
                    FilesManager.savePremiumUsers();
                    FilesManager.reloadPremiumUsers();
                    p.disconnect(fromString("You've been added to premium users. Please rejoin"));
                } else {
                    p.sendMessage(fromString("You'be been already added to premium users!"));
                }
            } else {
                sender.sendMessage(fromString("You're account is not premium!"));
            }
        } else {
            sender.sendMessage(fromString("Console cannot execute that command!"));
        }
    }

    private BaseComponent[] fromString(String d) {
        return new TextComponent().fromLegacyText(d);
    }

    private String stringToUUID(String d) {
        return d.substring(0,8) + "-" + d.substring(8, 12) + "-" + d.substring(12, 16) + "-" + d.substring(16, 20) + "-" + d.substring(20);
    }

}
