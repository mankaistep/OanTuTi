package mk.plugin.ott.game.manager;

import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import mk.plugin.ott.api.MoneyAPI;
import mk.plugin.ott.api.PointAPI;
import mk.plugin.ott.game.object.Asset;
import mk.plugin.ott.game.object.Game;
import mk.plugin.ott.game.object.Selection;
import mk.plugin.ott.game.object.SelectionType;
import mk.plugin.ott.gui.SelectGUI;
import mk.plugin.ott.lang.Lang;
import mk.plugin.ott.main.MainOTT;
import mk.plugin.ott.utils.Utils;

public class GameUtils {
	
	public static List<Game> games = Lists.newArrayList();
	
	public static boolean isInGame(UUID id) {
		for (Game game : games) {
			if (game.getPlayers().contains(id)) return true;
		}
		return false;
	}
	
	public static Game getGamePlaying(UUID id) {
		for (Game game : games) {
			if (game.getPlayers().contains(id)) return game;
		}
		return null;
	}
	
	public static void start(Game game) {
		// Check requirement
		boolean check = true;
		for (Entry<UUID, Asset> e : game.getAsset().entrySet()) {
			Player player = Bukkit.getPlayer(e.getKey());
			if (MoneyAPI.getMoney(player) < e.getValue().getMoney()) check = false;
			if (PointAPI.getPoint(player) < e.getValue().getPoint()) check = false;
		}
		if (!check) {
			Utils.sendMessage(game, Lang.ASSET_NOTENOUGH.get());
			return;
		}
		game.getAsset().forEach((id, asset) -> {
			MoneyAPI.moneyCost(Bukkit.getPlayer(id), asset.getMoney());
			PointAPI.pointCost(Bukkit.getPlayer(id), asset.getPoint());
		});
		
		game.getPlayers().forEach(uuid -> {
			Utils.sendMessage(uuid, Lang.GAME_START.get());
			SelectGUI.openGUI(Bukkit.getPlayer(uuid));
		});
		games.add(game);

		// Log
		MainOTT.plugin.getLogger().info("[OTT] Game start: " + Bukkit.getPlayer(game.getChallenger()).getName() + " challenges " + Bukkit.getPlayer(game.getTarget()).getName());
//		Utils.broadcast(Lang.ANNOUNCEMENT_START.get().replace("%target%", Bukkit.getPlayer(game.getTarget()).getName()).replace("%challenger%", Bukkit.getPlayer(game.getChallenger()).getName()));
	}
	
	public static void unExpectedFinish(Game game) {
		// Remove metadatas
		game.getPlayers().forEach(uuid -> {
			removeMetadatas(Bukkit.getPlayer(uuid));
		});
		
		// Asset
		game.getAsset().forEach((id, asset) -> {
			Player player = Bukkit.getPlayer(id);
			MoneyAPI.giveMoney(player, asset.getMoney());
			PointAPI.givePoint(player, asset.getPoint());
			asset.getItems().forEach(item -> {
				player.getInventory().addItem(item);
			});
			removeMetadatas(player);
			player.closeInventory();
		});
		
		// Remove game
		games.remove(game);
	}
	
	public static void finish(Game game, UUID winner, UUID loser) {
		// Remove metadatas
		game.getPlayers().forEach(uuid -> {
			removeMetadatas(Bukkit.getPlayer(uuid));
		});
		
		// Give money, point
		MoneyAPI.giveMoney(Bukkit.getPlayer(winner), game.getAsset().get(winner).getMoney() * 2);
		PointAPI.givePoint(Bukkit.getPlayer(winner), game.getAsset().get(winner).getPoint() * 2);
		
		// Give items
		List<ItemStack> wins = game.getAsset().get(loser).getItems();
		game.getAsset().get(loser).getItems().forEach(item -> {
			if (wins.contains(item)) {
				Bukkit.getPlayer(winner).getInventory().addItem(item);
			} else Bukkit.getPlayer(loser).getInventory().addItem(item);
		});
		game.getAsset().get(winner).getItems().forEach(item -> {
			Bukkit.getPlayer(winner).getInventory().addItem(item);
		});
		
		// Message
		Utils.sendMessage(winner, Lang.GAME_WIN.get());
		Utils.sendMessage(loser, Lang.GAME_LOSE.get());
		games.remove(game);
		
		Utils.broadcast(Lang.ANNOUNCEMENT_END.get().replace("%winner%", Bukkit.getPlayer(winner).getName()).replace("%loser%", Bukkit.getPlayer(loser).getName()));

		// Log
		MainOTT.plugin.getLogger().info("[OTT] Game end: " + Bukkit.getPlayer(game.getWinner()).getName() + " wins " + Bukkit.getPlayer(game.getLoser()).getName());
	}
	
	public static List<ItemStack> getRandomFrom(List<ItemStack> items, int amount) {
		amount = Math.min(amount, items.size());
		List<ItemStack> clone = Lists.newArrayList(items);
		List<ItemStack> result = Lists.newArrayList();
		while (result.size() < amount) {
			int index = new Random().nextInt(clone.size());
			result.add(clone.get(index));
			clone.remove(index);
		}
		return result;
	}

	
	public static void finish(Game game) {
		UUID winner = game.getWinner();
		UUID loser = game.getLoser();
		finish(game, winner, loser);
	}
	
	public static void select(UUID id, SelectionType type) {
		Game game = getGamePlaying(id);
		if (game == null) return;
		Selection s = game.getLastOrCreateSelection();
		if (s.getSelections().containsKey(id)) {
			return;
		}
		s.setSelection(id, type);
		Utils.sendMessage(id, Lang.GAME_SELECT.get().replace("%type%", type.getName()));
		checkSelection(id, game);

		// Log
		try {
			var player = Bukkit.getPlayer(id);
			MainOTT.plugin.getLogger().info("[OTT] Selection: " + player.getName() + " chose " + type.name() + " in a game with " + Bukkit.getPlayer(game.getOpponent(player.getUniqueId())).getName());
		}
		catch (NullPointerException e) {
			return;
		}
	}
	
	// Check last chooser
	public static void checkSelection(UUID id, Game game) { 
		Selection last = game.getLastSelection();
		if (last.getSelections().size() == 2) {
			// Reward
			UUID winner = last.getWinner();
			var pWinner = Bukkit.getPlayer(winner);
			// Tie
			if (winner == null) {
				last.clear();
				Utils.sendMessage(game, Lang.TURN_TIE.get());
				Bukkit.getScheduler().runTaskLater(MainOTT.plugin, () -> {
					game.getPlayers().forEach(uuid -> {
						SelectGUI.openGUI(Bukkit.getPlayer(uuid));
					});
				}, 15);
			}
			else {
				UUID loser = last.getLoser();
				var pLoser = Bukkit.getPlayer(loser);

				pWinner.sendMessage(Lang.TURN_WIN.get());
				pWinner.playSound(pWinner.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);

				pLoser.sendMessage(Lang.TURN_LOSE.get());
				pLoser.playSound(pLoser.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, 1);
				
				// Check if last turn
				if (game.canFinish()) {
					finish(game);
				}
				else {
					game.createSelection();
					game.getPlayers().forEach(uuid -> Utils.sendMessage(uuid, Lang.TURN_CONTINUE.get()));
					Bukkit.getScheduler().runTaskLater(MainOTT.plugin, () -> {
						game.getPlayers().forEach(uuid -> {
							SelectGUI.openGUI(Bukkit.getPlayer(uuid));
						});
					}, 15);
				}
			}

		}
		else Utils.sendMessage(id, Lang.TURN_WAIT.get());
	}
	
	public static List<String> getMetadatas() {
		return Lists.newArrayList("ott.checkmoney", "ott.checkpoint", "ott.item", "ott.selectgui", "ott.target", "ott.player1", "ott.player2", "ott.confirm");
	}
	
	public static void removeMetadatas(Player player) {
		getMetadatas().forEach(s -> {
			player.removeMetadata(s, MainOTT.plugin);
		});
	}
	
	
	
	
	
	
	
	
	
}
