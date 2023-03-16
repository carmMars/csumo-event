package me.sumo.listeners;

import me.sumo.game.StartAndFinish;
import me.sumo.Main;
import me.sumo.enums.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.sumo.game.GameData;
import me.sumo.scoreboard.Scoreboard;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class PlayerJoinListener implements Listener {
	
	private static PlayerJoinListener instance;
	private StartAndFinish startfinish = new StartAndFinish();
	public File MessageFile = new File(Main.getInstance().getDataFolder() + File.separator + "message.yml");
	public YamlConfiguration MessageYaml = Main.getInstance().CM.GetLoadedYaml(MessageFile);
	public static PlayerJoinListener getInstance() {
  		return instance;
  	}
	@EventHandler
	public void PlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		p.setFoodLevel(20);
		p.setGameMode(GameMode.ADVENTURE);
		e.setJoinMessage(null);
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
		Scoreboard.scoreboard(p);
		GameData.getInstance().CPS.put(p.getUniqueId(), 0);
		if(Bukkit.getOnlinePlayers().size() == GameData.getInstance().max_player) {
			GameData.getInstance().gamestate = GameState.COUNTING;

			new BukkitRunnable() {
				public void run() {
					if (GameData.getInstance().countTime == 0) {
						startfinish.startGame();
						cancel();
					} else if (GameData.getInstance().countTime == 1 ||
							GameData.getInstance().countTime == 2 ||
							GameData.getInstance().countTime == 3 ||
							GameData.getInstance().countTime == 4 ||
							GameData.getInstance().countTime == 5 ||
							GameData.getInstance().countTime == 10 ||
							GameData.getInstance().countTime == 30 ||
							GameData.getInstance().countTime == 60 ||
							GameData.getInstance().countTime == 120 ||
							GameData.getInstance().countTime == 180 ||
							GameData.getInstance().countTime == 240 ||
							GameData.getInstance().countTime == 300 ||
							GameData.getInstance().countTime == 600) {
						Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', MessageYaml.getString("countdown")
								.replace("<countdown>", String.valueOf(GameData.getInstance().countTime))
						));
					}
					GameData.getInstance().countTime -= 1;
				}
			}.runTaskTimer(Main.getInstance(), 20L, 20L);
		}
	}
}
