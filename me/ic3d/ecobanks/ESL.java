package me.ic3d.ecobanks;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;

import me.ic3d.eco.*;

public class ESL extends ServerListener {
    private EcoBanks plugin;

    public ESL(EcoBanks plugin) {
        this.plugin = plugin;
    }
    
    //All of the below can be copy + pasted at will! No need to explain this, but:
    @Override
    public void onPluginDisable(PluginDisableEvent event) {
    	//When the plugin is enabled and 3co isn't null, and the plugin that is disabled is 3co, say that we are releasing it
        if (plugin.eco != null) {
            if (event.getPlugin().getDescription().getName().equals("3co")) {
                plugin.eco = null;
                System.out.println("[3coShop] 3co released!");
            }
        }
    }

    @Override
    public void onPluginEnable(PluginEnableEvent event) {
    	//if 3co is null, get the plugin 3co and if that isn't null let the log know about it, and set the plugin.eco variable to the plugin 3co
        if (plugin.eco == null) {
            Plugin eco = plugin.getServer().getPluginManager().getPlugin("3co");

            if (eco != null) {
                if (eco.isEnabled() && eco.getClass().getName().equals("me.ic3d.eco.ECO")) {
                    plugin.eco = (ECO)eco;
                    System.out.println("[3coShop] 3co found, hooking in!");
                }
            }
        }
    }
}