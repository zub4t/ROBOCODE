package lucaspkg;

import java.awt.geom.Point2D;

import lucas.util.Util;

import robocode.*;
import robocode.util.Utils;

public class LucasRobot extends AdvancedRobot {
	// The coordinates of the last scanned robot
	int scannedX = Integer.MIN_VALUE;
	int scannedY = Integer.MIN_VALUE;

	public void run() {
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        do {
            turnRadarRight(Double.POSITIVE_INFINITY);
        } while (true);
    }

	public void onScannedRobot(ScannedRobotEvent event) {
		double bulletPower = Util.smartBulletPower(event);
		double enemyX = Util.getEnemyX(event, this);
		double enemyY = Util.getEnemyY(event, this);
		double enemyHeading = event.getHeadingRadians();
		double enemyVelocity = event.getVelocity();

		double simulationTime = 0; // No tempo simulationTime=0, o ponto previsto é onde o inimigo está
		double predictedX = enemyX;
		double predictedY = enemyY;
		// Simulação de onde o inimigo vai estar quando nossa bala alcançar ele
		while (Util.checkIfBulletDistanceIsGreaterThanEnemyDistance(event, this, bulletPower, simulationTime,
				predictedX, predictedY)) {
			predictedX += Math.sin(enemyHeading) * enemyVelocity;
			predictedY += Math.cos(enemyHeading) * enemyVelocity;
			// Se a previsão for fora da arena interrompe o ciclo
			if (predictedX < 0 || predictedX > 800 || predictedY < 0 || predictedY > 600)
				break;
			simulationTime++;
		}

		Util.setAimToPredictedPosition(predictedX, predictedY, this);
		this.setFire(bulletPower);

		Util.trackEnemyWithRadar(event, this);
	}

}