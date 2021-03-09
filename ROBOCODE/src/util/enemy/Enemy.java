/**
 * 
 */
package util.enemy;

import java.awt.geom.Point2D;

import robocode.util.Utils;
import sushllbot.Bot;
import util.common.Util;

/**
 * @author marco
 * 
 */
public interface Enemy {

	public void setWave(Wave wave);

	public Wave getWave();

}
