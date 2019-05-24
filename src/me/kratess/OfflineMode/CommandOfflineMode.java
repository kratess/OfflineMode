package me.kratess.OfflineMode;

import me.kratess.OfflineMode.Utils.FilesManager;
import me.kratess.OfflineMode.Utils.checkMethods;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class CommandOfflineMode extends Command {

    public CommandOfflineMode() {
        super("OfflineMode", "offlinemode.admin");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                sendHelp(sender);
            } else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
                FilesManager.reloadConfig();
                FilesManager.reloadPremiumUsers();
                sender.sendMessage(fromString("OfflineMode reloaded"));
            } else if (args[0].equalsIgnoreCase("premiumlock")) {
                for (String d : FilesManager.Config.getSection("PremiumLock").getKeys()) {
                    sender.sendMessage(fromString("PremiumLock::"+d+" " + (FilesManager.Config.get("PremiumLock."+d) instanceof Boolean ?
                            (FilesManager.Config.getBoolean("PremiumLock."+d) ? "§atrue" : "§cfalse") :
                            FilesManager.Config.get("PremiumLock."+d) instanceof Integer ?
                                    FilesManager.Config.getInt("PremiumLock."+d) :
                                    "\"" + FilesManager.Config.getString("PremiumLock."+d) + "\"")));
                }
            } else {
                sendHelp(sender);
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("premiumlock")) {
                boolean found = false;
                for (String d : FilesManager.Config.getSection("PremiumLock").getKeys()) {
                    if (args[1].equalsIgnoreCase(d)) {
                        sender.sendMessage(fromString("PremiumLock::"+d+" " + (FilesManager.Config.get("PremiumLock."+d) instanceof Boolean ?
                                (FilesManager.Config.getBoolean("PremiumLock."+d) ? "§atrue" : "§cfalse") :
                                FilesManager.Config.get("PremiumLock."+d) instanceof Integer ?
                                        FilesManager.Config.getInt("PremiumLock."+d) :
                                        "\"" + FilesManager.Config.getString("PremiumLock."+d) + "\"")));
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    for (String d : FilesManager.Config.getSection("PremiumLock").getKeys()) {
                        sender.sendMessage(fromString("PremiumLock::"+d+" " + (FilesManager.Config.get("PremiumLock."+d) instanceof Boolean ?
                                (FilesManager.Config.getBoolean("PremiumLock."+d) ? "§atrue" : "§cfalse") :
                                FilesManager.Config.get("PremiumLock."+d) instanceof Integer ?
                                        FilesManager.Config.getInt("PremiumLock."+d) :
                                        "\"" + FilesManager.Config.getString("PremiumLock."+d) + "\"")));
                    }
                }
            } else {
                sendHelp(sender);
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("premiumlock")) {
                boolean found = false;
                for (String d : FilesManager.Config.getSection("PremiumLock").getKeys()) {
                    if (args[1].equalsIgnoreCase(d)) {
                        FilesManager.Config.set("PremiumLock." + d, args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false") ? Boolean.valueOf(args[2]) : args[2].matches("[0-9]+") ? Integer.valueOf(args[2]) : args[2]);
                        FilesManager.saveConfig();
                        FilesManager.reloadConfig();
                        sender.sendMessage(fromString("PremiumLock::" + d + " set to " + args[2]));
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    for (String d : FilesManager.Config.getSection("PremiumLock").getKeys()) {
                        sender.sendMessage(fromString("PremiumLock::"+d+" " + (FilesManager.Config.get("PremiumLock."+d) instanceof Boolean ?
                                (FilesManager.Config.getBoolean("PremiumLock."+d) ? "§atrue" : "§cfalse") :
                                FilesManager.Config.get("PremiumLock."+d) instanceof Integer ?
                                        FilesManager.Config.getInt("PremiumLock."+d) :
                                        "\"" + FilesManager.Config.getString("PremiumLock."+d) + "\"")));
                    }
                }
            } else if (args[0].equalsIgnoreCase("premiumusers")) {
                if (args[1].equalsIgnoreCase("add")) {
                    String uuid = checkMethods.isPremiumAccount(args[2].split(":")[0]);

                    if (uuid != null) {

                        boolean found = false;
                        for (String d : FilesManager.PremiumUsers.getSection("users").getKeys()) {
                            if (d.replaceAll("-", "").equalsIgnoreCase(uuid)) {
                                sender.sendMessage(fromString("User already added"));
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            FilesManager.PremiumUsers.set("users." + stringToUUID(uuid) + ".name", args[2].split(":")[0]);
                            if (args[2].split(":").length == 2) {
                                FilesManager.PremiumUsers.set("users." + stringToUUID(uuid) + ".ip", args[2].split(":")[1]);
                            }
                            FilesManager.savePremiumUsers();
                            FilesManager.reloadPremiumUsers();
                            sender.sendMessage(fromString("User " + args[2] + " added"));
                        }

                    } else {
                        sender.sendMessage(fromString("User is not premium"));
                    }
                } else if (args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("rem")) {
                    String uuid = checkMethods.isPremiumAccount(args[2]);

                    if (uuid != null) {

                        boolean found = false;
                        for (String d : FilesManager.PremiumUsers.getSection("users").getKeys()) {
                            if (d.replaceAll("-", "").equalsIgnoreCase(uuid)) {
                                FilesManager.PremiumUsers.set("users." + d, null);
                                FilesManager.savePremiumUsers();
                                FilesManager.reloadPremiumUsers();
                                sender.sendMessage(fromString("User removed"));
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            sender.sendMessage(fromString("§cNobody found"));
                        }

                    } else {
                        sender.sendMessage(fromString("User is not premium"));
                    }
                } else if (args[1].equalsIgnoreCase("get")) {
                    String uuid = checkMethods.isPremiumAccount(args[2]);

                    if (uuid != null) {

                        boolean found = false;
                        for (String d : FilesManager.PremiumUsers.getSection("users").getKeys()) {
                            if (d.replaceAll("-", "").equalsIgnoreCase(uuid)) {
                                sender.sendMessage(fromString("§a" + d + ":"));
                                for (String dd : FilesManager.PremiumUsers.getSection("users." + d).getKeys()) {
                                    sender.sendMessage(fromString("  §a" + dd + ": §r" + FilesManager.PremiumUsers.getString("users." + d + "." + dd)));
                                }
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            sender.sendMessage(fromString("§cNobody found"));
                        }

                    } else {
                        sender.sendMessage(fromString("User is not premium"));
                    }
                } else {
                    sendHelp(sender);
                }
            } else {
                sendHelp(sender);
            }
        } else {
            sendHelp(sender);
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(fromString("§a/OfflineMode [help] §7- Help command"));
        sender.sendMessage(fromString("§a/OfflineMode reload §7- Reload command"));
        sender.sendMessage(fromString("§a/OfflineMode premiumlock [path] [value] §7- Settings for premiumlock"));
        sender.sendMessage(fromString("§a/OfflineMode premiumusers <add/rem/get> <name[:ip]> §7- Manage premium users"));
    }

    private BaseComponent[] fromString(String d) {
        return new TextComponent().fromLegacyText(d);
    }

    private String stringToUUID(String d) {
        return d.substring(0,8) + "-" + d.substring(8, 12) + "-" + d.substring(12, 16) + "-" + d.substring(16, 20) + "-" + d.substring(20);
    }
}
