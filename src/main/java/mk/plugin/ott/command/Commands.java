package mk.plugin.ott.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import mk.plugin.ott.game.manager.InvitationUtils;
import mk.plugin.ott.gui.TypeSelectGUI;
import mk.plugin.ott.lang.Lang;
import mk.plugin.ott.main.MainOTT;

public class Commands implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		
		Player player = (Player) sender;

		if (!sender.hasPermission("oantuti.play")) {
			player.sendMessage("§cBạn không thể dùng lệnh này bây giờ!");
			return false;
		}
		
		if (args.length == 0) {
			sender.sendMessage("");
			sender.sendMessage("§2§lHướng dẫn Oẳn Tù Tì");
			sender.sendMessage("§a/ott thachdau <player>: §fGửi lời mời");
			sender.sendMessage("§a/ott dongy: §fĐồng ý lời mời");
			sender.sendMessage("§a/ott tuchoi: §fTừ chối lời mời");
		}
		
		
		else if (args[0].equalsIgnoreCase("thachdau")) {
			try {
				if (!Bukkit.getOfflinePlayer(args[1]).isOnline()) {
					sender.sendMessage(Lang.INVITE_NOT_ONLINE.get());
					return false;
				}
				if (player.getName().equalsIgnoreCase(args[1])) {
					sender.sendMessage(Lang.INVITE_YOURSELF.get());
					return false;
				}
				
				Player target = Bukkit.getPlayer(args[1]);
				player.setMetadata("ott.target", new FixedMetadataValue(MainOTT.plugin, target.getUniqueId() + ""));
				target.setMetadata("ott.target", new FixedMetadataValue(MainOTT.plugin, player.getUniqueId() + ""));
				TypeSelectGUI.openGUI(player);
			}
			catch (ArrayIndexOutOfBoundsException e) {
				for (String s : Lang.COMMAND_PK.get().split(";")) {
					sender.sendMessage(s);
				}
			}
		}
		
		else if (args[0].equalsIgnoreCase("dongy")) {
			try {
				if (!InvitationUtils.hasInvitation(player.getUniqueId())) {
					player.sendMessage("§cKhông có lời mời");
					return false;
				}
				if (InvitationUtils.getInvitation(player.getUniqueId()).getChallenger().equals(player.getUniqueId())) {
					player.sendMessage(Lang.INVITE_AGREE_YOURSELF.get());
					return false;
				}
				InvitationUtils.agree(player.getUniqueId());
			}
			catch (ArrayIndexOutOfBoundsException e) {
				for (String s : Lang.COMMAND_DONGY.get().split(";")) {
					sender.sendMessage(s);
				}
			}
		}
		
		else if (args[0].equalsIgnoreCase("tuchoi")) {
			try {
				if (!InvitationUtils.hasInvitation(player.getUniqueId())) {
					player.sendMessage("§cKhông có lời mời");
					return false;
				}
				InvitationUtils.refuse(player.getUniqueId());
			}
			catch (ArrayIndexOutOfBoundsException e) {
				for (String s : Lang.COMMAND_TUCHOI.get().split(";")) {
					sender.sendMessage(s);
				}
			}
		}

		
		return false;
	}

}
