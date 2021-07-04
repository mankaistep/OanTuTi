package mk.plugin.ott.gui;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;

import mk.plugin.ott.config.Configs;
import mk.plugin.ott.lang.Lang;
import mk.plugin.ott.main.MainOTT;
import mk.plugin.ott.utils.GUIUtils;

public class TypeSelectGUI {
	
	public static final int MONEY_SLOT = 3;
	public static final int POINT_SLOT = 5;
	
	public static final String TITLE = "§c§lChọn loại cá cược";
	
	public static void openGUI(Player player) { 
		Inventory inv = Bukkit.createInventory(null, 9, TITLE);
		player.openInventory(inv);
		player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1, 1);
		
		Bukkit.getScheduler().runTaskAsynchronously(MainOTT.plugin, () -> {
			for (int i = 0 ; i < inv.getSize() ; i++) inv.setItem(i, GUIUtils.getSlot(DyeColor.BLACK));
			inv.setItem(MONEY_SLOT, Configs.MONEY_BET);
			inv.setItem(POINT_SLOT, Configs.POINT_BET);
		});
		
	}
	
	public static void onClick(InventoryClickEvent e) {
		if (!e.getView().getTitle().equals(TITLE)) return;
		e.setCancelled(true);
		if (e.getClickedInventory() != e.getWhoClicked().getOpenInventory().getTopInventory()) return;
		
		Player player = (Player) e.getWhoClicked();
		player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
		int slot = e.getSlot();
		
		switch (slot) {
			case MONEY_SLOT:
				player.closeInventory();
				player.sendMessage(Lang.ENTER_MONEY.get());
				player.setMetadata("ott.checkmoney", new FixedMetadataValue(MainOTT.plugin, ""));
				player.setMetadata("ott.challenger", new FixedMetadataValue(MainOTT.plugin, ""));
				break;
			case POINT_SLOT:
				player.closeInventory();
				player.sendMessage(Lang.ENTER_POINT.get());
				player.setMetadata("ott.checkpoint", new FixedMetadataValue(MainOTT.plugin, ""));
				player.setMetadata("ott.challenger", new FixedMetadataValue(MainOTT.plugin, ""));
				break;
		}
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
