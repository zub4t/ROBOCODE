package marco.common;

import java.awt.geom.Rectangle2D;

public class BattleField {

	public static Rectangle2D.Double battleFieldWithOffset; //usado principalmente no método WallSmooth para não barter nas paredes.
	public static Rectangle2D.Double battleField;// Usado principalmente no método onScanRobot da gun para previsão do inimigo.

	public static void setRect(int x, int y, int i, int j) {
		battleField = new java.awt.geom.Rectangle2D.Double(x, y, i, j);
	}

	public static void setRectWithOffset(int x, int y, int i, int j) {
		battleFieldWithOffset = new java.awt.geom.Rectangle2D.Double(x, y, i, j);
	}
}
