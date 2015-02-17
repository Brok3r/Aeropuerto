
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

public class Controlador implements Runnable{
	
	ArrayList <Avio> avions = new ArrayList<Avio>();
	ArrayList <Carrer> carrers = new ArrayList<Carrer>();
	final int numMaxAvions = 1;
        final int numFingers = 5;
        public  ArrayList <Finger> fingers = new ArrayList<>();
        
        
        //arraylist fingers ocupados
        //arraylist fingers libresº
	public Controlador(ArrayList<Carrer> c) {
		// TODO Auto-generated constructor stub
		this.carrers=c;
                 for(int i=0;i<numFingers;i++){	
              addFinger(i);
           }
	}

        
	public ArrayList<Avio> getAvio(){

		return avions;
	};

        
        
        public void paintFingers(Graphics g, float factorX, float factorY, int offsetX, int offsetY) {

        Iterator<Finger> itr = this.fingers.iterator();

        while (itr.hasNext()) {
            itr.next().paint(g, factorX, factorY, offsetX, offsetY);
            }

        }
        
    
        
         public void addFinger(int i) {

       // int id, int pos x, int pos y, estat
        int posx = 10000 + (i*2000);
        
            this.fingers.add(new Finger("finger" + i + "", 1000, 2000, 2500, posx, 5000, Direction.FORWARD));
           
        
    }

        
	public void addAvio(String matricula, Carrer way){
		
		Avio avio = new Avio(matricula,way,fingers.get(0));

		avions.add(avio);
		avio.start();
               
		}//add avio
	
	public synchronized void paintAvions(Graphics g, float factorX, float factorY, int offsetX,  int offsetY){
		
		for (int i = 0; i < avions.size(); i++) {
			
			avions.get(i).paint(g, factorX, factorY, offsetX, offsetY);
		}
	}   
        
        
	@Override
	public void run() {
		// TODO Auto-generated method stub
           
          
            //crear fingers
		for(int i=0;i<numMaxAvions;i++){	
			try {
                            
				Thread.sleep(500);
				addAvio("DABOA",carrers.get(0));
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}

		}
	
	}	
}
