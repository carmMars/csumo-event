package me.sumo.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import me.sumo.game.GameData;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class PlaceHolderAPIHook extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "sumo";
    }

    @Override
    public @NotNull String getAuthor() {
        return "CARTERHH";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }
	@Override
    public String onPlaceholderRequest(Player p, String identifier) {
		// always check if the player is null for placeholders related to the player!
        if (p == null) {
            return "";
        }

        if (identifier.equals("player_kills")) {
            return String.valueOf(GameData.getInstance().kills.get(p.getUniqueId()));
        }

        if (identifier.equals("player_deaths")) {
        	return String.valueOf(GameData.getInstance().deaths.get(p.getUniqueId()));
        }

        if (identifier.equals("player_wins")) {
        	return String.valueOf(GameData.getInstance().wins.get(p.getUniqueId()));
        }

        if (identifier.equals("game_round")) {
        	return String.valueOf(GameData.getInstance().Round);
        }

        if (identifier.equals("game_is_whitelisted")) {
        	return String.valueOf(GameData.getInstance().whitelist);
        }

        if (identifier.equals("player_is_spectating")) {
        	return String.valueOf(GameData.getInstance().inSpectate.contains(p));
        }

        if (identifier.equals("player_is_in_buildmode")) {
        	return String.valueOf(GameData.getInstance().BuildMode.contains(p));
        }
        
        return null;
    }
}
