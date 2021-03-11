package marco.bot;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import marco.common.BattleField;
import marco.common.Component;
import marco.common.Gun;
import marco.common.Moviment;
import marco.common.Radar;
import marco.monitoring.Wave;
import marco.monitoring.WaveManager;
import robocode.AdvancedRobot;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;
import util.common.Util;
import util.enemy.EnemyImpl;

public class MarteloDeGuerra extends AdvancedRobot {
	private Component moviment = new Moviment();
	private Component gun = new Gun();
	private Component radar = new Radar();

	private double oppEnergy;
	private Point2D.Double enemyLocation = null;
	private Point2D.Double myLocation;

	public void run() {
		moviment.setOwner(this);
		gun.setOwner(this);
		radar.setOwner(this);
		BattleField.setRect(18, 18, 764, 564);
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		do {
			turnRadarRightRadians(java.lang.Double.POSITIVE_INFINITY);

		} while (true);
	}

	@Override
	public void onHitByBullet(HitByBulletEvent event) {
		// TODO Auto-generated method stub
		moviment.onHitByBullet(event);
	}

	@Override
	public void onScannedRobot(ScannedRobotEvent event) {
		// TODO Auto-generated method stub
		moviment.onScannedRobot(event);
		gun.onScannedRobot(event);
		radar.onScannedRobot(event);
		paintRobot();
	}

	@Override
	public void onBulletMissed(BulletMissedEvent event) {
		moviment.onBulletMissed(event);

	}

	@Override
	public void onBulletHit(BulletHitEvent event) {
		moviment.onBulletHit(event);

	}

	@Override
	public void onPaint(java.awt.Graphics2D g) {
		g.setColor(java.awt.Color.red);
		for (Wave w : WaveManager.SINGLETON.enemyWaves) {
			Point2D.Double center = w.getFireLocation();
			int radius = (int) w.getDistanceTraveled();
			// Point2D.Double center = w.fireLocation;
			if (radius - 40 < center.distance(new Point2D.Double(getX(), getY())))
				g.drawOval((int) (center.x - radius), (int) (center.y - radius), radius * 2, radius * 2);
		}
	}

	public void paintRobot() {
		float color1 = (float) Math.random();
		float color2 = (float) Math.random();
		float color3 = (float) Math.random();
		setBodyColor(new Color(color1, color2, color3));
		setGunColor(new Color(color1, color2, color3));
		setRadarColor(new Color(color1, color2, color3));
		setBulletColor(new Color(color1, color2, color3));
		setScanColor(new Color(color1, color2, color3));
	}
}
