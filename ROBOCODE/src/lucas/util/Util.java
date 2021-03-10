package lucas.util;

import java.awt.geom.Point2D;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class Util {
	/**
	 * Calcula o ângulo do robô escaneado em radianos.
	 * 
	 * @param event
	 * @param myRobot
	 * @return Ângulo relativamente ao plano cartesiano do robô escaneado em
	 *         radianos.
	 */
	private static double absoluteBearingRadians(ScannedRobotEvent event, AdvancedRobot myRobot) {
		return event.getBearingRadians() + myRobot.getHeadingRadians();
	}

	/**
	 * Calcula a velocidade da bala segundo a física do robocode.
	 * 
	 * @param bulletPower
	 * @return Velocidade da bala.
	 */
	private static double getBulletSpeed(double bulletPower) {
		return 20 - (3.0 * bulletPower);
	}

	/**
	 * Roda a arma até apontar para a posição prevista do robô inimigo.
	 * 
	 * @param predictedX
	 * @param predictedY
	 * @param myRobot
	 */
	public static void setAimToPredictedPosition(double predictedX, double predictedY, AdvancedRobot myRobot) {
		double myX = myRobot.getX();
		double myY = myRobot.getY();
		double aimAngle = Utils.normalAbsoluteAngle(Math.atan2(predictedX - myX, predictedY - myY));
		myRobot.setTurnGunRightRadians(Utils.normalRelativeAngle(aimAngle - myRobot.getGunHeadingRadians()));
	}

	/**
	 * Calcula a força da bala em relação a distancia ao robô inimigo. Quão mais
	 * longe, menos energia na bala.
	 * 
	 * @param event
	 * @return Força da bala.
	 */
	public static double smartBulletPower(ScannedRobotEvent event) {
		double distance = event.getDistance();
		if (distance >= 150)
			return 1.0;
		else if (distance >= 75)
			return 2.0;
		else
			return 3.0;
	}

	/**
	 * Calcula a coordenada X do ponto onde o robô inimigo foi escaneado.
	 * 
	 * @param event
	 * @param myRobot
	 * @return Coordenada X do ponto onde o robô inimigo foi escaneado.
	 */
	public static double getEnemyX(ScannedRobotEvent event, AdvancedRobot myRobot) {
		double absBearing = absoluteBearingRadians(event, myRobot);
		return myRobot.getX() + event.getDistance() * Math.sin(absBearing);
	}

	/**
	 * Calcula a coordenada Y do ponto onde o robô inimigo foi escaneado.
	 * 
	 * @param event
	 * @param myRobot
	 * @return Coordenada Y do ponto onde o robô inimigo foi escaneado.
	 */
	public static double getEnemyY(ScannedRobotEvent event, AdvancedRobot myRobot) {
		double absBearing = absoluteBearingRadians(event, myRobot);
		return myRobot.getY() + event.getDistance() * Math.cos(absBearing);
	}

	/**
	 * Roda o radar para tentar manter o robô escaneado sempre no range do radar.
	 * 
	 * @param event
	 * @param myRobot
	 */
	public static void trackEnemyWithRadar(ScannedRobotEvent event, AdvancedRobot myRobot) {
		double absBearing = absoluteBearingRadians(event, myRobot);
		double radarHeading = myRobot.getRadarHeadingRadians();
		myRobot.setTurnRadarRightRadians(Utils.normalRelativeAngle(absBearing - radarHeading) * 2);
	}
	
	/**
	 * Checa se a distância percorrida pela bala na simulação é maior que a distância do robô
	 * inimigo ao nosso robô.
	 * 
	 * @param event
	 * @param myRobot
	 * @param bulletPower
	 * @param time
	 * @param predictedX
	 * @param predictedY
	 * @return true se a bala ainda não passou do inimigo na simulação do tiro; false se passou.
	 */
	public static boolean checkIfBulletDistanceIsGreaterThanEnemyDistance(ScannedRobotEvent event, AdvancedRobot myRobot,
			double bulletPower, double time, double predictedX, double predictedY) {
		double bulletSpeed = getBulletSpeed(bulletPower);
		double myX = myRobot.getX();
		double myY = myRobot.getY();
		double distanceToPredictedPosition = Point2D.Double.distance(myX, myY, predictedX, predictedY);
		return (time*bulletSpeed) < distanceToPredictedPosition;
	}
}
