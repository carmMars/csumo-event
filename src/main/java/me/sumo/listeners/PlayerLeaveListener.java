package me.sumo.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.sumo.game.GameData;
import me.sumo.utils.ScoreHelper;

public class PlayerLeaveListener implements Listener {
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		e.setQuitMessage(null);
		if(ScoreHelper.hasScore(p)) {
            ScoreHelper.removeScore(p);
        }
		GameData.getInstance().CPS.remove(p.getUniqueId(), 0);
	}

}
