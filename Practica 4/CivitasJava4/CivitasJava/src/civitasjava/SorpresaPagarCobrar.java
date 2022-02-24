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
public class SorpresaPagarCobrar extends Sorpresa {
    
    private int valor; // Guarda la cantidad de dinero a pagar o cobrar
        
    SorpresaPagarCobrar(int valor, String texto){
        super(texto);
        
        this.valor = valor;
    }
    
    
    /**
     * @brief Aplica la sorpresa de modificar el saldo en funcion de @valor
     * @param actual, el indice del jugador
     * @param todos, el vector de jugadores
     * @return void
     */    
    @Override
    void aplicarAJugador (int actual, ArrayList<Jugador> todos){
        
        if( jugadorCorrecto(actual, todos) ){
            
            this.informe(actual, todos);
            todos.get(actual).modificarSaldo(this.valor);
        }
    }
    
    
}
