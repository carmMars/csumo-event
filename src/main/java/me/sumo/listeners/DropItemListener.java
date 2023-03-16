package me.sumo.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropItemListener implements Listener {
	@EventHandler
	public void CancelDropItem(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}
}