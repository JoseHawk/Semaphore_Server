package Server2;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client_Thread implements Runnable {
    
     static DataOutputStream salida;
     static DataInputStream entrada;
     static String mensajeRecibido;
     static Socket socketCliente1;
     private Server servidor;
     private boolean bloqueado=false;
     static final int puertoEnvio1=7000;
     static final int puertoEnvio2=8000;
     static Socket conexionEnvio;
     static DataOutputStream mensajeEnviado;
     
     public Client_Thread (Socket socketCliente1)
    {
        
         try {
              salida = new DataOutputStream(socketCliente1.getOutputStream());
              entrada = new DataInputStream(socketCliente1.getInputStream());
             
            
         } catch (IOException ex) {
             Logger.getLogger(Client_Thread.class.getName()).log(Level.SEVERE, null, ex);
         }
         
        
    }
        
    public void run ()
    {
        int identificador=0;
        int identificadorCliente=0;
        int valor=0;
        boolean continuar=true;
        
         try{
                byte mensaje[]=new byte[1];
                entrada.read(mensaje);

                //Comprobamos a que recurso desea acceder
                identificador=3;
                
                //Comprobamos el cliente que esta intentando hacer la conexion
                if(mensaje[0]==(byte)1 || mensaje[0]==(byte)2 || mensaje[0]==(byte)3 ||  mensaje[0]==(byte)7 || mensaje[0]==(byte)8 || mensaje[0]==(byte)9){
                    identificadorCliente=1;
                }
             
                if(mensaje[0]==(byte)4 || mensaje[0]==(byte)5 || mensaje[0]==(byte)6 || mensaje[0]==(byte)10 || mensaje[0]==(byte)11 || mensaje[0]==(byte)12){
                    identificadorCliente=2;
                }
                
                //¿Existe el recurso?
                valor=servidor.semaforo.devolverValor(identificador);
                if(valor==999){ //Si vale 999, no tenemos semaforo para el recurso. Informar al cliente
                    salida.write((byte)14);
                    if(identificadorCliente==1){
                        conexionEnvio = new Socket(InetAddress.getByName("192.168.60.252"), puertoEnvio1);
                    }else{
                        conexionEnvio = new Socket(InetAddress.getByName("192.168.60.252"), puertoEnvio2);
                    }
                        
                    mensajeEnviado = new DataOutputStream(conexionEnvio.getOutputStream());
                    mensajeEnviado.write((byte)14);
                    
                }else{          //Si existe el recurso, diferenciamos entre UP y DOWN
                    
                    //Comprobamos si es un mensaje de liberacion de recurso
                    
                    if((mensaje[0]==(byte)10 || mensaje[0]==(byte)11 || mensaje[0]==(byte)12 || mensaje[0]==(byte)7 || mensaje[0]==(byte)8 || mensaje[0]==(byte)9)&&continuar==true){  
                        servidor.semaforo.incrementarSemaforo(identificador); //Sumamos 1 al semaforo
                        valor=servidor.semaforo.devolverValor(identificador);
                        
                        salida.write((byte)22); 
                        
                        if(identificadorCliente==1){
                            servidor.cliente2_bloqueado=false;
                        }else{
                            servidor.cliente1_bloqueado=false;
                        }
                        continuar=false;
                    }
                    
                    //Comprobamos si es un mensaje de solicitud de recurso
                     
                    if((mensaje[0]==(byte)1 || mensaje[0]==(byte)2 || mensaje[0]==(byte)3 || mensaje[0]==(byte)4 || mensaje[0]==(byte)5 || mensaje[0]==(byte)6)&&continuar==true){
                        valor=servidor.semaforo.devolverValor(identificador);
                        if(valor<=0){
                            if(identificadorCliente==1){
                                servidor.cliente1_bloqueado=true;
                            }else{
                                servidor.cliente2_bloqueado=true;
                            }
                        }
                        
                        if(identificadorCliente==1){
                            bloqueado=servidor.cliente1_bloqueado;
                        }else{
                            bloqueado=servidor.cliente2_bloqueado;
                        }

                        while(bloqueado==true){ //Esperamos hasta que el recurso este disponible
                            if(identificadorCliente==1){
                                bloqueado=servidor.cliente1_bloqueado; 
                            }else{
                                bloqueado=servidor.cliente2_bloqueado;
                            }
                            
                            System.out.print("");

                        }

                        //Una vez tenemos el recurso, lo indicamos en el semaforo
                        servidor.semaforo.decrementarSemaforo(identificador); //Restamos 1 al semaforo
                        salida.write((byte)25);
                        if(identificadorCliente==1){
                            conexionEnvio = new Socket(InetAddress.getByName("192.168.60.252"), puertoEnvio1);
                        }else{
                            conexionEnvio = new Socket(InetAddress.getByName("192.168.60.252"), puertoEnvio2);
                        }
                        
                        mensajeEnviado = new DataOutputStream(conexionEnvio.getOutputStream());
                        mensajeEnviado.write((byte)25); 

                        conexionEnvio.close();
                        continuar=false;
                    }
                    
                }
                
            }catch(Exception e )
        {
            System.out.println("Error: "+e.getMessage());
        }
         
        
       
    }
    
}
