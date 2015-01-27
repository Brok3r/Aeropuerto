package Aeropuerto;

import Aeropuerto.Avio.EstatAvio;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;

import Aeropuerto.Finger;
import Aeropuerto.Finger.Estat;
import aeroport.Avio.Direction;

public class Mapa extends Canvas implements Runnable {

    private int cityCmWidth; // aq valors van en funcio en factor XY
    private int cityCmHeight;
    private int mapWidth; //lo q ocupa al nostra mapa, el mapa q hem definit en cm
    private int mapHeigth;
    private float factorX; //escala
    private float factorY;
    private float zoomLevel;
    private int offsetX; //pan
    private int offsetY;
    private int cmCarrerWidth;
    private int cmCarrerMark;
    private BufferedImage imgMap, imgBg; //carregar imatges
    //private Traffic traffic;
    private ArrayList<Carrer> carrers;
    private ArrayList<CrossRoad> crossroads;
    private ArrayList<Finger> fingers;
    private Controlador controlador;

    public Mapa(int cityCmWidth, int cityCmHeight, int mapPixWidth, int mapPixHeight) {
        this.cityCmWidth = cityCmWidth;
        this.cityCmHeight = cityCmHeight;
        this.mapWidth = mapPixWidth;
        this.mapHeigth = mapPixHeight;

        this.offsetX = 0;
        this.offsetY = 0;
        this.zoomLevel = 2;
        this.setFactorXY();

        this.cmCarrerWidth = 800;
        this.cmCarrerMark = 300; // Longitud marcas viales en cm

        this.carrers = new ArrayList<Carrer>();
        this.crossroads = new ArrayList<CrossRoad>();
        this.fingers = new ArrayList<Finger>();

        this.loadCarrers();
        this.calculateCrossRoads();

        this.loadFinger();

        Dimension d = new Dimension(800, 400); //300,172
        this.setPreferredSize(d);

        try {
            this.imgBg = ImageIO.read(new File("bg.jpg"));
        } catch (IOException e) {
            System.out.println("Img Error: not found bg.jpg");
        }
    }

    public ArrayList<Carrer> getCarrers() {
        return this.carrers;
    }

    public void setWidth(int mapWidth) {
        this.mapWidth = mapWidth;
        this.setFactorXY();
    }

    public void setHeig(int mapHeigth) {
        this.mapHeigth = mapHeigth;
        this.setFactorXY();
    }

    public void setFactorXY() {
        this.mapWidth = this.getWidth();
        this.mapHeigth = this.getHeight();

        this.factorX = ((float) this.cityCmWidth / (float) this.mapWidth / this.zoomLevel);
        this.factorY = ((float) this.cityCmHeight / (float) this.mapHeigth / this.zoomLevel);
        this.paintImgMap();
    }

    private boolean addCrossRoad(CrossRoad newCr) {
        Iterator<CrossRoad> itr = this.crossroads.iterator();
        while (itr.hasNext()) {
            if (itr.next().equals(newCr)) {
                return false;  // ====== Crossroad duplicated ================>>
            }
        }

        this.crossroads.add(newCr);
        return true;
    }
// pasa per totes ses horitzontals i mira si se creua amb alguna vertical
    //i si coincideix pinta creuament

    private void calculateCrossRoads() {
        Iterator<Carrer> itrCarrers1;
        Iterator<Carrer> itrCarrers2;
        Carrer auxCarrer1, auxCarrer2;

        itrCarrers1 = this.carrers.iterator();
        while (itrCarrers1.hasNext()) {
            auxCarrer1 = itrCarrers1.next();

            itrCarrers2 = this.carrers.iterator();
            if (auxCarrer1 instanceof HCarrer) {
                while (itrCarrers2.hasNext()) {
                    auxCarrer2 = itrCarrers2.next();
                    if (auxCarrer2 instanceof VCarrer) {
                        if (auxCarrer1.carrerIntersection(auxCarrer2)) {
                            this.addCrossRoad(new CrossRoad(auxCarrer1, auxCarrer2));
                        }
                    }
                }
            }
        }
    }

    private void loadCarrers() {

        this.carrers.add(new HCarrer("H1", this.cmCarrerWidth, this.cmCarrerMark, 27000, 1500, 1000));
        this.carrers.add(new HCarrer("H2", this.cmCarrerWidth, this.cmCarrerMark, 27000, 1500, 5000));

    //	this.carrers.add(new HCarrer("H3", this.cmCarrerWidth/2, this.cmCarrerMark/2, 21000, 4000, 11500));
        // id, cmquetecarrer(amplada), marcad'enmig, longitud, posicio inicia x, pos inicia x
        //59800,0,0
        this.carrers.add(new VCarrer("V1", this.cmCarrerWidth, this.cmCarrerMark, 4000, 1500, 1000));
        this.carrers.add(new VCarrer("V2", this.cmCarrerWidth, this.cmCarrerMark, 4800, 28000, 1000));

    //	this.carrers.add(new VCarrer("V3", this.cmCarrerWidth, this.cmCarrerMark, 10800, 3500, 1100));
        //	this.carrers.add(new VCarrer("V4", this.cmCarrerWidth, this.cmCarrerMark, 10800, 25000, 1100));
    //	this.carrers.add(new VCarrer("V4", this.cmCarrerWidth, this.cmCarrerMark, 10800, 14750, 1100));
//
//    	this.carrers.add(new VCarrer("F1", this.cmCarrerWidth/2, this.cmCarrerMark/2, 2000, 5900, 11500));
//    	this.carrers.add(new VCarrer("F2", this.cmCarrerWidth/2, this.cmCarrerMark/2, 2000, 7900, 11500));
//    	this.carrers.add(new VCarrer("F3", this.cmCarrerWidth/2, this.cmCarrerMark/2, 2000, 9900, 11500));
//    	this.carrers.add(new VCarrer("F4", this.cmCarrerWidth/2, this.cmCarrerMark/2, 2000, 11900, 11500));
//    	this.carrers.add(new VCarrer("F5", this.cmCarrerWidth/2, this.cmCarrerMark/2, 2000, 13900, 11500));
//    	this.carrers.add(new VCarrer("F6", this.cmCarrerWidth/2, this.cmCarrerMark/2, 2000, 15900, 11500));
//    	this.carrers.add(new VCarrer("F7", this.cmCarrerWidth/2, this.cmCarrerMark/2, 2000, 17900, 11500));
//    	this.carrers.add(new VCarrer("F8", this.cmCarrerWidth/2, this.cmCarrerMark/2, 2000, 19900, 11500));
//    	this.carrers.add(new VCarrer("F9", this.cmCarrerWidth/2, this.cmCarrerMark/2, 2000, 21900, 11500));
//    	this.carrers.add(new VCarrer("F10", this.cmCarrerWidth/2, this.cmCarrerMark/2, 2000, 23900, 11500));
        //20000,0,100
    }

    public void moveRight() {
        this.offsetX += 10;
        this.setFactorXY();
    }

    public void moveLeft() {
        this.offsetX -= 10;
        this.setFactorXY();
    }

    public void moveDown() {
        this.offsetY += 10;
        this.setFactorXY();
    }

    public void moveUp() {
        this.offsetY -= 10;
        this.setFactorXY();
    }

    public synchronized void paint() {
        BufferStrategy bs;
        bs = this.getBufferStrategy();
        if (bs == null) {
            return;
        }

        if ((this.mapWidth < 0) || (this.mapHeigth < 0)) {
            System.out.println("Map size error: (" + this.mapWidth + "," + this.mapHeigth + ")");
            return;
        }

        Graphics gg = bs.getDrawGraphics();

        gg.drawImage(this.imgMap, 0, 0, this.mapWidth, this.mapHeigth, null);
        this.controlador.paintAvions(gg, factorX, factorY, offsetX, offsetY);
        bs.show();

        gg.dispose();
    }

    public void paintBackgroud(Graphics g) {
        g.drawImage(this.imgBg, 0, 0, null);
    }

    public void paintFingers(Graphics g) {

        Iterator<Finger> itr = this.fingers.iterator();

        while (itr.hasNext()) {
            itr.next().paint(g, this.factorX, this.factorY, this.offsetX, this.offsetY);
        }

    }

    public void paintTerminal(Graphics g) {

        paintTerm(g, factorX, factorY, offsetX, offsetY);

    }

    public void paintCrossRoads(Graphics g) {
        Iterator<CrossRoad> itr = this.crossroads.iterator();

        while (itr.hasNext()) {
            itr.next().paint(g, this.factorX, this.factorY, this.offsetX, this.offsetY);
        }
    }

    public synchronized void paintImgMap() {
        if ((this.mapWidth <= 0) || (this.mapHeigth <= 0)) {
            System.out.println("Map size error: (" + this.mapWidth + "," + this.mapHeigth + ")");
            return;
        }

        this.imgMap = new BufferedImage(this.mapWidth, this.mapHeigth, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = this.imgMap.createGraphics();

        this.paintBackgroud(g);
        this.paintCarrers(g);
        this.paintCrossRoads(g);
        this.paintFingers(g);
      //  this.paintTerminal(g);

        g.dispose();

    }

    /*
     public synchronized void paintAvio(Graphics g) {
       
     this.controlador.paintAvions(g, this.factorX, this.factorY, this.offsetX, this.offsetY);
    	
     }
     */
    public void paintCarrers(Graphics g) {

        Iterator<Carrer> itr = this.carrers.iterator();

        while (itr.hasNext()) {
            itr.next().paint(g, this.factorX, this.factorY, this.offsetX, this.offsetY);
        }
    }
// _____________________   MOTOR GRAFICO
boolean bol=true;
    @Override
    public void run() {
        this.createBufferStrategy(2);

        while (!Aeroport.isEnd()) {

            this.paint();
 
            for (int i = 0; i < controlador.avions.size(); i++) {
                Avio a = controlador.avions.get(i);

                for (int j = 0; j < this.crossroads.size(); j++) {
                    int iniX = this.crossroads.get(j).getIniX();
                    int finX = this.crossroads.get(j).getFinX();
                    if (a.getCmPosition() <= finX & a.getCmPosition() >= iniX) {
                        //metodo para cambiar
                       
                        if(bol){
                             if(a.getEstado()==EstatAvio.RUN){
                            
                            a.setWay(carrers.get(1));
                            a.setCmPosition(carrers.get(3).getEntryPoint(Avio.Direction.FORWARD));
                            a.setDirection(Avio.Direction.FORWARD);
                                 System.out.println("dentro");
                                  bol=false;
                            //a.setCmPosition(500);
                       }
                            
                        }
                    }
                }

            }

            do {
                try {
                    Thread.sleep(7); // nano -> ms
                } catch (InterruptedException ex) {
                }
            } while (Aeroport.isPaused());
        }
    }
//_______________________-

    public void zoomIn() {
        this.zoomIn(0.01f);
    }

    public void zoomIn(float inc) {
        this.zoomLevel += inc;
        this.setFactorXY();

    }

    public void zoomOut() {
        this.zoomOut(0.01f);
        this.setFactorXY();
    }

    public void zoomOut(float inc) {
        this.zoomLevel -= inc;
        this.setFactorXY();
    }

    public void zoomReset() {
        this.zoomLevel = 1;
        this.setFactorXY();
    }

    public void loadFinger() {

        int x = 5000;

        for (int i = 0; i < 8; i++) {
            fingers.add(new Finger(Estat.BUID, x, 12500, i));
            x = x + 2000;
        }

    }

    public void paintTerm(Graphics g, float factorX, float factorY, int offsetX, int offsetY) {

        int iniX, iniY, finX, finY;

        iniX = (int) ((2000 / factorX) + offsetX);
        iniY = (int) ((14000 / factorY) + offsetY);
        finX = (int) (25000 / factorX);
        finY = (int) (6000 / factorY);

        // Paint crossroad
        g.setColor(Color.GRAY);
        g.fillRect(iniX, iniY, finX, finY);
        g.setColor(Color.BLACK);
        g.drawRect(iniX, iniY, finX, finY);

    }

    public void setControlador(Controlador c) {
		// TODO Auto-generated method stub

        this.controlador = c;
    }

}
