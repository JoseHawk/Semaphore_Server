package Server2;

import java.util.ArrayList;
import java.net.*;
import java.io.*;

public class Semaphore {
    
    
    private ArrayList <Integer> semaforos;
    
    Semaphore (){
        
       semaforos = new ArrayList<Integer>();
       
       semaforos.add(0,999);
       semaforos.add(1,999);
       semaforos.add(2,999);
       semaforos.add(3,999);
    }
    
    //Añadimos 1 al semaforo indicado por el identificador
    public void incrementarSemaforo (int identificador){
        int valor=0;
        
        valor=semaforos.get(identificador);
        valor=valor+1;
        semaforos.set(identificador, valor);
    }
    
    //Restamos 1 al semaforo indicado por el identificador
    public void decrementarSemaforo (int identificador){
        int valor=0;
        
        valor=semaforos.get(identificador);
        semaforos.set(identificador,valor-1);
        
    }
    
    //Inicializar semaforo
    public void create(int identificador){
        semaforos.set(identificador,1);
    }
    
    //Eliminar semaforo
    public void delete(int identificador){
        semaforos.set(identificador,999);
    }
    
    //Mostrar el valor en una posicion
    public void imprimirValor(int identificador){
        
        int valor=semaforos.get(identificador);
        System.out.println("El valor es: "+valor);
    }
    
    public int devolverValor(int identificador){
        
        int valor=semaforos.get(identificador);
        
        return valor;
    }
}
