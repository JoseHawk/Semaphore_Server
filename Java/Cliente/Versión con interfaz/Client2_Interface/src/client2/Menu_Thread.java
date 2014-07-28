package client2;
import java.net.*;
import java.io.*;
import java.util.Random;

public class Menu_Thread implements Runnable {
    
     static DataOutputStream salida1;
     static String mensajeRecibido1;
     static Socket socketCliente1;
     static Client cliente;
     
     public Menu_Thread (int valor)
    {
          
    }
     
     private static final String menuPrincipal[]={
        "1.- UP",
        "2.- DOWN"
        
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
        int valorSemaforo=0;
        InputStreamReader isr=new InputStreamReader(System.in);
        BufferedReader br=new BufferedReader(isr);
        
        Random rand = new Random();
        
        while(true)
        {
            
            do{
               valorMenu=elegirOpcionMenu(menuPrincipal);                       
            }while(valorMenu<1 || valorMenu>2);
           
            
            switch(valorMenu){
  
                case 1: valorMenu=1;//UP
                    System.out.println("Select the semaphore to perform UP operation");
                     try{
                        entrada=Integer.parseInt(br.readLine());
                    }catch(IOException e){
                        System.out.println("Error en la lectura");
                        System.exit(0);
                    }
                    
                     //ACCIONES DE UP                  
                    
                break;
                    
                case 2: valorMenu=2;//DOWN
                    System.out.println("Select the semaphore to perform DOWN operation");
                    //servidor.semaforo.decrementarSemaforo(entrada);
                    
                    
                    try{
                        entrada=Integer.parseInt(br.readLine());
                    }catch(IOException e){
                        System.out.println("Error en la lectura");
                        System.exit(0);
                    }
                    
                    //ACCIONES DE DOWN
                    
                break;
                    
              
            }
        }
            
           
            
    }
   
}


