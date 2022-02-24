/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockettcp;

import java.net.*;
import java.io.*;
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
    private static final int _PUERTO = 1234;

    public static void main(String[] args) {
        InetAddress ipServidor = null;
        try {

            // TODO code application logic here
            
            ipServidor = InetAddress.getByName(args[0]);
            System.out.println(ipServidor);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        
        for (int i = 1; i < args.length; i++) {
           
                //ENVIAR DATOS
                
                Socket socketCliente = null;
                DataInputStream datosRecepcion = null;
                DataOutputStream datosSalida = null;
                
                try{
                   int numero = Integer.parseInt(args[i]);
                   
                   socketCliente =new Socket(ipServidor,_PUERTO);
                   datosRecepcion =new DataInputStream(socketCliente.getInputStream());
                   datosSalida = new DataOutputStream(socketCliente.getOutputStream());
                   
                   datosSalida.writeInt(numero);
                   
                   long resultado = datosRecepcion.readLong();
                    System.out.println("Solicitud: "+numero + " \tResultado: "+resultado);
                    datosRecepcion.close();
                    datosSalida.close();
                   
                }catch(Exception ex){
                    
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
                try{
                    if(socketCliente != null)
                        socketCliente.close();
                }catch(IOException ex){
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            
        }

    }

}
