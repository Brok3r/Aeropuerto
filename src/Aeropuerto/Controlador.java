
/*
 * CONTROLADOR
 * 
 * ArrayList <Avio>
 * ArrayList <Carrer>
 * final int numMaxAvions
 * 
 * 
 * ---------------
 * 
 * controlador(ArrayListCarrers)
 * getAvions
 * addAvio
 * run (){for  1...40}
 *    sleep
 *    addAvio
 * 
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
	
	
	public Controlador(ArrayList<Carrer> c) {
		// TODO Auto-generated constructor stub
		this.carrers=c;
	}


	public ArrayList<Avio> getAvio(){

		return avions;
	};
	
	
	public void addAvio(String matricula, Carrer way){
		
		Avio avio = new Avio(matricula,way);

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
		for(int i=0;i<numMaxAvions;i++){	
			try {
				Thread.sleep(500);
				addAvio("DABOA",carrers.get(0));
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}

		}//for

		
	}
	
}
