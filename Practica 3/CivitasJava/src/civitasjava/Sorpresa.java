
package civitasjava;

import java.util.ArrayList;

public class Sorpresa {
    private String texto;
    private int    valor; // A que casilla te vas, cuanto dinero recibes....
    
    private Tablero       tablero;
    private MazoSorpresas mazo;
    private TipoSorpresa  tipo;
    
    
    /**
     * @brief Aplica una sorpresa en funcion del autrbuto de instancia @tipo
     * @param actual, el indice del jugador
     * @param todos, el vector de jugadores
     * @return void
     */
    void aplicarAJugador(int actual, ArrayList<Jugador> todos){
       switch(this.tipo){
         case IRCASILLA   : aplicarAJugador_irAcasilla  (actual, todos); break;
         case IRCARCEL    : aplicarAJugador_irCarcel    (actual, todos); break;
         case PAGARCOBRAR : aplicarAJugador_pagarCobrar (actual, todos); break;
         case PORCASAHOTEL: aplicarAJugador_porCasaHotel(actual, todos); break;
         case PORJUGADOR  : aplicarAJugador_porJugador  (actual, todos); break;
         case SALIRCARCEL : aplicarAJugador_salirCarcel (actual, todos); break;
      }            
    }
    
    
    /**
     * @brief Modifica la posicion del jugador, actualizáad la casilla donde esta
     * @param actual, el indice del jugador
     * @param todos, el vector de jugadores
     * @return void
     */
    private void aplicarAJugador_irAcasilla  (int actual, ArrayList<Jugador> todos){
        
        if( this.jugadorCorrecto(actual, todos) ){
            
            this.informe(actual, todos); 
            int casillaActual = todos.get(actual).getNumCasillaActual();
            
            // Cuantas posiciones nos faltan hasta llegar a la casilla valor
            int tirada = tablero.calcularTirada(casillaActual, valor);
            
            // Calculamos la posicion donde nos encontramos al desplazarnos
            int nuevaPosicion = tablero.nuevaPosicion(casillaActual, tirada);
            
            // Movemos al jugador a esa nueva posicion
            todos.get(actual).moverACasilla(nuevaPosicion);
            
            // Indicamos a la casilla que reciba al jugador
            tablero.getCasilla(nuevaPosicion).recibeJugador(actual, todos);
            
            // Parece innecesario usar calcularTirada y nuevaPosicion pq
            // sorpresa.valor guarda la casilla donde hay que ir, pero
            // hace falta para guardar si se pasara por la salida (:
        }
    }
    
    
    /** @brief Aplica la sorpresa de encarcelar usando el método de jugador encarcelar
     *  @param actual, el indice del jugador
     *  @param todos, el vector de jugadores
     *  @return void
     */
    private void aplicarAJugador_irCarcel    (int actual, ArrayList<Jugador> todos){
        if( jugadorCorrecto(actual, todos) ){
            this.informe(actual, todos);
            todos.get(actual).encarcelar(this.tablero.getCarcel()); 
        }
    }
        
    
    /**
     * @brief Aplica la sorpresa de modificar el saldo en funcion de @valor
     * @param actual, el indice del jugador
     * @param todos, el vector de jugadores
     * @return void
     */
    private void aplicarAJugador_pagarCobrar (int actual, ArrayList<Jugador> todos){
        
        if( jugadorCorrecto(actual, todos) ){
            
            this.informe(actual, todos);
            todos.get(actual).modificarSaldo(this.valor);
        }
    }
         
    
    /**
     * @brief Modifica el saldo del jugador multiplicando valor con la
     *        cantidad de casas y hoteles que se poseen
     * @param actual, el indice del jugador
     * @param todos, el array de jugadores
     * @return void
     */
    private void aplicarAJugador_porCasaHotel(int actual, ArrayList<Jugador> todos){ 
        
      if( jugadorCorrecto(actual, todos) ){
        this.informe(actual, todos);
        
        Jugador jugador = todos.get(actual);
        jugador.modificarSaldo(this.valor * jugador.cantidadCasasHoteles());
      }
    }
     
    
    /**
     * @brief Todos los jugadores dan el valor de la sorpresa al jugador actual
     * @param actual, el indice del jugador
     * @param todos, el vector de jugadores
     * @return void
     */
    private void aplicarAJugador_porJugador  (int actual, ArrayList<Jugador> todos){
      
      if( jugadorCorrecto(actual, todos) ){
        
        this.informe(actual, todos);
        int numJugadores = todos.size();
        
        // Creamos dos Sorpresas con los valores de saldo a modificar
        Sorpresa restoPaga    = new Sorpresa
                (TipoSorpresa.PAGARCOBRAR, -this.valor                  , "");
        
        Sorpresa actualRecibe = new Sorpresa
                (TipoSorpresa.PAGARCOBRAR,  this.valor*(numJugadores -1), "");
        
        // Cobramos a todos los jugadores menos el que recibe
        for(int i = 0; i < numJugadores; i++){
            if(i != actual)
                restoPaga.aplicarAJugador(i, todos);    
        }
        
        // Ingresamos el dinero al jugador actual
        actualRecibe.aplicarAJugador(actual, todos);
      }
    }
    
    
    // Aqui hay un comentario pregunta SIN COMPROBAR CON ALVARO
    /**
     * @brief Si nadie tiene la sorpresa para evitar la carcel, la obtine el jugador actual
     * @param actual, el indice del jugador
     * @param todos, el vector de jugadores
     * @return void
     */
    private void aplicarAJugador_salirCarcel (int actual, ArrayList<Jugador> todos){
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
     * @brief Informa de una sorpresa al jugador pasado como parametro
     * @param actual, el indice del jugador
     * @param todos, el vector de jugadores
     * @return void
     * @pre Debe existir el jugador
     */
    private void informe(int actual, ArrayList<Jugador> todos){
        Diario.getInstance().ocurreEvento
                                  ( "Se ha aplicado al jugador " 
                                   + todos.get(actual).getNombre()
                                   + " la sorpresa\n"
                                   + this.toString()  
                                  );
    }
    
    
    /**
     * @brief Inicia los atributos de instancia de valor, tablero y mazo
     */
    private void init(){
        this.valor   = -1;
        this.mazo    = null;
        this.tablero = null;
    }
    
    
    /**
     * @brief Comprueba si un indice existe en un vector de jugadores
     * @param actual, el indice a comprobar
     * @param todos, el vector
     * @return TRUE si el indice es valido para acceder a los elementos de @todos
     */
    public boolean jugadorCorrecto(int actual, ArrayList<Jugador> todos){
        return actual >= 0 && todos.size() > actual;
    }
    
    
    /**
     * @brief Si la sorpresa es la que evita la carcel, inhabilita la carta 
     *        especial en el mazo de sorpresa
     * @return void
     */
    void salirDelMazo(){
        if( this.tipo == TipoSorpresa.SALIRCARCEL ) 
            this.mazo.inhabilitarCartaEspecial(this);
    }
    
    
    // Construye la sopresa que envia a la carcel
    Sorpresa(TipoSorpresa tipo, Tablero tablero){
        this.init();
        this.tablero = tablero;
        this.tipo    = tipo;
        
        this.valor   = tablero.getCarcel();
        this.texto   = "Envia a la carcel";
    }
    
    
    // Construye la sorpresa que envia a otra casilla
    Sorpresa(TipoSorpresa tipo, Tablero tablero, int valor, String texto){
        this.init();
        this.tablero = tablero;
        
        this.tipo    = tipo;
        
        this.valor   = valor;
        this.texto   = texto;
    }
   
    
    // Construye todas las sorpresas menos iracasilla, evitarcarcel e ircarcel
    Sorpresa(TipoSorpresa tipo, int valor, String texto){
        this.init();
        
        this.tipo  = tipo;
        
        this.valor = valor;
        this.texto = texto; 
    }
    
    
    
    // Construye la sorpresa que permite evitar la carcel
    Sorpresa(TipoSorpresa tipo, MazoSorpresas mazo){
        this.init();
        this.mazo = mazo;
        
        this.tipo = tipo;
        
        this.texto = "Evita la carcel";
    }
    
    
    /**
     * @brief Devuelve el nombre de la sorpresa(tipo)
     * @return Un string con el nombre
     */
    @Override
    public String toString(){
        return (    "TipoSorpresa: " + this.tipo.toString()
                  + "\nValor       " + this.valor
                  + "\nTexto:      " + this.texto
               );
    }
    
    
    /**
     * @brief Si la sorpresa es la que evita la carcél, habilita la
     *        carta especial en el mazo de sorpresas
     * @return void
     */
    void usada(){
        if(this.tipo == TipoSorpresa.SALIRCARCEL)
            mazo.habilitarCartaEspecial(this);
    }
    
    
    /**
     * @brief Metodo para probar la clase
     * @param args 
     *
    public static void main(String[] args){
        
        int casillaCarcel = 5;
        Tablero tablero = new Tablero(casillaCarcel);
        MazoSorpresas mazo = new MazoSorpresas();
        
        // Creamos varias sorpresas
        Sorpresa enviaCarcel  = new Sorpresa(TipoSorpresa.IRCASILLA, tablero);
        Sorpresa enviaCasilla = new Sorpresa(TipoSorpresa.IRCASILLA, 0, "Salida");
        Sorpresa beneficios = new Sorpresa(TipoSorpresa.PORJUGADOR, 50, "Fiesta");
        Sorpresa evitaCarcel  = new Sorpresa(TipoSorpresa.SALIRCARCEL, mazo);
        
        // Las mostramos
        System.out.println(enviaCarcel.toString() + "\n");
        System.out.println(enviaCasilla.toString()+ "\n");
        System.out.println(beneficios.toString()  + "\n");
        System.out.println(evitaCarcel.toString() + "\n");
    }
    */
        
} // Fin clase SORPRESA
