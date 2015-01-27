package Aeropuerto;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

public class Finger {
	
	public static enum Estat {OCUPAT, BUID, RESERVAT};
	Estat estat;
	private Avio avio;
	private int posicioX;
	private int posicioY;
	private int num;

	
	public Finger(Estat estat, int posicioX, int posicioY, int num) {
		super();
		this.estat = estat;
		this.posicioX = posicioX;
		this.posicioY = posicioY;
		this.num = num;
	}

	public Finger() {
	
		// TODO Auto-generated constructor stub
		
		this.estat = estat.BUID;
		//this.avio=avio;
		this.posicioX = posicioX;
		this.posicioY = posicioY;
		this.num=num;
	
	}

	public Estat getEstat() {
		return estat;
	}

	public void setEstat(Estat estat) {
		this.estat = estat;
	}
	
	/**
	 * torna true si el figer esta ocupat
	 * @return
	 */
	public boolean getOcupado(){
		return this.estat==estat.OCUPAT || this.estat==estat.RESERVAT;
		
	}
	
	/**
	 * torna true si el finger esta buid
	 * @return
	 */
	public boolean getBuid(){
		return estat==estat.BUID;
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
	
	public void paint(Graphics g, float factorX, float factorY, int offsetX, int offsetY) {
        
    	int iniX, iniY, finX, finY;
        
        iniX = (int)((this.posicioX / factorX) + offsetX);
        iniY = (int)((this.posicioY/ factorY) + offsetY);
        finX = (int)(750 / factorX);
        finY = (int)(1500 / factorY);                
                
        // Paint crossroad
        g.setColor(Color.GRAY);
        g.fillRect(iniX, iniY, finX, finY);
        g.setColor(Color.BLACK);
        g.drawRect(iniX, iniY, finX, finY);

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
}