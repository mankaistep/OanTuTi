package mk.plugin.ott.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

public class MoneyAPI {

	public static double getMoney(Player player) {
		Economy eco = getEconomy();
		return eco.getBalance(player);
	}

	public static boolean moneyCost(Player player, double money) {
		Economy eco = getEconomy();
		double moneyOfPlayer = eco.getBalance(player);
		if (moneyOfPlayer < money) {
			return false;
		}
		eco.withdrawPlayer(player, money);
		return true;
	}

	public static void giveMoney(Player player, double money) {
		Economy eco = getEconomy();
		eco.depositPlayer(player, money);
	}

	public static Economy getEconomy() {
		if (!Bukkit.getServer().getPluginManager().isPluginEnabled("Vault")) return null;
		RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) return null;
		return rsp.getProvider();
	}

}
