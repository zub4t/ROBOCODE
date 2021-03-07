package util.enemy;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

public class EnemyImpl extends Object implements Enemy {
	private String name = null;
	private double energy = 0;
	private double velocity = 0;
	private double previusEnergy = 0;
	private Point2D.Double location = null;

	public EnemyImpl(String name, double energy, double velocity) {
		super();
		this.name = name;
		this.energy = energy;
		this.velocity = velocity;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public double getEnergy() {
		// TODO Auto-generated method stub
		return energy;
	}

	@Override
	public double getVelocity() {
		// TODO Auto-generated method stub
		return velocity;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public void setPreviusEnergy(double previusEnergy) {
		this.previusEnergy = previusEnergy;
	}

	@Override
	public Point2D.Double getLocation() {
		// TODO Auto-generated method stub
		return location;
	}

	@Override
	public void setLocation(Point2D.Double location) {
		this.location = location;

	}

}
