
package civitasjava;

import java.util.Random; 

//La clase sigue el patrón singleton.
public class Dado {
    
    private Random random;      //Atributos de instancia
    private int ultimoResultado;
    private boolean debug;
    
    private static final Dado instance = new Dado();
    private final static int SalidaCarcel = 5;
    
// Constructor por defecto
    private Dado(){     
        debug = false;
        ultimoResultado = 0;
        random = new Random();
    }
    
    // Consultor del atributo instance. Debe ser de paquete y de clase
    static public Dado getInstance(){
        return instance;
    }
    
// @brief Metodo que genera un numero aleatorio y lo almacena en @ultimoResultado
// @return Un natural entre 1 y 6 si debug esta desactivado, 1 en otro caso    
    int tirar(){
        if(debug)
            ultimoResultado = 1;
        else
            //Genera aleatorios entre 0 y 5, sumamos 1 y entre 1 y 6
            ultimoResultado = (random.nextInt(6)+1);
        
        return ultimoResultado;
    }
    
// @brief Metodo para comprobar si un jugador sale de la carcel o no
// @return true si sale de la carcel, false en caso contrario
    boolean salgoDeLaCarcel(){
        tirar();        //Tiramos y si es SalidaCarcel, salimos de la carcel
        return(ultimoResultado == SalidaCarcel);
    }  
      
// @brief Metodo para decidir que jugador empieza el juego
// @param n numero de jugadores
// @return entero entre 0 y n-1. Indice del jugador que empieza
    int quienEmpieza(int n){
        return (random.nextInt(n));   //Aleatorio entre los n jugadores
    }
    
// @brief Modificador del atributo debug
// @param d el valor que tomara el atributo debug
// @allows dejar constancia en el diario
    public void setDebug(boolean d)
    {
        debug = d;
        String modoDado;
        
        if(d)
            modoDado = "setDebug activado (true)";
        else
            modoDado = "setDebug desactivado (false)";
        
        Diario.getInstance().ocurreEvento (modoDado);
    }
    
// @brief Consultor del atributo ultimoResultado
// @return el ultimo resultado
    int getUltimoResultado(){
        return ultimoResultado;
    }
           
            
}
