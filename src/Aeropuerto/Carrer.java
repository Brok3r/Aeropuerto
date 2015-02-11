package Aeropuerto;
import Aeropuerto.Avio.Direction;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Carrer {

    protected String idWay;
    protected int cmLong;
    protected int cmIniX;
    protected int cmIniY;
    protected int cmFinX;
    protected int cmFinY;
    protected int cmWidth;
    protected int cmMark;
    protected ArrayList<Avio> avions = new ArrayList();
    protected ArrayList<CrossRoad> crossRoads = new ArrayList();
    protected ArrayList<Integer> forwardEntryPoint = new ArrayList();
    protected ArrayList<Integer> backwardEntryPoint = new ArrayList();
    protected Direction dire;
    
    public Carrer(String idWay, int cmWayWidth, int cmWayMark, int cmLong, int cmPosIniX, int cmPosIniY, Direction direc) {
        this.idWay = idWay;
        this.cmLong = cmLong;
        this.cmWidth = cmWayWidth;
        this.cmMark = cmWayMark;
        this.cmIniX = cmPosIniX;
        this.cmIniY = cmPosIniY;
        this.dire = direc;
        this.createDefaultEntryPoints();
    }

    public String getId() {
        return this.idWay;
    }
    public int getCmLong() {
        return this.cmLong;
    }
    
    /*public abstract double getCourse(int cmPosition, Direction direction);    
    
    public abstract int getCmPosX(int cmPosition, Direction direction);

    public abstract int getCmPosY(int cmPosition, Direction direction);

    public abstract int getCmPosition(int cmPosX, int cmPosY, Avio.Direction direction);
    
    */

    public int getCmPosIniX() {
        return this.cmIniX;
    }

    public int getCmPosIniY() {
        return this.cmIniY;
    }

    public int getEntryPoint(Direction direc) {
        int cmPosition = 0;

        switch (dire) {
            case FORWARD:
                cmPosition = this.forwardEntryPoint.get(0);
                break;
            case BACKWARD:
                cmPosition = this.backwardEntryPoint.get(0);
                break;
            default:
                throw new AssertionError(dire.name());
        }

        return cmPosition;
    }

    public synchronized void addAvio(Avio avio) {

            this.avions.add(avio);

    }

    public abstract void addCrossRoad(CrossRoad cr);

    private void createDefaultEntryPoints() {
        this.forwardEntryPoint.add(0);
        this.backwardEntryPoint.add(this.cmLong);
    }

    public abstract int distanceToCrossRoadInCm(CrossRoad cr, Avio Avio);
    public abstract int getCmPosX(int cmPosition, Direction direction);
    public abstract int getCmPosY(int cmPosition, Direction direction);
    public abstract CrossRoad inFrontCrossRoad(Avio Avio);
  
    public synchronized Avio inFrontAvio(Avio Avio) {
        ArrayList<Avio> AvioList;
        Avio inFrontAvio, actualAvio;
        int minDistance, actualDistance;

        inFrontAvio = null;
            AvioList = this.avions;


        minDistance = this.getCmLong() + 1;

        Iterator<Avio> itr = AvioList.iterator();
        while (itr.hasNext()) {
            actualAvio = itr.next();

            if (!actualAvio.equals(Avio)) {
                actualDistance = Avio.getDirection().getIncrement() * (actualAvio.getCmPosition() - Avio.getCmPosition());

                if ((actualDistance < minDistance) && (actualDistance > 0)) {
                    minDistance = actualDistance;
                    inFrontAvio = actualAvio;
                }
            }
        }

        return inFrontAvio;
    }

    public abstract boolean insideAnyCrossRoad(int cmPosition);

    public abstract CrossRoad intersectedCrossRoad(int cmPosition);

    public abstract boolean insideThisCrossRoad(int cmPosition, CrossRoad crossRoad);

    public abstract boolean posIsInside(int cmPosition, Direction direction);

    public abstract int getCmPosition(int cmPosX, int cmPosY, Avio.Direction direction);
    
    public synchronized void removeAvio(Avio Avio) {
        this.avions.remove(Avio);
    }

    public boolean carrerIntersection(Carrer way) {
        Carrer vWay, hWay;

        hWay = vWay = null;
        if (way instanceof VCarrer) {
            vWay = way;
        }

        if (way instanceof HCarrer) {
            hWay = way;
        }

        if (this instanceof VCarrer) {
            vWay = this;
        }

        if (this instanceof HCarrer) {
            hWay = this;
        }

        if ((vWay == null) || (hWay == null)) {
            return false; // =================>
        }

        return (hWay.cmIniX <= vWay.cmFinX)
                && (vWay.cmIniX <= hWay.cmFinX)
                && (hWay.cmIniY <= vWay.cmFinY)
                && (vWay.cmIniY <= hWay.cmFinY);
    }

    public abstract void paint(Graphics g, float factorX, float factorY, int offsetX, int offsetY);

    @Override
    public String toString() {
        return "Avió: " + this.getId();
    }

  
}