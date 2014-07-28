package Server;
import java.net.*;
import java.io.*;

public class Menu_Thread implements Runnable {
    
     static DataOutputStream salida1;
     static String mensajeRecibido1;
     static Socket socketCliente1;
     private Semaphore semaforo;
     private Server servidor;
     
     public Menu_Thread (int valor)
    {
          
    }
     
     private static final String menuPrincipal[]={
        "1.- CREATE",
        "2.- DELETE"
        
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
    
        InputStreamReader isr=new InputStreamReader(System.in);
        BufferedReader br=new BufferedReader(isr);
    
        try{
            entrada=Integer.parseInt(br.readLine());
        }catch(IOException e){
         System.out.println("Error en la lectura");
         System.exit(0);
        }
    
        return entrada;
   
   };
    
    public void run ()
    {
        
        int valorMenu; 
        int entrada=0;
        InputStreamReader isr=new InputStreamReader(System.in);
        BufferedReader br=new BufferedReader(isr);
        
        while(true)
        {
            
            do{
               valorMenu=elegirOpcionMenu(menuPrincipal);                       
            }while(valorMenu<1 || valorMenu>2);
           
            
            switch(valorMenu){
                    
                case 1: valorMenu=3; //CREATE
                    System.out.println("Select the resource for which you want to create a semaphore");
                    
                    try{
                        entrada=Integer.parseInt(br.readLine());
                    }catch(IOException e){
                        System.out.println("Error en la lectura");
                        System.exit(0);
                    }
                    
                    if(servidor.semaforo.devolverValor(entrada)==999){
                        servidor.semaforo.create(entrada);
                        System.out.println("Semaphore "+entrada+" created");  
                    }else{
                        System.out.println("The semaphore already exists");
                    }
                    
                break;
                    
                case 2: valorMenu=4; //DELETE
                    System.out.println("Select the resource for which you want to delete the semaphore");
                    
                    try{
                        entrada=Integer.parseInt(br.readLine());
                    }catch(IOException e){
                        System.out.println("Error en la lectura");
                        System.exit(0);
                    }
                    
                    if(servidor.semaforo.devolverValor(entrada)==999){
                        System.out.println("The semaphore doesn't exist");
                    }
                    
                    if(servidor.semaforo.devolverValor(entrada)==1){
                        servidor.semaforo.delete(entrada); //We delete the semaphore
                        System.out.println("Semaphore "+entrada+" deleted"); 
                    }
                    
                    if(servidor.semaforo.devolverValor(entrada)<1){
                        System.out.println("Impossible to delete the semaphore. The resource is being used");
                    }                       
           
                break;
            }
        }
            
           
            
    }
   
}


