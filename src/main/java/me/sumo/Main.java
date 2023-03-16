package me.sumo;

import java.io.File;

import me.sumo.bungeecord.BungeeListener;
import me.sumo.scoreboard.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.sumo.commands.CountdownCMD;
import me.sumo.commands.PingCMD;
import me.sumo.commands.SettingsCMD;
import me.sumo.commands.StartCMD;
import me.sumo.commands.StatsCMD;
import me.sumo.commands.WhitelistCMD;
import me.sumo.GUI.SpectateTeleportGUI;
import me.sumo.GUI.StatsGUI;
import me.sumo.game.GameData;
import me.sumo.game.GameManager;
import me.sumo.game.Spectate;
import me.sumo.listeners.CPSChecker;
import me.sumo.listeners.ClickInventoryEvent;
import me.sumo.listeners.DropItemListener;
import me.sumo.listeners.GUIAntiMove;
import me.sumo.listeners.LeaveItemRightClick;
import me.sumo.listeners.NoBlockBreakAndPlaceListener;
import me.sumo.listeners.NoDamage;
import me.sumo.listeners.NoHungerLoseListener;
import me.sumo.listeners.PlayerJoinListener;
import me.sumo.listeners.PlayerLeaveListener;
import me.sumo.listeners.PlayerPreLoginEvent;
import me.sumo.listeners.motd;
import me.sumo.api.PlaceHolderAPIHook;
import me.sumo.utils.YamlManager;

public class Main extends JavaPlugin{
	
	private static Main instance;
  	public String nmsVersion;
  	
  	//data.yml
  	public YamlManager CM = new YamlManager();
    private File yamlpath;
    private String YamlName;

    public void onEnable() {
    	instance = this;
    	loadData();
    	loadMessage();
    	loadConfig();
    	loadLocation();
    	updateScoreboard();
    	loadServerVersion();
    	registerListeners();
    	registerCommands();
    	registerNewClass();
    	sumoWorldFix();
    	HookPlugins();
    	this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    	this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeListener());
    	Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[" + ChatColor.BOLD + "Sumo" + ChatColor.YELLOW + "] " + ChatColor.BOLD + "Sumo Plugin ");
    	Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[" + ChatColor.BOLD + "Sumo" + ChatColor.YELLOW + "] " + ChatColor.RED + " ");
    	Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[" + ChatColor.BOLD + "Sumo" + ChatColor.YELLOW + "] " + ChatColor.YELLOW + "Version: " + getDescription().getVersion());
    	Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[" + ChatColor.BOLD + "Sumo" + ChatColor.YELLOW + "] " + ChatColor.YELLOW + "Author: CARTERHH");
    }
    
	public static Main getInstance() {
  		return instance;
  	}
	
  	public String getNmsVersion() {
		return nmsVersion;
	}	
  	
	public void loadServerVersion() {
		nmsVersion = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
	}
	
	public void registerNewClass() {
		new GameData();
    	new StatsGUI();
    	new SpectateTeleportGUI();
	}
	
	public void registerListeners() {
		File ConfigFile = new File(Main.getInstance().getDataFolder() + File.separator + "configuration.yml");
		YamlConfiguration ConfigYaml = Main.getInstance().CM.GetLoadedYaml(ConfigFile);
		Bukkit.getPluginManager().registerEvents(new GameManager(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerLeaveListener(), this);
		Bukkit.getPluginManager().registerEvents(new NoBlockBreakAndPlaceListener(), this);
		Bukkit.getPluginManager().registerEvents(new NoHungerLoseListener(), this);
		Bukkit.getPluginManager().registerEvents(new NoDamage(), this);
		Bukkit.getPluginManager().registerEvents(new CPSChecker(), this);
		Bukkit.getPluginManager().registerEvents(new GUIAntiMove(), this);
		Bukkit.getPluginManager().registerEvents(new LeaveItemRightClick(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerPreLoginEvent(), this);
		Bukkit.getPluginManager().registerEvents(new DropItemListener(), this);
		Bukkit.getPluginManager().registerEvents(new Spectate(), this);
		Bukkit.getPluginManager().registerEvents(new ClickInventoryEvent(), this);
		if (ConfigYaml.getBoolean("motd.enable")) {
			Bukkit.getPluginManager().registerEvents(new motd(), this);
		}
	}
	
	public void registerCommands() {
		getCommand("settings").setExecutor(new SettingsCMD(this));
		getCommand("start").setExecutor(new StartCMD());
		getCommand("ping").setExecutor(new PingCMD());
		getCommand("countdown").setExecutor(new CountdownCMD());
		getCommand("stats").setExecutor(new StatsCMD());
		getCommand("wl").setExecutor(new WhitelistCMD());
	}
	
	public void sumoWorldFix() {
		File ConfigFile = new File(Main.getInstance().getDataFolder() + File.separator + "configuration.yml");
		YamlConfiguration ConfigYaml = Main.getInstance().CM.GetLoadedYaml(ConfigFile);
		World w = Bukkit.getWorld(ConfigYaml.getString("event-world-name"));
		w.setGameRuleValue("doDaylightCycle", "false");
		w.setGameRuleValue("doMobSpawning", "false");
		w.setGameRuleValue("mobGriefing", "false");
		new BukkitRunnable() {
			public void run() {			
				w.setTime(6000);
			}
		}.runTaskTimer(Main.getInstance(), 0L, 1200L);
	}
	
	public void updateScoreboard() {
  		new BukkitRunnable() {
            @Override
            public void run() {
                for(Player p : Bukkit.getServer().getOnlinePlayers()) {
                	Scoreboard.updateScoreboard(p);
                }  
            }
        }.runTaskTimer(this, 20L, 20L);
  	}
	
	//send message to console if the plugin is enabled
	@SuppressWarnings("deprecation") //For placeholderapi
	public void HookPlugins() {
		Bukkit.getConsoleSender().sendMessage(" ");
	    if (Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
	    	new PlaceHolderAPIHook().register();
	    	Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "PlaceholderAPI §§ahas been hooked!");
	    }
		Bukkit.getConsoleSender().sendMessage(" ");		
	}
    
    public void loadData() {
		Bukkit.getConsoleSender().sendMessage(" ");
	    Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Loading data.yml...");
		CM = new YamlManager();
		yamlpath = new File(this.getDataFolder() + File.separator + "data.yml");
		YamlName = "data.yml";
		CM.SetupMConfig(yamlpath, YamlName);
		CM.reloadMConfig(yamlpath, YamlName);
	}
    
    public void loadMessage() {
		Bukkit.getConsoleSender().sendMessage(" ");
	    Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Loading message.yml...");
		CM = new YamlManager();
		yamlpath = new File(this.getDataFolder() + File.separator + "message.yml");
		YamlName = "data.yml";
		CM.SetupMConfig(yamlpath, YamlName);
		CM.reloadMConfig(yamlpath, YamlName);
	}
    
    public void loadConfig() {
		Bukkit.getConsoleSender().sendMessage(" ");
	    Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Loading configuration.yml...");
		CM = new YamlManager();
		yamlpath = new File(this.getDataFolder() + File.separator + "configuration.yml");
		YamlName = "configuration.yml";
		CM.SetupMConfig(yamlpath, YamlName);
		CM.reloadMConfig(yamlpath, YamlName);
	}
    
    public void loadLocation() {
		Bukkit.getConsoleSender().sendMessage(" ");
	    Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Loading location.yml...");
		CM = new YamlManager();
		yamlpath = new File(this.getDataFolder() + File.separator + "location.yml");
		YamlName = "location.yml";
		CM.SetupMConfig(yamlpath, YamlName);
		CM.reloadMConfig(yamlpath, YamlName);
	}

}
