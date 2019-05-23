package me.kratess.OfflineMode.Utils;

import me.kratess.OfflineMode.Main;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class FilesManager {

    public FilesManager() {
        Directory();
        Config();
        premiumUsers();
    }

    private void Directory() {
        if (!(new File(Main.instance.getDataFolder(), "")).exists()) {
            (new File(Main.instance.getDataFolder(), "")).mkdir();
        }
    }

    public static Configuration Config;
    public static File File_Config = new File(Main.instance.getDataFolder(), "config.yml");

    private void Config() {
        try {
            if (!File_Config.exists()) {
                InputStream in = Main.instance.getResourceAsStream("config.yml");
                Files.copy(in, File_Config.toPath());
            }

            Config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(File_Config);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(FilesManager.Config, new File(Main.instance.getDataFolder(), "config.yml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void reloadConfig() {
        try {
            Config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(File_Config);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Configuration PremiumUsers;
    public static File File_PremiumUsers = new File(Main.instance.getDataFolder(), "premium_users.yml");

    private void premiumUsers() {
        try {
            if (!File_PremiumUsers.exists()) {
                InputStream in = Main.instance.getResourceAsStream("premium_users.yml");
                Files.copy(in, File_PremiumUsers.toPath());
            }

            PremiumUsers = ConfigurationProvider.getProvider(YamlConfiguration.class).load(File_PremiumUsers);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void savePremiumUsers() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(FilesManager.PremiumUsers, new File(Main.instance.getDataFolder(), "premium_users.yml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void reloadPremiumUsers() {
        try {
            PremiumUsers = ConfigurationProvider.getProvider(YamlConfiguration.class).load(File_PremiumUsers);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}