/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package A6;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pedro
 */
public class Cliente {

    /**
     * @param args the command line arguments
     */
    private static final int _PUERTO = 5000;

    public static void main(String[] args) {
        InetAddress ipServidor = null;
        try {

            // TODO code application logic here
            ipServidor = InetAddress.getByName(args[0]);
            System.out.println(ipServidor);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

        int noPaquetes = Integer.parseInt(args[1]);
        for (int i = 1; i < noPaquetes; i++) {

            //ENVIAR DATOS
            Socket socketCliente = null;
            DataInputStream datosRecepcion = null;
            DataOutputStream datosSalida = null;

            try {
                Random random = new Random();

                double temp = 10 + random.nextInt((43 - 10) + 1);
                int humedad = 10 + random.nextInt((90 - 10) + 1);
                double co2 = 2950 + random.nextInt((3030 - 2950) + 1);
                
                char tipo = 's';
                String data = "$Temp|"+temp+"#Hum|"+humedad+"%#Co2|"+co2+"$";
                byte  [] dataInBytes = data.getBytes(StandardCharsets.UTF_8);
                

                socketCliente = new Socket(ipServidor, _PUERTO);
                datosRecepcion = new DataInputStream(socketCliente.getInputStream());
                datosSalida = new DataOutputStream(socketCliente.getOutputStream());

                datosSalida.writeChar(tipo);
                datosSalida.writeInt(dataInBytes.length);
                datosSalida.write(dataInBytes);
                
                String resultado = datosRecepcion.readUTF();
                
                System.out.println("Solicitud: " + data + " \tResultado: " + resultado);
                datosRecepcion.close();
                datosSalida.close();
                
                Thread.sleep(2000);
            } catch (Exception ex) {

                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (socketCliente != null) {
                    socketCliente.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
