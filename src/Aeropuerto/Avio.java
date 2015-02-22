package Aeropuerto;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

public class Avio extends Thread {

    CrossRoad crFrente;
    CrossRoad crActual;
    Direction direct;

    public static enum EstatAvio {

        BUSCARFINGER, APARCADO,
        HIDE, STOP, RUN, TAKINGOFF, LANDING, FLYING
    };

    public static enum Orientation {

        NORTH(Direction.BACKWARD),
        SOUDTH(Direction.FORWARD),
        WEST(Direction.BACKWARD),
        EAST(Direction.FORWARD);
     

        private Orientation(Direction direction) {
            
        }

        public static Direction getDirection(Orientation orientation) {
            if ((orientation == Orientation.SOUDTH) || (orientation == Orientation.EAST)) {
                return Direction.FORWARD; // =================================>>
            }
            return Direction.BACKWARD;
        }

        public static Orientation getOrientation(Avio avio) {
            return getOrientation(avio.getWay(), avio.getDirection());
        }

        public static Orientation getOrientation(Carrer way, Direction direction) {
            if (way instanceof VCarrer) {
                if (direction == Direction.FORWARD) {
                    return Orientation.SOUDTH; //=============================>>
                } else {
                    return Orientation.NORTH; //==============================>>
                }
            }

            if (way instanceof HCarrer) {
                if (direction == Direction.FORWARD) {
                    return Orientation.EAST; //===============================>>
                } else {
                    return Orientation.WEST; //===============================>>
                }
            }

            return Orientation.WEST;
        }

        public static int getDegrees(Orientation orientation) {
            switch (orientation) {
                case NORTH:
                    return 0;
                case EAST:
                    return 90;
                case SOUDTH:
                    return 180;
                case WEST:
                    return 270;
            }

            return 0;
        }
    }

    public static enum Direction {

        FORWARD(1), BACKWARD(-1);
        private int increment;

        private Direction(int increment) {
            this.increment = increment;
        }

        public int getIncrement() {
            return this.increment;
        }

        public Orientation getOrientation(Carrer way) {
            if (way instanceof VCarrer) {
                if (this == Direction.FORWARD) {
                    return Orientation.SOUDTH; //=============================>>
                } else {
                    return Orientation.NORTH; //==============================>>
                }
            }

            if (way instanceof HCarrer) {
                if (this == Direction.FORWARD) {
                    return Orientation.EAST; //===============================>>
                } else {
                    return Orientation.WEST; //===============================>>
                }
            }

            return Orientation.WEST;
        }

        @Override
        public String toString() {
            switch (this) {
                case FORWARD:
                    return "FORWARD";
                case BACKWARD:
                    return "BACKWARD";
            }
            return "Undefined";
        }
    }

    public Direction getDirection() {

        // TODO Auto-generated method stub
        return this.direction;
    }

    /* 
     private int maxSpeedInCmSecond;
     private int maxAcceleration; // In units compatibles with speed
     private int maxDeceleration;    
     private volatile int acceleration;
     private boolean turn;
     */
    private int cmLong;
    private int cmWidth;
    private volatile int cmPosition;             // Car position relative to actual way   
    private volatile int speedInCmSecond;
    private volatile long lastUpdateTime;
    private float speed;
    private double course;
    private float factorX, factorY;
    private Image imgCar;
    private volatile Carrer way;
    private Color color;
    private String idAvio;
    private Direction direction;
    private EstatAvio estado;
    private Orientation orientation;
    private String[] aterrizar = {"H1", "V1", "H2"};
    private Finger finger;
    private Controlador controlador;

    public Avio(Controlador controlador, String idAvio, Carrer way) {

        this.idAvio = idAvio;
        this.cmLong = 800;
        this.cmWidth = 400;
        this.speed = 70;
        this.color = Color.CYAN;
        this.factorX = this.factorY = -1;
        this.course = -1;
        this.cmPosition = 0;
        this.speedInCmSecond = 0;
        this.estado = EstatAvio.BUSCARFINGER;
        this.setWay(way);
        this.direction = Direction.FORWARD;
        this.controlador = controlador;
        this.orientation=Orientation.EAST;
        try {
            this.imgCar = new ImageIcon(getClass().getResource("avio.png")).getImage();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public boolean estaEnCruce() {
        return this.getWay().insideAnyCrossRoad(this.getCmPosition());
    }

    public boolean estaEnFinger() {
        CrossRoad cr = this.way.intersectedCrossRoad(cmPosition);
        return cr.getVCarrer() instanceof Finger;

    }

    public CrossRoad recuperarCrossRoad() {
        return this.getWay().intersectedCrossRoad(this.getCmPosition());
    }

    public void esperar() {
        while (this.estaEnCruce()) {
            try {
                Thread.sleep(7);
                if (this.direction == Direction.FORWARD) {
                    this.cmPosition += this.speed;
                } else {
                    this.cmPosition -= this.speed;
                }
            } catch (InterruptedException ex) {
            }

        }
    }

    public synchronized void paint(Graphics g, float factorX, float factorY, int offsetX, int offsetY) {
        int iniX, iniY, finX, finY;
        if (way instanceof VCarrer) {
            iniX = (int) ((((this.way.cmFinX + this.way.cmIniX) / 2-300) / factorX) + offsetX);
            finX = (int) (((this.cmWidth) / factorX));

            iniY = (int) (((this.way.cmIniY + this.cmPosition) / factorY) + offsetY);
            finY = (int) (((this.cmLong) / factorY));

            imgCar = AvioGraphics.getAvionImage(this);
            g.drawImage(this.imgCar, iniX, iniY, finX, finY, null);

        }
        if (way instanceof HCarrer) {
            iniY = (int) ((((this.way.cmFinY + this.way.cmIniY) /2-200) / factorY) + offsetY);   
            finY = (int) (((this.cmWidth) / factorY));

            iniX = (int) (((this.way.cmIniX + this.cmPosition) / factorX) + offsetX);
            finX = (int) (((this.cmLong) / factorX));

            imgCar = AvioGraphics.getAvionImage(this);
            g.drawImage(this.imgCar, iniX, iniY, finX, finY, null);
        }

    }

    Carrer c;

    @Override
    public void run() {
        while (true) {
            try {
                
                Thread.sleep(7);
                if (this.direction == Direction.FORWARD) {
                    this.cmPosition += this.speed;
                } else {
                    this.cmPosition -= this.speed;
                }
                if (estado == EstatAvio.BUSCARFINGER) {
                    controlador.entrar(this);
                    estado = EstatAvio.RUN;

                }
//                      if(estado.equals(EstatAvio.STOP)){
//                          System.out.println("Aparcado.");
//                      }
                if (this.estaEnCruce()) {

                    if (this.estaEnFinger()) {
                        Finger f = (Finger) this.way.intersectedCrossRoad(cmPosition).getVCarrer();
                       
                        //  controlador.aparcarAvionEnfinger(this);
                        this.setEstado(estado.STOP);
                        if (this == f.getAvio()) {

                            orientation=Orientation.SOUDTH;
                            crActual = recuperarCrossRoad();
                            Carrer anterior = way;
                            this.direction = Direction.FORWARD;
                            this.way = crActual.getCarrer(way);
                            
                            this.cmPosition = this.way.getCmPosition(
                                anterior.getCmPosX(this.cmPosition, this.direction),
                                anterior.getCmPosY(this.cmPosition, this.direction),
                                this.direction)+300;
                            
                            this.estado = EstatAvio.APARCADO;
                            
                            sleep((int) (Math.random() * 7000));
                            controlador.salir(this);

                        }
                    }
                    crActual = recuperarCrossRoad();
                    if (crActual.getCarrer(way).getId().equals("V2")) {
                         orientation=Orientation.SOUDTH;
                        Carrer anterior = way;
                        this.way = crActual.getCarrer(way);
                        this.direction = way.dire;
                        this.cmPosition = this.way.getCmPosition(
                                anterior.getCmPosX(this.cmPosition, this.direction),
                                anterior.getCmPosY(this.cmPosition, this.direction),
                                this.direction);
                        esperar();
                    }
                    if (crActual.getCarrer(way).getId().equals("H2")) {
                         orientation=Orientation.WEST;
                        Carrer anterior = way;
                        this.way = crActual.getCarrer(way);

                        this.cmPosition = this.way.getCmPosition(
                                anterior.getCmPosX(this.cmPosition, this.direction),
                                anterior.getCmPosY(this.cmPosition, this.direction),
                                this.direction);
                        this.direction = way.dire; // despues de cambiar la posición, porque influye en la dirección anterior...
                        esperar();
                        /*
                         if (estado = aparcado) sleep random
                         controlador.salir
                            
                         */

                    }

//                    if (this.esCruce()) {
// cruceActual = quinCruce();
// if (numGir < rutaLanding.size()){
// if (cruceActual.getCarrer(way).getId().equals(rutaLanding.get(numGir))){
// Carrer anterior = way;
// this.way = cruceActual.getCarrer(way);
// this.direction = way.direccio;
// this.cmPosition = this.way.getCmPosition(
// anterior.getCmPosX(this.cmPosition, this.direction),
// anterior.getCmPosY(this.cmPosition, this.direction),
// this.direction);
// numGir++;
                    //}//if es cruce que cerc
//                            
//                         System.out.println(" ----------------------------------                      Cruceee");
//                         if(this.way.inFrontCrossRoad(this) != null) {
//                            crFrente= this.way.inFrontCrossRoad(this);
//                         }
//                       
//                         
//                        crActual = this.recuperarCrossRoad();
//                                            
//                        //Carrer c = (VCarrer) cr.getVCarrer();
//                       
//                        if(crActual.getHCarrer().idWay.equals("H1")) {
//                             c = crActual.getVCarrer();
//                        }
//                        else if(crActual.getVCarrer().idWay.equals("V2")) {
//                            c = crActual.getHCarrer();
//                            System.out.println(c.idWay+" <-- id");
//                    }
//                        
//                       
//                            this.setWay(c);
//                            Carrer anterior=  this.getWay();
//                            this.direction = this.way.direction;
//                            this.cmPosition= 
//                                    this.way.getCmPosition(
//                                            anterior.getCmPosX(this.cmPosition, this.direction),
//                                            anterior.getCmPosY(this.cmPosition, this.direction),
//                                            this.direction);
//                      
//
////                   
//                    }
                }

//                if (this.estado.equals(EstatAvio.FLYING)) {
//                    this.cmPosition += this.speed;
//                    this.speed -= 0.1;
//
//                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public Carrer getWay() {
        // TODO Auto-generated method stub
        return this.way;
    }

    public EstatAvio getEstado() {
        return estado;
    }

    public void setEstado(EstatAvio estado) {
        this.estado = estado;
    }

    public int getCmPosition() {
        return this.cmPosition;
    }

    public int getLongInCm() {
        // TODO Auto-generated method stub
        return this.cmLong;
    }

    public int getSpeedInCmSecond() {
        return speedInCmSecond;
    }

    public void setSpeedInCmSecond(int speedInCmSecond) {
        this.speedInCmSecond = speedInCmSecond;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public double getCourse() {
        return course;
    }

    public void setCourse(double course) {
        this.course = course;
    }

    public float getFactorX() {
        return factorX;
    }

    public void setFactorX(float factorX) {
        this.factorX = factorX;
    }

    public float getFactorY() {
        return factorY;
    }

    public void setFactorY(float factorY) {
        this.factorY = factorY;
    }

    public Image getImgCar() {
        return imgCar;
    }

    public void setImgCar(Image imgCar) {
        this.imgCar = imgCar;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getidAvio() {
        return idAvio;
    }

    public void setIdAvio(String idAvio) {
        this.idAvio = idAvio;
    }

    public void setCmPosition(int cmPosition) {
        this.cmPosition = cmPosition;
    }

    public void setWay(Carrer way) {
        this.way = way;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getWidthInCm() {
        // TODO Auto-generated method stub
        return this.cmWidth;
    }

    public void setPositionInCm(int cmPos) {
        this.cmPosition = cmPos;

    }

}
