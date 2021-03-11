package marco.common;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import marco.monitoring.Wave;
import marco.monitoring.WaveManager;
import marco.util.Util;
import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class Moviment implements Component {
	public static int _keepDistanceOf = 20;
	private ArrayList<Integer> surfDirections = new ArrayList<>();
	private ArrayList<Double> surfAbsBearings = new ArrayList<>();
	private double oppEnergy;
	private Point2D.Double enemyLocation = null;
	private Point2D.Double myLocation;
	private AdvancedRobot bot;

	public double getOppEnergy() {
		return oppEnergy;
	}

	public void setOppEnergy(double oppEnergy) {
		this.oppEnergy = oppEnergy;
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		myLocation = new Point2D.Double(bot.getX(), bot.getY());
		double lateralVelocity = bot.getVelocity() * Math.sin(e.getBearingRadians());
		double absBearing = e.getBearingRadians() + bot.getHeadingRadians();
		bot.setTurnRadarRightRadians(Utils.normalRelativeAngle(absBearing - bot.getRadarHeadingRadians()) * 2);

		surfDirections.add(0, (lateralVelocity >= 0) ? 1 : -1);
		surfAbsBearings.add(0, absBearing + Math.PI);
		double bulletPower = oppEnergy - e.getEnergy();
		if (bulletPower < 3.01 && bulletPower > 0.09 && surfDirections.size() > 2) {
			Wave ew = new Wave();
			ew.setFireTime(bot.getTime() - 1);
			ew.setBulletVelocity(Util.bulletVelocity(bulletPower));
			ew.setDistanceTraveled(Util.bulletVelocity(bulletPower));
			ew.setDirection(surfDirections.get(2));
			ew.setDirectAngle(surfAbsBearings.get(2));
			ew.setFireLocation((Point2D.Double) enemyLocation.clone());
			WaveManager.SINGLETON.enemyWaves.add(ew);

		}

		oppEnergy = e.getEnergy();
		enemyLocation = Util.project(myLocation, absBearing, e.getDistance());
		WaveManager.SINGLETON.updateWaves(bot.getTime(), myLocation);

		if (!doSurfing()) {
//			double angle = Util.wallSmoothing(myLocation, e.getBearingRadians() * -1,
//					Math.sin(e.getBearingRadians()) > 0 ? 1 : -1, BattleField.battleField);
//			bot.setTurnRightRadians(angle);
//			bot.ahead(100);

		}
		bot.setTurnGunRightRadians(Utils.normalRelativeAngle(absBearing - bot.getGunHeadingRadians()));
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
				WaveManager.SINGLETON.logHit(hitWave, hitBulletLocation);

				// We can remove this wave now, of course.
				WaveManager.SINGLETON.enemyWaves.remove(WaveManager.SINGLETON.enemyWaves.lastIndexOf(hitWave));
			}
		}
	}

	public boolean doSurfing() {
		Wave surfWave = WaveManager.SINGLETON.getClosestSurfableWave(myLocation);
		if (surfWave != null) {

			double dangerLeft = WaveManager.SINGLETON.checkDanger(surfWave, -1, bot);
			double dangerRight = WaveManager.SINGLETON.checkDanger(surfWave, 1, bot);
			Point2D.Double p1, p2;
			p1 = Util.predictPosition(bot, surfWave, 1);
			p2 = Util.predictPosition(bot, surfWave, -1);
			double goAngle = Util.absoluteBearing(surfWave.getFireLocation(), myLocation);
			if (dangerLeft < dangerRight) {
				if (p2.distance(enemyLocation) < 100) {
					goAngle = Util.wallSmoothing(myLocation, goAngle - (Math.PI / 2), 1, BattleField.battleField);

				} else {
					goAngle = Util.wallSmoothing(myLocation, goAngle - (Math.PI / 2), -1, BattleField.battleField);

				}
			} else {
				if (p1.distance(enemyLocation) < 100) {
					goAngle = Util.wallSmoothing(myLocation, goAngle - (Math.PI / 2), -1, BattleField.battleField);

				} else {
					goAngle = Util.wallSmoothing(myLocation, goAngle - (Math.PI / 2), +1, BattleField.battleField);

				}
			}

			Util.driveWithAngle(bot, goAngle);
			return true;
		}
		return false;
	}

	@Override
	public void setOwner(AdvancedRobot bot) {
		this.bot = bot;
	}

	@Override
	public void doTickAction() {
		// TODO Auto-generated method stub

	}

}
