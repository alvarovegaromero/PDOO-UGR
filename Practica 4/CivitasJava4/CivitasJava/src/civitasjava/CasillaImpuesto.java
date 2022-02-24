
package civitasjava;

import java.util.ArrayList;

public class CasillaImpuesto extends Casilla {
        
    private float importe;                 

    // Construye casillas de tipo IMPUESTO
    CasillaImpuesto(float cantidad, String nombre){
        super(nombre);
        this.init();
        
        this.importe = cantidad;
    }    
        
    /**
     *  @brief Inicia los atributos de instancia del objeto. 
     */
    private void init(){
        this.importe          = 0;           
    }
    
    /**
     * @brief El jugador dado(si existe), paga un impesto
     * @param actual, el indice del jugador
     * @param todos, el vector de jugadores
     * @return void
     */
    @Override
    public void recibeJugador(int iactual, ArrayList<Jugador> todos){
        if( jugadorCorrecto(iactual, todos) ){
            super.recibeJugador(iactual, todos);
            
            todos.get(iactual).pagaImpuesto(importe);
        }
    }
}
