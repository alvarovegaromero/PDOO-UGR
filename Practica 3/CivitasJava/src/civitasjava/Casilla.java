
package civitasjava;

import java.util.ArrayList;

// Si no hay declarada ninguna visibilidad, tendra visibilidad de paquete

public class Casilla {
    
    private static int carcel = 1;
    private float importe;                   //SOLO CUANDO TIPO==IMPUESTO----
    private String nombre;
    
    private TipoCasilla tipo;

    private TituloPropiedad tituloPropiedad; // SOLO CUANDO TIPO==CALLE------
    
    private Sorpresa sorpresa;               // SOLO CUANDO TIPO==SORPRESA---
    private MazoSorpresas mazo;              // SOLO CUANDO TIPO==SORPRESA---
    

    // Construye casillas de tipo DESCANSO
    Casilla(String nombre){
        this.init();
        this.tipo = TipoCasilla.DESCANSO;
        
        this.nombre = nombre;
    }
    

    // Construye casillas de tipo CALLE 
    Casilla(TituloPropiedad titulo){
        this.init();
        this.tipo = TipoCasilla.CALLE;
        
        this.tituloPropiedad = titulo;
        this.nombre = titulo.getNombre();
    }
    
    
    // Construye casillas de tipo IMPUESTO
    Casilla(float cantidad, String nombre){
        this.init();
        this.tipo = TipoCasilla.IMPUESTO;
        
        this.importe = cantidad;
        this.nombre  = nombre;
    }
    
    
    // Construye casilla de tipo JUEZ
    Casilla(int numCasillaCarcel, String nombre){ // Solo debe construirlas el metodo añadeJuez
        this.init();
        this.tipo   = TipoCasilla.JUEZ;
        
        this.carcel = numCasillaCarcel;
        this.nombre = nombre;
    }
    
    
    // Construye casillas de tipo SORPRESA
    Casilla(MazoSorpresas mazo, String nombre){
        this.init();
        this.tipo = TipoCasilla.SORPRESA;
        
        this.mazo   = mazo;
        this.nombre = nombre;
    }
    
    
    // Consultor del atributo nombre
    public String getNombre(){
        return this.nombre;
    }
    
    // Consultor del atributo tituloPropiedad
    TituloPropiedad getTituloPropiedad(){
        return this.tituloPropiedad;
    }
    
    
    /**
     * @brief Informa de que el jugador todos.[actual] ha caido en una casilla
     * @param actual, el indice del jugador
     * @param todos, el vector de jugadores
     * @return void
     * @pre Debe existir el jugador
     */
    private void informe(int iactual, ArrayList<Jugador> todos){ 
        Diario.getInstance().ocurreEvento(
                "El jugador " + todos.get(iactual).getNombre() +
                " ha caido en la casilla " + this.getNombre());
    }
    
    
    /**
     *  @brief Inicia los atributos de instancia del objeto. 
     */
    private void init(){
        this.importe         = 0;
        this.nombre          = "";
        
        this.tipo            = null;
        this.tituloPropiedad = null;
        
        this.sorpresa        = null;
        this.mazo            = null;            
    }
    
    
    /**
     * @brief Comprueba si un indice existe en un vector de jugadores
     * @param actual, el indice a comprobar
     * @param todos, el vector
     * @return TRUE si es un indice valido para acceder a los elementos de @todos
     */
    boolean jugadorCorrecto(int iactual, ArrayList<Jugador> todos){
        return todos.size() > iactual && iactual >= 0;
    }
    
    /**
    * @brief La propiedad recibe a un jugador que ha caido sobre ella
    * @param iactual, el indice del jugador
    * @param todos, el vector de jugadores
    * @return Es void
    */
    void recibeJugador(int iactual, ArrayList<Jugador> todos){
        switch(this.tipo){
         case CALLE   : recibeJugador_calle(iactual, todos); break;
         case IMPUESTO    : recibeJugador_impuesto(iactual, todos); break;
         case JUEZ : recibeJugador_juez(iactual, todos); break;
         case SORPRESA: recibeJugador_sorpresa(iactual, todos); break;
         default  : informe(iactual,todos); break;
      }  
    }
    
    
    /**
    * @brief El jugador dado(si existe), permite que compre la casilla o
    *        si tiene dueño y no está hipotecada, paga el alquiler
    * @param iactual, el indice del jugador
    * @param todos, el vector de jugadores
    * @return Es void
    */
    private void recibeJugador_calle(int iactual, ArrayList<Jugador> todos){
        if(jugadorCorrecto(iactual, todos))
        {
            informe(iactual,todos);
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
    
    
    /**
     * @brief El jugador dado(si existe), paga un impesto
     * @param actual, el indice del jugador
     * @param todos, el vector de jugador
     * @return void
     */
    private void recibeJugador_impuesto(int iactual, ArrayList<Jugador> todos){
        if( jugadorCorrecto(iactual, todos) ){
            this.informe(iactual, todos);
            
            todos.get(iactual).pagaImpuesto(importe);
        }
    }
    
    
    /**
     * @brief Encarcela a el jugador dado(si existe)
     * @param actual, el indice del jugador
     * @param todos, el vector de jugador
     * @return void
     */
    private void recibeJugador_juez(int iactual, ArrayList<Jugador> todos){
        if( jugadorCorrecto(iactual, todos) ){
            this.informe(iactual, todos);
            
            todos.get(iactual).encarcelar(carcel);
        }
    }
    
    /**
     * @brief Aplica una sorpresa a un jugador que cae en una casilla sorpresa
     * @param actual, el indice del jugador
     * @param todos, el vector de jugador
     * @return void
    */
    private void recibeJugador_sorpresa(int iactual, ArrayList<Jugador> todos){
        if(jugadorCorrecto(iactual,todos))
        {
            Sorpresa sorpresa = mazo.siguiente();
            informe(iactual,todos);
            sorpresa.aplicarAJugador(iactual, todos);
        }
    }
    
    
    /**
     * @brief Devuelve el estado del objeto en forma de un string
     * @return Un string con la informacion
     */
    public String toString(){
        String info = ("Nombre: "            + this.getNombre()      +
                       "\nTipo de casilla: " + this.tipo.toString() );
        
        if(this.importe != 0)
            info += "\nImporte: "        + this.importe;
        
        if(this.tituloPropiedad != null)            
            info += this.tituloPropiedad.toString();
        
        if(this.sorpresa != null)
            info += this.sorpresa.toString();
        
        // No necesitamos verificar el tipo antes de los if porque una
        // casilla solo puede ser de un tipo y por tanto, o tiene 
        // titulo de propiedad o tiene sorpresas, nunca ambas.
                
        return info;
    }
        
} // Fin de la clase CASILLA
