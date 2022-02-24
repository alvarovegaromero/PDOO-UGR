/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civitasjava;

/**
 *
 * @author Francisco Prados y Alvaro Vega
 */
public class TituloPropiedad {
    
    private static final int PORDEFECTONUMHOTELES = 0;
    private static final int PORDEFECTONUMCASAS   = 0;
    
    // Atributos privados
    private static final float factorInteresesHipoteca = (float) 1.1;
    private float   alquilerBase;
    private float   factorRevalorizacion;
    private float   hipotecaBase;
    private boolean hipotecado;
    private String  nombre;
    private int     numCasas;
    private int     numHoteles;
    private float   precioCompra;
    private float   precioEdificar;
    private Jugador propietario;
    
    /**
     *  @brief Constructor de la clase. Es único
     *  @param nombre de la propiedad
     *  @param precio base del alquiler de la propiedad
     *  @param factor de revalorización de la propiedad
     *  @param el precio base de la hipoteca
     *  @param precio de compra de la propiedad
     *  @param precio de edificar
     *  @return un objeto de la clase TituloPropiedad  
     */
    TituloPropiedad(String name, float precioBaseAlquiler, 
                    float factorDeRevalorizacion, float precioBaseHipoteca,
                    float precioDeCompra, float precioPorEdificar){
    
        this.alquilerBase           = precioBaseAlquiler;
        this.factorRevalorizacion   = factorDeRevalorizacion;
        this.hipotecaBase           = precioBaseHipoteca;
        this.hipotecado             = false;
        this.nombre                 = name;
        this.numCasas               = PORDEFECTONUMCASAS;
        this.numHoteles             = PORDEFECTONUMHOTELES;
        this.precioCompra           = precioDeCompra;
        this.precioEdificar         = precioPorEdificar;
        this.propietario            = null;
    }
    
    
    // Ya lo entenderemos en la practica 4 pq hay subclases
    /**
     * @brief Modifica el propietario de una propiedad
     * @param jugador, el nuevo propietario
     * @return void
     */
    void actualizaPropietarioPorConversion(Jugador jugador){
        this.propietario = jugador;
        // No hace falta poner num casas y hoteles a por defecto segun la profe
    }
    
    /**
     * @brief Metodo para deshipotecar una propiedad
     * @param jugador, el propietario de la propiedad
     * @return true si cancela la hipoteca, false en otro caso
    */
    boolean cancelarHipoteca(Jugador jugador){
        boolean result = false;
        
        if(hipotecado && esEsteElPropietario(jugador))
        {
            jugador.paga(getImporteCancelarHipoteca());
            hipotecado = false;
            result = true;
        }
        
        return result;
    }
    
    
    /**
     * @brief Calcula la suma de hoteles y casas
     * @return suma previa
     */
    int cantidadCasasHoteles(){
        return this.numCasas + this.numHoteles;
    }
    
    /**
     * @brief Metodo para comprar una propiedad.
     * @param El jugador que la va a comprar
     * @return true si la compra, false en otro caso
     */
    boolean comprar(Jugador jugador){
        boolean result = false;
        if(!tienePropietario())
        {
            propietario = jugador;
            result = true;
            propietario.paga(precioCompra);
        }
        return result;
    }
    
   /**
    * @brief Metodo para constuir una casa en una propiedad.
    * @param jugador El jugador que debe ser el dueño
    * @return true si se construye la casa, false en otro caso
    */
    boolean construirCasa(Jugador jugador){
        boolean result = false;

        if(esEsteElPropietario(jugador))
        {
            jugador.paga(precioEdificar);
            numCasas++;
            result = true;
        }

        return result;
    }
    
    /**
    * @brief Metodo para constuir un hotel en una propiedad.
    * @param jugador El jugador que debe ser el dueño
    * @return true si se construye el hotel, false en otro caso
    */
    boolean construirHotel(Jugador jugador){
        boolean result = false;
                
        if(esEsteElPropietario(jugador))
        {
            jugador.paga(precioEdificar);
            numHoteles++;
            result = true;
        }
        
        return result;
    }
    
    
    /**
     * @brief Decrementa, si es posible, el contador de casas construidas en 
              n unidades.     
    *  @param n, el número de casas a eliminar
    *  @param jugador, el propietario de la casilla
    *  @return TRUE si se ha podido decrementar, FALSE en otro caso
    *  @pre El jugador debe ser el propietario de la casilla
    *  @pre Se debe de disponer de n o más casas (previamente)
    */
    boolean derruirCasas(int n, Jugador jugador){
        boolean seRealizaOperacion = false;
        
        if(esEsteElPropietario(jugador) && this.numCasas >= n){
            this.numCasas -= n;
            seRealizaOperacion = true;
        }
        
        return seRealizaOperacion;
    }
    
    
    /**
     * @brief Verifica si un jugador es el propìetario
     * @param jugador, el jugador a comprobar si es propietario
     * @return TRUE si es el propietario, FALSE en otro caso
     */
    private boolean esEsteElPropietario(Jugador jugador){
        return this.getPropietario() == jugador;
    }
    
    
    /**
     * @brief Consultor del atributo hipotecado
     * @return TRUE si el titulo esta hipotecado, FALSE en otro caso
     */
    public boolean getHipotecado(){
        return this.hipotecado;
    }
    
    
    /**
     * @brief Calcula el valor que se recibiria al hipotecar una prop.
     * @return El importe a recibir si hipotecas una propiedad
     */
    float getImporteCancelarHipoteca(){
        return this.hipotecaBase * this.factorInteresesHipoteca;
    }
    
    
    /**
     * @brief Devuelve el valor de hipoteca
     * @return Valor base de la hipoteca
     */
    private float getImporteHipoteca(){
        return this.hipotecaBase;
    }
    
    
    /**
     * @brief Consultor del atributo nombre
     * @return nombre del jugador
     */
    public String getNombre(){
        return this.nombre;
    }
    
    
    /**
     * @brief Consultor del atributo numCasas
     * @return numero de casas del titulo
     */
    int getNumCasas(){
        return this.numCasas;
    }
    
    
    /**
     * @brief Consultor del atributo numCasas
     * @return numero de casas del titulo
     */
    int getNumHoteles(){
        return this.numHoteles;
    }
    
    
    /**
     * @brief Devuelve el precio del alquiler de la propiedad
     * @return El valor a pagar si caes en una propiedad
     * @pre Que no este hipotecada la propiedad
     */
    private float getPrecioAlquiler(){
        float precioAlquiler = 0;
        
        if(!this.hipotecado && !this.propietarioEncarcelado())
            precioAlquiler = alquilerBase * (float) (1+(numCasas*0.5)+(numHoteles*2.5));          
        
        return precioAlquiler;
    }
    
    
    /**
     * @brief Consultor del atributo precioCompra
     * @return precio del titulo de propiedad
     */
    float getPrecioCompra(){
        return this.precioCompra;
    }

    
    /**
     * @brief Consultor del atributo precioEdificar
     * @return importe por edificar en el titulo de Propiedad
     */
    float getPrecioEdificar(){
        return this.precioEdificar;
    }
    
    
    /**
     * @brief Calcula el precio de venta
     * @return la suma del precio de compra con el precio de edificar las
     *         casas y hoteles que tenga, multiplicado éste último por 
     *         el factor de revalorización.private
     */
    private float getPrecioVenta(){
        return precioCompra + (precioEdificar * factorRevalorizacion);
    } 
    
    
    /**
     * @brief Consultor del atributo propietario
     * @return Jugador que posee el titulo de propiedad
     */
    Jugador getPropietario(){
        return this.propietario;
    }
    
    
    /**
    * @brief Metodo para hipotecar una propiedad.
    * @param jugador, el jugador que quiere hipoteca la propiedad
    * @return true si se hipoteca, false en otro caso
    */
    boolean hipotecar(Jugador jugador){
        boolean salida = false;
        
        if(!hipotecado && esEsteElPropietario(jugador))
        {
            propietario.recibe(getImporteHipoteca());
            hipotecado = true;
            salida = true;
        }
        
        return false;
    }
    
    
    /**
     * @brief Dice sin el propietario de una propiedad esta encarcelado
     * @return Un valor booleano true si lo está, false si no está en la cárcel
     */
    private boolean propietarioEncarcelado(){       
        return this.getPropietario().isEncarcelado();
    }
        
    
    /**
     * @brief Verifica si una propiedad tiene dueño
     * @return true si tiene propietario, false en otro caso
     */
    boolean tienePropietario(){
        return this.getPropietario() != null;
    }    
    
    
    /**
     *  @brief Expresa el estado completo de un objeto
     *  @return Un string con los valores de los atributos de instancia
     */
    @Override // Esto es para que no utilice el metodo por defecto sino este
    public String toString(){            
        String nombrePropietario = (this.tienePropietario()) ? 
                this.getPropietario().getNombre() : "Sin propietario";
        
        return ("\nNombre:                   "  + this.nombre               +
                "\nPrecio base de alquiler:  "  + this.alquilerBase         + 
                "\nFactor de revalorizacion: "  + this.factorRevalorizacion +
                "\nPrecio base de hipoteca:  "  + this.hipotecaBase         +
                "\nHipotecado =              "  + this.hipotecado           +
                "\nNumero de casas:          "  + this.numCasas             +
                "\nNumero de hoteles:        "  + this.numHoteles           +
                "\nPrecio de compra:         "  + this.precioCompra         +
                "\nPrecio por edificar:      "  + this.precioEdificar       +
                "\nPropietario:              "  + nombrePropietario
               );
    }
    
    
    /**
     * @brief Tramita el alquiler de una casilla cuando un jugador cae en ella
     * @param jugador que cae a la casilla
     * @return void
     * @pre La propiedad debe tener propietario
     */
    void tramitarAlquiler(Jugador jugador){
        if(tienePropietario() && !esEsteElPropietario(jugador)){
            float importe = this.getPrecioAlquiler();
            
            jugador.pagaAlquiler(importe);
            (this.getPropietario()).recibe(importe);
        }
    }    
    

    /**
     * @brief Vende una propiedad, reembolsandose el dinero. Se eliminan casas,
              hoteles y se desvincula al propietario de la propiedad
     *  @param jugador que quiere vender la propiedad
     *  @pre El jugador pasado como parámetro debe ser el propietario
     *  @pre La propiedad no puede estar hipotecada
     */
    boolean vender(Jugador jugador){
        boolean seRealizaOperacion = false;
        
        if(esEsteElPropietario(jugador) && !this.hipotecado){
            
            jugador.recibe(this.getPrecioVenta());
            
            this.propietario = null;
            this.numCasas    = 0;
            this.numHoteles  = 0;
            
            seRealizaOperacion = true;
        }
        
        return seRealizaOperacion;
    }
    
    
    /**
     * @brief Metodo para probar la clase
     * @param args 
     *
    public static void main(String[] args){
        // Creamos un TituloPropiedad
        TituloPropiedad Granada = new TituloPropiedad(
                                       "Granada", 50, (float) 0.8, 25, 400, 50);
        
        System.out.println(Granada.toString());
        
        System.out.println("El importe por cancelar la hipoteca es "
                            + Granada.getImporteCancelarHipoteca());
        
        
        // Aniadimos un propietario a Granada
        Jugador comprador = new Jugador("Antonio");
        
        Granada.actualizaPropietarioPorConversion(comprador);
        
        System.out.println("El nuevo propietario de Granada es "
                + Granada.getPropietario().getNombre());
    }
    */
    
} // Fin clase TITULOPROPIEDAD