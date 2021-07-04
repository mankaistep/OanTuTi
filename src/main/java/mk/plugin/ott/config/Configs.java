package mk.plugin.ott.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import mk.plugin.ott.itembuilder.ItemBuilder;

public class Configs {
	
	public static ItemStack ITEM_BET;
	public static ItemStack POINT_BET;
	public static ItemStack MONEY_BET;
	public static ItemStack TUTORIAL_BET;
	
	public static ItemStack ICON_HEAD;
	
	public static ItemStack SELECT_BUA;
	public static ItemStack SELECT_BAO;
	public static ItemStack SELECT_KEO;
	
	public static int MONEY_TIMES;
	public static int POINT_TIMES;
	public static int ITEM_TIMES;
	public static int INVITATION_EXPIRE;
	public static int SELECT_TIME;
	public static int ITEM_SELECT_TIME;
	public static int RANDOM_ITEM;
	public static int OTT_INFO_SLOT;
	
	public static void load(FileConfiguration config) {
		ITEM_BET = ItemBuilder.buildItem(config.getConfigurationSection("icon-gui.icon-item"));
		POINT_BET = ItemBuilder.buildItem(config.getConfigurationSection("icon-gui.icon-point"));
		MONEY_BET = ItemBuilder.buildItem(config.getConfigurationSection("icon-gui.icon-money"));
		TUTORIAL_BET = ItemBuilder.buildItem(config.getConfigurationSection("icon-gui.icon-tutorial"));
		
		ICON_HEAD = ItemBuilder.buildItem(config.getConfigurationSection("icon-gui.icon-head"));
		
		SELECT_BUA = ItemBuilder.buildItem(config.getConfigurationSection("icon-gui.select-bua"));
		SELECT_BAO = ItemBuilder.buildItem(config.getConfigurationSection("icon-gui.select-bao"));
		SELECT_KEO = ItemBuilder.buildItem(config.getConfigurationSection("icon-gui.select-keo"));
		
		MONEY_TIMES = config.getInt("option.money-times");
		POINT_TIMES = config.getInt("option.point-times");
		ITEM_TIMES = config.getInt("option.item-times");
		INVITATION_EXPIRE = config.getInt("option.invitation-expired");
		SELECT_TIME = config.getInt("option.select-time");
		ITEM_SELECT_TIME = config.getInt("option.item-select-time");
		RANDOM_ITEM = config.getInt("option.random-item");
		OTT_INFO_SLOT = config.getInt("option.ott-info-slot");
	}
	
}
