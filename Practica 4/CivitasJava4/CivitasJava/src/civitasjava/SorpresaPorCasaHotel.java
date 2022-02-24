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
public class SorpresaPorCasaHotel extends Sorpresa {
    
    private int valor; // Guarda cuanto se paga por cada casa u hotel
    
    SorpresaPorCasaHotel(int valor, String texto){
        super(texto);
        
        this.valor = valor;
    }
    
    
    /**
     * @brief Modifica el saldo del jugador multiplicando valor con la
     *        cantidad de casas y hoteles que se poseen
     * @param actual, el indice del jugador
     * @param todos, el array de jugadores
     * @return void
     */
    @Override
    void aplicarAJugador (int actual, ArrayList<Jugador> todos){ 
        
      if( jugadorCorrecto(actual, todos) ){
        this.informe(actual, todos);
        
        Jugador jugador = todos.get(actual);
        jugador.modificarSaldo(this.valor * jugador.cantidadCasasHoteles());
      }
    }
}
