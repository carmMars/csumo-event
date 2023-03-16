package me.sumo.commands;

import org.bukkit.entity.*;

import me.sumo.Main;
import me.sumo.game.GameData;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.*;
import org.bukkit.configuration.file.YamlConfiguration;

public class SettingsCMD implements CommandExecutor {
    public File MessageFile = new File(Main.getInstance().getDataFolder() + File.separator + "message.yml");
	public YamlConfiguration MessageYaml = Main.getInstance().CM.GetLoadedYaml(MessageFile);
	
	public File LocationFile = new File(Main.getInstance().getDataFolder() + File.separator + "location.yml");
	public YamlConfiguration LocationYaml = Main.getInstance().CM.GetLoadedYaml(LocationFile);
    
    public SettingsCMD(final Main main) {
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if (!sender.hasPermission("sumo.admin")) {
        	sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.MessageYaml.getString("no-permission")));
            return false;
        }
        final Player player = (Player)sender;
        if (args.length < 1) {
            this.info(player);
            return false;
        }
        switch (args[0]) {
            case "sethub": {
                this.setspawn(player, "hub");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.MessageYaml.getString("set-hub")));
                return true;
            }
            case "build": {
            	if (GameData.getInstance().BuildMode.contains(player)) {
            		GameData.getInstance().BuildMode.remove(player);
            		player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.MessageYaml.getString("exit-buildmode")));
            		player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 1);
            	} else {
            		GameData.getInstance().BuildMode.add(player);
            		player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.MessageYaml.getString("join-buildmode")));
            		player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
            	}         
                return true;
            }
            case "setspawn": {
                if (args.length != 2) {
                    this.info(player);
                    return false;
                }
                if (args[1].equals("1") || args[1].equals("2")) {
                    this.setspawn(player, new StringBuilder("spawn").append(args[1]).toString());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.MessageYaml.getString("set-spawn")));
                    return true;
                }
                this.info(player);
                return false;
            }
            default:
                break;
        }
        this.info(player);
        return false;
    }
    
    public void setspawn(final Player player, final String name) {
        final String world = player.getWorld().getName();
        final double x = player.getLocation().getX();
        final double y = player.getLocation().getY();
        final double z = player.getLocation().getZ();
        final float pitch = player.getLocation().getPitch();
        final float yaw = player.getLocation().getYaw();
        this.LocationYaml.set(name, (Object)(String.valueOf((Object)world) + "," + x + "," + y + "," + z + "," + yaw + "," + pitch));
        Main.getInstance().CM.saveMConfig(this.LocationFile, this.LocationYaml);
    }
    
    public void info(final Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSumo settings help: "));
        player.sendMessage(" ");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/settings sethub"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/settings build"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/settings setspawn 1"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/settings setspawn 2"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------"));
    }
}

