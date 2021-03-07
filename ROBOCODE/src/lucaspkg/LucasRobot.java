package lucaspkg;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import robocode.*;
import robocode.util.Utils;

public class LucasRobot extends AdvancedRobot {
	// The coordinates of the last scanned robot
	int scannedX = Integer.MIN_VALUE;
	int scannedY = Integer.MIN_VALUE;

	public void run() {

	}

	// Paint a transparent square on top of the last scanned robot
	public void onPaint(Graphics2D g) {
		// Set the paint color to a red half transparent color
		g.setColor(Color.red);

		Point2D.Double sourceLocation = new Point2D.Double();
		sourceLocation.setLocation(getX(), getY());
		sourceLocation = projectt(sourceLocation, Math.PI / 3, 21);

		g.fillRect((int) sourceLocation.getX(), (int) sourceLocation.getY(), 10, 10);
		g.setColor(Color.blue);

		sourceLocation = project(sourceLocation, Math.PI / 3, 20);

		g.fillRect((int) sourceLocation.getX(), (int) sourceLocation.getY(), 10, 10);

		g.setColor(Color.white);
		g.fillRect((int) getX(), (int) getY(), 10, 10);

	}

	public static Point2D.Double project(Point2D.Double sourceLocation, double angle, double length) {
		// CREDIT: from CassiusClay, by PEZ robowiki.net?CassiusClay
		return new Point2D.Double(sourceLocation.x + (Math.sin(angle) * length),
				sourceLocation.y + (Math.cos(angle) * length));
	}

	public static Point2D.Double projectt(Point2D.Double sourceLocation, double angle, double length) {
		// CREDIT: from CassiusClay, by PEZ robowiki.net?CassiusClay
		return new Point2D.Double(sourceLocation.x + (Math.cos(angle) * length),
				sourceLocation.y + (Math.sin(angle) * length));
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		// fire(1);
	}
}