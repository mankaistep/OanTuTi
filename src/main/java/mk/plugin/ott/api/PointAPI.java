package mk.plugin.ott.api;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PointAPI {
	
	public static int getPoint(Player player) {
		int points = getAPI().look(player.getUniqueId());
		return points;
	}
	
	public static boolean pointCost(Player player, int points) {
		if (points > getPoint(player)) {
			return false;
		} else {
			getAPI().take(player.getUniqueId(), points);
			return true;
		}
	}
	
	public static void givePoint(Player player, int point) {
		getAPI().give(player.getUniqueId(), point);
	}
	
	public static PlayerPointsAPI getAPI() {
    	Plugin pl = Bukkit.getServer().getPluginManager().getPlugin("PlayerPoints");
    	return PlayerPoints.class.cast(pl).getAPI();
	}
	
}
