

package sockets;

import Aeropuerto.Avio;
import Utils.Constantes;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author broker
 */
public class Cliente {

    
    public static Avio recibirAvion() {
        
        byte[] missatge=new byte[1024];
        
        int portEscoltar=Constantes.puerto_socket;
        DatagramPacket paquet= new DatagramPacket(missatge,missatge.length);
       
        try {
             DatagramSocket socket= new DatagramSocket(portEscoltar);
            socket.setSoTimeout(500);
        
            socket.receive(paquet);
            ByteArrayInputStream bs= new ByteArrayInputStream(missatge); // bytes es el byte[]
        ObjectInputStream is = new ObjectInputStream(bs);
        Avio c = (Avio)is.readObject();
         
        is.close();
        return c;
        } catch (IOException ex) {
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
     
        return null;
       
    } 

}
