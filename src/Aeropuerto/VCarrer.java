package Aeropuerto;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import Aeropuerto.Avio.Direction;
import Aeropuerto.Carrer;
import Aeropuerto.CrossRoad;

public class VCarrer extends Carrer {

	private CrossRoad cr;

	public VCarrer(String idWay, int cmWayWidth, int cmWayMark, int cmLong, int cmPosIniX, int cmPosIniY ) {

        super(idWay, cmWayWidth, cmWayMark, cmLong, cmPosIniX, cmPosIniY);

        this.cmFinX = this.cmIniX + this.cmWidth;
        this.cmFinY = this.cmIniY + this.cmLong;
    }

	@Override
	public void addCrossRoad(CrossRoad cr) {
		// TODO Auto-generated method stub
		
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
		// return cmPosition >= this.cmFinY;
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

        wayMark = (int) (((float) this.cmMark) / factorY);

        if (wayMark <= 0) {
            return; // ===========================================>>
        }
        wayWidth = (int) (((float) this.cmWidth) / factorX);
        xIni = (int) ((this.cmIniX / factorX) + offsetX);
        yIni = (int) ((this.cmIniY / factorY) + offsetY);
        xFin = (int) ((this.cmFinX / factorX) + offsetX);
        yFin = (int) ((this.cmFinY / factorY) + offsetY);


        // Road
        g2d = (Graphics2D) g;
        GradientPaint gp5 =
                new GradientPaint(xIni, 0, Color.decode("0x404040"), xIni + (wayWidth / 2.9F), 0, Color.decode("0x606060"), true);
        g2d.setPaint(gp5);
        g.fillRect(xIni, yIni, xFin - xIni, yFin - yIni);
        g.setColor(Color.decode("0x505050"));
        g.drawRect(xIni, yIni, xFin - xIni, yFin - yIni);


        // Central Mark
        widthMark = Math.max(1, (int) (50 / factorX));
        g2d = (Graphics2D) g;
        stk = (BasicStroke) g2d.getStroke();
        g2d.setStroke(new BasicStroke(widthMark, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 5.0f, new float[]{wayMark}, 0.0f));
        g.setColor(Color.decode("0xc5a002"));
        g.drawLine(xIni + (wayWidth / 2), yIni, xIni + (wayWidth / 2), yFin);
        g2d.setStroke(stk);

        // Lateral marks
        gp5 = new GradientPaint(0, yIni, Color.decode("0x787800"), 0, yIni + widthMark * 100F, Color.decode("0x505000"), true);
//        gp5 = new GradientPaint(0, yIni, Color.decode("0x999999"), 0, yIni + widthMark * 110F, Color.decode("0x606060"), true);
        g2d.setPaint(gp5);
        g.fillRect(xIni + widthMark, yIni, widthMark, yFin - yIni);

        gp5 = new GradientPaint(0, yFin / 2, Color.decode("0x787800"), 0, yFin / 2 + widthMark * 125F, Color.decode("0x505000"), true);
        g2d.setPaint(gp5);
        g.fillRect(xFin - 2 * widthMark, yIni, widthMark, yFin - yIni);

        //g.setColor(Color.black);
        //g.drawString(this.idWay, xIni + (int) ((float) this.cmWidth / factorX) + 3, yFin + 12);
    }
	
	
}