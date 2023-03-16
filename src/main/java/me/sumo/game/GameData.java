package me.sumo.game;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.sumo.Main;
import me.sumo.enums.GameState;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GameData {

	public static GameData instance;
	
	public File ConfigFile = new File(Main.getInstance().getDataFolder() + File.separator + "configuration.yml");
	public YamlConfiguration ConfigYaml = Main.getInstance().CM.GetLoadedYaml(ConfigFile);
	
	public File MessageFile = new File(Main.getInstance().getDataFolder() + File.separator + "message.yml");
	public YamlConfiguration MessageYaml = Main.getInstance().CM.GetLoadedYaml(MessageFile);
	
	public String Winner;
	
	public int countTime;
	public int Round;
	public int max_player;
	public int restart;
	
	public List<Player> Players = new ArrayList<>();
	public List<Player> BuildMode = new ArrayList<>();
	public ArrayList<Player> PlayerAlive = new ArrayList<>();
	public ArrayList<Player> currentLevelFight = new ArrayList<>();
	public ArrayList<Player> fighting = new ArrayList<>();
	public ArrayList<Player> spectateClickItemCooldown = new ArrayList<>();
	public ArrayList<Player> inSpectate = new ArrayList<>();
	public HashMap<UUID, Integer> CPS = new HashMap<>();
	public HashMap<UUID, Integer> wins = new HashMap<>();
	public HashMap<UUID, Integer> kills = new HashMap<>();
	public HashMap<UUID, Integer> deaths = new HashMap<>();
	
	public Player reserve;
	
	public GameState gamestate;
	
	public boolean whitelist;
	public boolean sumoStart = false;
	
	//leave item
	public ItemStack leave = new ItemStack(Material.BED, 1);

	public GameData() {
		instance = this;
		
		Winner = null;
		
		countTime = this.ConfigYaml.getInt("countdown");
		Round = 0;
		max_player = this.ConfigYaml.getInt("max-players");
		restart = this.ConfigYaml.getInt("restart-second");
		
		gamestate = GameState.WAITING;
		
		whitelist = this.ConfigYaml.getBoolean("whitelist");
		
		//leave item
		ItemMeta leaveMeta = leave.getItemMeta();
		leaveMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("bungeecord-mode.leave-item.name")));
		leave.setItemMeta(leaveMeta);
	}
	
	public static GameData getInstance() {
  		return instance;
  	}
	
}
