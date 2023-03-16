package me.sumo.commands;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.sumo.Main;
import me.sumo.game.StartAndFinish;

public class StartCMD implements CommandExecutor {
    private StartAndFinish startfinish = new StartAndFinish();
    
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
    	this.startfinish.startGame();
    	return true;
    }
}
