package mk.plugin.ott.game.object;

import java.util.UUID;

import mk.plugin.ott.config.Configs;

public class Invitation {
	
	private UUID challenger;
	private long timeExpired;
	private int times;
	private double money;
	private int point;
	
	public Invitation(UUID challenger, long timeExpired, int times, double money, int point) {
		this.challenger = challenger;
		this.timeExpired = timeExpired;
		this.money =  money;
		this.times = times;
		this.point = point;
	}
	
	public Invitation(UUID challenger, double money, int point) {
		this.challenger = challenger;
		this.timeExpired = System.currentTimeMillis() + Configs.INVITATION_EXPIRE * 1000;
		this.money =  money;
		this.times = money == 0 ? point == 0 ? Configs.ITEM_TIMES : Configs.POINT_TIMES : Configs.MONEY_TIMES;
		this.point = point;
	}
	
	public UUID getChallenger() {
		return this.challenger;
	}
	
	public long getTimeExpired() {
		return this.timeExpired;
	}
	
	public int getGameTimes() {
		return this.times;
	}
	
	public boolean equals(Invitation i) {
		return this.challenger.equals(i.getChallenger()) && this.timeExpired == i.getTimeExpired();
	}
	
	public double getMoney() {
		return this.money;
	}
	
	public int getPoint() {
		return this.point;
	}
	
	public void setMoney(double money) {
		this.money = money;
	}
	
	public void setPoint(int point) {
		this.point = point;
	}
	
	public void setTimeExpired(long timeExpired) {
		this.timeExpired = timeExpired;
	}
	
}
