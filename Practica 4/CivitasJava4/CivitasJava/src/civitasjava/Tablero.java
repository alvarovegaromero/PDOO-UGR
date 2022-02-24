package civitasjava;

import java.util.ArrayList;

//IMPORTANTE: LA CLASE VA DE LA CASILLA 0 A LA NUM_CASILLAS-1

public class Tablero 
{
    // Atributos de instancia
    private final int numCasillaCarcel;
    private ArrayList<Casilla> casillas;
    private int porSalida;
    private boolean tieneJuez;
    
    private static final int POSPORDEFECTOCARCEL = 1;
    private static final int TABLEROERRONEO = -1;
    
// Constructor con un parametro
// @param numCasillaCarcel indice de la casilla de la carcel
    Tablero(int numCasillaCarcel){
        
        //Guarda el valor de casilla_carcel si es >= 1 e
        if(numCasillaCarcel >= POSPORDEFECTOCARCEL)
            this.numCasillaCarcel = numCasillaCarcel;
        else
            this.numCasillaCarcel = POSPORDEFECTOCARCEL;
        
        //Inicializamos a un vector vacío
        casillas = new ArrayList<>();
        
        //Inicializamos las veces que paso por salida a 0 y tieneJuez a false
        porSalida = 0;
        tieneJuez = false;
        
        //Primera casilla = salida
        
        añadeCasilla(new Casilla("Salida"));
    }
    
// @brief Metodo para comprobar si se puede jugar en el tablero
// @return true si se puede jugar, false en caso contrario
    private Boolean correcto(){
        return((casillas.size() > numCasillaCarcel) && tieneJuez);
    }
    
  
// @brief Comprueba si se puede jugar y el parametro es valido para acceder a @casillas
// @param numCasilla indice de la casilla que se desea comprobar si es valida 
// @return true si se puede jugar y numCasilla es correcta, 
//  false en caso contrario    
    private Boolean correcto(int numCasilla){
        return (correcto() && numCasilla < casillas.size());
    }
    
// @brief Consultor del atributo numCasillaCarcel
// @return Devuelve la casilla del atributo de instancia numCasillaCarcel
    int getCarcel(){
        return numCasillaCarcel;
    }
    
// @brief Resta uno al valor de porSalida si es mayor que cero
// @return Devuelve el valor de porSalida cuando se hace la llamada
    int getPorSalida(){
        int copia = porSalida; //En cualquiera de los casos, 
                               //devuelve el valor porSalida inicial, así
                               // que lo devolvemos
        
        if(porSalida > 0)      // Condición del enunciado
            porSalida--;
        
        return copia;        
    }
      
// @brief Metodo para aniadir casillas al tablero
// @param casilla instancia de la clase Casilla a agregar
// @return void
    void añadeCasilla(Casilla casilla){
        if(casillas.size() == numCasillaCarcel)
            this.casillas.add(new Casilla("Carcel"));  // Casilla tipo descanso

        this.casillas.add(casilla);
        
        if(casillas.size() == numCasillaCarcel)
            this.casillas.add(new Casilla("Carcel"));  // Casilla tipo descanso
    }
    
// @brief Metodo para agregar un Juez al tablero
// @allows que no haya dos casillas de Juez en el tablero
    void añadeJuez(){
        if( !tieneJuez ){   // Verificamos que la variable booleana no esta activa
            tieneJuez = true;     // Actualizamos el valor de tieneJuez
            añadeCasilla(new CasillaJuez(this.numCasillaCarcel, "Juez"));  // Añadimos la casilla "Juez"
        }
    }
    
// @brief Devuelve la casilla que ocupa la posicion pasada como parametro
// @param posicion a buscar
// @return casilla en la posicion numCasilla si es valida, null en otro caso
    Casilla getCasilla(int numCasilla){
        if(correcto(numCasilla))
            return casillas.get(numCasilla);
        else
            return null;
    }
    
// @brief Metodo para calcular la posicion de un jugador tras tirar el dado
// @param actual posicion actual del jugador
// @param tirada cuantas posiciones se avanzan (numero que ha salido en el dado)
// @return casilla donde debe colocarse el jugador (-1 si no se puede ir a esa casilla)
    int nuevaPosicion(int actual, int tirada){ 
        int casillaFinal = TABLEROERRONEO;
        if(correcto())
        {
            // Si se pasa por salida se incrmenta el contador
            porSalida += (actual+tirada)/casillas.size();
                                              
            // Devolvemos la casilla final
            casillaFinal = (actual+tirada)%casillas.size(); 
        }
            return casillaFinal;
    }
    
// @brief Metodo para calcular cuantas casillas faltan para llegar a una determinada casilla
// @param origen posicion donde se encuentra el jugador
// @param destino lugar hasta el que queremos calcular el numero de casillas
// @return el numero de casillas desde origen hasta destino (en el sentido del juego, no la geodesica) 
    int calcularTirada(int origen, int destino){
        int tirada = destino - origen;
        
        if(tirada < 0)     // Si es <= 0, entonces no es un posición real
            tirada += casillas.size();
        
        return tirada;
    }
}
