package sushllbot;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public interface Bot {

	public static Rectangle2D.Double _fieldRect = new java.awt.geom.Rectangle2D.Double(18, 18, 764, 564);

	public String getName();

	public double getEnergy();

	public void setEnergy(double energy);

	public Rectangle2D.Double getFieldRect();

	public double getMyVelocity();

	public double getMyHeadingRadians();

	public Point2D.Double getMyLocation();

	public double getDirectAngle();

	public double getDirection();

	public void setVelocity(double velocity);

	public void setLocation(Point2D.Double location);

	public void doShoot();

	public void doSurfing();
}
