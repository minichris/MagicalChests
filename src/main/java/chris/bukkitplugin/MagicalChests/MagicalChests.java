package chris.bukkitplugin.MagicalChests;

import org.bukkit.plugin.java.JavaPlugin;

public final class MagicalChests extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("MagicalChests Enabled!");
        getServer().getPluginManager().registerEvents(new PlayerClickChestListener(), this);
    }
}