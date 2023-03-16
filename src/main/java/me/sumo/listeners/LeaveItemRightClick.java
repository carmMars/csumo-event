package me.sumo.listeners;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.sumo.Main;
import me.sumo.bungeecord.BungeeListener;
import me.sumo.game.GameData;

public class LeaveItemRightClick implements Listener {
	
	public File ConfigFile = new File(Main.getInstance().getDataFolder() + File.separator + "configuration.yml");
	public YamlConfiguration ConfigYaml = Main.getInstance().CM.GetLoadedYaml(ConfigFile);
	
	private BungeeListener BL = new BungeeListener();
	
	@EventHandler
	public void leave(PlayerInteractEvent event) {
	    Player p = event.getPlayer();
	    if (p.getItemInHand().equals(GameData.getInstance().leave) && event.getAction().equals(Action.RIGHT_CLICK_AIR) || p.getItemInHand().equals(GameData.getInstance().leave) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
	    	BL.connectToServer(p, this.ConfigYaml.getString("bungeecord-mode.lobby-server"));
	    }
	}

}
