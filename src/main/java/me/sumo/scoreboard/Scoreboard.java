package me.sumo.scoreboard;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.sumo.Main;
import me.sumo.game.GameData;
import me.sumo.enums.GameState;
import me.sumo.utils.GetPlayerPing;
import me.sumo.utils.ScoreHelper;
import me.sumo.utils.TimeUtil;

public class Scoreboard {
	
	public static File DataFile = new File(Main.getInstance().getDataFolder() + File.separator + "message.yml");
	public static YamlConfiguration Yaml = Main.getInstance().CM.GetLoadedYaml(DataFile);
	
	public static void scoreboard(Player p) {
		ScoreHelper helper = ScoreHelper.createScore(p);
        helper.setTitle(Yaml.getString("scoreboard.title"));
	}
	
	public static void updateScoreboard(Player p) {
		ScoreHelper helper = ScoreHelper.getByPlayer(p);

		if(ScoreHelper.hasScore(p)) {
			//WAITING
			if (GameData.getInstance().gamestate.equals(GameState.WAITING)) {
				ArrayList<String> list = new ArrayList<>();
		        for (String s : Yaml.getStringList("scoreboard.waiting-scoreboard")) {
		        	list.add(s
		        			.replace("<player_left>", String.valueOf(GameData.getInstance().Players.size()))
		        			.replace("<max_player>", String.valueOf(GameData.getInstance().max_player))
		        			);
		        }
		        helper.setSlotsFromList(list);
			}
			//COUNTING DOWN
			if (GameData.getInstance().gamestate.equals(GameState.COUNTING)) {
				ArrayList<String> list = new ArrayList<>();
		        for (String s : Yaml.getStringList("scoreboard.countdown-scoreboard")) {
		        	list.add(s
		        			.replace("<player_left>", String.valueOf(GameData.getInstance().Players.size()))
		        			.replace("<max_player>", String.valueOf(GameData.getInstance().max_player))
		        			.replace("<countdown_int>", String.valueOf(GameData.getInstance().countTime))
		        			.replace("<countdown_format>", String.valueOf(TimeUtil.setFormat(GameData.getInstance().countTime)))
		        			);
		        }
		        helper.setSlotsFromList(list);
			}
			//INGAME
			if (GameData.getInstance().gamestate.equals(GameState.INGAME)) {
				ArrayList<String> list = new ArrayList<>();
		        for (String s : Yaml.getStringList("scoreboard.ingame-scoreboard")) {
		        	list.add(s
		        			.replace("<player_left>", String.valueOf(GameData.getInstance().PlayerAlive.size()))
		        			.replace("<max_player>", String.valueOf(GameData.getInstance().max_player))
		        			.replace("<player1>", GameData.getInstance().fighting.get(0).getDisplayName())
		        			.replace("<player2>", GameData.getInstance().fighting.get(1).getDisplayName())
		        			.replace("<round>", String.valueOf(GameData.getInstance().Round))
		        			.replace("<player1_ping>", String.valueOf(GetPlayerPing.getPlayerPing(GameData.getInstance().fighting.get(0))))
		        			.replace("<player2_ping>", String.valueOf(GetPlayerPing.getPlayerPing(GameData.getInstance().fighting.get(1))))
		        			.replace("<player1_cps>", String.valueOf(GameData.getInstance().CPS.get(GameData.getInstance().fighting.get(0).getUniqueId())))
		        			.replace("<player2_cps>", String.valueOf(GameData.getInstance().CPS.get(GameData.getInstance().fighting.get(1).getUniqueId())))
		        			);
		        }
		        helper.setSlotsFromList(list);
			}
			//FINISH
			if (GameData.getInstance().gamestate.equals(GameState.FINISH)) {
				ArrayList<String> list = new ArrayList<>();
		        for (String s : Yaml.getStringList("scoreboard.finish-scoreboard")) {
		        	list.add(s
		        			.replace("<player_left>", String.valueOf(GameData.getInstance().PlayerAlive.size()))
		        			.replace("<max_player>", String.valueOf(GameData.getInstance().max_player))
		        			.replace("<winner>", GameData.getInstance().Winner)
		        			.replace("<restart>", String.valueOf(TimeUtil.setFormat(GameData.getInstance().restart)))
		        			);
		        }
		        helper.setSlotsFromList(list);
			}
        }	
        
	}
	
}
