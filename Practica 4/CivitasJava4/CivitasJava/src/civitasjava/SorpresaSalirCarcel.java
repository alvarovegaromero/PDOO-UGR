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
public class SorpresaSalirCarcel extends Sorpresa {
    
    private MazoSorpresas mazo;

    SorpresaSalirCarcel(MazoSorpresas mazo){  
        super("Evita la carcel");

        this.mazo = mazo;
    }
    
    /**
     * @brief Si nadie tiene la sorpresa para evitar la carcel, la obtine el jugador actual
     * @param actual, el indice del jugador
     * @param todos, el vector de jugadores
     * @return void
     */
    @Override
    void aplicarAJugador (int actual, ArrayList<Jugador> todos){
      if( jugadorCorrecto(actual, todos) ){
        this.informe(actual, todos);
        
        // Preguntamos si alguien tiene la sorpresa para evitar la carcel
        boolean tieneSalvoconducto = false;
        for(int i = 0; i < todos.size() && !tieneSalvoconducto; i++)
            tieneSalvoconducto = todos.get(i).tieneSalvoconducto();
        
        // Si nadie la tiene, la obtiene el jugador actual
        if(!tieneSalvoconducto){
            todos.get(actual).obtenerSalvoconducto(this);
            this.salirDelMazo();
        }
        
        // Por que no miramos simplemente si alguien tiene la sorpresa 
        // haciendo mazo.cartasEspeciales.size() > 0 ??
      }
    }
    
    
    /**
     * @brief Inhabilita la carta especial en el mazo de sorpresa
     * @return void
     */
    void salirDelMazo(){
        this.mazo.inhabilitarCartaEspecial(this);
    }
    
    
    /**
     * @brief Habilita la carta especial en el mazo de sorpresas
     * @return void
     */
    void usada(){
        this.mazo.habilitarCartaEspecial(this);
    }
}
