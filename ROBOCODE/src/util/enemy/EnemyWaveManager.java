package util.enemy;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import robocode.util.Utils;
import sushllbot.Bot;
import util.common.Util;

public class EnemyWaveManager {
	public static EnemyWaveManager SINGLETON = new EnemyWaveManager();
	public ArrayList<EnemyWave> enemyWaves;
	public static int BINS = 47;
	public static double surfStats[] = new double[BINS];

	public EnemyWaveManager() {

		this.enemyWaves = new ArrayList<>();
	}

	/**
	 * Essa fun��o � usada para atualizar as "Waves" que estamos rastreando. Em que
	 * atualizar as "Waves" siginifica que atualizamos a possi��o espacial dos tiros
	 * j� disparados e aqueles que j� n�o tem mais como nos atingir s�o excluidos da
	 * "enemyWaves"
	 * 
	 * @param current_time
	 * @param myLocation
	 */
	public void updateWaves(long current_time, Point2D.Double myLocation) {
		for (int x = 0; x < enemyWaves.size(); x++) {
			EnemyWave ew = (EnemyWave) enemyWaves.get(x);
			double new_distance = (current_time - ew.getFireTime()) * ew.getBulletVelocity();
			ew.setDistanceTraveled(new_distance);
			if (ew.getDistanceTraveled() > myLocation.distance(ew.getFireLocation()) + 50) {
				enemyWaves.remove(x);
				x--;
			}
		}

	}

	/**
	 * Dado a localiza��o do robo atual, retorna a proxima "Wave" que devemos nos
	 * precupar.
	 * 
	 * @param myLocation
	 * @return Uma instancia da class EnemyWave ou NULL, para NULL basicamente n�o
	 *         pode haver nenhuma "Wave" que possa nos acertar.
	 */
	public EnemyWave getClosestSurfableWave(Point2D.Double myLocation) {
		double closestDistance = Double.POSITIVE_INFINITY;
		EnemyWave surfWave = null;
		for (int x = 0; x < enemyWaves.size(); x++) {
			EnemyWave ew = (EnemyWave) enemyWaves.get(x);
			double distance = myLocation.distance(ew.getFireLocation()) - ew.getDistanceTraveled();
			if (distance > ew.getBulletVelocity() && distance < closestDistance) {
				surfWave = ew;
				closestDistance = distance;
			}
		}

		return surfWave;
	}

	/**
	 * 
	 * @param ew
	 * @param targetLocation
	 * @return
	 */
	public static int getFactorIndex(EnemyWave ew, Point2D.Double targetLocation) {
		// CREDIT: Wave_Surfing_Tutorial,
		// https://www.robowiki.net/wiki/Wave_Surfing_Tutorial
		double offsetAngle = (Util.absoluteBearing(ew.getFireLocation(), targetLocation) - ew.getDirectAngle());
		// offsetAngle � o �ngulo em que o inimigo estava a mirar quando atirou.
		// Util.absoluteBearing(ew.getFireLocation(), targetLocation) descreve o �ngulo
		// de deslocamento desde que o inimigo atirou at� o exato momento.
		// ew.getDirectAngle() � um �ngulo normal com o �ngulo criado por
		// enemy.getBearingRadians() + getHeadingRadians(); 2 ticks antes de detectarmos
		// que o inimigo atirou

		double factor = Utils.normalRelativeAngle(offsetAngle) / Util.maxEscapeAngle(ew.getBulletVelocity())
				* ew.getDirection();

		// factor
		// representa o Bearing que o inimigo alcan�aria se maximizasse seu
		// �ngulo de escape enquanto se move em sua dire��o atual em rela��o ao rob� de
		// disparo (ou seja, no sentido hor�rio ou anti-hor�rio), -1.0 representa o
		// deslocamento de rumo que o inimigo alcan�aria se maximizasse seu �ngulo de
		// escape depois de inverter as dire��es, e 0,0 representa o deslocamento de
		// rumo que aponta diretamente para o inimigo
		// https://www.robowiki.net/wiki/Maximum_Escape_Angle
		return (int) Util.limit(0, (factor * ((BINS - 1) / 2)) + ((BINS - 1) / 2), BINS - 1);
	}

	/**
	 * 
	 * @param ew
	 * @param targetLocation
	 */
	public void logHit(EnemyWave ew, Point2D.Double targetLocation) {
		int index = getFactorIndex(ew, targetLocation);
		for (int x = 0; x < BINS; x++) {
			// for the spot bin that we were hit on, add 1;
			// for the bins next to it, add 1 / 2;
			// the next one, add 1 / 5; and so on...
			surfStats[x] += 1.0 / (Math.pow(index - x, 2) + 1);
		}
	}

	public double checkDanger(EnemyWave surfWave, int direction, Bot bot) {
		int index = getFactorIndex(surfWave, Util.predictPosition(bot, surfWave, direction));

		return surfStats[index];
	}

}
