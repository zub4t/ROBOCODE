/**
 * 
 */
package util.enemy;

import java.awt.geom.Point2D;

/**
 * @author marco
 * 
 */
public interface Enemy {
	public String getName();

	public double getEnergy();

	public double getVelocity();

	public void setEnergy(double energy);

	public void setVelocity(double velocity);

	public void setLocation(Point2D.Double location);

	public Point2D.Double getLocation();

}
