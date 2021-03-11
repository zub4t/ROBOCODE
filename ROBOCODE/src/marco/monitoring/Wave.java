package marco.monitoring;

import java.awt.geom.Point2D;


public class Wave {
	private Point2D.Double fireLocation;
	private long fireTime;
	private double bulletVelocity, directAngle, distanceTraveled;
	private int direction;

	public Wave() {
	}

	public Wave(double velocity, long time, double distanceTraveled, double directAngle, Point2D.Double location) {
		this.bulletVelocity = velocity;
		this.fireTime = time;
		this.distanceTraveled = distanceTraveled;
		this.directAngle = directAngle;
		this.fireLocation = location;

	}

	public Point2D.Double getFireLocation() {
		return fireLocation;
	}

	public void setFireLocation(Point2D.Double fireLocation) {
		this.fireLocation = fireLocation;
	}

	public long getFireTime() {
		return fireTime;
	}

	public void setFireTime(long fireTime) {
		this.fireTime = fireTime;
	}

	public Double getBulletVelocity() {
		return bulletVelocity;
	}

	public void setBulletVelocity(Double bulletVelocity) {
		this.bulletVelocity = bulletVelocity;
	}

	public Double getDirectAngle() {
		return directAngle;
	}

	public void setDirectAngle(Double directAngle) {
		this.directAngle = directAngle;
	}

	public double getDistanceTraveled() {
		return distanceTraveled;
	}

	public void setDistanceTraveled(Double distanceTraveled) {
		this.distanceTraveled = distanceTraveled;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

}
