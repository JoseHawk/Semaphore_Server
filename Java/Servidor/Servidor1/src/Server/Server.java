
package Server;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    
    
    static Socket socketCliente;
    static ServerSocket socketServidor;
    static final int PUERTO=5000;
    static DataOutputStream salida1;
    static String mensajeRecibido1;
    static DataOutputStream salida2;
    static String mensajeRecibido2;
    static boolean conexionServidor=false;
    static Semaphore semaforo;
    static boolean cliente1_bloqueado;
    static boolean cliente2_bloqueado;
    static Thread hilo;
    static Runnable nuevoCliente;
    static Socket envio;
    
    
    //SERVIDOR
    
    Server(){
        
        this.semaforo=semaforo;
        this.nuevoCliente=nuevoCliente;
        this.cliente1_bloqueado=cliente1_bloqueado;
        this.cliente2_bloqueado=cliente2_bloqueado;
        this.hilo=hilo;
    }
    
    public static void main(String[] args) {
        
        conexionServidor=true;
        semaforo=new Semaphore();
        cliente1_bloqueado=false;
        cliente2_bloqueado=false;
        
        try

        {
            socketServidor=new ServerSocket(PUERTO);
            
            Runnable nuevoMenu = new Menu_Thread(0);
            Thread hiloMenu = new Thread(nuevoMenu);
            hiloMenu.start();
            
            Runnable nuevoDeadlock = new Deadlock_Thread(0);
            Thread hiloDeadLock = new Thread(nuevoDeadlock);
            hiloDeadLock.start();
            
            
        }   catch(Exception e ){
            System.out.println("Error: "+e.getMessage());
        }
        
       do{
            
            try{
                
                socketCliente=socketServidor.accept();
                
                nuevoCliente = new Client_Thread(socketCliente);
                hilo = new Thread (nuevoCliente);
                hilo.start();
                
                } catch(Exception e )
                {
                System.out.println("Error: "+e.getMessage());
            }
            
       }while(conexionServidor==true);
            
        
         
    }
}
