package civitasjava;

import java.util.ArrayList;

public class CasillaJuez extends Casilla {
        
    private int carcel; //Solo tendremos una casilla juez
    
    // Construye casilla de tipo JUEZ
    CasillaJuez(int numCasillaCarcel, String nombre){ // Solo debe construirlas el metodo añadeJuez
        super(nombre);
        this.init();
        
        this.carcel = numCasillaCarcel;
        setCarcel(numCasillaCarcel); 
    }   
    
    private void init(){
        carcel = 0;           
    }     
    
    /**
     * @brief Encarcela a el jugador dado(si existe)
     * @param actual, el indice del jugador
     * @param todos, el vector de jugador
     * @return void
     */
    @Override
    public void recibeJugador(int iactual, ArrayList<Jugador> todos){
        if( jugadorCorrecto(iactual, todos) ){
            super.recibeJugador(iactual, todos);
            
            todos.get(iactual).encarcelar(carcel);
        }
    }
}
