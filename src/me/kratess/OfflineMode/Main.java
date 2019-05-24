package me.kratess.OfflineMode;

import me.kratess.OfflineMode.PremiumLock.onJoin;
import me.kratess.OfflineMode.Utils.FilesManager;
import me.kratess.OfflineMode.Utils.Metrics;
import me.kratess.OfflineMode.Utils.SpigotChecker;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {

    public static Plugin instance;

    @Override
    public void onEnable() {
        instance = this;

        switch (new SpigotChecker().checkForUpdates()) {
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
        getProxy().getPluginManager().registerCommand(this, new CommandOfflineMode());
    }

}
