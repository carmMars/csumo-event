package me.sumo.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import me.sumo.game.GameData;

public class NoBlockBreakAndPlaceListener implements Listener {
	
	@EventHandler
	public void noBlockBreak(BlockBreakEvent e) {
		if (!GameData.getInstance().BuildMode.contains(e.getPlayer())) {
			e.setCancelled(true);
		}	
	}
	
	@EventHandler
	public void noBlockPlace(BlockPlaceEvent e) {
		if (!GameData.getInstance().BuildMode.contains(e.getPlayer())) {
			e.setCancelled(true);
		}	
	}
}
