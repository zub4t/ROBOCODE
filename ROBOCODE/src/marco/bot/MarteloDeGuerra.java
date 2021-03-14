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
import robocode.RoundEndedEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;
import util.common.Util;
import util.enemy.EnemyImpl;

public class MarteloDeGuerra extends AdvancedRobot {

	/*
	 * Esse robô tem como inspiração os artigos abaixo do roboWiki.
	 * https://robowiki.net/wiki/Maximum_Escape_Angle
	 * https://robowiki.net/wiki/GuessFactor
	 * https://robowiki.net/wiki/Wave_Surfing
	 */

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
		BattleField.setRectWithOffset(18, 18, (int) getBattleFieldWidth() - 100, (int) getBattleFieldHeight() - 100);
		BattleField.setRect(0, 0, (int) getBattleFieldWidth(), (int) getBattleFieldHeight());

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
	public void onRoundEnded(RoundEndedEvent event) {
		WaveManager.SINGLETON.enemyWaves.clear();
		
	}
	public void paintRobot() {
		float red = (float) Math.random();
		float green = (float) Math.random();
		float blue = (float) Math.random();
		setBodyColor(new Color(red, green, blue));
		setGunColor(new Color(red, green, blue));
		setRadarColor(new Color(red, green, blue));
		setScanColor(new Color(red, green, blue));
	}
}
