package Aeropuerto;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;


public class Aeroport extends JFrame implements Runnable, MouseWheelListener, ActionListener, ComponentListener {

    public static final int CIUTAT_CM_WIDTH = 60000; // Amplada ciutat
    public static final int CIUTAT_CM_HEIGHT = 34500; // Al�ada ciutat
    public static final int FRAME_PIX_WIDTH = 1350; // Amplada window
    public static final int FRAME_PIX_HEIGHT = 750;  // Al�ada window 
    public static final int MAPA_PIX_WIDTH = 1080; // Amplada window
    public static final int MAPA_PIX_HEIGH = 620;  // Al�ada window 
    private JButton bUp, bDown, bLeft, bRight, bZoomPlus, bZoomMinus;
    private JButton bBottomFill1, bBottomFill2;
    private JTable tStatistics;
    private Mapa map;
    private Carrer carrer;
    private Controlador traffic;
    private static volatile boolean pauseCity;
    private static volatile boolean endCity;

    public Aeroport() {
        this.map = new Mapa(
                Aeroport.CIUTAT_CM_WIDTH, Aeroport.CIUTAT_CM_HEIGHT,
                Aeroport.MAPA_PIX_WIDTH, Aeroport.MAPA_PIX_HEIGH);

        this.createFrame();
        
        traffic = new Controlador(map.getCarrers());
        
        this.map.setControlador(this.traffic);

        this.setLocationRelativeTo(null);

        new Thread(this.map, "Hilo 1").start();
        new Thread(this.traffic, "Hilo 2").start();
        this.play(); // Arracar el simul·lador

        new Thread(this, "Hilo 3").start();
    }

    public static void main(String[] args) {
        Aeroport ciutat = new Aeroport();
        ciutat.play();
 
    }

    public static int getCmWidth() {
        return Aeroport.CIUTAT_CM_WIDTH;
    }

    public static int getCmHeight() {
        return Aeroport.CIUTAT_CM_HEIGHT;
    }

    public static int getFramePixWidth() {
        return Aeroport.FRAME_PIX_WIDTH;
    }

    public static int getFramePixHeight() {
        return Aeroport.FRAME_PIX_HEIGHT;
    }

    
    private void addButtonsToPane(Container pane) {
        this.bUp = new JButton("Up");
        this.bDown = new JButton("Down");
        this.bLeft = new JButton("<");
        this.bRight = new JButton(">");
        this.bZoomPlus = new JButton("Z+");
        this.bZoomMinus = new JButton("Z-");
        this.bBottomFill1 = new JButton("6");
        this.bBottomFill2 = new JButton("7");

        this.bUp.addActionListener(this);
        this.bDown.addActionListener(this);
        this.bLeft.addActionListener(this);
        this.bRight.addActionListener(this);
        this.bZoomPlus.addActionListener(this);
        this.bZoomMinus.addActionListener(this);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.weighty = 0;
        c.gridx = 1;
        c.gridy = 10;
        pane.add(this.bUp, c);

        c.gridx++;
        pane.add(this.bDown, c);

        c.gridx++;
        pane.add(this.bLeft, c);

        c.gridx++;
        pane.add(this.bRight, c);

        c.gridx++;
        pane.add(this.bZoomPlus, c);

        c.gridx++;
        pane.add(this.bZoomMinus, c);

        c.gridx++;
        pane.add(this.bBottomFill1, c);

        c.weightx = 0;
        c.weighty = 0;
        c.gridx++;
        pane.add(this.bBottomFill2, c);
    }

    private void addMapToPane(Container pane) {
        GridBagConstraints c = new GridBagConstraints();

        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1F;
        c.weighty = 0;
        c.gridheight = 10;
        c.gridwidth = 8;
        pane.add(this.map, c);
    }





    private void createFrame() {
        Container panel;
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());

        panel = this.getContentPane();
        this.addMapToPane(panel);
        this.addButtonsToPane(panel);
       
        panel.addMouseWheelListener(this);
        
        this.pack();
        this.setVisible(true);

        addComponentListener(this);
    }

    public static boolean isEnd() {
        return Aeroport.endCity;
    }

    public static boolean isPaused() {
        return Aeroport.pauseCity;
    }

    public static void play() {
        // Iniciar el rellotge
        // Engegar el generador de trafic
        Aeroport.pauseCity = false;
        Aeroport.endCity = false;
    }

    public static void pause() {
        // Aturar el rellotge
        // Aturar el generador de trafic
        // Bloquejar el vehicles

        Aeroport.pauseCity = true;
    }

    private void showStatistics() {
        int row, col;

    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();

        if (str.equals("Z+")) {
            this.map.zoomIn(0.1f);
        } else if (str.equals("Z-")) {
            this.map.zoomOut(0.1f);
        } else if (str.equals("Up")) {
            this.map.moveUp();
        } else if (str.equals("Down")) {
            this.map.moveDown();
        } else if (str.equals("<")) {
            this.map.moveLeft();
        } else if (str.equals(">")) {
            this.map.moveRight();
        }
    }
   
    
    @Override
    public void componentResized(ComponentEvent e) {
        this.map.setFactorXY();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        if (notches < 0) {
            this.map.zoomIn();
        } else {
            this.map.zoomOut();
        }
    }

  
  
    @Override
    public void run() {
        while (!Aeroport.isEnd()) {
            if (!Aeroport.isPaused()) {
                this.showStatistics();
            }

            try {
                Thread.sleep(800); // nano -> ms
            } catch (InterruptedException ex) {
            }
        }

    }
}