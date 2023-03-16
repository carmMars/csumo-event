package me.sumo.GUI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.sumo.game.GameData;
import me.sumo.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SpectateTeleportGUI {
	
	private static SpectateTeleportGUI instance;
	public static SpectateTeleportGUI getInstance() {
	    return instance;
	}
	public SpectateTeleportGUI() {
		instance = this;
	}
	
	public File ConfigFile = new File(Main.getInstance().getDataFolder() + File.separator + "configuration.yml");
	public YamlConfiguration ConfigYaml = Main.getInstance().CM.GetLoadedYaml(ConfigFile);
	
	public void open(Player p) {
		Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("spectate-mode.spectate-inventory.inventory-name")));
		ItemStack player1 = new ItemStack(Material.SKULL_ITEM, 1 ,(short)SkullType.PLAYER.ordinal());
		SkullMeta player1Meta = (SkullMeta) player1.getItemMeta();
		player1Meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("spectate-mode.spectate-inventory.player1-item.name"))
				.replace("<player>", GameData.getInstance().fighting.get(0).getName())
				);
		List<String> player1_lore = new ArrayList<>();
		for(String s : this.ConfigYaml.getStringList("spectate-mode.spectate-inventory.player1-item.lore")) {
			player1_lore.add(ChatColor.translateAlternateColorCodes('&', s)
					.replace("<player>", GameData.getInstance().fighting.get(0).getName())
					);
		}
		player1Meta.setLore(player1_lore);
		player1Meta.setOwner(GameData.getInstance().fighting.get(0).getDisplayName());
		player1.setItemMeta(player1Meta);
		
		ItemStack player2 = new ItemStack(Material.SKULL_ITEM, 1 ,(short)SkullType.PLAYER.ordinal());
		SkullMeta player2Meta = (SkullMeta) player2.getItemMeta();
		player2Meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("spectate-mode.spectate-inventory.player2-item.name"))
				.replace("<player>", GameData.getInstance().fighting.get(1).getName())
				);
		List<String> player2_lore = new ArrayList<>();
		for(String s : this.ConfigYaml.getStringList("spectate-mode.spectate-inventory.player2-item.lore")) {
			player2_lore.add(ChatColor.translateAlternateColorCodes('&', s)
					.replace("<player>", GameData.getInstance().fighting.get(1).getName())
					);
		}
		player2Meta.setLore(player2_lore);
		player2Meta.setOwner(GameData.getInstance().fighting.get(1).getDisplayName());
		player2.setItemMeta(player2Meta);
		
		inventory.setItem(this.ConfigYaml.getInt("spectate-mode.spectate-inventory.player1-item.position"), player1);
		inventory.setItem(this.ConfigYaml.getInt("spectate-mode.spectate-inventory.player2-item.position"), player2);
				
		p.openInventory(inventory);
	}

}
