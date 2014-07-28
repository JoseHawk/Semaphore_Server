package Server;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.ServerSocket;
import java.net.Socket;

public class Deadlock_Thread implements Runnable {
    
    private Server servidor;
    private boolean cliente1bloqueado=false;
    private boolean cliente2bloqueado=false;
    private ServerSocket servidorHilo1;
    private ServerSocket servidorHilo2;
    static DataInputStream entrada1;
    static DataInputStream entrada2;
    static final int puertoEnvio1=9000;
    static final int puertoEnvio2=9500;
    static byte recepcion1[] = new byte[1];
    static byte recepcion2[] = new byte[1];
    
    
    public Deadlock_Thread (int valor)
    {
        try {
            servidorHilo1=new ServerSocket(puertoEnvio1);
            Socket clienteHilo1=servidorHilo1.accept();
            entrada1 = new DataInputStream(clienteHilo1.getInputStream());
            servidorHilo2=new ServerSocket(puertoEnvio2);
            Socket clienteHilo2=servidorHilo2.accept();
            entrada2 = new DataInputStream(clienteHilo2.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(Deadlock_Thread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void run(){
        
        while(true){
               
            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Deadlock_Thread.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                cliente1bloqueado=servidor.cliente1_bloqueado;
                cliente2bloqueado=servidor.cliente2_bloqueado;
                entrada1.read(recepcion1);
                entrada2.read(recepcion2);
                
                //Estudiamos los posibles casos de deadlock
                if(cliente1bloqueado==true && cliente2bloqueado==true){
                    System.out.println("DEADLOCK!");
                }
                
                if(cliente1bloqueado==true && recepcion2[0]==1){
                    System.out.println("DEADLOCK!");
                }
                
                if(recepcion1[0]==1 && cliente2bloqueado==true){
                    System.out.println("DEADLOCK!");
                }
                
                if(recepcion1[0]==1 && recepcion2[0]==1){
                    
                }
                           
            } catch (IOException ex) {
                Logger.getLogger(Deadlock_Thread.class.getName()).log(Level.SEVERE, null, ex);
            }
 

        }
    }
    
}
