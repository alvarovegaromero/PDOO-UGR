package juegotexto;

import civitasjava.CivitasJuego;
import civitasjava.Diario;
import civitasjava.OperacionesJuego;
import civitasjava.Respuestas;
import civitasjava.SalidasCarcel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import civitasjava.Casilla;
import civitasjava.Jugador;

public class VistaTextual {
  
  CivitasJuego juegoModel; 
  int iGestion   =-1;
  int iPropiedad =-1;
  private static String separador = "=====================";
  
  private Scanner in;
  
  // CONSTRUCTOR
  public VistaTextual () {
    in = new Scanner (System.in);
  }
  
  
  /**
   * Método para mostrar por pantalla un string
   * 
   * @param estado string a mostrar
   */
  void mostrarEstado(String estado) {
    System.out.println (estado);
  }
               
  
  void pausa() {
    System.out.print ("Pulsa una tecla");
    in.nextLine();
  }
  

  /**
   * Método para que el usuario introduzca un numero por teclado
   * 
   * @param max numero mas alto que se puede introducir (el minimo es cero)
   * @param msg1 mensaje a mostrar para que el cliente sepa qué número se le está pidiendo
   * @param msg2 mensaje de error en caso de que el cliente introduzca un valor inesperado
   * 
   * @pre Se solicita un entero hasta que se introduzca uno correcto
   * @post El número introducido siempre cumplirá las condiciones exigidas
   * 
   * @return El número introducido por el cliente por el teclado
   */
  int lee_entero (int max, String msg1, String msg2) {
    Boolean ok;
    String cadena;
    int numero = -1;
    do {
      System.out.print (msg1);
      cadena = in.nextLine();
      try {  
        numero = Integer.parseInt(cadena);
        ok = true;
      } catch (NumberFormatException e) { // No se ha introducido un entero
        System.out.println (msg2);
        ok = false;  
      }
      if (ok && (numero < 0 || numero >= max)) {
        System.out.println (msg2);
        ok = false;
      }
    } while (!ok);

    return numero;
  }
  

  /**
   * Método para mostrar un menú interactivo al usuario
   * 
   * @param titulo a mostrar cuando se le pida al usuario una opcion del menu 
   * @param lista de opciones que tiene el usuario
   * 
   * @pre Se basa en el método lee_entero y por ende la opción siempre será 
   *      un valor dentro del rango esperado
   * @see lee_entero
   * 
   * @return número de opción que ha escogido el usuario
   */
  int menu (String titulo, ArrayList<String> lista) {
    String tab = "  ";
    int opcion;
    System.out.println (titulo);
    for (int i = 0; i < lista.size(); i++) {
      System.out.println (tab+i+"-"+lista.get(i));
    }

    opcion = lee_entero(lista.size(),
                          "\n"+tab+"Elige una opción: ",
                          tab+"Valor erróneo");
    return opcion;
  }

  
  /**
   * Método para preguntar al usuario de qué manera desea abandonar la cárcel
   * 
   * @return Opción de salida, PAGANDO o TIRANDO
   */
  SalidasCarcel salirCarcel() {
    int opcion = menu ("Elige la forma para intentar salir de la carcel",
      new ArrayList<> (Arrays.asList("Pagando","Tirando el dado")));
    return (SalidasCarcel.values()[opcion]);
  }

  
  /**
   * Método para preguntar al usuario si desea comprar la calle a la que ha llegado
   * 
   * @return SI si se quiere comprar NO en otro caso
   */
  Respuestas comprar() {
    String casillaActual = this.juegoModel.getCasillaActual().toString();
    
    int opcion = menu ("Has llegado a la casilla\n" + casillaActual + "\n¿Quieres comprarla?",
                        new ArrayList<> (Arrays.asList("SI","NO") ) 
                      );
    
    return (Respuestas.values()[opcion]);
  }

  
  /**
   * Método para preguntar al cliente que gestión desea realizar y sobre qué propiedad
   * 
   * @post Se actualizan los atributos de iGestion e iPropiedad
   */
  void gestionar () {
    int opcionGest = menu ("¿Qué número de gestión inmobiliara quieres realizar?",
                        new ArrayList<> (Arrays.asList("-> VENDER",
                                                       "-> HIPOTECAR",
                                                       "-> CANCELAR_HIPOTECA",
                                                       "-> CONSTRUIR_CASA", 
                                                       "-> CONSTRUIR_HOTEL",
                                                       "-> TERMINAR"
                                                       )
                                        ) 
            
                           );
    
    if (opcionGest != 5 ){
        int numProps   = this.juegoModel.getJugadorActual().getPropiedades().size();
        int opcionProp = this.lee_entero(numProps, 
                               "¿Sobre qué propiedad quieres hacer la gestión? ", 
                               "Valor Erróneo");
        this.iPropiedad = opcionProp;
    }
    
    this.iGestion   = opcionGest;
    
  }
  
 
  /**
   * Consultor del atributo privado iGestion
   * @return iGestion
   */
  public int getGestion(){
      return this.iGestion;
  }
  
  
  /**
   * Consultor del atributo privado iPropiedad
   * @return iPropiedad
   */
  public int getPropiedad(){
      return this.iPropiedad;
  }
    

  /**
   * Método para mostrar por pantalla la siguiente operación que va a realizar el juego
   * 
   * @param operacion siguiente que va a realizar el juego
   */
  void mostrarSiguienteOperacion(OperacionesJuego operacion) {
      this.mostrarEstado("La siguiente operacion que va a realizar el juego es "
                       + operacion.toString() );
  }


  /**
   * Método para mostrar por consola todos los eventos pendientes del diario
   */
  void mostrarEventos() {
      while( Diario.getInstance().eventosPendientes() )
          this.mostrarEstado( Diario.getInstance().leerEvento() );
  }
  
  
  /**
   * Método para inicializar el atributo juegoModel
   * 
   * @param civitas el modelo, para que lo conozca la vista y pueda consultarlo directamente
   * 
   * @pre Este metodo muestra el estado actial
   */
  public void setCivitasJuego(CivitasJuego civitas){ 
        this.juegoModel = civitas;
        this.actualizarVista(); 
    }
  
  
  /**
   * Metodo para mostrar información del jugador actual, sus propiedades y la casilla actual
   */
  void actualizarVista(){
      String propiedades = "";
      
      if( this.juegoModel.getJugadorActual().getPropiedades().isEmpty() )
          propiedades = "No tiene propiedades\n";
      
      else {
        for(int i = 0; i < this.juegoModel.getJugadorActual().getPropiedades().size(); i++){
            propiedades += this.juegoModel.getJugadorActual().getPropiedades().get(i).toString();        
        }
      }
      
      this.mostrarEstado( "\nEs el turno de " + 
                          this.juegoModel.getJugadorActual().toString()   +
                          "\nCuyas propiedades son\n" + propiedades + "\n"  +
                          "Actualmente se encuentra en la casilla\n" +
                          this.juegoModel.getCasillaActual().getNombre() + "\n" 
                        );
  }
  
}
