package sushllbot;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public interface Bot {

	public Rectangle2D.Double getFieldRect();

	public double getMyVelocity();

	public double getMyHeadingRadians();

	public Point2D.Double getMyLocation();
}
