package civitasjava;

import java.util.ArrayList;

public class CasillaCalle extends Casilla {
    private TituloPropiedad tituloPropiedad; // SOLO CUANDO TIPO==CALLE------    

    // Construye casillas de tipo CALLE 
    CasillaCalle(TituloPropiedad titulo){
        super(titulo.getNombre());
        
        this.init();
        
        this.tituloPropiedad = titulo;
    }
        
    // Consultor del atributo tituloPropiedad
    TituloPropiedad getTituloPropiedad(){
        return this.tituloPropiedad;
    }
    
    /**
     *  @brief Inicia los atributos de instancia del objeto. 
     */
    private void init(){
        this.tituloPropiedad = null;           
    }     
    
    /**
    * @brief El jugador dado(si existe), permite que compre la casilla o
    *        si tiene dueño y no está hipotecada, paga el alquiler
    * @param iactual, el indice del jugador
    * @param todos, el vector de jugadores
    * @return Es void
    */
    @Override
    public void recibeJugador(int iactual, ArrayList<Jugador> todos){
        if(jugadorCorrecto(iactual, todos))
        {
            super.recibeJugador(iactual,todos);
            Jugador jugador = todos.get(iactual);
            
            if(!tituloPropiedad.tienePropietario())
            {
                jugador.puedeComprarCasilla();
            }
            else
            {
                tituloPropiedad.tramitarAlquiler(jugador);   
            }    
        }
    }
    
}
