
package civitasjava;

import java.util.ArrayList;

// Si no hay declarada ninguna visibilidad, tendra visibilidad de paquete

public class Casilla {
    
    private static int carcel;  //SET Y GET - LO DEJAMOS EN CASILLA
    private String nombre;

    
    // Construye casillas de tipo DESCANSO
    Casilla(String nombre){
        this.init();        
        this.nombre = nombre;
    }   
    
    // Consultor del atributo nombre
    public String getNombre(){
        return this.nombre;
    }
    
    // Consultor del atributo carcel
    public static int getCarcel() {
        return carcel;
    }

    // Set del atributo nombre
    public static void setCarcel(int aCarcel) {
        carcel = aCarcel;
    }
    
    /**
     * @brief Informa de que el jugador todos.[actual] ha caido en una casilla
     * @param actual, el indice del jugador
     * @param todos, el vector de jugadores
     * @return void
     * @pre Debe existir el jugador
     */
    private void informe(int iactual, ArrayList<Jugador> todos){ 
        Diario.getInstance().ocurreEvento(
                "El jugador " + todos.get(iactual).getNombre() +
                " ha caido en la casilla " + this.getNombre());
    }
    
    /**
     *  @brief Inicia los atributos de instancia del objeto. 
     */
    private void init(){
        this.nombre          = "";           
    }
    
    /**
     * @brief Comprueba si un indice existe en un vector de jugadores
     * @param actual, el indice a comprobar
     * @param todos, el vector
     * @return TRUE si es un indice valido para acceder a los elementos de @todos
     */
    boolean jugadorCorrecto(int iactual, ArrayList<Jugador> todos){
        return todos.size() > iactual && iactual >= 0;
    }
    
    /**
    * @brief La propiedad recibe a un jugador que ha caido sobre ella
    * @param iactual, el indice del jugador
    * @param todos, el vector de jugadores
    * @return Es void
    */
    void recibeJugador(int iactual, ArrayList<Jugador> todos){
        informe(iactual,todos);
    }    
    
    /**
     * @brief Devuelve el estado del objeto en forma de un string
     * @return Un string con la informacion
     */
    @Override
    public String toString(){
        String info = ("Nombre: "+ this.getNombre());
                
        return info;
    }
    
    
        
} // Fin de la clase CASILLA
