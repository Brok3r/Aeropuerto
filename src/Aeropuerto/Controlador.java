
/*
 * CONTROLADOR
 * 
 * ArrayList <Avio>
 * ArrayList <Carrer>
 * final int numMaxAvions
 *
 * ---------------
 * 
 * controlador(ArrayListCarrers)
 * getAvions
 * addAvio
 * run (){for  1...40}
 *    sleep
 *    addAvio
 */
package Aeropuerto;

import Aeropuerto.Avio.Direction;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

public class Controlador implements Runnable {

    ArrayList<Avio> avions = new ArrayList<Avio>();
    ArrayList<Carrer> carrers = new ArrayList<Carrer>();
    final int numMaxAvions = 3;
    final int numFingers = 5;
    public ArrayList<Finger> fingers = new ArrayList<>();
    private ArrayList<Avio> aparcados = new ArrayList<>();
    private ArrayList<Avio> esperando = new ArrayList<>();
    private int plazas = 5;

    //arraylist fingers ocupados
    //arraylist fingers libresº
    public Controlador(ArrayList<Carrer> c) {
        // TODO Auto-generated constructor stub
        this.carrers = c;
        for (int i = 0; i < numFingers; i++) {
            addFinger(i);
        }
    }

    public ArrayList<Avio> getAvio() {

        return avions;
    }

    ;

        
        
        public void paintFingers(Graphics g, float factorX, float factorY, int offsetX, int offsetY) {

        Iterator<Finger> itr = this.fingers.iterator();

        while (itr.hasNext()) {
            itr.next().paint(g, factorX, factorY, offsetX, offsetY);
        }

    }

    public void addFinger(int i) {

        // int id, int pos x, int pos y, estat
        int posx = 10000 + (i * 2000);

        this.fingers.add(new Finger("F" + i, 1000, 2000, 2500, posx, 5000, Direction.FORWARD));
        
    }

    public void addAvio(String matricula, Carrer way) {

        Avio avio = new Avio(this, matricula, way);

        avions.add(avio);
        avio.start();

    }//add avio

    public synchronized void paintAvions(Graphics g, float factorX, float factorY, int offsetX, int offsetY) {

        for (int i = 0; i < avions.size(); i++) {

            avions.get(i).paint(g, factorX, factorY, offsetX, offsetY);
        }
    }

    @Override
    public void run() {
		// TODO Auto-generated method stub

        //crear fingers
        for (int i = 0; i < numMaxAvions; i++) {
            try {

                Thread.sleep(1000);
                addAvio("A"+i, carrers.get(0));

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    public synchronized void entrar(Avio avio) { // cotxe entra al pàrquing
        while (!fingerLibre()) {
            try {
                System.out.println(avio.getidAvio() + " :Esperando parking lleno.");
                esperando.add(avio);
                imprimirEstado();
                wait();
            } catch (InterruptedException e) {
            }
        }
        esperando.remove(avio);
        aparcados.add(avio);
        aparcarAvionEnfinger(avio);
        plazas--;
    }

    public void imprimirEstado() {
        for (Finger finger : fingers) {
            finger.imprimirEstado();
        }
        System.out.println("-------");
    }

    private boolean fingerLibre() {
        for (Finger finger : fingers) {
            if (!finger.getOcupado()) {
                return true;
            }
        }
        return false;
    }

    public void aparcarAvionEnfinger(Avio avion) {
        
       Finger f=  buscarFingerVacio();
       f.setAvio(avion);
       f.setEstat(Finger.Estat.OCUPADO);       
      
        }

    public Finger buscarFingerVacio() {
        for (Finger finger : fingers) {
            if (!finger.getOcupado()) {
                return finger;
            }
        }
        return null;
    }

}
