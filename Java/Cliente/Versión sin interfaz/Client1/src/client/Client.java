package client;

//Librerias
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.net.ServerSocket;
import java.net.Socket;

//Clase cliente
public class Client {
    
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
    
    private static final String menuPrincipal[]={
      "1.- Access to resource (DOWN)",
      "2.- Release resource (UP)",
      "3.- Disconnect"
    };
    
    //Metodo utilizado para mostrar un menu dado por un array de caracteres
    private static void mostrarMenu(String menu[]){       
      
        for(int i=0;i<menu.length;i++){
            System.out.println(menu[i]);}     
    
    }
    
    //Permite interactuar con el teclado para elegir entre las distintas opciones del menu
    private static int elegirOpcionMenu(String menu[]){
     
        mostrarMenu(menu);
    
        int entrada=0;
    
        try{
            entrada=Integer.parseInt(br.readLine());
        }catch(IOException e){
         System.out.println("Error en la lectura");
         System.exit(0);
        }
    
        return entrada;
   
   }
  
    //Realiza la conexion entre cliente y servidor
    public static void conexion(int valorMenu){
        
        int seleccion=0;
        int valorRecurso=0;
        
        try{
              
            switch(valorMenu){
  
                case 1: valorMenu=1;    //DOWN Operation
                
                     System.out.println("Select the resource to access");
                     try{
                         seleccion=Integer.parseInt(br.readLine());
                     }catch(IOException e){
                         System.out.println("Error en la lectura");
                         System.exit(0);
                     }
                     
                     //Comprobamos si no lo tiene
                     valorRecurso=posesionRecurso.get(seleccion-1);
                     if(valorRecurso==1){          
                         System.out.println("You have this resource right now");
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
                             System.out.println("There is NO semaphore for this resource. Impossible to access");
                         }else{
                             if(recepcion[0]==23){
                                System.out.println("Resource 1 obtained");
                             }
                             if(recepcion[0]==24){
                                System.out.println("Resource 2 obtained");
                             }
                             if(recepcion[0]==25){
                                System.out.println("Resource 3 obtained");
                             }
                             posesionRecurso.set((seleccion-1),1);
                         }
                         socketRecepcion.close();
                         socketCliente.close();
                     }
                           
                break;
                    
                case 2: valorMenu=2;    //UP operation
                    System.out.println("Select the resource to release");
                     try{
                         seleccion=Integer.parseInt(br.readLine());
                     }catch(IOException e){
                         System.out.println("Error en la lectura");
                         System.exit(0);
                     }
                     
                     //Comprobamos si no lo tiene
                     valorRecurso=posesionRecurso.get(seleccion-1);
                     if(valorRecurso==0){          
                         System.out.println("You don't have this resource right now");
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
                             System.out.println("Resource 1 released");
                         }
                         if(recepcion[0]==21){
                             System.out.println("Resource 2 released");
                         }
                         if(recepcion[0]==22){
                             System.out.println("Resource 3 released");
                         }
                         posesionRecurso.set((seleccion-1),0);
                         socketCliente.close();
                     
                     }
                     
                break;
                   
                case 3: valorMenu=3;
                    conexionCliente=false;
                break;
            }       

        }catch(Exception e ){
            System.out.println("Error: "+e.getMessage());
        }
        
    }

 
    public static void main(String[] args) {
      
        conexionCliente=true;   //Conectamos el cliente
        posesionRecurso = new ArrayList<Integer>();
        posesionRecurso.add(0,0);
        posesionRecurso.add(1,0);
        posesionRecurso.add(2,0);
        
        int valorMenu;
        
        do{
            
            //Mostramos el menu y pedimos un valor por teclado hasta seleccionar un valor adecuado
            do{
                valorMenu=elegirOpcionMenu(menuPrincipal);
            }while(valorMenu <1 || valorMenu >3);
            
            conexion(valorMenu);
          
        }while(conexionCliente==true);

        
    } //Final del main
    
} //Final de la clase
