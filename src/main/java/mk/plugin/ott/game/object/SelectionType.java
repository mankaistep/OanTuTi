package mk.plugin.ott.game.object;

public enum SelectionType {
	
	KEO("Kéo"),
	BUA("Búa"),
	BAO("Bao"),
	THUA("Không");
	
	private String name;
	
	private SelectionType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public static boolean canWin(SelectionType t1, SelectionType t2) {
		if (t1 == THUA) return false;
		if (t2 == THUA) return true;
		switch (t1) {
		case KEO:
			if (t2 == BAO) return true;
			break;
		case BUA:
			if (t2 == KEO) return true;
			break;
		case BAO:
			if (t2 ==  BUA) return true;
			break;
		default:
			return false;
		}
		return false;
	}
	
}
