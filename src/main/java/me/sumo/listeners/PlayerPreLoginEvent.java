package me.sumo.listeners;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import me.sumo.Main;
import me.sumo.game.GameData;
import me.sumo.enums.GameState;

public class PlayerPreLoginEvent implements Listener {
	
	public File MessageFile = new File(Main.getInstance().getDataFolder() + File.separator + "message.yml");
	public YamlConfiguration MessageYaml = Main.getInstance().CM.GetLoadedYaml(MessageFile);
	
	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerLoginEvent(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		if (GameData.getInstance().Players.size() >= GameData.getInstance().max_player && !p.hasPermission("Sumo.fullbypass")) {
			e.disallow(PlayerLoginEvent.Result.KICK_FULL, ChatColor.translateAlternateColorCodes('&', MessageYaml.getString("game-full")));
		}
		if (GameData.getInstance().gamestate == GameState.INGAME && !p.hasPermission("sumo.spectate")) {
			e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&', MessageYaml.getString("no-permissions-to-spectate")));
		}
		if (GameData.getInstance().whitelist == true && !p.hasPermission("sumo.admin")) {
			e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&', MessageYaml.getString("whitelist-is-on")));
		}
	}
}
