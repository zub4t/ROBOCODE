package marco.common;

import robocode.AdvancedRobot;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class Radar implements Component {
	private AdvancedRobot bot;

	@Override
	public void onScannedRobot(ScannedRobotEvent e) {
		double absBearing = e.getBearingRadians() + bot.getHeadingRadians();
		bot.setTurnRadarRightRadians(Utils.normalRelativeAngle(absBearing - bot.getRadarHeadingRadians()) * 2);
	}

	@Override
	public void doTickAction() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onHitByBullet(HitByBulletEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOwner(AdvancedRobot bot) {
		this.bot = bot;
	}

	@Override
	public void onBulletMissed(BulletMissedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBulletHit(BulletHitEvent event) {
		// TODO Auto-generated method stub
		
	}
}
