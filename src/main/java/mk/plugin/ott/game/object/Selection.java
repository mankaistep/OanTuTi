package mk.plugin.ott.game.object;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Selection {
	
	private Map<UUID, SelectionType> selections;
	private UUID winner;
	
	public Selection() {
		selections = Maps.newHashMap();
	}
	
	public Map<UUID, SelectionType> getSelections() {
		return this.selections;
	}
	
	public SelectionType getSelection(UUID uuid) {
		return this.selections.getOrDefault(uuid, null);
	}
	
	public UUID getWinner() {
		return this.winner;
	}
	
	public void clear() {
		this.selections.clear();
	}
	
	public void setSelection(UUID uuid, SelectionType st) {
		this.selections.put(uuid, st);
		this.winner = checkWinner();
	}
	
	public UUID checkWinner() {
		if (selections.size() == 0) return null;
		UUID winner = Lists.newArrayList(selections.keySet()).get(0);
		SelectionType type = selections.get(winner);
		boolean different = false;
		for (Entry<UUID, SelectionType> e : selections.entrySet()) {
			SelectionType c = e.getValue();
			if (!c.equals(type)) different = true;
			if (SelectionType.canWin(c, type)) {
				return e.getKey();
			}
		}
		if (!different) return null;
		return winner;
	}
	
	public UUID getLoser() {
		for (UUID id : selections.keySet()) {
			if (!id.equals(getWinner())) return id;
		}
		return null;
	}
	
}
