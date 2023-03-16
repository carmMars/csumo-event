package me.sumo.listeners;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.sumo.Main;
import me.sumo.game.GameData;

public class ClickInventoryEvent implements Listener{
	
	public File ConfigFile = new File(Main.getInstance().getDataFolder() + File.separator + "configuration.yml");
	public YamlConfiguration ConfigYaml = Main.getInstance().CM.GetLoadedYaml(ConfigFile);
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
	    Player p = (Player) event.getWhoClicked();
	    if ((p.getOpenInventory().getTopInventory() != null) && (p.getOpenInventory().getTopInventory().getName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("spectate-mode.spectate-inventory.inventory-name"))))) {
	    	if (event.getCurrentItem() != null) {
	    		event.setCancelled(true);
	    	}
	    	if (event.getSlot() == 11) {
	    		Location loc1 = GameData.getInstance().fighting.get(0).getLocation();
	    		p.teleport(loc1);
	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("spectate-mode.teleport-success"))
	    				.replace("<player>", GameData.getInstance().fighting.get(0).getName())
	    				);
	    	}
	    	if (event.getSlot() == this.ConfigYaml.getInt("spectate-mode.spectate-inventory.player2-item.position")) {
	    		Location loc2 = GameData.getInstance().fighting.get(1).getLocation();
	    		p.teleport(loc2);
	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("spectate-mode.teleport-success"))
	    				.replace("<player>", GameData.getInstance().fighting.get(1).getName())
	    				);
	    	}
	    }
	}
}
