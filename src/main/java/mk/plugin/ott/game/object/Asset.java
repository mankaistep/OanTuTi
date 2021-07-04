package mk.plugin.ott.game.object;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public class Asset {
	
	private double money;
	private int point;
	private List<ItemStack> items;
	
	public Asset(double money, int point, List<ItemStack> items) {
		this.money = money;
		this.point = point;
		this.items = items;
	}
	
	public double getMoney() {
		return this.money;
	}
	
	public int getPoint() {
		return this.point;
	}
	
	public List<ItemStack> getItems() {
		return this.items;
	}
	
	public void setMoney(double money) {
		this.money = money;
	}
	
	public void setPoint(int point) {
		this.point =  point;
	}
	
	public void setItems(List<ItemStack> items) {
		this.items = items;
	}
	
}
