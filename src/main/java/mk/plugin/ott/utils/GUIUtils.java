package mk.plugin.ott.utils;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class GUIUtils {
	
	public static void addCheck(Player player, Plugin plugin) {
		player.setMetadata("checkGUI", new FixedMetadataValue(plugin, ""));
	}
	
	public static void removeCheckGUI(Player player, Plugin plugin) {
		player.removeMetadata("checkGUI", plugin);
	}
	
	public synchronized static boolean checkCheck(Player player, Plugin plugin) {
		if (player.hasMetadata("checkGUI")) {
			removeCheckGUI(player, plugin);
			return false;
		} 
		addCheck(player, plugin);
		
		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			if (player.hasMetadata("checkGUI")) {
				removeCheckGUI(player, plugin);
			}
		}, 2);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack getSlot(DyeColor color) {
		ItemStack other = new ItemStack(Material.valueOf(color.name() + "_STAINED_GLASS_PANE"));
		ItemMeta meta = other.getItemMeta();
		meta.setDisplayName("§r");
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		other.setItemMeta(meta);
		return other;
	}

}
