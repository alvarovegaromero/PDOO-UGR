
package civitasjava;

import java.util.ArrayList;

public abstract class Sorpresa {
    
    private String texto;
    
    Sorpresa(String texto){
        this.texto = texto;
    }
       
    
    /**
     * @brief Informa de una sorpresa al jugador pasado como parametro
     * @param actual, el indice del jugador
     * @param todos, el vector de jugadores
     * @return void
     * @pre Debe existir el jugador
     */
    void informe(int actual, ArrayList<Jugador> todos){
        Diario.getInstance().ocurreEvento
                                  ( "Se ha aplicado al jugador " 
                                   + todos.get(actual).getNombre()
                                   + " la sorpresa\n"
                                   + this.toString()  
                                  );
    }
        
    
    /**
     * @brief Comprueba si un indice existe en un vector de jugadores
     * @param actual, el indice a comprobar
     * @param todos, el vector
     * @return TRUE si el indice es valido para acceder a los elementos de @todos
     */
    public boolean jugadorCorrecto(int actual, ArrayList<Jugador> todos){
        return actual >= 0 && todos.size() > actual;
    }  

    
    /**
     * @brief Devuelve el nombre de la sorpresa(tipo)
     * @return Un string con el nombre
     */
    @Override
    public String toString(){
        return ("Texto: " + this.texto);
    }

    
    
    /**
     * @brief Aplica una sorpresa en funcion del autrbuto de instancia @tipo
     * @param actual, el indice del jugador
     * @param todos, el vector de jugadores
     * @return void
     */
    void aplicarAJugador(int actual, ArrayList<Jugador> todos){
        
    }
    
    /**
     * @brief Si la sorpresa es la que evita la carcel, habilita la
     *        carta especial en el mazo de sorpresas
     * @return void
     */
    void usada(){
       
    }
    
    
    /**
     * @brief Metodo para probar la clase
     * @param args 
     *
    public static void main(String[] args){
        
        int casillaCarcel = 5;
        Tablero tablero = new Tablero(casillaCarcel);
        MazoSorpresas mazo = new MazoSorpresas();
        
        // Creamos varias sorpresas
        Sorpresa enviaCarcel  = new Sorpresa(TipoSorpresa.IRCASILLA, tablero);
        Sorpresa enviaCasilla = new Sorpresa(TipoSorpresa.IRCASILLA, 0, "Salida");
        Sorpresa beneficios = new Sorpresa(TipoSorpresa.PORJUGADOR, 50, "Fiesta");
        Sorpresa evitaCarcel  = new Sorpresa(TipoSorpresa.SALIRCARCEL, mazo);
        
        // Las mostramos
        System.out.println(enviaCarcel.toString() + "\n");
        System.out.println(enviaCasilla.toString()+ "\n");
        System.out.println(beneficios.toString()  + "\n");
        System.out.println(evitaCarcel.toString() + "\n");
    }
    */
        
} // Fin clase SORPRESA
