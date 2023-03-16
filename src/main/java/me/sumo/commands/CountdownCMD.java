package me.sumo.commands;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.sumo.Main;
import me.sumo.game.GameData;
import me.sumo.game.StartAndFinish;
import me.sumo.enums.GameState;

public class CountdownCMD implements CommandExecutor {
	
	public File MessageFile = new File(Main.getInstance().getDataFolder() + File.separator + "message.yml");
	public YamlConfiguration MessageYaml = Main.getInstance().CM.GetLoadedYaml(MessageFile);
	
	private StartAndFinish startfinish = new StartAndFinish();
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (!(sender instanceof Player)) {
    		return false;
    	}
    	if (!sender.hasPermission("sumo.admin")) {
    		return false;
    	}
    	
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
    	
    	return true;
    }

}
