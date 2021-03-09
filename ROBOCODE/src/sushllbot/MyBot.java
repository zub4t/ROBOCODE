package sushllbot;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;

import robocode.util.Utils;
import util.enemy.*;
import util.common.Util;

import robocode.AdvancedRobot;
import robocode.BulletHitEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;

public class MyBot extends AdvancedRobot implements Bot {
	private ArrayList<Integer> surfDirections;
	private ArrayList<java.lang.Double> surfAbsBearings;
	private Point2D.Double myLocation;
	private double directAngle = 0;
	private int direction = 0;
	private Wave mw = null;
	private boolean isToSurf = true;

	private EnemyImpl enemy = null;

	public static double WALL_STICK = 160;

	public void run() {
		surfDirections = new ArrayList<>();
		surfAbsBearings = new ArrayList<>();
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);

		do {
			directAngle = getHeadingRadians() + Math.PI;
			double myLateralVelocity = (getVelocity() * Math.sin(getHeadingRadians()));
			direction = myLateralVelocity > 0 ? 1 : -1;
			turnRadarRightRadians(java.lang.Double.POSITIVE_INFINITY);
			ahead(100);

		} while (true);
	}

	public void onScannedRobot(ScannedRobotEvent e) {

		if (enemy == null) {
			enemy = new EnemyImpl(e.getName(), e.getEnergy(), e.getVelocity());
		}
		myLocation = new Point2D.Double(getX(), getY());
		double lateralVelocity = getVelocity() * Math.sin(e.getBearingRadians());
		double absBearing = e.getBearingRadians() + getHeadingRadians();
		setTurnRadarRightRadians(Utils.normalRelativeAngle(absBearing - getRadarHeadingRadians()) * 2);

		surfDirections.add(0, (lateralVelocity >= 0) ? 1 : -1);
		surfAbsBearings.add(0, absBearing + Math.PI);
		// Adicionar o Math.pi para trasnformar o norte verdadeiro em uma reta
		// horizontal, pois para
		// frente vai ser usado o absBearing entre 2 pontos e esse função calcula o
		// angulo formado pela diferença e entre as e o eixo das Abscissa

		double bulletPower = enemy.getEnergy() - e.getEnergy();
		if (bulletPower < 3.01 && bulletPower > 0.09 && surfDirections.size() > 2) {
			Wave ew = new Wave();
			ew.setFireTime(getTime() - 1);
			ew.setBulletVelocity(Util.bulletVelocity(bulletPower));
			ew.setDistanceTraveled(Util.bulletVelocity(bulletPower));
			ew.setDirection(surfDirections.get(2));
			ew.setDirectAngle(surfAbsBearings.get(2));
			ew.setFireLocation((Point2D.Double) enemy.getMyLocation().clone());
			WaveManager.SINGLETON.enemyWaves.add(ew);
		}

		enemy.setEnergy(e.getEnergy());
		enemy.setLocation(Util.project(myLocation, absBearing, e.getDistance()));
		WaveManager.SINGLETON.updateWaves(getTime(), myLocation);
		if (isToSurf)
			doSurfing();
		setTurnGunRightRadians(Utils.normalRelativeAngle(absBearing - getGunHeadingRadians()));
		// doShoot();
	}

	public void onHitByBullet(HitByBulletEvent e) {

		if (!WaveManager.SINGLETON.enemyWaves.isEmpty()) {
			Point2D.Double hitBulletLocation = new Point2D.Double(e.getBullet().getX(), e.getBullet().getY());
			Wave hitWave = null;
			for (int x = 0; x < WaveManager.SINGLETON.enemyWaves.size(); x++) {
				Wave ew = WaveManager.SINGLETON.enemyWaves.get(x);

				if (Math.abs(ew.getDistanceTraveled() - myLocation.distance(ew.getFireLocation())) < 50
						&& Math.abs(Util.bulletVelocity(e.getBullet().getPower()) - ew.getBulletVelocity()) < 0.001) {
					hitWave = ew;
					break;
				}
			}
			if (hitWave != null) {
				WaveManager.SINGLETON.logHit(hitWave, hitBulletLocation, WaveManager.SINGLETON.surfStats);

				// We can remove this wave now, of course.
				WaveManager.SINGLETON.enemyWaves.remove(WaveManager.SINGLETON.enemyWaves.lastIndexOf(hitWave));
			}
		}
	}

	@Override
	public void onBulletHit(BulletHitEvent event) {
		Point2D.Double bullet = enemy.getMyLocation();
		WaveManager.SINGLETON.logHit(mw, bullet, WaveManager.SINGLETON.enemyStat);

	}

	@Override
	public void doShoot() {
		mw = new Wave();
		mw.setFireTime(getTime());
		mw.setBulletVelocity(Util.bulletVelocity(1));
		mw.setDistanceTraveled(Util.bulletVelocity(1));
		mw.setDirection(direction);
		mw.setDirectAngle(directAngle);
		mw.setFireLocation((Point2D.Double) getMyLocation().clone());
		WaveManager.SINGLETON.myWaves.add(mw);

		int indexL = WaveManager.SINGLETON.guess(mw, 1, enemy);
		int indexR = WaveManager.SINGLETON.guess(mw, -1, enemy);

		// System.out.println(WaveManager.SINGLETON.enemyStat[indexL] + " " +
		// WaveManager.SINGLETON.enemyStat[indexR]);
		double offset = 0;
		if (indexL < indexR) {
			offset = Util.wallSmoothing(myLocation, getHeadingRadians() - (Math.PI / 2), -1, _fieldRect);
		} else {
			offset = Util.wallSmoothing(myLocation, getHeadingRadians() + (Math.PI / 2), 1, _fieldRect);
		}

		// +Math.PI;
		// System.out.println(goAngle);
		// goAngle -= getGunHeadingRadians();
		// setTurnGunLeft(goAngle);
		// WaveManager.SINGLETON.enmyStatPrint();
		// System.out.println(indexL + " " + indexR);

		setFire(1);
	}

	@Override
	public void onHitRobot(HitRobotEvent event) {
		isToSurf = false;
		double angle = Util.wallSmoothing(getMyLocation(), getHeadingRadians() + (Math.PI / 2), direction, _fieldRect);
		setTurnLeftRadians(angle);
		ahead(100);
		isToSurf = true;
	}

	@Override
	public void doSurfing() {
		Wave surfWave = WaveManager.SINGLETON.getClosestSurfableWave(myLocation);
		if (surfWave == null) {
			return;
		}
		double dangerLeft = WaveManager.SINGLETON.checkDanger(surfWave, -1, this);
		double dangerRight = WaveManager.SINGLETON.checkDanger(surfWave, 1, this);

		double goAngle = Util.absoluteBearing(surfWave.getFireLocation(), myLocation);
		if (dangerLeft < dangerRight) {
			goAngle = Util.wallSmoothing(myLocation, goAngle - (Math.PI / 2), -1, _fieldRect);
		} else {
			goAngle = Util.wallSmoothing(myLocation, goAngle + (Math.PI / 2), 1, _fieldRect);
		}

		Util.driveWithAngle(this, goAngle);
	}

	@Override
	public Double getFieldRect() {
		// TODO Auto-generated method stub
		return _fieldRect;
	}

	@Override
	public double getMyVelocity() {
		// TODO Auto-generated method stub
		return getVelocity();
	}

	@Override
	public double getMyHeadingRadians() {
		// TODO Auto-generated methodo stub
		return getGunHeadingRadians();
	}

	@Override
	public java.awt.geom.Point2D.Double getMyLocation() {
		// TODO Auto-generated method stub
		return myLocation;
	}

	public void onPaint(java.awt.Graphics2D g) {
		g.setColor(java.awt.Color.red);
		for (int i = 0; i < WaveManager.SINGLETON.enemyWaves.size(); i++) {
			Wave w = (Wave) (WaveManager.SINGLETON.enemyWaves.get(i));
			Point2D.Double center = w.getFireLocation();

			// int radius = (int)(w.distanceTraveled + w.bulletVelocity);
			// hack to make waves line up visually, due to execution sequence in robocode
			// engine
			// use this only if you advance waves in the event handlers (eg. in
			// onScannedRobot())
			// NB! above hack is now only necessary for robocode versions before 1.4.2
			// otherwise use:
			int radius = (int) w.getDistanceTraveled();

			// Point2D.Double center = w.fireLocation;
			if (radius - 40 < center.distance(myLocation))
				g.drawOval((int) (center.x - radius), (int) (center.y - radius), radius * 2, radius * 2);
		}
	}

	@Override
	public double getDirectAngle() {
		return directAngle;
	}

	@Override
	public double getDirection() {
		// TODO Auto-generated method stub
		return direction;
	}

	@Override
	public void setEnergy(double energy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setVelocity(double velocity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLocation(java.awt.geom.Point2D.Double location) {
		this.myLocation = location;
	}

}
