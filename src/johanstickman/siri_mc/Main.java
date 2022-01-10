package johanstickman.siri_mc;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    // Quand le plugin est activé
    @Override
    public void onEnable() {
        // Register le listener
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);

        // Dire que SiriMC est activé
        getLogger().info("SiriMC enabled! You can speak to Siri by starting your message by \"siri \" (doesn't work for CONSOLE).");
    }

    // Quand le plugin est désactivé
    @Override
    public void onDisable() {
        getLogger().info("SiriMC disabled!");
    }
}