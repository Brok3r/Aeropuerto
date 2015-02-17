package Aeropuerto;

import Aeropuerto.Avio.Direction;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

public class Finger extends VCarrer {

    public static enum Estat {

        OCUPAT, BUID, RESERVAT
    };
    Estat estat;
    private Avio avio;
    private int posicioX;
    private int posicioY;
    private int num;

    public Finger(String idWay, int cmWayWidth, int cmWayMark, int cmLong, int cmPosIniX, int cmPosIniY, Avio.Direction direccio) {
        super(idWay, cmWayWidth, cmWayMark, cmLong, cmPosIniX, cmPosIniY, direccio);
        this.cmFinX = this.cmIniX + this.cmWidth;
        this.cmFinY = this.cmIniY + this.cmLong;
    }

    public Estat getEstat() {
        return estat;
    }

    public void setEstat(Estat estat) {
        this.estat = estat;
    }

    /**
     * torna true si el figer esta ocupat
     *
     * @return
     */
    public boolean getOcupado() {
        return this.estat == estat.OCUPAT || this.estat == estat.RESERVAT;

    }

    public void addCrossRoad(CrossRoad cr) {
        this.crossRoads.add(cr);
    }

    /**
     * torna true si el finger esta buid
     *
     * @return
     */
    public boolean getBuid() {
        return estat == estat.BUID;
    }

    public Avio getAvio() {
        return avio;
    }

    public void setAvio(Avio avio) {
        this.avio = avio;
    }

    public int getPosicioX() {
        return posicioX;
    }

    public void setPosicioX(int posicioX) {
        this.posicioX = posicioX;
    }

    public int getPosicioY() {
        return posicioY;
    }

    public void setPosicioY(int posicioY) {
        this.posicioY = posicioY;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public void paint(Graphics g, float factorX, float factorY, int offsetX, int offsetY) {
        int xIni, yIni, xFin, yFin;
        xIni = (int) ((this.cmIniX / factorX) + offsetX);
        yIni = (int) ((this.cmIniY / factorY) + offsetY);
        xFin = (int) ((this.cmFinX / factorX) + offsetX);
        yFin = (int) ((this.cmFinY / factorY) + offsetY);
        g.setColor(Color.DARK_GRAY);
        g.drawRect(xIni, yIni, xFin - xIni, yFin - yIni);

    }

    public CrossRoad inFrontCrossRoad(Avio avio) {
        int minDistance;
        int actualDistance;
        int crossRoadPos;
        CrossRoad inFrontCR, actualCR;
        Iterator<CrossRoad> itr;
        inFrontCR = null;
        minDistance = this.getCmLong() + 1;
        itr = this.crossRoads.iterator();
        while (itr.hasNext()) {
            actualCR = itr.next();
            crossRoadPos = actualCR.getFinY();
            if (avio.getDirection() == Direction.FORWARD) {
                crossRoadPos = actualCR.getIniY();
            }
            actualDistance = avio.getDirection().getIncrement()
                    * (crossRoadPos - this.getCmPosY(avio.getCmPosition(),
                            avio.getDirection()));
            if ((actualDistance < minDistance) && (actualDistance > 0)) {
                minDistance = actualDistance;
                inFrontCR = actualCR;
            }
        }
        return inFrontCR;
    }
   

    @Override
    public boolean insideAnyCrossRoad(int cmPosition) {
        return this.intersectedCrossRoad(cmPosition) != null;
    }

    @Override
    public CrossRoad intersectedCrossRoad(int cmPosition) {
        CrossRoad cr;
        int cmPosY;
        cmPosY = this.getCmPosY(cmPosition, Direction.FORWARD);
        Iterator<CrossRoad> itr = this.crossRoads.iterator();
        while (itr.hasNext()) {
            cr = itr.next();
            if (this.insideThisCrossRoad(cmPosY, cr)) {
                return cr; // ================================================>>
            }
        }
        return null;
    }

    @Override
    public boolean insideThisCrossRoad(int cmPosY, CrossRoad crossRoad) {
        return ((cmPosY >= crossRoad.getIniY()) && (cmPosY <= crossRoad
                .getFinY()));
    }

    public int getCmPosX(int cmPosition, Direction direction) {
        if (direction == Direction.FORWARD) {
            return this.cmIniX + (this.cmWidth / 4); // ======================>>
        }
        return this.cmFinX - (this.cmWidth / 4);
    }

    public int getCmPosY(int cmPosition, Avio.Direction direction) {
        int cmPosY;
        cmPosY = this.cmIniY + cmPosition;
        if (cmPosY < this.cmIniY || cmPosY > this.cmFinY) {
            return -1; // Fuera de la via ====================================>>
        }
        return cmPosY;
    }

    public int getCmPosition(int cmPosX, int cmPosY, Avio.Direction direction) {
        int cmPosition;
        cmPosition = cmPosY - this.cmIniY;
        if (cmPosY < this.cmIniY || cmPosY > this.cmFinY) {
            return -1; // ============== Off road ============================>>
        }
        return cmPosition;
    }

    @Override
    public int distanceToCrossRoadInCm(CrossRoad cr, Avio Avio) {
// TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean posIsInside(int cmPosition, Direction direction) {
// TODO Auto-generated method stub
        return false;
    }
}


/*	public synchronized void entra(Finger f, String m) { // cotxe entra al p�rquing
			
 while ((f.getEstat().equals(estat.OCUPAT)) || (f.getEstat().equals(estat.RESERVAT))) {
 try {
 System.out.println(m+" :Esperant, fingers plens.");
 esperant.add(m);
 imprimirEstat();
 wait();
 } catch (InterruptedException e) {
 }
 }
 esperant.remove(m);
 aparcats.add(m);
 imprimirEstat();
 f.setEstat(estat.OCUPAT);
 }


 private void imprimirEstat() {
 if (aparcats.size()>0){
 System.out.print("Aparcats: ");
 for (int i=0;i<aparcats.size()-1;i++){
 System.out.print(aparcats.get(i));
 }
 System.out.println(aparcats.get(aparcats.size()-1));
 }
			
 if (esperant.size()>0){
 System.out.print("Esperant: ");
 for (int i=0;i<esperant.size()-1;i++){
 System.out.print(esperant.get(i));
 }
 System.out.println(esperant.get(esperant.size()-1));
 }
			
			
 System.out.println("");
 }

 public synchronized void surt(Finger f,String m) { // el coche deixa el p�rquing

 System.out.println("Pla�a alliberada x l'avio "+m);

 f.setEstat(estat.BUID);
 aparcats.remove(m);
 imprimirEstat();
 notify();
 }*/
