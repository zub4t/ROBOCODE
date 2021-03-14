package marco.common;

import java.awt.geom.Point2D;

import robocode.AdvancedRobot;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;

public class Gun implements Component {
	private AdvancedRobot bot;
	private Point2D.Double myLocation;
	private Point2D.Double enemyLocation;
	private double bulletPower;
	private double enemyHeading;
	private double enemyVelocity;
	private double simulationTime;
	private double predictedX;
	private double predictedY;

	public void getInfoAboutTheEnemyRobot(ScannedRobotEvent event) {
		myLocation = new Point2D.Double(bot.getX(), bot.getY());
		double absBearing = event.getBearingRadians() + bot.getHeadingRadians();
		enemyLocation = Util.project(myLocation, absBearing, event.getDistance());
		bulletPower = Util.smartBulletPower(event);
		enemyHeading = event.getHeadingRadians();
		enemyVelocity = event.getVelocity();
		simulationTime = 0;
		predictedX = enemyLocation.getX();
		predictedY = enemyLocation.getY();
	}

	/**
	 *A estrategia aqui é dar um previsão linear de onde o inimigo estára se seguir a mesma direção de quando miramos. 
	 */
	public void onScannedRobot(ScannedRobotEvent event) {
		getInfoAboutTheEnemyRobot(event);
		while (Util.checkIfBulletDistanceIsGreaterThanEnemyDistance(event, bot, bulletPower, simulationTime, predictedX,
				predictedY)) {
			predictedX += Math.sin(enemyHeading) * enemyVelocity;
			predictedY += Math.cos(enemyHeading) * enemyVelocity;
			Point2D.Double predictedPosition = new Point2D.Double(predictedX, predictedY);
			if (!BattleField.battleField.contains(predictedPosition))
				break;
			simulationTime++;
		}
		Util.setAimToPredictedPosition(predictedX, predictedY, bot);

		if (myLocation.distance(enemyLocation) < DISTANCE_TO_ALL_IN)
			bot.setFire(3);
		if (event.getEnergy() < 90)
			bot.setFire(1);

	}

	@Override
	public void doTickAction() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHitByBullet(HitByBulletEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOwner(AdvancedRobot bot) {
		this.bot = bot;
	}

	@Override
	public void onBulletMissed(BulletMissedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBulletHit(BulletHitEvent event) {
		// TODO Auto-generated method stub

	}

}
