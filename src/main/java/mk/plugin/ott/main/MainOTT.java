package mk.plugin.ott.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import mk.plugin.ott.command.Commands;
import mk.plugin.ott.config.Configs;
import mk.plugin.ott.game.manager.GameUtils;
import mk.plugin.ott.lang.Lang;
import mk.plugin.ott.listener.Listeners;
import mk.plugin.ott.yaml.YamlFile;

public class MainOTT extends JavaPlugin {
	
	public static MainOTT plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		this.getCommand("ott").setExecutor(new Commands());
		
		YamlFile.reloadAll(this);
		Lang.init();
		Configs.load(YamlFile.CONFIG.get());
		
		Bukkit.getPluginManager().registerEvents(new Listeners(), this);
	}
	
	@Override
	public void onDisable() {
		GameUtils.games.forEach(game -> {
			GameUtils.unExpectedFinish(game);
			game.getPlayers().forEach(id -> {
				Bukkit.getPlayer(id).closeInventory();
			});
		});
	}
	
}
