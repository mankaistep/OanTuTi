package mk.plugin.ott.lang;

import org.bukkit.configuration.file.FileConfiguration;

import mk.plugin.ott.main.MainOTT;
import mk.plugin.ott.yaml.YamlFile;

public enum Lang {
	
	INVITE_INGAME("§cĐối phương trong game rồi"),
	INVITE_TARGET_ITEM("§aNhận lời mời từ %challenger% %point% %money%"),
	INVITE_CHALLENGER_ITEM("§aĐã gửi lời mời cho %target%"),
	INVITE_TARGET_POINT("§aNhận lời mời từ %challenger% %point% %money%"),
	INVITE_CHALLENGER_POINT("§aĐã gửi lời mời cho %target%"),
	INVITE_TARGET_MONEY("§aNhận lời mời từ %challenger% %point% %money%"),
	INVITE_CHALLENGER_MONEY("§aĐã gửi lời mời cho %target%"),
	INVITE_REMOVE("§aLời mời hết hạn"),
	INVITE_AGREE_TARGET("§aChấp nhận lời mời"),
	INVITE_AGREE_CHALLENGER("§cĐối phương đã chấp nhận"),
	INVITE_REFUSE_TARGET("§aTừ chối lời mời"),
	INVITE_REFUSE_CHALLENGER("§aĐối phương đã từ chối"),
	INVITE_ALREADY("§cĐối phương đã có lời mời rồi, đợi đối phương từ chối trước"),
	INVITE_NOT_ENOUGH("§cTài sản của bạn hoặc đối phương không đủ"),
	INVITE_YOURSELF("§cKhông thể nhập tên bạn"),
	INVITE_NOT_ONLINE("§cĐối phương không online"),
	INVITE_WAIT("§cĐợi sau %invitation-expired% để gửi tiếp"),
	INVITE_CHECK_INVITATION_FIRST("§cXử lý lời mời của bạn trước khi gửi một lời mời khác"),
	INVITE_AGREE_YOURSELF("Bạn là người thách đấu mà ?"),
	
	COMMAND_PK("§c/ott pk <?>;Bla bla"),
	COMMAND_DONGY("§c/ott dongy;Bla bla"),
	COMMAND_TUCHOI("§c/ott tuchoi;Bla bla"),
	
	ENTER_POINT("§cNhập point vào"),
	ENTER_MONEY("Nhập money"),
	
	ASSET_NOTENOUGH("Tài sản của bạn hoặc đối phương không đủ"),
	
	GAME_START("Game bắt đầu"),
	GAME_WIN("Bạn thắng"),
	GAME_LOSE("Bạn thua"),
	GAME_SELECT("Bạn đã chọn %type%"),
	
	TURN_WIN("Thắng ván này"),
	TURN_LOSE("Thua ván này"),
	TURN_TIE("Hòa ván này"),
	TURN_CONTINUE("Tiếp tục nào"),
	TURN_WAIT("Đợi đối phương..."),
	
	ITEMSELECT_CANCEL("§cHủy game vì một trong 2 người thoát ra"),
	ITEMSELECT_NOTENOUGH("§cKhông đủ item"),
	
	ANNOUNCEMENT_START("%challenger% %target% start"),
	ANNOUNCEMENT_END("Win:%winner%  Lose:%loser% end"),
	
	;
	
	private String value;
	
	private Lang(String value) {
		this.value = value;
	}
	
	public String get() {
		return this.value;
	}
	
	public void set(String value) {
		this.value = value;
	}
	
	public String getName() {
		return this.name().toLowerCase().replace("_", "-");
	}
	
	public static void init() {
		FileConfiguration config = YamlFile.LANG.get();
		for (Lang l : Lang.values()) {
			if (config.contains(l.getName())) {
				l.set(config.getString(l.getName()).replace("&", "§"));
			}
			else {
				config.set(l.getName(), l.get().replace("§", "&"));
				YamlFile.LANG.save(MainOTT.plugin);
			}
		}
	}
	
}
