package Server2;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.Socket;


public class Deadlock_Thread implements Runnable {
    
    private Server servidor;
    private boolean cliente1bloqueado=false;
    private boolean cliente2bloqueado=false;
    static DataOutputStream salida1;
    static DataOutputStream salida2;
    private Socket hiloCliente1;
    private Socket hiloCliente2;
    static final int puertoEnvio1=9000;
    static final int puertoEnvio2=9500;

    
    public Deadlock_Thread (int valor)
    {
        
        try {
            hiloCliente1 = new Socket(InetAddress.getByName("localHost"), puertoEnvio1);
            hiloCliente2 = new Socket(InetAddress.getByName("localHost"), puertoEnvio2);
            salida1 = new DataOutputStream(hiloCliente1.getOutputStream());
            salida2 = new DataOutputStream(hiloCliente2.getOutputStream());
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

                if(cliente1bloqueado==true){
                    salida1.write((byte)1);
                }else{
                    salida1.write((byte)0);
                }

                if(cliente2bloqueado==true){
                    salida2.write((byte)1);
                }else{
                    salida2.write((byte)0);
                }
            
            } catch (IOException ex) {
                Logger.getLogger(Deadlock_Thread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
