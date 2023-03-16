package me.sumo.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import me.sumo.game.GameData;

public class NoDamage implements Listener {
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (!GameData.getInstance().fighting.contains(e.getEntity())) {
			e.setCancelled(true);
		}
		
		     
		e.setDamage(0);	
	}
	
	@EventHandler
	public void onDamageOthers(EntityDamageByEntityEvent e) {
		if (GameData.getInstance().inSpectate.contains(e.getDamager())) {
			e.setCancelled(true);
		}
	}

}
