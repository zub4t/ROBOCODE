package marco.common;

import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;

public interface Component {

	public void onScannedRobot(ScannedRobotEvent e);

	public void doTickAction();

	public void onHitByBullet(HitByBulletEvent e);
	
	public void setOwner(AdvancedRobot bot);
}
