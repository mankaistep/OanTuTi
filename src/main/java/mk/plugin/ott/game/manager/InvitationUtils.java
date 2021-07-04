package mk.plugin.ott.game.manager;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import mk.plugin.ott.api.MoneyAPI;
import mk.plugin.ott.api.PointAPI;
import mk.plugin.ott.config.Configs;
import mk.plugin.ott.game.object.Game;
import mk.plugin.ott.game.object.Invitation;
import mk.plugin.ott.lang.Lang;
import mk.plugin.ott.main.MainOTT;

public class InvitationUtils {
	
	private static Map<UUID, Invitation> invitations = Maps.newHashMap();
	private static Map<UUID, Long> sent = Maps.newHashMap();
	
	
	public static Invitation getInvitation(UUID id) {
		return invitations.getOrDefault(id, null);
	}
	
	public static boolean hasInvitation(UUID id) {
		return getInvitation(id) != null;
	}
	
	public static boolean send(UUID id, Invitation i, boolean message) {
		Player target = Bukkit.getPlayer(id);
		Player challenger = Bukkit.getPlayer(i.getChallenger());
		
		if (hasInvitation(challenger.getUniqueId())) {
			challenger.sendMessage(Lang.INVITE_CHECK_INVITATION_FIRST.get());
			return false;
		}
		
		if (sent.containsKey(challenger.getUniqueId())) {
			if (System.currentTimeMillis() - sent.get(challenger.getUniqueId()) < Configs.INVITATION_EXPIRE * 1000) {
				challenger.sendMessage(Lang.INVITE_WAIT.get().replace("%invitation-expired%", Configs.INVITATION_EXPIRE + ""));
				return false;
			}
		}
		sent.put(challenger.getUniqueId(), System.currentTimeMillis());
		
		// ?
		if (target == challenger) return false;
		// Check if ingame
		if (GameUtils.isInGame(id)) {
			challenger.sendMessage(Lang.INVITE_INGAME.get());
			return false;
		}
		// Check invitation expiration
		if (invitations.containsKey(id)) {
			Invitation ei = invitations.get(id);
			if (ei.getTimeExpired() > System.currentTimeMillis()) {
				challenger.sendMessage(Lang.INVITE_ALREADY.get());
				return false; 
			}
		}
		invitations.put(id, i);
		if (message) {
			// Is money
			if (i.getMoney() > 0) {
				if (i.getMoney() > MoneyAPI.getMoney(challenger) || i.getMoney() > MoneyAPI.getMoney(target)) {
					challenger.sendMessage(Lang.INVITE_NOT_ENOUGH.get());
					return false;
				}
				target.sendMessage(Lang.INVITE_TARGET_MONEY.get().replace("%challenger%", challenger.getName()).replace("%money%", i.getMoney() + "").replace("%point%", i.getPoint() + ""));
				challenger.sendMessage(Lang.INVITE_CHALLENGER_MONEY.get().replace("%target%", target.getName()).replace("%money%", i.getMoney() + "").replace("%point%", i.getPoint() + ""));
			}
			// Point
			else if (i.getPoint() > 0) {
				if (i.getPoint() > PointAPI.getPoint(challenger) || i.getPoint() > PointAPI.getPoint(target)) {
					challenger.sendMessage(Lang.INVITE_NOT_ENOUGH.get());
					return false;
				}
				target.sendMessage(Lang.INVITE_TARGET_POINT.get().replace("%challenger%", challenger.getName()).replace("%money%", i.getMoney() + "").replace("%point%", i.getPoint() + ""));
				challenger.sendMessage(Lang.INVITE_CHALLENGER_POINT.get().replace("%target%", target.getName()).replace("%money%", i.getMoney() + "").replace("%point%", i.getPoint() + ""));
			}
			// Item
			else {
				target.sendMessage(Lang.INVITE_TARGET_ITEM.get().replace("%challenger%", challenger.getName()).replace("%money%", i.getMoney() + "").replace("%point%", i.getPoint() + ""));
				challenger.sendMessage(Lang.INVITE_CHALLENGER_ITEM.get().replace("%target%", target.getName()).replace("%money%", i.getMoney() + "").replace("%point%", i.getPoint() + ""));
			}
		}
		
		Bukkit.getScheduler().runTaskLaterAsynchronously(MainOTT.plugin, () -> {
			// Check invitation
			Invitation in = getInvitation(id);
			if (in == null || !in.equals(i)) return;
			// Remove
			remove(id, message);
		}, (i.getTimeExpired() - System.currentTimeMillis()) / 50 + 10);
		return true;
	}
	
	public static void remove(UUID id, boolean message) {
		invitations.remove(id);
		if (message) {
			Bukkit.getPlayer(id).sendMessage(Lang.INVITE_REMOVE.get());
		}
	}
	
//	public static void agreeItem(UUID id) {
//		Player player = Bukkit.getPlayer(id);
//		Player target = Bukkit.getPlayer(UUID.fromString(player.getMetadata("ott.target").get(0).asString()));
//		ItemUniqueSelectGUI.openGUI(player, target);
//		
//		// Remove invitation
//		remove(id, false);
//	}
	
	public static void agree(UUID id) {
		Invitation i = getInvitation(id);
		if (i == null) return;
		
		// Message
		Player target = Bukkit.getPlayer(id);
		Player challenger = Bukkit.getPlayer(i.getChallenger());
		target.sendMessage(Lang.INVITE_AGREE_TARGET.get().replace("%challenger%", challenger.getName()));
		challenger.sendMessage(Lang.INVITE_AGREE_CHALLENGER.get().replace("%target%", target.getName()));
		
		// Start game
		Game game = new Game(i.getChallenger(), id, i.getGameTimes());
		game.setAsset(target.getUniqueId(), i.getMoney(), i.getPoint(), Lists.newArrayList());
		game.setAsset(challenger.getUniqueId(), i.getMoney(), i.getPoint(), Lists.newArrayList());
		GameUtils.start(game);
		
		// Remove invitation
		remove(id, false);
	}
	
	public static void refuse(UUID id)  {
		Invitation i = getInvitation(id);
		if (i == null) return;
		
		// Message
		Player target = Bukkit.getPlayer(id);
		Player challenger = Bukkit.getPlayer(i.getChallenger());
		target.sendMessage(Lang.INVITE_REFUSE_TARGET.get().replace("%challenger%", challenger.getName()));
		challenger.sendMessage(Lang.INVITE_REFUSE_CHALLENGER.get().replace("%target%", target.getName()));
		
		// Remove invitation
		remove(id, false);
		
		// Remove metadata
		GameUtils.removeMetadatas(target);
		GameUtils.removeMetadatas(challenger);
	}
	
}
