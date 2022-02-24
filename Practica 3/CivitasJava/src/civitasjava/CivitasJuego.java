/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civitasjava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author Francisco Prados y Alvaro Vega
 */
public class CivitasJuego {
    
    // ATRIBUTOS
    private int indiceJugadorActual;
    
    private ArrayList<Jugador> jugadores; // Este array tiene al menos un elemento
    private MazoSorpresas mazo;
    private Tablero tablero;
    private EstadosJuego estado;
    private GestorEstados gestorEstados;
    
    
    
    // METODOS

    private void avanzaJugador(){
      Jugador jugadorActual = jugadores.get(indiceJugadorActual);
      int posicionActual = jugadorActual.getNumCasillaActual();
      
      int tirada = Dado.getInstance().tirar();
      
      Diario.getInstance().ocurreEvento("Ha salido un " + tirada + " en los dados");
      
      int posicionNueva = tablero.nuevaPosicion(posicionActual, tirada);
      
      Casilla casilla = tablero.getCasilla(posicionNueva);
      contabilizarPasosPorSalida(jugadorActual);
      jugadorActual.moverACasilla(posicionNueva);
      
      casilla.recibeJugador(indiceJugadorActual, jugadores);
      contabilizarPasosPorSalida(jugadorActual);
    }
    
    public boolean cancelarHipoteca(int ip){
        return this.jugadores.get(this.indiceJugadorActual).cancelarHipoteca(ip);
    }
    
    
    // CONSTRUCTOR
    public CivitasJuego(ArrayList<String> nombres){
        
        int numJugadores = nombres.size();
        this.jugadores = new ArrayList<>();
        
        for(int i = 0; i < numJugadores; i++)
            this.jugadores.add( new Jugador(nombres.get(i)) );
            
        this.gestorEstados = new GestorEstados();
        this.estado = this.gestorEstados.estadoInicial();
        
        this.indiceJugadorActual = Dado.getInstance().quienEmpieza(numJugadores);
        
        this.mazo = new MazoSorpresas();        
        
        this.inicializarTablero(this.mazo);
        
        this.inicializarMazoSorpresas(this.tablero);

    }
    
    
    /**
     * @brief Método para comprar la casilla en la que el jugador actual ha caido
     * @return true si puede comprarla, false en otro caso
    */
    public boolean comprar(){
        boolean res;
        
        Jugador jugadorActual = jugadores.get(indiceJugadorActual);
        int numCasillaActual = jugadorActual.getNumCasillaActual();
        Casilla casilla = tablero.getCasilla(numCasillaActual);
        TituloPropiedad titulo = casilla.getTituloPropiedad();
        res = jugadorActual.comprar(titulo);
        
        return res;
    }
    
    public boolean construirCasa(int ip){
        return this.jugadores.get(this.indiceJugadorActual).construirCasa(ip);
    }
    
    public boolean construirHotel(int ip){
        return this.jugadores.get(this.indiceJugadorActual).construirHotel(ip);
    }
    
    /**
     * @brief El jugador pasado como parametro cobra por todas las veces que
     *        ha pasado por la salida en su turno actual
     * @param jugadorActual
     * @return void
     */
    private void contabilizarPasosPorSalida(Jugador jugadorActual){
        while(tablero.getPorSalida() > 0)
            jugadorActual.pasaPorSalida();
    }
    
    
    /**
     * @brief Metodo para comprobar si el juego ha terminado
     * @return TRUE si algun jugador esta en bancarrota, FALSE en otro caso
     */
    public boolean finalDelJuego(){
        int numJugadores = this.jugadores.size();
        boolean alguienBancarrota = false;
        
        for(int i = 0; i < numJugadores && !alguienBancarrota ; i++)
            alguienBancarrota = this.jugadores.get(i).enBancarrota();
            
        return alguienBancarrota;
    }
    
    
    /**
     * @brief Metodo para encontrar la casilla en la que se encuentra
     *        el jugador actual
     * @return objeto Casilla donde se encuentra el jugador actual
     */
    public Casilla getCasillaActual(){
        return this.tablero.getCasilla
             ( this.getJugadorActual().getNumCasillaActual() );
    }
    
    
    /**
     * @brief Metodo para encontrar el jugador que esta desarrollando su actividad
     * @return objeto Jugador activo en el juego actualmente
     */
    public Jugador getJugadorActual(){
        return this.jugadores.get(this.indiceJugadorActual);
    }
    
    
    /**
     * @brief Metodo para hipotecar una propiedad para obtener un beneficio
     * @param ip indice de propiedad a hipotecar
     * @return TRUE si se consigue hipotecar la propiedad, FALSE en otro caso
     */
    public boolean hipotecar(int ip){
        return this.jugadores.get(this.indiceJugadorActual).hipotecar(ip);
    }
    
    
    /**
    public String infoJugadorTexto(){
        
    }
    */
    
    
    /**
     * @brief Metodo para iniciar el mazo de sorpresas
     * @param tablero necesario para crear las sorpresas
     * @return void
     */
    private void inicializarMazoSorpresas(Tablero tablero){
        
        // Construimos la sorpresa que envia a la carcel
        this.mazo.alMazo( new Sorpresa(TipoSorpresa.IRCARCEL,  tablero) );
        
        // Construimos la sorpresa que envia a otra casilla
        this.mazo.alMazo( new Sorpresa(TipoSorpresa.IRCASILLA, tablero, 17, "Viaja hasta la casilla 17!") );

        // Construimos otros tipos de sorpresas (todos menos iracasilla, evitarcarcel e ircarcel)
        this.mazo.alMazo( new Sorpresa(TipoSorpresa.PAGARCOBRAR,  150, "A cobrar 150!") );
        this.mazo.alMazo( new Sorpresa(TipoSorpresa.PAGARCOBRAR, -150, "A pagar 150!" ) );
        
        this.mazo.alMazo( new Sorpresa(TipoSorpresa.PORCASAHOTEL, 50, "A pagar 50 por cada casa u hotel ):") );
        this.mazo.alMazo( new Sorpresa(TipoSorpresa.PORJUGADOR,  100, "A pagar 100 a cada jugador!") );
        
        // Construimos la sorpresa que permite evitar la carcel
        this.mazo.alMazo( new Sorpresa(TipoSorpresa.SALIRCARCEL, this.mazo) );

    }
    
    
    /**
     * @brief Metodo para iniciar el tablero
     * @param mazo de sorpresas necesario para crear las sorpresas del tablero
     * @return void
     */
    private void inicializarTablero(MazoSorpresas mazo){
        final int numCasillaCarcel = 9;
        final float precioImpuesto = 500;
        
        // Creamos el tablero
        this.tablero = new Tablero(numCasillaCarcel);    
        
        // Aniadimos casillas de CALLE 0, 1
        this.tablero.añadeCasilla( new Casilla( new TituloPropiedad ("Calle Nispero", 50, (float) 1.1, 50, 100, 60 ) ) );
        this.tablero.añadeCasilla( new Casilla( new TituloPropiedad ("Calle Chispas", 50, (float) 1.1, 50, 100, 60 ) ) );
        
        // Aniadimos casillas SORPRESA 2 
        this.tablero.añadeCasilla(new Casilla (this.mazo,"SORPRESA ALEATORIA") ); 
        
        // Aniadimos casilla del JUEZ 3
        this.tablero.añadeJuez();
        
        // Aniadimos casillas de CALLE 4, 5
        this.tablero.añadeCasilla( new Casilla( new TituloPropiedad ("Calle Dendi",   50, (float) 1.1, 50, 100, 60 ) ) );
        this.tablero.añadeCasilla( new Casilla( new TituloPropiedad ("Calle Ambi",    60, (float) 1.1, 60, 120, 70 ) ) );        
        
        // Aniadimos casilla SORPRESA 6
        this.tablero.añadeCasilla(new Casilla (this.mazo,"SORPRESA ALEATORIA") );
        
        // Aniadimos casilla IMPUESTO 7
        this.tablero.añadeCasilla(new Casilla ( precioImpuesto, "Impuesto Circulacion") );
    
        // Aniadimos casilla de CALLE 8
        this.tablero.añadeCasilla( new Casilla( new TituloPropiedad ("Calle Liada",   60, (float) 1.1, 60, 120, 70 ) ) );
        
        // La carcel esta en la 9
        
        // Aniadimos casilla de CALLE 10
        this.tablero.añadeCasilla( new Casilla( new TituloPropiedad ("Calle Alexa",   60, (float) 1.1, 60, 120, 70 ) ) );
        
        // Aniadimos casillas de CALLE 11, 12
        this.tablero.añadeCasilla( new Casilla( new TituloPropiedad ("Calle Tiempo",  70, (float) 1.1, 70, 140, 90 ) ) );
        this.tablero.añadeCasilla( new Casilla( new TituloPropiedad ("Calle Seya",    70, (float) 1.1, 70, 140, 90 ) ) ); 
        
        // Aniadimos casilla SORPRESA 13
        this.tablero.añadeCasilla(new Casilla (this.mazo,"SORPRESA ALEATORIA") );
        
        // Aniadimos casilla IMPUESTO 14
        this.tablero.añadeCasilla(new Casilla ( precioImpuesto * 2, "Impuesto Medioambiental") );
        
        // Aniadimos casillas de CALLE 15, 16
        this.tablero.añadeCasilla( new Casilla( new TituloPropiedad ("Calle Yeyiii",  70, (float) 1.1, 70, 140, 90 ) ) );
        this.tablero.añadeCasilla( new Casilla( new TituloPropiedad ("Calle Brasa",   90, (float) 1.1, 90, 160, 120) ) );
               
        // Aniadimos casilla de DESCANSO 17
        this.tablero.añadeCasilla( new Casilla("PARKING") );
        
        // Aniadimos casillas SORPRESA 18
        this.tablero.añadeCasilla(new Casilla (this.mazo,"SORPRESA ALEATORIA") );
        
        // Aniadimos casillas de CALLE 19
        this.tablero.añadeCasilla( new Casilla( new TituloPropiedad ("Calle Lastima", 90, (float) 1.1, 90, 160, 120) ) );
              
    }
    
    
    /**
     * @brief Metodo para cambiar adecuadamente el indice del jugador actual
     * @return void
     */
    private void pasarTurno(){
        this.indiceJugadorActual = (this.indiceJugadorActual + 1) % this.jugadores.size();
    }
    
    
    /**
     * @brief Metodo para ordenar los jugadores de la partida atendiendo a su saldo
     * @return Lista ordenada de jugadores en funcion de su saldo
     */
    public ArrayList<Jugador> ranking(){
        
        // Como la clase jugador implementa compareTo, lo utilizamos
        Collections.sort(this.jugadores);
        
        return this.jugadores;
    }
    
    
    /**
     * @brief Metodo para que un jugador salga de la carcel con su saldo
     * @return TRUE si el jugador sale de la carcel, FALSE en otro caso
     */
    public boolean salirCarcelPagando(){
        return this.jugadores.get(this.indiceJugadorActual).salirCarcelPagando();
    }
    
    
    /**
     * @brief Metodo para que un jugador salga de la carcel tirando los dados
     * @return TRUE si el jugador sale de la carcel FALSE en otro caso 
     */
    public boolean salirCarcelTirando(){
        return this.jugadores.get(this.indiceJugadorActual).salirCarcelTirando();
    }
    
    
    /**
    * @brief Hace el siguiente paso del jugador en funcion de su estado
    * @return la operacion
    */
    public OperacionesJuego siguientePaso(){
       Jugador jugadorActual =  jugadores.get(indiceJugadorActual); 
       OperacionesJuego operacion = gestorEstados.operacionesPermitidas(jugadorActual, estado);
       
       if(operacion == OperacionesJuego.PASAR_TURNO)
       {
           pasarTurno();
           siguientePasoCompletado(operacion);
       }
       else if(operacion  == OperacionesJuego.AVANZAR)
       {
           avanzaJugador();
           siguientePasoCompletado(operacion);
       }
       
       return operacion;
    }
    
    
    /**
     * @brief Actualizar el estado del juego
     *        Modifica adecuadamente el valor del atributo estado
     * @param operacion 
     * @return void
     */
    public void siguientePasoCompletado(OperacionesJuego operacion){
        this.estado = this.gestorEstados.siguienteEstado
            (this.jugadores.get(indiceJugadorActual), this.estado, operacion);
    }
    
    
    /**
     * @brief Metodo para que un jugador venda una de sus propiedades
     * @param ip indice de propiedad a vender
     * @return TRUE si se realiza la venta, FALSE en otro caso
     */
    public boolean vender(int ip){
        return this.jugadores.get(this.indiceJugadorActual).vender(ip);
    }
    
} // Fin clase CIVITASJUEGO