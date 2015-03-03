

package sockets;

import Aeropuerto.Avio;
import Utils.Constantes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author broker
 */
public class Servidor {
 
    
  public static void enviarAvion(Avio avion ) throws IOException{
  
    InetAddress destino = InetAddress.getByName(Constantes.ip_servidor);
    int puerto = Constantes.puerto_socket;
    
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ObjectOutputStream objout = new ObjectOutputStream(out);
    objout.writeObject(avion);
    byte [] byteObj = out.toByteArray();
    DatagramPacket paquete = new DatagramPacket( byteObj, byteObj.length, destino, puerto );
    DatagramSocket socket = new DatagramSocket();
    socket.send(paquete);
  }

}
