/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civitasjava;

import java.util.ArrayList;

/**
 *
 * @author francisco
 */
public class SorpresaIrCasilla extends Sorpresa {
    
    private int      valor; // Guarda la casilla a la que se envía
    private Tablero  tablero;
    
    SorpresaIrCasilla(Tablero tablero, int valor, String texto){
        super(texto);
        
        this.tablero = tablero;
        this.valor   = valor;
    }
    
    
    /**
     * @brief Modifica la posicion del jugador, actualizáad la casilla donde esta
     * @param actual, el indice del jugador
     * @param todos, el vector de jugadores
     * @return void
     */
    @Override
    void aplicarAJugador  (int actual, ArrayList<Jugador> todos){
        
        if( this.jugadorCorrecto(actual, todos) ){
            
            this.informe(actual, todos); 
            int casillaActual = todos.get(actual).getNumCasillaActual();
            
            // Cuantas posiciones nos faltan hasta llegar a la casilla valor
            int tirada = tablero.calcularTirada(casillaActual, this.valor);
            
            // Calculamos la posicion donde nos encontramos al desplazarnos
            int nuevaPosicion = tablero.nuevaPosicion(casillaActual, tirada);
            
            // Movemos al jugador a esa nueva posicion
            todos.get(actual).moverACasilla(nuevaPosicion);
            
            // Indicamos a la casilla que reciba al jugador
            tablero.getCasilla(nuevaPosicion).recibeJugador(actual, todos);
            
            // Parece innecesario usar calcularTirada y nuevaPosicion pq
            // sorpresa.valor guarda la casilla donde hay que ir, pero
            // hace falta para guardar si se pasara por la salida (:
        }
    }
}
