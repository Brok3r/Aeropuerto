package Aeropuerto;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import Aeropuerto.Avio.Direction;
import Aeropuerto.Carrer;


public class HCarrer extends Carrer {

	private CrossRoad cr;

	public HCarrer(String idWay, int cmWayWidth, int cmWayMark, int cmLong, int cmPosIniX, int cmPosIniY) {
        super(idWay, cmWayWidth, cmWayMark, cmLong, cmPosIniX, cmPosIniY);
                    
        this.cmFinX = this.cmIniX + this.cmLong;
        this.cmFinY = this.cmIniY + this.cmWidth;
    }

	@Override
	public void addCrossRoad(CrossRoad cr) {
		this.cr= cr;
		
	}

	@Override
	public int distanceToCrossRoadInCm(CrossRoad cr, Avio Avio) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CrossRoad inFrontCrossRoad(Avio Avio) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insideAnyCrossRoad(int cmPosition) {
       
            return false;
	}

	@Override
	public CrossRoad intersectedCrossRoad(int cmPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insideThisCrossRoad(int cmPosition, CrossRoad crossRoad) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean posIsInside(int cmPosition, Direction direction) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
    public void paint(Graphics g, float factorX, float factorY, int offsetX, int offsetY) {
        int wayWidth;
        int wayMark;
        int widthMark;
        int xIni, yIni, xFin, yFin;
        Graphics2D g2d;
        BasicStroke stk;

        wayMark = (int) ((float) this.cmMark / factorY);

        if (wayMark <= 0) {
            return;
        }

        wayWidth = (int) ((float) this.cmWidth / factorY);
        xIni = (int) (((float) this.cmIniX / factorX) + offsetX);
        yIni = (int) (((float) this.cmIniY / factorY) + offsetY);
        xFin = (int) (((float) this.cmFinX / factorX) + offsetX);
        yFin = (int) (((float) this.cmFinY / factorY) + offsetY);


        // Road
        g2d = (Graphics2D) g;
        GradientPaint gp5 =
                new GradientPaint(0, yIni + 2, Color.decode("0x404040"), 0, yIni + wayWidth / 2.9F, Color.decode("0x606060"), true);
        g2d.setPaint(gp5);
        g.fillRect(xIni, yIni, xFin - xIni, yFin - yIni);
        g.setColor(Color.BLACK);
        g.drawRect(xIni, yIni, xFin - xIni, yFin - yIni);


        // Central mark
        widthMark = Math.max(1, (int) (50 / factorX));
        g2d = (Graphics2D) g;
        stk = (BasicStroke) g2d.getStroke();
        g2d.setStroke(new BasicStroke(widthMark, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 5.0f, new float[]{wayMark}, 0.0f));
        g.setColor(Color.decode("0xc5a002"));
        g.drawLine(xIni, yIni + (wayWidth / 2), xFin, yIni + (wayWidth / 2));
        g2d.setStroke(stk);

        // Lateral mark
        gp5 = new GradientPaint(xIni, 0, Color.decode("0x787800"), xIni + widthMark * 90F, 0, Color.decode("0x505000"), true);
        g2d.setPaint(gp5);
        g.fillRect(xIni, yIni + widthMark, xFin - xIni, widthMark);

        gp5 = new GradientPaint(xIni / 2, 0, Color.decode("0x787800"), xIni / 2 + widthMark * 125F, 0, Color.decode("0x505000"), true);
        g2d.setPaint(gp5);
        g.fillRect(xIni, yFin - 2 * widthMark, xFin - xIni, widthMark);
	}
}