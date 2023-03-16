package me.sumo.api;

import org.bukkit.entity.Player;

import me.sumo.game.GameData;

public class SumoEventAPI {
	
	public static Integer getPlayerKills(Player p){
		return GameData.getInstance().kills.get(p.getUniqueId()); 
	}

	public static Integer getPlayerDeaths(Player p){
		return GameData.getInstance().deaths.get(p.getUniqueId()); 
	}
	
	public static Integer getPlayerWins(Player p){
		return GameData.getInstance().wins.get(p.getUniqueId()); 
	}
	
	public static Integer getRound(){
		return GameData.getInstance().Round; 
	}
	
	public static Boolean isWhitelisted(){
		return GameData.getInstance().whitelist; 
	}
	
	public static Boolean inSpectating(Player p){
		return GameData.getInstance().inSpectate.contains(p); 
	}
	
	public static Boolean inBuildMode(Player p){
		return GameData.getInstance().BuildMode.contains(p); 
	}
	
}
