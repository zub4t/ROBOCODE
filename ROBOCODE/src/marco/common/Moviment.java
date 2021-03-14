package marco.common;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import hackersNL.BerendBotje;
import marco.monitoring.Wave;
import marco.monitoring.WaveManager;
import robocode.AdvancedRobot;
import robocode.BattleEndedEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.HitByBulletEvent;
import robocode.RoundEndedEvent;
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
	private boolean isEnemyShoting = true;
	private int direction = 1;
	private int missedShot = 0;

	public double getOppEnergy() {
		return oppEnergy;
	}

	public void setOppEnergy(double oppEnergy) {
		this.oppEnergy = oppEnergy;
	}

	/**
	 * Função principal da movimentação, existem duas movimentações aqui,
	 * waveSurfing e ZigZagAllIn. A cada vez que o inimigo atira é gerada uma wave
	 * que usaremos para desviar das tiros. Wave seria como uma pedra caindo em um
	 * lago. A cada vez que é possivel "surfar" uma Wave decidimos qual a direção
	 * com o menor risco a tomar, baseado em um Guess Factor.
	 * https://robowiki.net/wiki/GuessFactor Caso haja uma oportunidade de Allin
	 * então a executamos, essa oportunidade vem de estarmos a uma distância de
	 * DISTANCE_TO_ALL_IN de um robô.
	 *
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		System.out.println("bot.getTime() "+bot.getTime()+" - "+WaveManager.SINGLETON.enemyWaves.size());
		if (bot.getTime() > 50 && WaveManager.SINGLETON.enemyWaves.size() == 0) {
			isEnemyShoting = false;
		}
		myLocation = new Point2D.Double(bot.getX(), bot.getY());
		double lateralVelocity = bot.getVelocity() * Math.sin(e.getBearingRadians());
		double absBearing = e.getBearingRadians() + bot.getHeadingRadians();
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
		if (myLocation.distance(enemyLocation) < DISTANCE_TO_ALL_IN || !isEnemyShoting) {
			int preferredDistance = 20;
			int side = e.getDistance() > preferredDistance ? 1 : -1;
			if (bulletPower < 3.01 && bulletPower > 0.09 && surfDirections.size() > 2)
				bot.setTurnRightRadians(e.getBearingRadians() * direction * side);
			if (bot.getDistanceRemaining() == 0) {
				direction *= -1;
				bot.setAhead((e.getDistance() / 4 + 60) * direction);
			}
			bot.setTurnRightRadians(e.getBearingRadians() + Math.PI / 2 - 0.6 * direction * side);

		} else {
			doSurfing();
		}

	}

	/**
	 * Metodo que irá incrementar o risco de seguirmos uma direção (esquerda ou
	 * direita),O GuessFactor irá nos dizer isso mais tarde.
	 */
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
				WaveManager.SINGLETON.enemyWaves.remove(WaveManager.SINGLETON.enemyWaves.lastIndexOf(hitWave));
			}
		}
	}

	public boolean doSurfing() {
		Wave surfWave = WaveManager.SINGLETON.getClosestSurfableWave(myLocation);
		if (surfWave != null) {
			double dangerLeft = WaveManager.SINGLETON.checkDanger(surfWave, -1, bot);
			double dangerRight = WaveManager.SINGLETON.checkDanger(surfWave, 1, bot);
			double goAngle = Util.absoluteBearing(surfWave.getFireLocation(), myLocation);
			if (dangerLeft < dangerRight) {
				goAngle = Util.wallSmoothing(myLocation, goAngle - (Math.PI / 2), -1,
						BattleField.battleFieldWithOffset);
			} else {
				goAngle = Util.wallSmoothing(myLocation, goAngle - (Math.PI / 2), +1,
						BattleField.battleFieldWithOffset);
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

	@Override
	public void onBulletMissed(BulletMissedEvent event) {
		missedShot++;

	}




	@Override
	public void onBulletHit(BulletHitEvent event) {
		// missedShot = 0;

	}

}
