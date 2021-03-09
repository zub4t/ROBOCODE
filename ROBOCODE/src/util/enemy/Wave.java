package util.enemy;

import java.awt.geom.Point2D;

public class Wave {
	private Point2D.Double fireLocation;
	private long fireTime;
	private Double bulletVelocity, directAngle, distanceTraveled;
	private int direction;

	public Wave() {
	}

	public Point2D.Double getFireLocation() {
		return fireLocation;
	}

	public long getFireTime() {
		return fireTime;
	}

	public double getBulletVelocity() {
		return bulletVelocity;
	}

	public double getDirectAngle() {
		return directAngle;
	}

	public double getDistanceTraveled() {
		return distanceTraveled;
	}

	public int getDirection() {
		return direction;
	}

	public void setFireLocation(Point2D.Double fireLocation) {
		this.fireLocation = fireLocation;
	}

	public void setFireTime(long fireTime) {
		this.fireTime = fireTime;
	}

	public void setBulletVelocity(double bulletVelocity) {
		this.bulletVelocity = bulletVelocity;
	}

	public void setDirectAngle(double directAngle) {
		this.directAngle = directAngle;
	}

	public void setDistanceTraveled(double distanceTraveled) {
		this.distanceTraveled = distanceTraveled;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
}
