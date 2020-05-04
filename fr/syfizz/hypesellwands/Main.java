package fr.syfizz.hypesellwands;

import fr.syfizz.hypesellwands.commands.SellWandsCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public void onEnable(){
        saveDefaultConfig();
        System.out.println(this.getConfig().getString("messages.pluginEnabled").replaceAll("&","§"));
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getCommand("sellwands").setExecutor(new SellWandsCommand(this));
    }
    public void onDisable(){
        System.out.println(getConfig().getString("messages.pluginDisabled").replaceAll("&","§"));
    }

}
