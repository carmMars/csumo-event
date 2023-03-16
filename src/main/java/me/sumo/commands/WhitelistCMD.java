package me.sumo.commands;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.sumo.Main;
import me.sumo.game.GameData;

public class WhitelistCMD implements CommandExecutor {
	
	public File MessageFile = new File(Main.getInstance().getDataFolder() + File.separator + "message.yml");
	public YamlConfiguration MessageYaml = Main.getInstance().CM.GetLoadedYaml(MessageFile);
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
    	if (!(sender instanceof Player)) {
    		return false;
    	}
    	if (!sender.hasPermission("Sumo.admin")) {
    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.MessageYaml.getString("no-permission")));
    		return false;
    	}
    	
    	if (GameData.getInstance().whitelist == true) {
    		GameData.getInstance().whitelist = false;
        	p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.MessageYaml.getString("whitelist-off")));
    	} else {
    		GameData.getInstance().whitelist = true;
        	p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.MessageYaml.getString("whitelist-on")));
    	}
    	
		
		return false;
	}

}
