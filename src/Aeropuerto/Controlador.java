package Aeropuerto;

import Aeropuerto.Avio.Direction;
import Utils.Constantes;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import sockets.Cliente;
import sockets.Servidor;

public class Controlador implements Runnable {

    

    ArrayList<Avio> avions = new ArrayList<>();
    ArrayList<Carrer> carrers = new ArrayList<>();
    public ArrayList<Finger> fingers = new ArrayList<>();
 

    public Controlador(ArrayList<Carrer> c) {
     
        this.carrers = c;
        for (int i = 0; i < Constantes.numFingers; i++) {
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

        int i= 0;
        while(true){
            try {
                
                Thread.sleep(1000);
                if(fingerLibre()){ 
                      Avio tmp=null;
                    tmp = Cliente.recibirAvion();
                    //Si los aviones se reciben por sockets:
                    if(null != tmp){
                        avions.add(tmp);
                     
                    }else {
                    addAvio("A"+i, carrers.get(0));
                    Servidor.enviarAvion(avions.get(i));
                    i++;  
                    }
                  
                }
               

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException ex) {
            }

        }

    }

    public synchronized void entrar(Avio avio) { // cotxe entra al pàrquing
        while (!fingerLibre()) {
            try {
                String mensaje = (avio.getidAvio().isEmpty())? "Ningún avión esperando.":avio.getidAvio() + " :Esperando parking lleno.";
                System.out.println(mensaje);
                imprimirEstado();
                wait();
             
            } catch (InterruptedException e) {
            }
        }
       
        aparcarAvionEnfinger(avio);
       
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

    public void delete(Avio avio) {
        avions.remove(avio);
    }
    public void aparcarAvionEnfinger(Avio avion) {
        
       Finger f=  buscarFingerVacio();
       f.setAvio(avion);
       f.setEstat(Finger.Estat.OCUPADO);       
      
        }
    public synchronized void salir(Avio avio) { 
        Finger f = getFingerAvion(avio);
        f.setAvio(null);
        f.setEstat(Finger.Estat.VACIO);
        imprimirEstado();
        notifyAll();
 
      
        
    }
    public Finger buscarFingerVacio() {
        for (Finger finger : fingers) {
            if (!finger.getOcupado()) {
                return finger;
            }
        }
        return null;
    }
    public Finger getFingerAvion(Avio avion){
        for (Finger finger : fingers) {
            if(finger.getAvio()==avion){
                return finger;
            }
        }return null;
    }

}
