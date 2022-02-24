/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package A6;

import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLServerSocket;

/**
 *
 * @author pedro
 */
public class Server {

    private static final String _IP = "25.66.109.25";
    private static final int _PUERTO = 5000;
    private static final int _BAGLOG = 50;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException {

        InetAddress ip = InetAddress.getByName(_IP);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        try {
            System.out.println("IP localhost: " + InetAddress.getLocalHost().toString());

            System.out.println("Escuchando en: \n");
            System.out.println("IP host: " + ip.getHostName());
            System.out.println("PUERTO: " + _PUERTO + "\n");
        } catch (UnknownHostException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        ServerSocket servidor = null;

        try {
            servidor = new ServerSocket(_PUERTO, _BAGLOG, ip);
            System.out.println("Escuchando en: " + servidor.getInetAddress().toString());
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (true) {
            try {
                Socket socketPeticion = servidor.accept();
                DataOutputStream datosSalida = new DataOutputStream(socketPeticion.getOutputStream());
                DataInputStream datosEntrada = new DataInputStream(socketPeticion.getInputStream());

                int puertoRemitente = socketPeticion.getPort();
                InetAddress ipRemitente = socketPeticion.getInetAddress();

                char tipoDato = datosEntrada.readChar();
                int longitud = datosEntrada.readInt();
                if (tipoDato == 's') {
                    
                    byte[] byteDatos = new byte[longitud];
                    boolean finData = false;
                    StringBuilder dataMensaje = new StringBuilder(longitud);
                    int totalLeidos = 0;

                    while (!finData) {
                        int byteActualesLeidos = datosEntrada.read(byteDatos);
                        totalLeidos += byteActualesLeidos;

                        if (totalLeidos <= longitud) {
                            dataMensaje.append(new String(byteDatos, 0, byteActualesLeidos, StandardCharsets.UTF_8));
                        } else {
                            dataMensaje.append(new String(byteDatos, 0,
                                    longitud - totalLeidos + byteActualesLeidos, StandardCharsets.UTF_8));
                        }
                        if (dataMensaje.length() >= longitud) {
                            finData = true;
                        }

                    }

                    System.out.println(formatter.format(new Date()) + "\tCliente = " + ipRemitente.toString() + ":" + puertoRemitente
                            + "\tEntrada = " + dataMensaje.toString() + "\tSalida = " + "OK");

                }

                datosSalida.writeUTF("OK");
                datosEntrada.close();
                datosSalida.close();
                socketPeticion.close();

            } catch (Exception ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
}
