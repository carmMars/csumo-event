package me.sumo.commands;

import java.io.File;

import me.sumo.Main;
import me.sumo.utils.GetPlayerPing;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PingCMD implements CommandExecutor {
	
	public File MessageFile = new File(Main.getInstance().getDataFolder() + File.separator + "message.yml");
	public YamlConfiguration MessageYaml = Main.getInstance().CM.GetLoadedYaml(MessageFile);

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("ping")) {
			if ((sender instanceof Player)) {
				Player p = ((Player)sender).getPlayer();
				if ((args.length == 1) && (Bukkit.getPlayer(args[0]) != null)) {
					int ping = GetPlayerPing.getPlayerPing(Bukkit.getPlayer(args[0]));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.MessageYaml.getString("other-player-ping")
							.replace("<ping>", String.valueOf(ping))
							.replace("<player>", Bukkit.getPlayer(args[0]).getDisplayName())
							));
					p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
					return true;
				}

				int ping = GetPlayerPing.getPlayerPing(Bukkit.getPlayer(p.getName()));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.MessageYaml.getString("ping")
						.replace("<ping>", String.valueOf(ping))
						));
				p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
				return true;
			}
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou must be a player to excute this command!"));
			return false;
		}
		return false;
	}
}