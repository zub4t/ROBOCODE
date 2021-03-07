package sushllbot;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;

import robocode.util.Utils;
import util.enemy.*;
import util.common.Util;

import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;

public class MyBot extends AdvancedRobot implements Bot {
	private ArrayList<Integer> surfDirections;
	private ArrayList<java.lang.Double> surfAbsBearings;
	private Point2D.Double myLocation;

	private Enemy enemy = null;

	public static Rectangle2D.Double _fieldRect = new java.awt.geom.Rectangle2D.Double(18, 18, 764, 564);
	public static double WALL_STICK = 160;

	public void run() {
		surfDirections = new ArrayList<>();
		surfAbsBearings = new ArrayList<>();
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);

		do {
			turnRadarRightRadians(java.lang.Double.POSITIVE_INFINITY);
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

		double bulletPower = enemy.getEnergy() - e.getEnergy();
		if (bulletPower < 3.01 && bulletPower > 0.09 && surfDirections.size() > 2) {
			EnemyWave ew = new EnemyWave();
			ew.setFireTime(getTime() - 1);
			ew.setBulletVelocity(Util.bulletVelocity(bulletPower));
			ew.setDistanceTraveled(Util.bulletVelocity(bulletPower));
			ew.setDirection(surfDirections.get(2));
			ew.setDirectAngle(surfAbsBearings.get(2));
			ew.setFireLocation((Point2D.Double) enemy.getLocation().clone());
			EnemyWaveManager.SINGLETON.enemyWaves.add(ew);
		}

		enemy.setEnergy(e.getEnergy());
		enemy.setLocation(Util.project(myLocation, absBearing, e.getDistance()));
		EnemyWaveManager.SINGLETON.updateWaves(getTime(), myLocation);
		doSurfing();
		setTurnGunRightRadians(Utils.normalRelativeAngle(absBearing - getGunHeadingRadians()));
		setFire(1);
	}

	public void onHitByBullet(HitByBulletEvent e) {

		if (!EnemyWaveManager.SINGLETON.enemyWaves.isEmpty()) {
			Point2D.Double hitBulletLocation = new Point2D.Double(e.getBullet().getX(), e.getBullet().getY());
			EnemyWave hitWave = null;
			for (int x = 0; x < EnemyWaveManager.SINGLETON.enemyWaves.size(); x++) {
				EnemyWave ew = EnemyWaveManager.SINGLETON.enemyWaves.get(x);

				if (Math.abs(ew.getDistanceTraveled() - myLocation.distance(ew.getFireLocation())) < 50
						&& Math.abs(Util.bulletVelocity(e.getBullet().getPower()) - ew.getBulletVelocity()) < 0.001) {
					hitWave = ew;
					break;
				}
			}
			if (hitWave != null) {
				EnemyWaveManager.SINGLETON.logHit(hitWave, hitBulletLocation);

				// We can remove this wave now, of course.
				EnemyWaveManager.SINGLETON.enemyWaves
						.remove(EnemyWaveManager.SINGLETON.enemyWaves.lastIndexOf(hitWave));
			}
		}
	}

	public void doSurfing() {
		EnemyWave surfWave = EnemyWaveManager.SINGLETON.getClosestSurfableWave(myLocation);
		if (surfWave == null) {
			return;
		}
		double dangerLeft = EnemyWaveManager.SINGLETON.checkDanger(surfWave, -1, this);
		double dangerRight = EnemyWaveManager.SINGLETON.checkDanger(surfWave, 1, this);

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
		// TODO Auto-generated method stub
		return getGunHeadingRadians();
	}

	@Override
	public java.awt.geom.Point2D.Double getMyLocation() {
		// TODO Auto-generated method stub
		return myLocation;
	}

}
