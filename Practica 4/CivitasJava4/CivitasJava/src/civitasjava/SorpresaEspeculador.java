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
public class SorpresaEspeculador extends Sorpresa {
    
    private int valor; // Guarda la fianza para salir de la carcel
        
    SorpresaEspeculador(int valor, String texto){
        super(texto);
        
        this.valor = valor;
    }
    
    /**
     * @brief Convierte al jugador actual en jugador especulador
     * @param actual, el indice del jugador
     * @param todos, el array de jugadores
     * @return void
     */
    @Override
    void aplicarAJugador (int actual, ArrayList<Jugador> todos){ 
    
      if(jugadorCorrecto(actual, todos) ){
        this.informe(actual, todos);
        
        Jugador jugador = todos.get(actual);
        Especulador especulador = new Especulador(jugador, this.valor);
        
        // Guardamos pos actual para que sigan el mismo orden tras la conversion
        int indice = todos.indexOf(jugador); 
        
        todos.remove(jugador);
        
        todos.add(indice, especulador);
      }
    }
}
