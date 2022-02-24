
package civitasjava;

import java.util.ArrayList;

public class CasillaSorpresa extends Casilla {
    
    private Sorpresa sorpresa;               
    private MazoSorpresas mazo;             
        
    // Construye casillas de tipo SORPRESA
    CasillaSorpresa(MazoSorpresas mazo, String nombre){
        super(nombre);
        this.init();
        
        this.mazo   = mazo;
    }
    
    /**
     *  @brief Inicia los atributos de instancia del objeto. 
     */
    private void init(){
        this.mazo          = null;           
    }
    
        /**
     * @brief Aplica una sorpresa a un jugador que cae en una casilla sorpresa
     * @param actual, el indice del jugador
     * @param todos, el vector de jugador
     * @return void
    */
    @Override
    public void recibeJugador(int iactual, ArrayList<Jugador> todos){
        if(jugadorCorrecto(iactual,todos))
        {
            super.recibeJugador(iactual,todos);
            this.mazo.siguiente().aplicarAJugador(iactual, todos);
        }
    }
}
