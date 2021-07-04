package mk.plugin.ott.utils;

import java.util.UUID;

import org.bukkit.Bukkit;

import mk.plugin.ott.game.object.Game;

public class Utils {
	
	public static void sendMessage(UUID uuid, String message) {
		Bukkit.getPlayer(uuid).sendMessage(message);
	}
	
	public static void sendMessage(Game game, String message) {
		game.getPlayers().forEach(uuid -> Utils.sendMessage(uuid, message));
	}
	
	@SuppressWarnings("deprecation")
	public static void sendTitle(UUID uuid, String title, String subtitle) {
		Bukkit.getPlayer(uuid).sendTitle(title, subtitle);
	}
	
	public static void broadcast(String message) {
		Bukkit.getOnlinePlayers().forEach(player -> {
			player.sendMessage(message);
		});
	}
	
}
