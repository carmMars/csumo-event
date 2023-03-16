package me.sumo.game;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import me.sumo.Main;
import me.sumo.enums.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;


public class StartAndFinish {

	private ArrayList<Player> firstPlace = new ArrayList<>();
	
	private static StartAndFinish instance;
  
	public File ConfigFile = new File(Main.getInstance().getDataFolder() + File.separator + "configuration.yml");
	public YamlConfiguration ConfigYaml = Main.getInstance().CM.GetLoadedYaml(ConfigFile);
	
	public File MessageFile = new File(Main.getInstance().getDataFolder() + File.separator + "message.yml");
	public YamlConfiguration MessageYaml = Main.getInstance().CM.GetLoadedYaml(MessageFile);
  
	public static int a = 1;

	public StartAndFinish(){
		 instance = this;
	}
	
	public void startGame() {
		this.firstPlace.clear();
		GameData.getInstance().currentLevelFight.clear();
		GameData.getInstance().PlayerAlive.clear();
		GameData.getInstance().fighting.clear();
		GameData.getInstance().sumoStart = false;
		GameData.getInstance().gamestate = GameState.INGAME;

		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			this.firstPlace.add(p);
		}
		while (this.firstPlace.size() > 0) {
			int random = new Random().nextInt(this.firstPlace.size());
			GameData.getInstance().PlayerAlive.add((Player)this.firstPlace.get(random));
			this.firstPlace.remove(random);
		}

		for (Player p : GameData.getInstance().PlayerAlive) {
			GameData.getInstance().currentLevelFight.add(p);
		}

		if (GameData.getInstance().PlayerAlive.size() <= 1) {
			GameData.getInstance().gamestate = GameState.WAITING;
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&cNot enough players in game!"));
			return;
		}	
		
		getReserve();
		RoundBroadcast();
		clearInventory();
		new GameManager().teleport();
		
	}
	
	public void getReserve() {
	    GameData.getInstance().reserve = GameData.getInstance().PlayerAlive.get(GameData.getInstance().PlayerAlive.size() - 1);
	}
	
	public void RoundBroadcast() {
		GameData.getInstance().Round = GameData.getInstance().Round + 1;
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', MessageYaml.getString("round")
				.replace("<round>", String.valueOf(GameData.getInstance().Round))
				));
	}
	
	public static StartAndFinish getInstance() {
  		return instance;
  	}
  
	
	public void clearInventory() {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			p.getInventory().clear();
		}
			
	}
}
