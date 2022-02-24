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
public class SorpresaPorJugador extends Sorpresa {
    
    private int valor; // Guarda cuanto se paga por cada jugador
    
    SorpresaPorJugador(int valor, String texto){
        super(texto);
        
        this.valor = valor;
    }
     
    /**
     * @brief Todos los jugadores dan el valor de la sorpresa al jugador actual
     * @param actual, el indice del jugador
     * @param todos, el vector de jugadores
     * @return void
     */
    @Override
    void aplicarAJugador(int actual, ArrayList<Jugador> todos){
      
      if( jugadorCorrecto(actual, todos) ){
        
        this.informe(actual, todos);
        int numJugadores = todos.size();
        
        // Creamos dos Sorpresas con los valores de saldo a modificar
        SorpresaPagarCobrar restoPaga    = new 
                        SorpresaPagarCobrar(-this.valor , "");
        
        SorpresaPagarCobrar actualRecibe = new
                        SorpresaPagarCobrar(this.valor * (numJugadores -1), "");
        
        // Cobramos a todos los jugadores menos el que recibe
        for(int i = 0; i < numJugadores; i++){
            if(i != actual)
                restoPaga.aplicarAJugador(i, todos);    
        }
        
        // Ingresamos el dinero al jugador actual
        actualRecibe.aplicarAJugador(actual, todos);
      }
    }
}
