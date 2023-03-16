package me.sumo.GUI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.sumo.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.sumo.game.GameData;

public class StatsGUI {
	
	private static StatsGUI instance;
	public static StatsGUI getInstance() {
	    return instance;
	}
	public StatsGUI() {
		instance = this;
	}
	
	public File ConfigFile = new File(Main.getInstance().getDataFolder() + File.separator + "configuration.yml");
	public YamlConfiguration ConfigYaml = Main.getInstance().CM.GetLoadedYaml(ConfigFile);
	
	public void open(Player p) {
		Inventory inventory = Bukkit.createInventory(null, this.ConfigYaml.getInt("stats-gui.inventory-size"), ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("stats-gui.inventory-name")));
		
		//wins
		ItemStack wins = new ItemStack(Material.SKULL_ITEM, 1 ,(short)SkullType.PLAYER.ordinal());
		SkullMeta winsMeta = (SkullMeta) wins.getItemMeta();
		winsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("stats-gui.inventory.wins.name")));
		List<String> wins_lore = new ArrayList<>();
		for(String s : this.ConfigYaml.getStringList("stats-gui.inventory.wins.lore")) {
			wins_lore.add(ChatColor.translateAlternateColorCodes('&', s)
					.replace("<wins>", String.valueOf(GameData.getInstance().wins.get(p.getUniqueId())))
					);
		}
		winsMeta.setLore(wins_lore);
		winsMeta.setOwner(p.getName());
		wins.setItemMeta(winsMeta);
		
		//kills
		ItemStack kills = new ItemStack(Material.DIAMOND_SWORD, 1);
		ItemMeta killsMeta = kills.getItemMeta();
		killsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("stats-gui.inventory.kills.name")));
		List<String> kills_lore = new ArrayList<>();
		for(String s : this.ConfigYaml.getStringList("stats-gui.inventory.kills.lore")) {
			kills_lore.add(ChatColor.translateAlternateColorCodes('&', s)
					.replace("<kills>", String.valueOf(GameData.getInstance().kills.get(p.getUniqueId())))
					);
		}
		killsMeta.setLore(kills_lore);
		kills.setItemMeta(killsMeta);
		
		//deaths
		ItemStack deaths = new ItemStack(Material.REDSTONE, 1);
		ItemMeta deathsMeta = deaths.getItemMeta();
		deathsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("stats-gui.inventory.deaths.name")));
		List<String> deaths_lore = new ArrayList<>();
		for(String s : this.ConfigYaml.getStringList("stats-gui.inventory.deaths.lore")) {
			deaths_lore.add(ChatColor.translateAlternateColorCodes('&', s)
					.replace("<deaths>", String.valueOf(GameData.getInstance().deaths.get(p.getUniqueId())))
					);
		}
		deathsMeta.setLore(deaths_lore);
		deaths.setItemMeta(deathsMeta);
		
		
		inventory.setItem(this.ConfigYaml.getInt("stats-gui.inventory.wins.position"), wins);
		inventory.setItem(this.ConfigYaml.getInt("stats-gui.inventory.kills.position"), kills);
		inventory.setItem(this.ConfigYaml.getInt("stats-gui.inventory.deaths.position"), deaths);	

		p.openInventory(inventory);
	}

}
