package mk.plugin.ott.yaml;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public enum YamlFile {
	
	CONFIG,
	LANG;
	
	private FileConfiguration config;
	
	private YamlFile() {}
	
	public FileConfiguration get() {
		return this.config;
	}
	
	public String getName() {
		return this.name().toLowerCase().replace("_", "-") + ".yml";
	}
	
	public void reload(Plugin plugin) {
		File file = new File(plugin.getDataFolder(), getName());
		if (!file.exists()) {
			plugin.saveResource(getName(), false);
			reload(plugin);
		}
		this.config = YamlConfiguration.loadConfiguration(file);
	}
	
	public void save(Plugin plugin) {
		File file = new File(plugin.getDataFolder(), getName());
		try {
			this.config.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void save(Plugin plugin, FileConfiguration config) {
		File file = new File(plugin.getDataFolder(), getName());
		try {
			config.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void reloadAll(Plugin plugin) {
		for (YamlFile file : values()) file.reload(plugin);
	}
	
}
