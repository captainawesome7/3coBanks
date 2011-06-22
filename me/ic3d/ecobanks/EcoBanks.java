package me.ic3d.ecobanks;
//Java Imports
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import me.ic3d.eco.ECO;
import me.ic3d.eco.ECOP;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class EcoBanks extends JavaPlugin {
	//Use this to find out if the server uses permissions
	private boolean UsePermissions;
	public static PermissionHandler Permissions;
	private void setupPermissions() {
	    Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");
	    if (this.Permissions == null) {
	        if (test != null) {
	            UsePermissions = true;
	            this.Permissions = ((Permissions) test).getHandler();
	            System.out.println("[3coBanks] Version 1.0 Permissions system detected!");
	        } else {
	            log.info("[3coBanks] Version 1.0 Permissions system not detected, defaulting to OP");
	            UsePermissions = false;
	        }
	    }
	}
	

	
	//A convenience method of accessing permissions
    public boolean hasPerm(Player p, String string) {
        if (UsePermissions) {
            return this.Permissions.has(p, "3co.banks." + string);
        }
        return p.isOp();
    }
    
    //This is the main thing right here! You need to put this in your plugin's main class
    //In the server listener that you create it will set this to the 3co plugin
    public ECO eco;
	
	//Get the logger (console)
	private static final Logger log = Logger.getLogger("Minecraft");
	
	//Set the server listener to hook into 3co
	private final ESL serverlistener = new ESL(this);
	 private void setupDatabase() {
	        try {
	            getDatabase().find(EBP.class).findRowCount();
	        } catch (PersistenceException ex) {
	            System.out.println("Installing database for " + getDescription().getName() + " due to first time usage");
	            installDDL();
	        }
	    }
	    @Override
	    public List<Class<?>> getDatabaseClasses() {
	        List<Class<?>> list = new ArrayList<Class<?>>();
	        list.add(EBP.class);
	        return list;
	    }

	//When the plugin is enabled:
	public void onEnable() {
		//Get the plugin manager and register events to the listener

		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Type.PLUGIN_ENABLE, serverlistener, Priority.Monitor, this);
		pm.registerEvent(Type.PLUGIN_DISABLE, serverlistener, Priority.Monitor, this);
		//Setup Permissions 
	    setupPermissions();
	    setupDatabase();
		//Get the command for buying items
		this.getCommand("eb").setExecutor(new EC(this));
		//Log some info
		log.info("[3coBanks] Version " + this.getDescription().getVersion() + " by IC3D enabled");
	}
	//When the plugin is disabled:
	public void onDisable() {
		//Log some info
		log.info("[3coBanks] Version " + this.getDescription().getVersion() + " by IC3D disabled");
	}
	//Banks API Methods
	public Integer getSavings(Player player) {
		EBP pClass = getDatabase().find(EBP.class).where().ieq("PlayerName", player.getName()).findUnique();
		if (pClass== null) {
			pClass = new EBP ();
			pClass.setPlayerName(player.getName());
			pClass.setSavings(0);
		}
		return pClass.getSavings();
	}
	public void depositMoney(Player player, Integer amount) {
		EBP pClass = getDatabase().find(EBP.class).where().ieq("PlayerName", player.getName()).findUnique();
		if (pClass== null) {
			pClass = new EBP ();
			pClass.setPlayerName(player.getName());
			pClass.setSavings(0);
		}
		Integer oldMoney = pClass.getSavings();
		Integer newMoney = oldMoney + amount;
		pClass.setSavings(newMoney);
		this.getDatabase().save(pClass);
	}
	public void withdrawMoney(Player player, Integer amount) {
		EBP pClass = getDatabase().find(EBP.class).where().ieq("PlayerName", player.getName()).findUnique();
		if (pClass== null) {
			pClass = new EBP ();
			pClass.setPlayerName(player.getName());
			pClass.setSavings(0);
		}
		Integer oldMoney = pClass.getSavings();
		Integer newMoney = oldMoney - amount;
		pClass.setSavings(newMoney);
		this.getDatabase().save(pClass);
	}
	public Boolean hasSaved(Player player, Integer amount) {
		EBP pClass = getDatabase().find(EBP.class).where().ieq("PlayerName", player.getName()).findUnique();
		if (pClass== null) {
			pClass = new EBP ();
			pClass.setPlayerName(player.getName());
			pClass.setSavings(0);
		}
		Integer savings = pClass.getSavings();
		if(amount > savings) {
			return false;
		} else {
			return true;
		}
	}
}