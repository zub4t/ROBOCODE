package util.common;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import marco.common.BattleField;
import robocode.AdvancedRobot;
import robocode.util.Utils;
import util.enemy.Wave;
import sushllbot.Bot;

public class Util {
	/**
	 * Esse metodo descreve o comportamento de getHeading() + enemy.getBearing(),
	 * mas usando posi��es em um plano cartesiano de 2 cordenadas. (x,y)
	 * 
	 * @param source O ponto da sua localiza��o
	 * @param target O ponto da localiza��o do inimigo
	 * @return Um double "absoluteBearing" que � a soma do angulo que voc� est�
	 *         virado, de acordo com as cordernadas no Robocode, "+" o angulo que o
	 *         seu inimigo est� em rela��o a voc�.
	 */
	public static double absoluteBearing(Point2D.Double source, Point2D.Double target) {
		return Math.atan2(target.x - source.x, target.y - source.y);
	}

	public static double absoluteBearing2(Point2D.Double source, Point2D.Double target) {
		double dx = target.x - source.x;
		double dy = target.y - source.y;
		double d = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		if (d != 0) {
			double t = (180 / Math.PI) * Math.acos(dx / d);
			if (dy < 0) {
				return 360 - t;
			}
		}
		return 0;

	}

	/**
	 * Fun��o usado para normalizar um valor entre um m�nimo e um m�ximo
	 * 
	 * @param min   Valor base de um conjunto.
	 * @param value Valor a ser normalizado dentro do conjunto. defindo pelos
	 *              limites min e max
	 * @param max   Valor mais alto de um conjunto.
	 * @return Valor normalizado, garante que min=<value>=max
	 */
	// clamp
	public static double limit(double min, double value, double max) {
		return Math.max(min, Math.min(value, max));
	}

	/**
	 * Metodo usado para descrever a velocidade de um bala dado sua energia, Metodo
	 * descrito em robocode physics.
	 * 
	 * @param power Poder da bala que foi disparada.
	 * @return Um inteiro que descreve a velocidade da bala.
	 */
	public static double bulletVelocity(double power) {
		return (20D - (3D * power));
	}

	/**
	 * When firing, the Maximum Escape Angle (MEA) is the largest angle offset from
	 * zero (i.e., Head-On Targeting) that could possibly hit an enemy bot, given
	 * the Game Physics of Robocode. With a maximum Robot velocity of 8.0, a
	 * theoretical Maximum Escape Angle would be asin(8.0/Vb). Note that the actual
	 * maximum depends on the enemy's current heading, speed, and Wall Distance.
	 * 
	 * @param velocity Sua velocidade atual
	 * @return Um double que descreve o angulo m�ximo que � possivel virar de acordo
	 *         com usa velocidade.
	 */
	public static double maxEscapeAngle(double velocity) {
		return Math.asin(8.0 / velocity);
	}

	/**
	 * Esse fun��o faz com que seu robo se movimente em uma dire��o com um certo
	 * �ngulo, de maneira n�o aditiva exemplo : se der como goAngle 90graus � seu
	 * robot j� estiver virado a 45graus ent�o seu robo � virado somente 45graus. O
	 * sentido varia de acordo com o �ngulo para �ngulos que s�o menores que 90graus
	 * em valores absolutos o robo vai a frente , ao contr�rio atr�s.
	 * 
	 * @param robot   Um robot que herda de AdvancedRobot.
	 * @param goAngle Um angulo para onde se movimentar.
	 */
	public static void driveWithAngle(AdvancedRobot robot, double goAngle) {
		double angle = Utils.normalRelativeAngle(goAngle - robot.getHeadingRadians());
		if (Math.abs(angle) > (Math.PI / 2)) {
			if (angle < 0) {
				robot.setTurnRightRadians(Math.PI + angle);
			} else {
				robot.setTurnLeftRadians(Math.PI - angle);
			}
			robot.setBack(100);
		} else {
			if (angle < 0) {
				robot.setTurnLeftRadians(-1 * angle);
			} else {
				robot.setTurnRightRadians(angle);
			}
			robot.setAhead(100);
		}
	}

	/**
	 * Projeta um vetor com origem em na localiza��o do seu robo atual com magnitude
	 * e �ngulo fornecidos.
	 * 
	 * @param sourceLocation A sua localiza��o atual new
	 *                       Point2D.Double(getX(),getY()).
	 * @param angle          O �ngulo em que vetor estar� a apontar
	 * @param length         A magnitude do vetor.
	 * @return Um inst�ncia de Point2D.Double que � o ponto final do vetor.
	 */
	public static Point2D.Double project(Point2D.Double sourceLocation, double angle, double length) {
		// CREDIT: from CassiusClay, by PEZ robowiki.net?CassiusClay
		return new Point2D.Double(sourceLocation.x + Math.sin(angle) * length,
				sourceLocation.y + Math.cos(angle) * length);
	}

	/**
	 * Usado para n�o bater na parede do campo de batalha, essa fun��o projeta um
	 * vetor do centro do robo com um tamnho X e um certo �ngulo, usando a fun��o
	 * <b>poject</b> Verifica se o final do vetor ainda est� dentro do campo de
	 * batalha, caso n�o esteja vai incrementando o �ngulo at� que fique.
	 * 
	 * @param botLocation Localiza��o atual do seu robo.
	 * @param angle       �ngulo inicial que seu robo est�.
	 * @param orientation Sentidado em que ele est� a andar, frente ou tras.
	 * @param fieldRect   Campo de batalha em que estamos.
	 * @return
	 */
	public static double wallSmoothing(Point2D.Double botLocation, double angle, int orientation,
			Rectangle2D.Double fieldRect) {
		// CREDIT: Iterative WallSmoothing by Kawigi, robowiki.net?WallSmoothing
		while (!fieldRect.contains(project(botLocation, angle, 160))) {
			angle += orientation * 0.05;
		}
		return angle;
	}

	public static Point2D.Double predictPosition(AdvancedRobot bot, Wave surfWave, int direction) {
		// CREDIT: mini sized predictor from Apollon, by rozu
		// http://robowiki.net?Apollon
		Point2D.Double predictedPosition = new Point2D.Double(bot.getX(), bot.getY());
		double predictedHeading = bot.getHeadingRadians();
		double predictedVelocity = bot.getVelocity();
		double maxTurning, moveAngle, moveDir;

		int counter = 0; // number of ticks in the future
		boolean intercepted = false;

		do {
			moveAngle = wallSmoothing(predictedPosition,
					absoluteBearing(surfWave.getFireLocation(), predictedPosition) + (direction * (Math.PI / 2)),
					direction, BattleField.battleField) - predictedHeading;
			moveDir = 1;

			if (Math.cos(moveAngle) < 0) {
				moveAngle += Math.PI;
				moveDir = -1;
			}

			moveAngle = Utils.normalRelativeAngle(moveAngle);

			// maxTurning is built in like this, you can't turn more then this in one tick
			maxTurning = Math.PI / 720d * (40d - 3d * Math.abs(predictedVelocity));
			predictedHeading = Utils.normalRelativeAngle(predictedHeading + limit(-maxTurning, moveAngle, maxTurning));

			// this one is nice ;). if predictedVelocity and moveDir have
			// different signs you want to breack down
			// otherwise you want to accelerate (look at the factor "2")
			predictedVelocity += (predictedVelocity * moveDir < 0 ? 2 * moveDir : moveDir);
			predictedVelocity = limit(-8, predictedVelocity, 8);

			// calculate the new predicted position
			predictedPosition = project(predictedPosition, predictedHeading, predictedVelocity);

			counter++;

			if (predictedPosition.distance(surfWave.getFireLocation()) < surfWave.getDistanceTraveled()
					+ (counter * surfWave.getBulletVelocity()) + surfWave.getBulletVelocity()) {
				intercepted = true;
			}
		} while (!intercepted && counter < 500);

		return predictedPosition;
	}

}
