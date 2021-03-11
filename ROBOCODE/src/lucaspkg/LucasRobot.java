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
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		do {
			turnRadarRight(Double.POSITIVE_INFINITY);
		} while (true);
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		double absBearing = e.getBearingRadians() + getHeadingRadians();
		setTurnRadarRightRadians(Utils.normalRelativeAngle(absBearing - getRadarHeadingRadians()) * 2);
		setTurnGunRightRadians(absBearing - getGunHeadingRadians());
	}
}