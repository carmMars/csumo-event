package me.sumo.listeners;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import me.sumo.Main;
import me.sumo.game.GameData;
import me.sumo.enums.GameState;


public class motd implements Listener {
	
	public File ConfigFile = new File(Main.getInstance().getDataFolder() + File.separator + "configuration.yml");
	public YamlConfiguration ConfigYaml = Main.getInstance().CM.GetLoadedYaml(ConfigFile);
	
	@EventHandler
	public void Motd(ServerListPingEvent e) {
		if (GameData.getInstance().gamestate == GameState.WAITING) {
			e.setMotd(ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("motd.waiting-for-players")));
		} else if (GameData.getInstance().gamestate == GameState.COUNTING) {
			e.setMotd(ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("motd.counting-down")));
		} else if (GameData.getInstance().gamestate == GameState.INGAME) {
			e.setMotd(ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("motd.in-game")));
		} else if (GameData.getInstance().gamestate == GameState.FINISH) {
			e.setMotd(ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("motd.game-finished")));
		}
	}
}
