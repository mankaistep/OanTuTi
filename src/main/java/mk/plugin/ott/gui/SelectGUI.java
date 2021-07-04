package mk.plugin.ott.gui;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

import mk.plugin.ott.config.Configs;
import mk.plugin.ott.game.manager.GameUtils;
import mk.plugin.ott.game.object.Game;
import mk.plugin.ott.game.object.Selection;
import mk.plugin.ott.game.object.SelectionType;
import mk.plugin.ott.main.MainOTT;
import mk.plugin.ott.utils.GUIUtils;
import mk.plugin.ott.utils.ItemStackUtils;

public class SelectGUI {
	
	public static final int BUA_SLOT = 10;
	public static final int KEO_SLOT = 11;
	public static final int BAO_SLOT = 12;
	
	public static final List<Integer> TIMING_SLOTS = Lists.newArrayList(28, 29, 30, 31, 32, 33, 34);
	
	public static final String TITLE = "§c§lChọn nước đi của bạn";
	
	public static void openGUI(Player player) {
		Inventory inv = Bukkit.createInventory(null, 45, TITLE);
		player.openInventory(inv);
		player.setMetadata("ott.selectgui", new FixedMetadataValue(MainOTT.plugin, "dit me"));
		player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1, 1);
		
		Bukkit.getScheduler().runTaskAsynchronously(MainOTT.plugin, () -> {
			for (int i = 0 ; i < inv.getSize() ; i++) {
				if (!TIMING_SLOTS.contains(i)) inv.setItem(i, GUIUtils.getSlot(DyeColor.BLACK));
			}
			inv.setItem(BUA_SLOT, Configs.SELECT_BUA);
			inv.setItem(KEO_SLOT, Configs.SELECT_KEO);
			inv.setItem(BAO_SLOT, Configs.SELECT_BAO);
			Game game = GameUtils.getGamePlaying(player.getUniqueId());
			if (game != null) inv.setItem(Configs.OTT_INFO_SLOT, getInfoItem(game));
		});
		
		
		long start = System.currentTimeMillis();
		BukkitRunnable br = new BukkitRunnable() {
			@Override
			public void run() {
				if (player.hasMetadata("ott.selectgui")) {
					if (!player.getOpenInventory().getTitle().equals(TITLE)) {
						player.openInventory(inv);
					}
				} else this.cancel();
				
				// Check, end
				long pass = System.currentTimeMillis() - start;
				if (pass >=  Configs.SELECT_TIME * 1000L) {
					// Debug
					try {
						var startT = LocalDateTime.ofInstant(Instant.ofEpochMilli(start), TimeZone.getDefault().toZoneId());
						player.sendMessage("§6Bạn đã không chọn, server tự động chọn cho bạn: §cKhông");
						player.sendMessage("§6Nếu bạn nghĩ đây là lỗi, report lên group kèm với nội dung ở dưới");
						player.sendMessage("§cThời gian bắt đầu: §f" + startT.getHour() + ":" + startT.getMinute() + ":" + startT.getSecond());
						player.sendMessage("§cThời gian chọn: §f" + pass + " ms");
					}
					finally {
						// Do
						player.closeInventory();
						Game game = GameUtils.getGamePlaying(player.getUniqueId());
						if (game != null) {
							GameUtils.select(player.getUniqueId(), SelectionType.THUA);
//						GameUtils.finish(game, game.getOpponent(player.getUniqueId()), player.getUniqueId());
						}
						this.cancel();
						return;
					}
				}
				
				// Check timings
				int a = new Double(TIMING_SLOTS.size() * pass / (Configs.SELECT_TIME * 1000)).intValue();
				for (int i = 0 ; i < Math.min(a, TIMING_SLOTS.size()) ; i++) {
					inv.setItem(TIMING_SLOTS.get(i), getTimingItem());
				}
			}
		};
		br.runTaskTimer(MainOTT.plugin, 0, 5);
	}
	
	public static void onClick(InventoryClickEvent e) {
		if (!e.getView().getTitle().equals(TITLE)) return;
		e.setCancelled(true);
		if (e.getClickedInventory() != e.getWhoClicked().getOpenInventory().getTopInventory()) return;
		Inventory inv = e.getClickedInventory();
		Player player = (Player) e.getWhoClicked();
		player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
		
		int slot = e.getSlot();
		SelectionType st = null;
		switch (slot) {
		case BUA_SLOT:
			st = SelectionType.BUA;
			break;
		case KEO_SLOT:
			st = SelectionType.KEO;
			break;
		case BAO_SLOT:
			st = SelectionType.BAO;
			break;
		}
		if (st != null) {
			player.removeMetadata("ott.selectgui", MainOTT.plugin);
			player.closeInventory();
			GameUtils.select(player.getUniqueId(), st);
			inv.setItem(Configs.OTT_INFO_SLOT, getInfoItem(GameUtils.getGamePlaying(player.getUniqueId())));
		}
	}
	
	public static ItemStack getTimingItem() {
		return GUIUtils.getSlot(DyeColor.RED);
	}
	
	public static ItemStack getInfoItem(Game game) {
		if (game == null) return null;
		Player player1 = Bukkit.getPlayer(game.getChallenger());
		Player player2 = Bukkit.getPlayer(game.getTarget());
		int sum = game.getTimes();
		int score1 = 0;
		int score2 = 0;
		for (Selection s : game.getSelections()) {
			if (s.getWinner() == null) continue;
 			if (s.getWinner().equals(player1.getUniqueId())) score1++;
			else if (s.getWinner().equals(player2.getUniqueId())) score2++;
		}
		
		ItemStack item = new ItemStack(Material.BOOK);
		ItemStackUtils.setDisplayName(item, "§2§lThông tin ván đấu");
		ItemStackUtils.addLoreLine(item, "§6Số ván tối đa: §f" + sum);
		ItemStackUtils.addLoreLine(item, "§e" + player1.getName() + " " + score1 + "/" + score2 + " " + player2.getName());
		return item;
	}
	
}
