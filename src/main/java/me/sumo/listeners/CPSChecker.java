package me.sumo.listeners;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.sumo.Main;
import me.sumo.game.GameData;

public class CPSChecker implements Listener {
	
	public File ConfigFile = new File(Main.getInstance().getDataFolder() + File.separator + "configuration.yml");
	public YamlConfiguration ConfigYaml = Main.getInstance().CM.GetLoadedYaml(ConfigFile);
	
	@EventHandler
	public void Click(PlayerInteractEvent e){
		if (this.ConfigYaml.getBoolean("check-player-cps")) {
			if (GameData.getInstance().fighting.contains(e.getPlayer())) {
				if(e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR) {
					GameData.getInstance().CPS.replace(e.getPlayer().getUniqueId(), GameData.getInstance().CPS.get(e.getPlayer().getUniqueId()) + 1);
					new BukkitRunnable() {
						public void run() {
							GameData.getInstance().CPS.replace(e.getPlayer().getUniqueId(), GameData.getInstance().CPS.get(e.getPlayer().getUniqueId()) - 1);
						}
					}.runTaskLater(Main.getInstance(), 20L);
				}
			}
		}
	}
}
