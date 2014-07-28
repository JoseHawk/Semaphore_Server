package client_interface;

//Librerias
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;

//Clase cliente
public class Client extends javax.swing.JFrame implements Serializable{
    
    static final int puertoServidor1=5000;
    static final int puertoServidor2=6000;
    static final int puertoRecepcion=7000;
    static Socket socketCliente;
    static DataOutputStream salida;            //Flujo de salida por el que enviamos mensaje
    static DataInputStream entrada;            //Flujo de salida por el que recibimos mensaje
    static String mensajeRecibido;
    static boolean conexionCliente=false;
    static ArrayList <Integer> posesionRecurso;
    static InputStreamReader isr=new InputStreamReader(System.in);
    static BufferedReader br=new BufferedReader(isr);
    static byte codigo[] = new byte[1];
    static byte recepcion[] = new byte[1];
    static ServerSocket socketRecepcion;
    static DataInputStream respuesta;
    static Client_Interface interfazCliente;
    
    Client(){
       
       this.conexionCliente=conexionCliente;
       
       this.interfazCliente=new Client_Interface(this);
       
       this.interfazCliente.pack();
       this.interfazCliente.setVisible(true);
       this.interfazCliente.setResizable(false);
       this.interfazCliente.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       
    }
    
    public static void down(int seleccion){
        
        try{
            
            int valorRecurso=0;

            //Comprobamos si no lo tiene
            valorRecurso=posesionRecurso.get(seleccion-1);
            if(valorRecurso==1){
                interfazCliente.getPantalla().setText("You have this resource right now");
            }else{

             if(seleccion==1){  //Si el recurso es el primero => Servidor 1
                 socketCliente = new Socket(InetAddress.getByName("localHost"), puertoServidor1);
                 entrada = new DataInputStream(socketCliente.getInputStream());
                 salida = new DataOutputStream(socketCliente.getOutputStream());
                 codigo[0]=(byte)1;
                 salida.write(codigo);
             }
             if(seleccion==2){  //Si el recurso es el segundo => Servidor 1
                 socketCliente = new Socket(InetAddress.getByName("localHost"), puertoServidor1);
                 entrada = new DataInputStream(socketCliente.getInputStream());
                 salida = new DataOutputStream(socketCliente.getOutputStream());
                 codigo[0]=(byte)2;
                 salida.write(codigo);
             }
             if(seleccion==3){  //Si el recurso es el tercero => Servidor 2
                 socketCliente = new Socket(InetAddress.getByName("localHost"), puertoServidor2);
                 entrada = new DataInputStream(socketCliente.getInputStream());
                 salida = new DataOutputStream(socketCliente.getOutputStream());
                 codigo[0]=(byte)3;
                 salida.write(codigo);
             }

             socketRecepcion=new ServerSocket(puertoRecepcion);
             Socket conexion = socketRecepcion.accept();
             respuesta = new DataInputStream(conexion.getInputStream());
             respuesta.read(recepcion);

             if (recepcion[0]==(byte)14){
                 interfazCliente.getPantalla().setText("There is NO semaphore for this resource. Impossible to access");
             }else{
                 if(recepcion[0]==23){
                     interfazCliente.getPantalla().setText("Resource 1 obtained");
                 }
                 if(recepcion[0]==24){
                     interfazCliente.getPantalla().setText("Resource 2 obtained");
                 }
                 if(recepcion[0]==25){
                     interfazCliente.getPantalla().setText("Resource 3 obtained");
                 }
                 posesionRecurso.set((seleccion-1),1);
             }
             socketRecepcion.close();
             socketCliente.close();
          }
            
        }catch(Exception e ){
            System.out.println("Error: "+e.getMessage());
        }
        
  
    }
    
    public static void up (int seleccion){
        
     try{
        
        int valorRecurso=0;
           //Comprobamos si no lo tiene
        valorRecurso=posesionRecurso.get(seleccion-1);
        if(valorRecurso==0){ 
            interfazCliente.getPantalla().setText("You don't have this resource right now");
        }else{

            if(seleccion==1){  //Si el recurso es el primero => Servidor 1
                socketCliente = new Socket(InetAddress.getByName("localHost"), puertoServidor1);
                entrada = new DataInputStream(socketCliente.getInputStream());
                salida = new DataOutputStream(socketCliente.getOutputStream());
                codigo[0]=(byte)7;
                salida.write(codigo);
            }
            if(seleccion==2){  //Si el recurso es el segundo => Servidor 1
                socketCliente = new Socket(InetAddress.getByName("localHost"), puertoServidor1);
                entrada = new DataInputStream(socketCliente.getInputStream());
                salida = new DataOutputStream(socketCliente.getOutputStream());
                codigo[0]=(byte)8;
                salida.write(codigo);
            }
            if(seleccion==3){  //Si el recurso es el tercero => Servidor 2
                socketCliente = new Socket(InetAddress.getByName("localHost"), puertoServidor2);
                entrada = new DataInputStream(socketCliente.getInputStream());
                salida = new DataOutputStream(socketCliente.getOutputStream());
                codigo[0]=(byte)9;
                salida.write(codigo);
            }

            entrada.read(recepcion);
            if(recepcion[0]==20){
                interfazCliente.getPantalla().setText("Resource 1 released");
            }
            if(recepcion[0]==21){
                interfazCliente.getPantalla().setText("Resource 2 released");
            }
            if(recepcion[0]==22){
                interfazCliente.getPantalla().setText("Resource 3 released");
            }
            posesionRecurso.set((seleccion-1),0);
            socketCliente.close();

        }
     
     }catch(Exception e ){
            System.out.println("Error: "+e.getMessage());
     }
     
    }
   
 
    public static void main(String[] args) {
        
        new Client();
      
        conexionCliente=true;   //Conectamos el cliente
        posesionRecurso = new ArrayList<Integer>();
        posesionRecurso.add(0,0);
        posesionRecurso.add(1,0);
        posesionRecurso.add(2,0);
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
            }
        });
        
    } //Final del main
    
} //Final de la clase
