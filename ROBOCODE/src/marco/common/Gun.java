package marco.common;

import lucas.util.Util;
import robocode.AdvancedRobot;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;

public class Gun implements Component {
	private AdvancedRobot bot;

	public void onScannedRobot(ScannedRobotEvent event) {
		double bulletPower = Util.smartBulletPower(event);
		double enemyX = Util.getEnemyX(event, bot);
		double enemyY = Util.getEnemyY(event, bot);
		double enemyHeading = event.getHeadingRadians();
		double enemyVelocity = event.getVelocity();
		double simulationTime = 0; // No tempo simulationTime=0, o ponto previsto é onde o inimigo está
		double predictedX = enemyX;
		double predictedY = enemyY;
		// Simulação de onde o inimigo vai estar quando nossa bala alcançar ele
		while (Util.checkIfBulletDistanceIsGreaterThanEnemyDistance(event, bot, bulletPower, simulationTime, predictedX,
				predictedY)) {
			predictedX += Math.sin(enemyHeading) * enemyVelocity;
			predictedY += Math.cos(enemyHeading) * enemyVelocity;
			// Se a previsão for fora da arena interrompe o ciclo
			if (predictedX < 0 || predictedX > 800 || predictedY < 0 || predictedY > 600)
				break;
			simulationTime++;
		}

		Util.setAimToPredictedPosition(predictedX, predictedY, bot);
		bot.setFire(bulletPower);

		Util.trackEnemyWithRadar(event, bot);
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
