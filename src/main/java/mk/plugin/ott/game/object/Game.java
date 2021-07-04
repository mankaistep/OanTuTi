package mk.plugin.ott.game.object;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Game {
	
	private UUID challenger;
	private UUID target;
	private Map<UUID, Asset> asset;
	private int times;
	private List<Selection> selections;
	
	public Game(UUID challenger, UUID target, int times) {
		this.challenger = challenger;
		this.target = target;
		this.times = times;
		this.selections = Lists.newArrayList();
	}

	public UUID getChallenger() {
		return challenger;
	}

	public UUID getTarget() {
		return target;
	}
	
	public List<UUID> getPlayers() {
		return Lists.newArrayList(this.target, this.challenger);
	}
	
	public UUID getOpponent(UUID uuid) {
		if (uuid.equals(this.target)) return this.challenger;
		return this.target;
	}

	public Map<UUID, Asset> getAsset() {
		return asset;
	}
	
	public void setAsset(Map<UUID, Asset> asset) {
		this.asset = asset;
	}

	public void setAsset(UUID id, double money, int point, List<ItemStack> items) {
		if (asset == null) asset = Maps.newHashMap();
		if (asset.containsKey(id)) {
			Asset a = asset.get(id);
			a.setMoney(money);
			a.setPoint(point);
			a.setItems(items);
		}
		else asset.put(id, new Asset(money, point, Lists.newArrayList()));
	}
	
	public List<Selection> getSelections() {
		return selections;
	}
	
	public Selection getSelection(int index) {
		return this.selections.get(index);
	}
	
	public Selection getLastSelection() {
		if (this.selections.size() == 0) return null;
		return this.selections.get(this.selections.size() - 1);
	}
	
	public Selection createSelection() {
		Selection last = new Selection();
		this.selections.add(last);
		return last;
	}
	
	public Selection getLastOrCreateSelection() {
		Selection last = null;
		if (this.selections.size() == 0)  return createSelection();
		last = this.selections.get(this.selections.size() - 1);
		if (last.getSelections().size() == 2) return createSelection();
		return last;
	}
	
	public int addNewSelection(Selection selection) {
		this.selections.add(selection);
		return this.selections.size() - 1;
	}

	public int getTimes() {
		return this.times;
	}
	
	public UUID getWinner() { 
		int c = 0;
		if (this.selections.size() > 0 && this.getLastSelection().getSelections().size() == 2)  {
			for (Selection s : this.selections) {
				if (s.getWinner().equals(this.challenger)) c++;
			}
		}
		if (c > times / 2) return this.challenger;
		return this.target;
	}
	
	public UUID getLoser() {
		if (getWinner().equals(challenger)) return target;
		return challenger;
	}

	public boolean canFinish() {
		Player player1 = Bukkit.getPlayer(this.getChallenger());
		Player player2 = Bukkit.getPlayer(this.getTarget());
		int sum = this.getTimes();
		int score1 = 0;
		int score2 = 0;
		for (Selection s : this.getSelections()) {
			if (s.getWinner() == null) continue;
			if (s.getWinner().equals(player1.getUniqueId())) score1++;
			else if (s.getWinner().equals(player2.getUniqueId())) score2++;
		}

		if (score1 > sum / 2 || score2 > sum / 2) return true;
		return false;
	}
	
}
