package mk.plugin.ott.listener;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import mk.plugin.ott.game.manager.GameUtils;
import mk.plugin.ott.game.manager.InvitationUtils;
import mk.plugin.ott.game.object.Game;
import mk.plugin.ott.game.object.Invitation;
import mk.plugin.ott.gui.SelectGUI;
import mk.plugin.ott.gui.TypeSelectGUI;
import mk.plugin.ott.main.MainOTT;

public class Listeners implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		TypeSelectGUI.onClick(e);
		SelectGUI.onClick(e);
//		ItemSelectGUI.onClick(e);
//		ItemUniqueSelectGUI.onClick(e);
	}
	
//	@EventHandler
//	public void onClose(InventoryCloseEvent e) {
//		ItemUniqueSelectGUI.onClose(e);
//	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		Game game = GameUtils.getGamePlaying(player.getUniqueId());
		if (game != null) {
//			GameUtils.unExpectedFinish(game);
			GameUtils.finish(game, game.getOpponent(player.getUniqueId()), player.getUniqueId());
		}
		
	}
	
//	@EventHandler
//	public void onDrag(InventoryDragEvent e) {
//		ItemSelectGUI.onDrag(e);
//		ItemUniqueSelectGUI.onDrag(e);
//	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();
		String mess = e.getMessage();

		if (player.hasMetadata("ott.checkmoney") || player.hasMetadata("ott.checkpoint")) {
			if (!mess.matches("\\d+(\\.)?")) {
				player.sendMessage("§cNhập đầu buồi gì thế? Nhập lại đi địt cụ");
				e.setCancelled(true);
				return;
			}
			UUID target = UUID.fromString(player.getMetadata("ott.target").get(0).asString());
			double money = 0;
			int point = 0;
			if (player.hasMetadata("ott.checkmoney")) {
				money = Double.valueOf(mess);
				if (money == 0) {
					player.sendMessage("§cNhập một số khác 0");
					return;
				}
				player.removeMetadata("ott.checkmoney", MainOTT.plugin);
			}
			else if (player.hasMetadata("ott.checkpoint")) {
				point = Integer.valueOf(mess);
				if (point == 0) {
					player.sendMessage("§cNhập một số khác 0");
					return;
				}
				player.removeMetadata("ott.checkpoint", MainOTT.plugin);
			}

			if (money > 0) player.sendMessage("§aBạn vừa nhập §f" + money + "$");
			if (point > 0) player.sendMessage("§aBạn vừa nhập §e" + point + "P");

			Invitation i = new Invitation(player.getUniqueId(), money, point);
			InvitationUtils.send(target, i, true);
			e.setCancelled(true);
		}

	}
	
}
