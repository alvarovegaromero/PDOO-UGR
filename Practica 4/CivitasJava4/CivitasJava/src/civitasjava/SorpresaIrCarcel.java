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
public class SorpresaIrCarcel extends Sorpresa {
   
    private Tablero tablero;
   
    // Construye la sopresa que envia a la carcel
    SorpresaIrCarcel(Tablero tablero){
        super("Envia a la carcel");

        this.tablero = tablero;
    }
    
    /** @brief Aplica la sorpresa de encarcelar usando el método de jugador encarcelar
     *  @param actual, el indice del jugador
     *  @param todos, el vector de jugadores
     *  @return void
     */
    @Override
    void aplicarAJugador (int actual, ArrayList<Jugador> todos){
        if( jugadorCorrecto(actual, todos) ){
            this.informe(actual, todos);
            todos.get(actual).encarcelar(this.tablero.getCarcel()); 
        }
    }
    
}
