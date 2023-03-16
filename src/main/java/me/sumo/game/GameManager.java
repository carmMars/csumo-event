package me.sumo.game;

import java.io.File;

import me.sumo.bungeecord.BungeeListener;
import me.sumo.Main;
import me.sumo.enums.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class GameManager implements Listener {
	private Plugin main = Main.getPlugin(Main.class);
	
	public File ConfigFile = new File(Main.getInstance().getDataFolder() + File.separator + "configuration.yml");
	public YamlConfiguration ConfigYaml = Main.getInstance().CM.GetLoadedYaml(ConfigFile);
	
	public File MessageFile = new File(Main.getInstance().getDataFolder() + File.separator + "message.yml");
	public YamlConfiguration MessageYaml = Main.getInstance().CM.GetLoadedYaml(MessageFile);
	
	public File LocationFile = new File(Main.getInstance().getDataFolder() + File.separator + "location.yml");
	public YamlConfiguration LocationYaml = Main.getInstance().CM.GetLoadedYaml(LocationFile);
	
	public File DataFile = new File(Main.getInstance().getDataFolder() + File.separator + "data.yml");
	public YamlConfiguration DataYaml = Main.getInstance().CM.GetLoadedYaml(DataFile);
	
	private BungeeListener BL = new BungeeListener();
	private Spectate spec = new Spectate();
	
	public void getWinner() {
		final Player winner = GameData.getInstance().fighting.get(0);
		
		if (GameData.getInstance().PlayerAlive.size() == 1) {
			GameData.getInstance().fighting.clear();
			GameData.getInstance().gamestate = GameState.FINISH;
			GameData.getInstance().Winner = winner.getName();
			if (this.ConfigYaml.getBoolean("bungeecord-mode.bungee-win-broadcast")) {
				BL.broadcastMessage(winner, "§4§lSUMO §8» §c" + winner.getName() + " §fha appena vinto un §bevento §bsumo§f!");
			} else {
				for (String s : this.MessageYaml.getStringList("broadcast-winner")) {
		        	Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', s)
		        			.replace("<winner>", GameData.getInstance().Winner)
		        			);
		        }
			}
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				p.playSound(winner.getLocation(), Sound.ENDERDRAGON_DEATH, 1.0F, 1.0F);
			}

			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', this.MessageYaml.getString("restart-message")
					.replace("<restart>", String.valueOf(GameData.getInstance().restart))
					));
			//data add and save
			if (this.ConfigYaml.getBoolean("create-and-save-players-data")) {
				GameData.getInstance().wins.replace(winner.getUniqueId(), GameData.getInstance().wins.get(winner.getUniqueId()) + 1);
				this.DataYaml.set("PlayersData." + winner.getUniqueId() + ".wins", GameData.getInstance().wins.get(winner.getUniqueId()));
				Main.getInstance().CM.saveMConfig(this.DataFile, this.DataYaml);
				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[&6Sumo&e] " + "&a" + winner.getName() + "'s wins + 1 (now: " + GameData.getInstance().wins.get(winner.getUniqueId()) + ")"));
			}
			
			new BukkitRunnable() {
				public void run() {			
					if (GameData.getInstance().restart == ConfigYaml.getInt("kick-player-second")) {
						if (ConfigYaml.getBoolean("bungeecord-mode.enable")) {
							for (Player p : Bukkit.getServer().getOnlinePlayers()) {
								BL.connectToServer(p, ConfigYaml.getString("bungeecord-mode.lobby-server"));
							}
						} else {
							for (Player p : Bukkit.getServer().getOnlinePlayers()) {
								p.kickPlayer(ChatColor.translateAlternateColorCodes('&', ConfigYaml.getString("kick-player-reason")));
							}
						}
						
					}
					if (GameData.getInstance().restart == 0) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
					}
					GameData.getInstance().restart -= 1;
				}
			}.runTaskTimer(Main.getInstance(), 20L, 20L);

			return;
		}
		GameData.getInstance().currentLevelFight.remove(winner);
		winner.playSound(winner.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
		GameData.getInstance().fighting.clear();
		GameData.getInstance().sumoStart = false;
		tpHub(winner);

		if (GameData.getInstance().currentLevelFight.size() < 2) {
			if (GameData.getInstance().currentLevelFight.size() == 1) {
				Player onlyOne = (Player)GameData.getInstance().currentLevelFight.get(0);
				GameData.getInstance().currentLevelFight.clear();
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', this.MessageYaml.getString("reserve-broadcast"))
	    				.replace("<reserve>", onlyOne.getName())
	    				.replace("<round>", String.valueOf(GameData.getInstance().Round))
	    				);
				
				//new random system
				if (GameData.getInstance().reserve != null) {
					GameData.getInstance().PlayerAlive.remove(GameData.getInstance().reserve);
					GameData.getInstance().PlayerAlive.add(0, GameData.getInstance().reserve);
		        }
				
			}	

			for (Player p : GameData.getInstance().PlayerAlive) {
				GameData.getInstance().currentLevelFight.add(p);
			}
			
			StartAndFinish.getInstance().RoundBroadcast();
			StartAndFinish.getInstance().getReserve();
			teleport();
		} else {
			teleport();
		}
	}

	public void teleport() {
		for (int run = 0; run < 2; run++) {
			if (run == 0) {
				try {
					String[] loc = this.LocationYaml.getString("spawn1").split(",");
					GameData.getInstance().fighting.add((Player)GameData.getInstance().currentLevelFight.get(run));
					((Player)GameData.getInstance().currentLevelFight.get(run))
					.teleport(new Location(Bukkit.getWorld(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3]), Float.parseFloat(loc[4]), Float.parseFloat(loc[5])));
				} catch(Exception e) {
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&cPlease use /settings to set player spawn"));
				}
			} else {
				try {
					String[] loc = this.LocationYaml.getString("spawn2").split(",");
					GameData.getInstance().fighting.add((Player)GameData.getInstance().currentLevelFight.get(run));
					((Player)GameData.getInstance().currentLevelFight.get(run))
					.teleport(new Location(Bukkit.getWorld(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3]), Float.parseFloat(loc[4]), Float.parseFloat(loc[5])));
				} catch(Exception e) {
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&cPlease use /settings to set player spawn"));
				}
			}
		}

		new BukkitRunnable() {
			int count = 4;
			public void run() {
				if (this.count == 0) {
					GameData.getInstance().sumoStart = true;
					for (Player p : GameData.getInstance().fighting) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageYaml.getString("match-start")));
					}
					cancel();
				} else if (this.count == 1 || this.count == 2 || this.count == 3) {
					for (Player p : GameData.getInstance().fighting) {
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageYaml.getString("start-count")
								.replace("<time>", String.valueOf(count))
								));
					}
				} else if (this.count == 4) {
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', MessageYaml.getString("starting-event-match-againest")
							.replace("<player1>", String.valueOf(GameData.getInstance().fighting.get(0).getName()))
							.replace("<player2>", String.valueOf(GameData.getInstance().fighting.get(1).getName()))
							));
				}
				
				this.count -= 1;
			}
		}.runTaskTimer(this.main, 20L, 20L);
	}

	@EventHandler
	public void noStartnoMove(PlayerMoveEvent e) {
		if ((GameData.getInstance().fighting.contains(e.getPlayer())) && (!GameData.getInstance().sumoStart))
			e.setTo(e.getFrom());
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		GameData.getInstance().Players.add(p);
		tpHub(p);
		p.removePotionEffect(PotionEffectType.INVISIBILITY);
		p.getInventory().clear();
		if (GameData.getInstance().gamestate == GameState.WAITING || GameData.getInstance().gamestate == GameState.COUNTING) {
			for (String s : this.MessageYaml.getStringList("join-message")) {
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', s)
						.replace("<player>", String.valueOf(GameData.getInstance().Players.size()))
						.replace("<max_player>", String.valueOf(GameData.getInstance().max_player))
						.replace("<name>", p.getName())
						);
			}
		}	
		//give leave item
		if (this.ConfigYaml.getBoolean("bungeecord-mode.enable")) {
			p.getInventory().setItem(this.ConfigYaml.getInt("bungeecord-mode.leave-item.position"), GameData.getInstance().leave);
		}
		//give spectate item when player join and gamestate is INGAME
		if (this.ConfigYaml.getBoolean("spectate-mode.enable") && GameData.getInstance().gamestate == GameState.INGAME && p.hasPermission("sumo.spectate")) {
			spec.giveSpectateItem(p);
		}
		
		//data create
		if (this.ConfigYaml.getBoolean("create-and-save-players-data")) {
			if (this.DataYaml.getString("PlayersData." + p.getUniqueId() + ".Player") == null) {
				this.DataYaml.set("PlayersData." + p.getUniqueId() + ".Player", p.getName());
				Main.getInstance().CM.saveMConfig(this.DataFile, this.DataYaml);
				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[&6Sumo&e] " + "&a" + p.getName() + "'s profile(Player name) successfully created!"));
			} 
			if (this.DataYaml.getString("PlayersData." + p.getUniqueId() + ".wins") == null) {
				this.DataYaml.set("PlayersData." + p.getUniqueId() + ".wins", Integer.valueOf(0));
				Main.getInstance().CM.saveMConfig(this.DataFile, this.DataYaml);
				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[&6Sumo&e] " + "&a" + p.getName() + "'s profile(wins) successfully created!"));
			}
			if (this.DataYaml.getString("PlayersData." + p.getUniqueId() + ".kills") == null) {
				this.DataYaml.set("PlayersData." + p.getUniqueId() + ".kills", Integer.valueOf(0));
				Main.getInstance().CM.saveMConfig(this.DataFile, this.DataYaml);
				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[&6Sumo&e] " + "&a" + p.getName() + "'s profile(kills) successfully created!"));
			}
			if (this.DataYaml.getString("PlayersData." + p.getUniqueId() + ".deaths") == null) {
				this.DataYaml.set("PlayersData." + p.getUniqueId() + ".deaths", Integer.valueOf(0));
				Main.getInstance().CM.saveMConfig(this.DataFile, this.DataYaml);
				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[&6Sumo&e] " + "&a" + p.getName() + "'s profile(deaths) successfully created!"));
			}
			
			DataYaml = Main.getInstance().CM.GetLoadedYaml(this.DataFile);
			GameData.getInstance().wins.put(p.getUniqueId(), this.DataYaml.getInt("PlayersData." + p.getUniqueId() + ".wins"));
			GameData.getInstance().kills.put(p.getUniqueId(), this.DataYaml.getInt("PlayersData." + p.getUniqueId() + ".kills"));
			GameData.getInstance().deaths.put(p.getUniqueId(), this.DataYaml.getInt("PlayersData." + p.getUniqueId() + ".deaths"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[&6Sumo&e] " + "&a" + p.getName() + "'s profile successfully loaded!"));
		}	
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		GameData.getInstance().Players.remove(p);
		if (GameData.getInstance().gamestate == GameState.WAITING || GameData.getInstance().gamestate == GameState.COUNTING) {
			for (String s : this.MessageYaml.getStringList("leave-message")) {
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', s)
						.replace("<player>", String.valueOf(GameData.getInstance().Players.size()))
						.replace("<max_player>", String.valueOf(GameData.getInstance().max_player))
						.replace("<name>", p.getName())
						);
			}
		}	
		if (GameData.getInstance().fighting.contains(e.getPlayer())) {
			for (String s : this.MessageYaml.getStringList("leave-while-fighting-message")) {
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', s)
						.replace("<player>", String.valueOf(GameData.getInstance().Players.size()))
						.replace("<max_player>", String.valueOf(GameData.getInstance().max_player))
						.replace("<name>", p.getName())
						);
			}
			GameData.getInstance().sumoStart = false;
			GameData.getInstance().currentLevelFight.remove(e.getPlayer());
			GameData.getInstance().PlayerAlive.remove(e.getPlayer());
			GameData.getInstance().fighting.remove(e.getPlayer());
			getWinner();
			return;
		}
		if (GameData.getInstance().currentLevelFight.contains(e.getPlayer())) {
			GameData.getInstance().currentLevelFight.remove(e.getPlayer());
		}
		if (GameData.getInstance().PlayerAlive.contains(e.getPlayer())) {
			GameData.getInstance().PlayerAlive.remove(e.getPlayer());
		}	
	}

	@EventHandler
	public void TouchWaterDied(PlayerMoveEvent e) {
		if ((GameData.getInstance().fighting.contains(e.getPlayer())) && (e.getPlayer().getLocation().getBlock().getType().equals(Material.STATIONARY_WATER))) {
			tpHub(e.getPlayer());
			GameData.getInstance().currentLevelFight.remove(e.getPlayer());
			GameData.getInstance().PlayerAlive.remove(e.getPlayer());
			Player loser = e.getPlayer();
			GameData.getInstance().fighting.remove(e.getPlayer());
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', this.MessageYaml.getString("eliminated-message")
					.replace("<loser>", loser.getName())
					.replace("<winner>", ((Player)GameData.getInstance().fighting.get(0)).getName())
					));
			loser.playSound(loser.getLocation(), Sound.NOTE_BASS, 0.0F, 1.0F);
			
			//data add and save
			if (this.ConfigYaml.getBoolean("create-and-save-players-data")) {
				GameData.getInstance().kills.replace(((Player)GameData.getInstance().fighting.get(0)).getUniqueId(), GameData.getInstance().kills.get(((Player)GameData.getInstance().fighting.get(0)).getUniqueId()) + 1);
				GameData.getInstance().deaths.replace(loser.getUniqueId(), GameData.getInstance().deaths.get(loser.getUniqueId()) + 1);
				this.DataYaml.set("PlayersData." + ((Player)GameData.getInstance().fighting.get(0)).getUniqueId() + ".kills", GameData.getInstance().kills.get(((Player)GameData.getInstance().fighting.get(0)).getUniqueId()));
				this.DataYaml.set("PlayersData." + loser.getUniqueId() + ".deaths", GameData.getInstance().deaths.get(loser.getUniqueId()));
				Main.getInstance().CM.saveMConfig(this.DataFile, this.DataYaml);
				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[&6Sumo&e] " + "&a" + ((Player)GameData.getInstance().fighting.get(0)).getName() + "'s kills + 1 (now: " + GameData.getInstance().kills.get(((Player)GameData.getInstance().fighting.get(0)).getUniqueId()) + ")"));
				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[&6Sumo&e] " + "&a" + loser.getName() + "'s deaths + 1 (now: " + GameData.getInstance().deaths.get(loser.getUniqueId()) + ")"));
			}
			getWinner();
		}
	}

	public void tpHub(Player player) {
		try {
			String[] loc = this.LocationYaml.getString("hub").split(",");
			player.teleport(new Location(Bukkit.getWorld(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), 
			Double.parseDouble(loc[3]), Float.parseFloat(loc[4]), Float.parseFloat(loc[5])));
		} catch(Exception e) {
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&cPlease use /settings to set player spawn"));
		}
		
	}
}
