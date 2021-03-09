package util.enemy;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import robocode.util.Utils;
import sushllbot.Bot;
import util.common.Util;

public class EnemyImpl extends Object implements Enemy, Bot {
	private String name = null;
	private double energy = 0;
	private double velocity = 0;
	private double previusEnergy = 0;

	private Point2D.Double location = null;
	Wave enemyWave;

	public EnemyImpl(String name, double energy, double velocity) {
		super();
		this.name = name;
		this.energy = energy;
		this.velocity = velocity;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public double getEnergy() {
		// TODO Auto-generated method stub
		return energy;
	}

	@Override
	public void setEnergy(double energy) {
		// TODO Auto-generated method stub
		this.energy = energy;

	}

	@Override
	public java.awt.geom.Rectangle2D.Double getFieldRect() {
		// TODO Auto-generated method stub
		return _fieldRect;
	}

	@Override
	public double getMyVelocity() {
		// TODO Auto-generated method stub
		return velocity;
	}

	@Override
	public double getMyHeadingRadians() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Point2D.Double getMyLocation() {
		// TODO Auto-generated method stub
		return location;
	}

	@Override
	public double getDirectAngle() {
		// TODO Auto-generated method stub
		return enemyWave.getDirectAngle();
	}

	@Override
	public double getDirection() {
		// TODO Auto-generated method stub
		return enemyWave.getDirection();
	}

	@Override
	public void setVelocity(double velocity) {
		// TODO Auto-generated method stub
		this.velocity = velocity;
	}

	@Override
	public void setLocation(Point2D.Double location) {
		// TODO Auto-generated method stub
		this.location=location;
	}

	

	@Override
	public void doShoot() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSurfing() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setWave(Wave wave) {
		// TODO Auto-generated method stub
		this.enemyWave = wave;

	}

	@Override
	public Wave getWave() {
		// TODO Auto-generated method stub
		return enemyWave;
	}

}
