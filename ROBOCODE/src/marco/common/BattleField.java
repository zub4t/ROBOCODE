package marco.common;

import java.awt.geom.Rectangle2D;

public class BattleField {

	public static Rectangle2D.Double battleField;

	public static void setRect(int x, int y, int i, int j) {
		battleField = new java.awt.geom.Rectangle2D.Double(x, y, i, j);
	}

}
