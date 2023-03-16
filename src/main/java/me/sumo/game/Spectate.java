package me.sumo.game;

import java.io.File;

import me.sumo.GUI.SpectateTeleportGUI;
import me.sumo.Main;
import me.sumo.enums.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Spectate implements Listener{
	
	public File ConfigFile = new File(Main.getInstance().getDataFolder() + File.separator + "configuration.yml");
	public YamlConfiguration ConfigYaml = Main.getInstance().CM.GetLoadedYaml(ConfigFile);
	
	public File LocationFile = new File(Main.getInstance().getDataFolder() + File.separator + "location.yml");
	public YamlConfiguration LocationYaml = Main.getInstance().CM.GetLoadedYaml(LocationFile);
	
	public void giveSpectateItem(Player p) {
		ItemStack spectate = new ItemStack(Material.ENDER_PEARL, 1);
		ItemMeta spectateMeta = spectate.getItemMeta();
		spectateMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("spectate-mode.join-spectate-enderpearl-name"))));
		spectate.setItemMeta(spectateMeta);
		
		//setItem in inventory
		p.getInventory().setItem(4, spectate);
	}
	
	public void joinSpectateMode(Player p) {
		ItemStack leaveSpectate = new ItemStack(Material.EYE_OF_ENDER, 1);
		ItemMeta leaveSpectateMeta = leaveSpectate.getItemMeta();
		leaveSpectateMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("spectate-mode.leave-spectate-ender-eye-name"))));
		leaveSpectate.setItemMeta(leaveSpectateMeta);
		
		ItemStack teleport = new ItemStack(Material.SLIME_BALL, 1);
		ItemMeta teleportMeta = teleport.getItemMeta();
		teleportMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("spectate-mode.teleport-player-smileball-name"))));
		teleport.setItemMeta(teleportMeta);
		
		ItemStack teleportToSpawn = new ItemStack(Material.PAINTING, 1);
		ItemMeta teleportToSpawnMeta = teleportToSpawn.getItemMeta();
		teleportToSpawnMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("spectate-mode.teleport-spawn-painting-name"))));
		teleportToSpawn.setItemMeta(teleportToSpawnMeta);
		
		//setItem in inventory
		p.getInventory().clear();
		p.getInventory().setItem(0, teleport);
		p.getInventory().setItem(1, teleportToSpawn);
		p.getInventory().setItem(8, leaveSpectate);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
        if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player p = e.getPlayer();
            if(p.getItemInHand() != null) {
            	if(p.getItemInHand().getType().equals(Material.ENDER_PEARL)) {
                    e.setCancelled(true);
                    //run joinSpectate
                    joinSpectate(p);
                    
                    if (!GameData.getInstance().spectateClickItemCooldown.contains(p)) {
                    	GameData.getInstance().spectateClickItemCooldown.add(p);
                    	new BukkitRunnable() {
                    		public void run() {
                    			GameData.getInstance().spectateClickItemCooldown.remove(p);
                    	    }
                    	}.runTaskLater(Main.getInstance(), 60L);
                    	joinSpectateMode(p);
                        p.updateInventory();
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("spectate-mode.join-spectate-mode-message"))));
                    } else {
                    	p.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("spectate-mode.cooldown-message"))));
                    	p.updateInventory();
                    }
                    
                }
            	if(p.getItemInHand().getType().equals(Material.EYE_OF_ENDER)) {
                    e.setCancelled(true);
                    //run leaveSpectate
                    leaveSpectate(p);
                    
                    if (!GameData.getInstance().spectateClickItemCooldown.contains(p)) {
                    	GameData.getInstance().spectateClickItemCooldown.add(p);
                    	new BukkitRunnable() {
                    		public void run() {
                    			GameData.getInstance().spectateClickItemCooldown.remove(p);
                    	    }
                    	}.runTaskLater(Main.getInstance(), 60L);
                    	p.getInventory().clear();
                        giveSpectateItem(p);
                        p.getInventory().setItem(8, GameData.getInstance().leave);
                        p.updateInventory();
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("spectate-mode.leave-spectate-mode-message"))));
                    } else {
                    	p.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("spectate-mode.cooldown-message"))));
                    } 
                }
            	if(p.getItemInHand().getType().equals(Material.PAINTING)) {
                    e.setCancelled(true);
                    if (!GameData.getInstance().spectateClickItemCooldown.contains(p)) {
                    	GameData.getInstance().spectateClickItemCooldown.add(p);
                    	new BukkitRunnable() {
                    		public void run() {
                    			GameData.getInstance().spectateClickItemCooldown.remove(p);
                    	    }
                    	}.runTaskLater(Main.getInstance(), 60L);
                    	String[] loc = this.LocationYaml.getString("hub").split(",");
            			p.teleport(new Location(Bukkit.getWorld(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3]), Float.parseFloat(loc[4]), Float.parseFloat(loc[5])));
            			p.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("spectate-mode.teleport-back-to-spawn-message"))));
                    } else {
                    	p.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("spectate-mode.cooldown-message"))));
                    } 
                }
            	if(p.getItemInHand().getType().equals(Material.SLIME_BALL)) {
                    e.setCancelled(true);
                    if (GameData.getInstance().gamestate != GameState.INGAME) {
                		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo player is currently fighting."));
                		return;
                	}
                    if (!GameData.getInstance().spectateClickItemCooldown.contains(p)) {
                    	GameData.getInstance().spectateClickItemCooldown.add(p);
                    	new BukkitRunnable() {
                    		public void run() {
                    			GameData.getInstance().spectateClickItemCooldown.remove(p);
                    	    }
                    	}.runTaskLater(Main.getInstance(), 60L);
                    	SpectateTeleportGUI.getInstance().open(p);
                    } else {
                    	p.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', this.ConfigYaml.getString("spectate-mode.cooldown-message"))));
                    } 
                }
            }
        }
    }
	
	public void joinSpectate(Player p) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0));
		p.setAllowFlight(true);
		GameData.getInstance().inSpectate.add(p);
		for (Player p2 : Bukkit.getServer().getOnlinePlayers()) {
			if (!GameData.getInstance().inSpectate.contains(p2)) {
				p2.hidePlayer(p);
			}
		}
	}
	public void leaveSpectate(Player p) {
		p.removePotionEffect(PotionEffectType.INVISIBILITY);
		p.setAllowFlight(false);
		GameData.getInstance().inSpectate.remove(p);
		String[] loc = this.LocationYaml.getString("hub").split(",");
		p.teleport(new Location(Bukkit.getWorld(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3]), Float.parseFloat(loc[4]), Float.parseFloat(loc[5])));
		for (Player p2 : Bukkit.getServer().getOnlinePlayers()) {
			if (!GameData.getInstance().inSpectate.contains(p2)) {
				p2.showPlayer(p);
			}
		}
	}

}
