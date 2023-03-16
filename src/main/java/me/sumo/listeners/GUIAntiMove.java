package me.sumo.listeners;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.sumo.Main;

public class GUIAntiMove implements Listener {
	
	public File ConfigFile = new File(Main.getInstance().getDataFolder() + File.separator + "configuration.yml");
	public YamlConfiguration ConfigYaml = Main.getInstance().CM.GetLoadedYaml(ConfigFile);
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
	    Player p = (Player) event.getWhoClicked();
	    if ((p.getOpenInventory().getTopInventory() != null) && (p.getOpenInventory().getTopInventory().getName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("stats-gui.inventory-name"))))) {
	    	if (event.getCurrentItem() != null) {
	    	  event.setCancelled(true);
	    	}
	    }
	    if ((p.getOpenInventory().getTopInventory() != null) && (p.getOpenInventory().getTopInventory().getName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("spectate-mode.spectate-inventory.inventory-name"))))) {
	    	if (event.getCurrentItem() != null) {
	    	  event.setCancelled(true);
	    	}
	    }
	}

}
