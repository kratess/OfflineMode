package me.kratess.OfflineMode;

import me.kratess.OfflineMode.PremiumLock.Command;
import me.kratess.OfflineMode.PremiumLock.onJoin;
import me.kratess.OfflineMode.Utils.BotAttack;
import me.kratess.OfflineMode.Utils.FilesManager;
import me.kratess.OfflineMode.Utils.Metrics;
import me.kratess.OfflineMode.Utils.SpigotChecker;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public class Main extends Plugin {

    public static Plugin instance;

    @Override
    public void onEnable() {
        instance = this;

        SpigotChecker update = new SpigotChecker();

        if (update.getVersion_behind() >= 1) {
            System.out.println("This plugin is " + update.getVersion_behind() + " version behind latest");
        }

        switch (update.getStatus()) {
            case 0x00:
                break;
            case 0x01:
                break;
            case 0x02:
                getLogger().severe("Older plugin versions are vulnerable.");
                getLogger().severe("YOU MUST DOWNLOAD THE NEWEST VERSION!");
                getProxy().stop();
            case 0x03:
                getLogger().info("You should download the newest version");
        }

        new FilesManager();

        Metrics metrics = new Metrics(this);

        getProxy().getPluginManager().registerListener(this, new onJoin());
        getProxy().getPluginManager().registerListener(this, new me.kratess.OfflineMode.NoProxy.onJoin());
        getProxy().getPluginManager().registerCommand(this, new CommandOfflineMode());
        if (!FilesManager.Config.getString("PremiumLock.command").equalsIgnoreCase("")) {
            getProxy().getPluginManager().registerCommand(this, new Command());
        }

        getProxy().getScheduler().schedule(this, new BotAttack(), 1, 1, TimeUnit.SECONDS);

        getProxy().getScheduler().schedule(this, new CheckSpigotEveryTime(), 30, 30, TimeUnit.MINUTES);
    }

    public class CheckSpigotEveryTime implements Runnable {

        @Override
            public void run() {
            switch (new SpigotChecker().getStatus()) {
                case 0x00:
                    break;
                case 0x01:
                    break;
                case 0x02:
                    getLogger().severe("***");
                    getLogger().severe("Older plugin versions are vulnerable.");
                    getLogger().severe("YOU MUST DOWNLOAD THE NEWEST VERSION!");
                    getLogger().severe("");
                    getLogger().severe("This error could be vulnerable for your server!!!");
                    getLogger().severe("");
                    getLogger().severe("PLEASE UPDATE PLUGIN");
                    getLogger().severe("");
                    getLogger().severe("***");
                case 0x03:
                    getLogger().info("You should download the newest version");
            }
        }
    }

}
