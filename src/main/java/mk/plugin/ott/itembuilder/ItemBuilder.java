package mk.plugin.ott.itembuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;

import me.clip.placeholderapi.PlaceholderAPI;
import mk.plugin.ott.utils.ItemStackUtils;

public class ItemBuilder {
	
	// .?
	public static ItemStack buildItem(ConfigurationSection config) {
		Material material = Material.valueOf(config.getString(".type"));
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		String name = config.getString(".name").replace("&", "§");
		if (name.equals("")) name = "§r";
		meta.setDisplayName(name);
		List<String> lore = Lists.newArrayList();
		config.getStringList(".lore").forEach(s -> lore.add(s.replace("&", "§")));;
		Map<Enchantment, Integer> e = new HashMap<Enchantment, Integer> ();
		for (int i = 0 ; i < config.getStringList(".enchant").size(); i ++) {
			String s = config.getStringList(".enchant").get(i);
			String eString = s.substring(0, s.indexOf(":"));
			Enchantment enchant = Enchantment.getByName(eString);
			int level = Integer.parseInt(s.substring(s.indexOf(":") + 1));
			e.put(enchant, level);
		}
		if (config.contains("flag")) {
			config.getStringList("flag").forEach(s -> {
				meta.addItemFlags(ItemFlag.valueOf(s));
			});
		}
		
		meta.setLore(lore);
		for (Enchantment en : e.keySet()) {
			meta.addEnchant(en, e.get(en), true);
		}
		
		int data = config.getInt(".data");
		meta.setCustomModelData(data);
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static ItemStack getWithPlaceholder(Player player, ItemStack itemm) {
		ItemStack item = itemm.clone();
		String name = checkPlaceholders(player, ItemStackUtils.getName(item));
		List<String> lore = Lists.newArrayList();
		ItemStackUtils.getLore(item).forEach(s -> {
			lore.add(checkPlaceholders(player, s));
		});
		ItemStackUtils.setDisplayName(item, name);
		ItemStackUtils.setLore(item, lore);
		return item;
	}
	
	public static String checkPlaceholders(Player player, String s) {
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			return PlaceholderAPI.setPlaceholders(player, s);
		}
		return s;
	}
	
}
