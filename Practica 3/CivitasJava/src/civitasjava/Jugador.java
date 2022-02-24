/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civitasjava;

import java.util.ArrayList;

/**
 *
 * @author Francisco Prados y Alvaro Vega
 */
public class Jugador implements Comparable<Jugador>{
    
    // Atributos 
    protected final static int     CasasMax       = 4;
    protected final static int     CasasPorHotel  = 4;
    protected              boolean encarcelado;
    protected final static int     HotelesMax     = 4;
    private                String  nombre;
    private                int     numCasillaActual;
    protected final static float  PasoPorSalida  = 1000;
    protected final static float  PrecioLibertad = 200;
    private                boolean puedeComprar;
    private                float  saldo;
    private   final static float  SaldoInicial   = 7500;
    
    private ArrayList<TituloPropiedad>  propiedades;
    private Sorpresa salvoconducto;
    
    
    // Metodos
    
    /**
     * @brief Metodo para deshipotecar una propiedad
     * @param ip, el número de la propiedad en el vector
     * @return true si cancela la hipoteca, false en otro caso
     */
    boolean cancelarHipoteca(int ip){
        boolean result = false;
        
        if(!encarcelado && existeLaPropiedad(ip))
        {
            
            TituloPropiedad propiedad = propiedades.get(ip);
            float cantidad = propiedad.getImporteCancelarHipoteca();
            boolean puedoGastar = puedoGastar(cantidad);
              
            if(puedoGastar)
            {
                result = propiedad.cancelarHipoteca(this);
                  
                if(result)
                {
                    Diario.getInstance().ocurreEvento("El jugador: "+nombre+
                    ", cancela la hipoteca de la propiedad: "+
                    propiedades.get(ip).getNombre()+"\n"); 
                }
            }
        }   
        
        return result;
    }
    
    
    /**
     * @brief Calcula el numero total de casas y hoteles que posee un jugador
     * @return Numero total de edificios que tiene el jugador
     */
    int cantidadCasasHoteles(){
        int numEdificios = 0;
            
        for(int i = 0; i < this.propiedades.size(); i++)
            numEdificios += this.propiedades.get(i).cantidadCasasHoteles();
        
        return numEdificios;
    }
    
    
    /**
     * @brief Metodo para comprobar si un objeto tiene un orden mayor o menor
     *        que el objeto que realiza la llamada
     * @param otro jugador con el que se compara
     * @post Este metodo es reciproco, a.compareTo(b) = - b.compareTo(a)
     * @return 0 si ambos ojetos tienen el mismo orden (no hay uno mayor que otro)
     *         un numero entero negativo (-1) si el objeto actual es menor que el pasado como parametro
     *         un numero entero positivo (+1) si el objeto actual es mayor que el pasado como parametro
     */
    @Override
    public int compareTo(Jugador otro){
        int orden = 0;
        
        if( this.getSaldo() > otro.getSaldo() )
            orden = 1;
        else if ( this.getSaldo() < otro.getSaldo() )
            orden = -1;
             
        return orden;
    }
  
    /**
     * @brief Metodo para comprar una propiedad.
     * @param titulo La propiedad a comprar
     * @return true si la compra, false en otro caso
     */
    boolean comprar(TituloPropiedad titulo){
        boolean result = false;
        
        if( !isEncarcelado() && puedeComprar)
        {
            float precio = titulo.getPrecioCompra();
            
            if(puedoGastar(precio))
            {
                result = titulo.comprar(this);
                
                if(result)
                {
                    this.propiedades.add(titulo);
                    Diario.getInstance().ocurreEvento("El jugador " + nombre    
                                                    +  " compra la propiedad " 
                                                    +  titulo.toString());
                }
                puedeComprar = false;
            }
        }
        return result;
    }
    
    /**
    * @brief Metodo para constuir una casa en una propiedad.
    * @param ip, el indice de la propiedad en el vector de propiedades
    * @return true si se construye la casa, false en otro caso
    */
    boolean construirCasa(int ip){
        boolean result = false;
        boolean puedoEdificarCasa = false;
        boolean existe = existeLaPropiedad(ip);
        
        if( !isEncarcelado() && existe)
        {
           TituloPropiedad propiedad = propiedades.get(ip);
           puedoEdificarCasa = puedoEdificarCasa(propiedad);
           float precio = propiedad.getPrecioEdificar();
         
           if(puedoEdificarCasa && puedoGastar(precio))
           {
               result = propiedad.construirCasa(this);
               
               if(result)
               {
                Diario.getInstance().ocurreEvento("El jugador " + nombre
                                     + " construye casa en la propiedad " + ip);   
               }
           }
        }
        return result;
    }
    
    /**
    * @brief Metodo para constuir un hotel en una propiedad.
    * @param ip, el indice de la propiedad en el vector de propiedades
    * @return true si se construye el hotel, false en otro caso
    */
    boolean construirHotel(int ip){
        boolean result = false;
        
        if(!isEncarcelado() && existeLaPropiedad(ip))
        {
            TituloPropiedad propiedad = propiedades.get(ip);
            boolean puedoEdificarHotel = puedoEdificarHotel(propiedad);
            puedoEdificarHotel = false;
            float precio = propiedad.getPrecioEdificar();
            
            if(puedoGastar(precio) && 
              (propiedad.getNumHoteles() < getHotelesMax()) &&
              (propiedad.getNumCasas() >= getCasasPorHotel()))
            {
                puedoEdificarHotel = true;
            }
            
            if(puedoEdificarHotel)
            {
                result = propiedad.construirHotel(this);
                
                int casasPorHotel = getCasasPorHotel();
                propiedad.derruirCasas(casasPorHotel, this);
                Diario.getInstance().ocurreEvento("El jugador: " +
                        nombre + " construye hotel en la propiedad " + ip);
            }
        }
        
        return result;
    }
    
    
    
    /**
     * @brief Metodo para comprobar si un jugador tiene que ir a la carcel
     * @post false si el jugador ya estaba encarcelado
     * @post si se usa la carta de librarse de la cacel esta se consume
     * @return TRUE si hay que encarcelar al jugador, FALSE en otro caso
     */
    protected boolean debeSerEncarcelado(){
        boolean debeSerEncarcelado = false;
        
        if( !this.isEncarcelado() ){
            
            if( this.tieneSalvoconducto() ){
                
                this.perderSalvoconducto();
                Diario.getInstance().ocurreEvento(
                    "El jugador " + this.getNombre() + 
                    " se ha librado de la carcel porque tenia salvoconducto");
            } else 
                debeSerEncarcelado = true;
        }
        
        return debeSerEncarcelado;
    }
    
    
    /**
     * @brief Metodo para comprobar si un jugador tiene dinero
     * @return TRUE si tiene dinero, FALSE si esta en numeros rojos
     */
    boolean enBancarrota(){
        return this.saldo < 0;
    }
    
    
    /**
     * @brief Si un jugador tiene que ser encarcelado, se mueve a la carcel
     * @param numCasillaCarcel
     * @pre el parametro numCasillaCarcel debera ser la casilla correcta 
     * @return TRUE si se ha encarcelado al jugador FALSE en otro caso 
     */ 
    boolean encarcelar(int numCasillaCarcel){
        
      // En cualquier caso se mueve al jugador a la casilla de la carcel,
      // dependiendo de si tiene salvoconducto o no se encarcelara o no
      this.moverACasilla(numCasillaCarcel); 
     
      if( debeSerEncarcelado() ){
          this.encarcelado = true;
          Diario.getInstance().ocurreEvento("El jugador " + this.getNombre() 
                                            + " ha sido encarcelado");
      }
      
      return this.encarcelado;
    }
    
    
    /**
     * @brief Metodo para comprobar si hay una propiedad @ip
     * @param ip numero de propiedad a evaluar
     * @return TRUE si hay una propiedad @ip, FALSE en otro caso
     */
    private boolean existeLaPropiedad(int ip){
        return this.propiedades.get(ip) != null && this.propiedades.size() > ip;
    }
    
    
    /**
     * @brief Consultor del atributo CasasMax
     * @return CasasMax
     */
    private int getCasasMax(){
        return this.CasasMax;
    }
    
    
    /**
     * @brief Consultor del atributo CasasPorHotel
     * @return CasasPorHotel
     */
    int getCasasPorHotel(){
        return this.CasasPorHotel;
    }
    
    
    /**
     * @brief Consultor del atributo HotelesMax
     * @return HotelesMax
     */
    private int getHotelesMax(){
        return this.HotelesMax;
    }
    
    
    /**
     * @brief Consultor del atributo nombre
     * @return nombre
     */
    public String getNombre(){ // protected->public para acceder desde vistaTxtual->actualizarVista()
        return this.nombre;
    }
    
    
    /**
     * @brief Consultor del atributo numCasillaActual
     * @return numCasillaActual
     */
    int getNumCasillaActual(){
        return this.numCasillaActual;
    }
    
    
    /**
     * @brief Consultor del atributo PrecioLibertad
     * @return PrecioLibertad
     */
    private float getPrecioLibertad(){
        return this.PrecioLibertad;
    }
    
    
    /**
     * @brief Consultor del atributo PasoPorSalida
     * @return PasoPorSalida
     */
    private float getPremioPasoSalida(){
        return this.PasoPorSalida;
    }
    
    
    /**
     * @brief Consutltor del atributo propiedades
     * @return Array de propiedades
     */
    public ArrayList<TituloPropiedad> getPropiedades(){
        return this.propiedades;
    }
    
    
    /**
     * @brief Consultor del atributo puedeComprar
     * @return puedeComprar
     */
    boolean getPuedeComprar(){
        return this.puedeComprar;
    }
    
    
    /**
     * @brief Consultor del atributo saldo
     * @return saldo
     */
    protected float getSaldo(){
        return this.saldo;
    }
    
    
     /**
    * @brief Metodo para hipotecar una propiedad.
    * @param ip, el indice de la propiedad en el vector de propiedades
    * @return true si se hipoteca la propiedad, false en otro caso
    */
    boolean hipotecar(int ip){
        boolean result = false;
                
        if(!encarcelado && existeLaPropiedad(ip))
        {
            TituloPropiedad propiedad = propiedades.get(ip);
            result = propiedad.hipotecar(this);
            
            if(result)
            {
                Diario.getInstance().ocurreEvento("El jugador "+nombre+ " hipoteca la propiedad "+ip);
            }
        }
        
        return result;
    }
    
    
    /**
     * @brief Consultor del atributo encarcelado
     * @return atributo encarcelado
     */
    public boolean isEncarcelado(){
        return this.encarcelado;
    }
    
    
    // Constructor
    Jugador(String nombre){
        this.encarcelado      = false;
        this.nombre           = nombre;
        this.numCasillaActual = 0;
        this.puedeComprar     = true;
        this.saldo            = SaldoInicial;
        
        this.propiedades      = new ArrayList<>();
        this.salvoconducto    = null;
    }
    
    
    // Constructor de copia
    protected Jugador(Jugador otro){
        this.encarcelado      = otro.isEncarcelado();
        this.nombre           = otro.getNombre();
        this.numCasillaActual = otro.getNumCasillaActual();
        this.puedeComprar     = otro.getPuedeComprar();
        this.saldo            = otro.getSaldo();
        
        this.propiedades      = otro.getPropiedades();
        this.salvoconducto    = otro.salvoconducto;
    }
    
    
    /**
     * @brief Incrementa el saldo @cantidad dinero
     * @param cantidad de dinero a sumar al jugador
     * @return TRUE en cualquier caso
     */
    boolean modificarSaldo(float cantidad){
        this.saldo += cantidad;
        Diario.getInstance().ocurreEvento("El nuevo saldo de " + this.getNombre()
                                              + " es " + this.getSaldo());
        return true;
    }
    
    
    /**
     * @brief Metodo para desplazar a un jugador a una determinada casilla
     * @param numCasilla casilla a la que se desplazara el jugador
     * @return TRUE si se realiza la operacion FALSE en otro caso
     */
    boolean moverACasilla(int numCasilla){
        boolean seMueve = false;
       
        if( !this.isEncarcelado() ){
            this.numCasillaActual = numCasilla;
            this.puedeComprar     = false;
            
            Diario.getInstance().ocurreEvento("Se mueve al jugador " +
                                               this.getNombre() + 
                                               " a la casilla " + numCasilla);
            seMueve = true;
        }
        
        return seMueve;            
    }
    
    
    /**
     * @brief Metodo para hacer que un jugador consiga un salvoconducto
     * @pre el parametro debe ser una sorpresa de tipo SALIRCARCEL
     * @param sorpresa
     * @post el jugador no obtendra salvoconducto si esta encarcelado
     * @return TRUE si el jugador obtiene el salvoconducto FALSE en otro caso
     */
    boolean obtenerSalvoconducto(Sorpresa sorpresa){
        boolean obtiene = false;
        
        if(!this.isEncarcelado()){
            this.salvoconducto = sorpresa;
            obtiene = true;
        }
        
        return obtiene;
    }
    
    
    /**
     * @brief Metodo para que un jugador pierda @cantidad dinero
     * @param cantidad de dinero que se descuenta
     * @see this.modificarSaldo(float cantidad)
     * @return TRUE si se modifica el saldo FALSE si no (siempre devuelve TRUE)
     */
    boolean paga(float cantidad){
        return this.modificarSaldo(-cantidad);
    }
    
    
    /**
     * @brief Metodo para restar @cantidad dinero a un jugador por un ALQUILER 
     * @param cantidad de dinero a restar
     * @return TRUE si se realiza la operacion FALSE en otro caso (siempre TRUE)
     */
    boolean pagaAlquiler(float cantidad){
        return !this.isEncarcelado() && this.paga(cantidad); 
    }
    
    
    /**
     * @brief Metodo para restar @cantidad dinero a un jugador por un IMPUESTO
     * @param cantidad de dinero a restar
     * @return TRUE si se realiza la operacion FALSE en otro caso (siempre TRUE)
     */
    boolean pagaImpuesto(float cantidad){
        return !this.isEncarcelado() && this.paga(cantidad);
    }
    
    
    /**
     * @brief Indica que el jugador ha pasado por la salida y el cobra el premio
     * @return TRUE en cualquier caso 
     */
    boolean pasaPorSalida(){
        this.modificarSaldo(this.getPremioPasoSalida());
        Diario.getInstance().ocurreEvento("El jugador " + this.getNombre() + 
                                          " ha pasado por la salida");
        return true;        
    }
    
    
    /**
     * @brief Metodo para indicar que un jugador usa un salvoconducto
     * @post El jugador pierde el salvoconducto tras usarlo
     */
    private void perderSalvoconducto(){
        this.salvoconducto.usada();
        this.salvoconducto = null;
    }
    
    
    /**
     * @brief Metodo para compobar si un jugador puede comprar una casilla
     * @post el resultado se almacenara en el atributo puedeComprar
     * @return TRUE si puede comprar, FALSE en otro caso
     */
    boolean puedeComprarCasilla(){
        if( this.isEncarcelado() )
            this.puedeComprar = false;
        else 
            this.puedeComprar = true;
        
        return this.puedeComprar;
    }
    
    
    /**
     * @brief Metodo para comprobar si un jugador tiene suficiente 
     *        dinero para librarse de la carcel
     * @return TRUE si tiene suficiente saldo, FALSE en otro caso
     */
    private boolean puedeSalirCarcelPagando(){
        // No se utiliza el metodo puedoGastar porque este tiene en cuenta
        // si estamos encarcelados, lo cual es bastante probable al utilizar
        // este metodo
        return this.saldo >= this.PrecioLibertad;
    }
    
    
    /**
     * @brief Metodo para comprobar si se cumplen los requisitios para construir casas
     * @param propiedad donde comprobar si se puede edificar
     * @return 
     */
    private boolean puedoEdificarCasa(TituloPropiedad propiedad){
        return !this.isEncarcelado() 
            && propiedad.getNumCasas() < this.getCasasMax();
    }
    
    
    /**
     * @brief Metodo para comprobar si se cumplen los requisitios para construir hoteles
     * @param propiedad donde comprobar si se puede edificar
     * @return 
     */
    private boolean puedoEdificarHotel(TituloPropiedad propiedad){
        return !this.isEncarcelado() 
            && propiedad.getNumCasas() == this.getCasasPorHotel()
            && propiedad.getNumHoteles() < this.getHotelesMax();
    }
    
    
    /**
     * @brief Metodo para comprobar si un jugador tiene al menos @precio dinero
     * @param precio a comparar
     * @post Aunque tenga suficiente dinero, si esta encarcelado devuelve FALSE 
     * @return TRUE si hay suficiente dinero FALSE en otro caso
     */
    private boolean puedoGastar(float precio){
        return !this.isEncarcelado() && this.saldo >= precio;
    }
    
    
    /**
     * @brief Metodo para que un jugador reciba @cantidad dinero
     * @param cantidad de dinero que se incrementa
     * @see this.modificarSaldo(float cantidad)
     * @return TRUE si se modifica el saldo FALSE si no (siempre devuelve TRUE)
     */
    boolean recibe(float cantidad){
        return !this.isEncarcelado() && this.modificarSaldo(cantidad);         
    }
    
    
    /**
     * @brief Metodo para que un jugador salga de la carcel con su saldo
     * @return TRUE si el jugador sale de la carcel, FALSE en otro caso
     */
    boolean salirCarcelPagando(){
        boolean seSale = false;
        
        if( this.isEncarcelado() && this.puedeSalirCarcelPagando() ){
            
            seSale = this.paga(this.getPrecioLibertad()); // Paga siempre devuelve true
            this.encarcelado = false;
            
            Diario.getInstance().ocurreEvento("El jugador " + this.getNombre() +
                                             " ha salido de la carcel pagando");
        } else {
            Diario.getInstance().ocurreEvento(this.getNombre() +
                                    " no ha podido salir de la carcel pagando\n"
                                  + "Probablemente no tenia suficinete dinero");  
        }
        
        return seSale;
    }
    
    
    /**
     * @brief Metodo para que un jugador salga de la carcel tirando los dados
     * @return TRUE si el jugador sale de la carcel FALSE en otro caso 
     */
    boolean salirCarcelTirando(){
        boolean seSale = Dado.getInstance().salgoDeLaCarcel();
            
        if(seSale){
            this.encarcelado = false;
            Diario.getInstance().ocurreEvento("El jugador " + this.getNombre() +
                                           " ha salido de la carcel tirando");  
        } else {
            Diario.getInstance().ocurreEvento(this.getNombre() +
                                    " no ha podido salir de la carcel tirando\n"
                      + "Ya que no ha salido el numero requerido en los dados");  
        }
        
        return seSale;    
    }
    
    
    /**
     * @brief Metodo para comprobar si un jugador tiene, al menos, una propiedad
     * @return TRUE si tiene alguna propiedad, FALSE en otro caso
     */
    boolean tieneAlgoQueGestionar(){
        return this.propiedades.size() > 0;
    }
    
    
    /**
     * @brief Metodo para consultar si un jugador tiene un salvoconducto
     * @return TRUE si tiene un salvoconducto, FALSE en otro caso
     */
    boolean tieneSalvoconducto(){
        return this.salvoconducto != null;
    }
    
    
    /**
     * @brief Metodo para convertir la informacion de un jugador a String
     * @return String con la informacion 
     */
    @Override
    public String toString(){
     
        String numPropiedades;
        if( this.getPropiedades() != null )
            numPropiedades =  "" + this.propiedades.size();
        else 
            numPropiedades = "0";
        
        
        String info = ( "Nombre: "          + this.getNombre()
              + "\nSaldo: "                 + this.getSaldo()
              + "\nNumero de propiedades "  + numPropiedades
              + "\nCasilla actual: "        + this.getNumCasillaActual() );
               
        
        return info;
       
    }
    
    
    /**
     * @brief Metodo para perder una propiedad
     * @param ip propiedad a perder
     * @return TRUE si se realiza la venta, FALSE en otro caso
     */
    boolean vender(int ip){
        boolean seVende = false;
        
        if( !this.isEncarcelado() ){
            if( this.existeLaPropiedad(ip) && !this.getPropiedades().get(ip).getHipotecado() ){
                seVende = this.propiedades.get(ip).vender(this);
                
                if(seVende){
                    Diario.getInstance().ocurreEvento("El jugador "        +
                            this.getNombre() + " ha vendido la propiedad " + 
                            this.propiedades.get(ip).getNombre()           );
                    
                    this.propiedades.remove(ip);
                }
            }   
        }
        
        return seVende;
    } 
    
} // FIN de la clase Jugador