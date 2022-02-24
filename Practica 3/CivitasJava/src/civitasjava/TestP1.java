
package civitasjava;

import java.util.ArrayList;

public class TestP1 {
    
    /*
    public static void main(String[] args) {
         
        System.out.println("\nEjercicio 1.");
        
        // 1
        // Llama 100 veces al método quienEmpieza() de Dado considerando que hay
        // 4 jugadores, y calcula cuantas veces se obtiene cada uno de los 
        // valores posibles. Comprueba si se cumplen a nivel práctico las 
        // probabilidades de cada valor.
        
        // Vector con 4 componentes, donde por cada ocurrencia, aumentaremos
        // una unidad la posicion valor-1 (ya que el vector empieza en el 0)
        // Al final veremos el % de ocurrencia de cada valor.
        
        final int NumJugadores = 4;
            
        ArrayList<Integer> frecuencias = new ArrayList<>();
        for(int i = 0 ; i < NumJugadores ; i++)
            frecuencias.add(0);
            
        int ult_valor; //Variable para guardar cada tirada
         
        for(int i = 0; i < 100 ; i++)
        {
            ult_valor = Dado.getInstance().quienEmpieza(NumJugadores);
            frecuencias.set(ult_valor,frecuencias.get(ult_valor)+1);
        }
        
        System.out.println("Los porcentajes aleatorios son: \n");

        for(int i = 0 ; i < NumJugadores ; i++)
            System.out.println("Jugador num: " + (i+1) + ", porcentaje: " 
                       + (frecuencias.get(i)*1.0) + " %");

        
        System.out.println("\nEjercicio 2.");

        // 2 
        // Probamos el modo debug del dado y sus tiradas con y sin él.
        
        final int TiradasDado = 10;
        
        System.out.println("Sin debug los resultdos son: \n");
        
        for(int i = 0 ; i < TiradasDado/2 ; i++)
            System.out.println(Dado.getInstance().tirar());    
            
        Dado.getInstance().setDebug(true);
        System.out.println("Con debug los resultdos son: \n");

            
        for(int i = 0 ; i < TiradasDado/2 ; i++)
            System.out.println(Dado.getInstance().tirar());   
            
        Dado.getInstance().setDebug(false);
        
        System.out.println("\nEjercicio 3.");

        // 3 
        // Prueba de los método getUltimoResultado() y salgoDeLaCarcel();
        
        if(Dado.getInstance().salgoDeLaCarcel())
            System.out.println("Salimos de la carcel ya que la tirada fue: "
                                + Dado.getInstance().getUltimoResultado());
        else
            System.out.println("NO Salimos de la carcel ya que la tirada fue: "
                                + Dado.getInstance().getUltimoResultado());
        
        // 4
        // Mostrar un valor de cada enumerado
        
        System.out.println("\nEjercicio 4.");
        
        System.out.println(TipoCasilla.CALLE);
        System.out.println(TipoSorpresa.IRCARCEL);
        System.out.println(OperacionesJuego.AVANZAR);
        System.out.println(EstadosJuego.INICIO_TURNO);

        // 5
        // Objeto MazoSorpresas y añade 2 sorpresas, obtén la siguiente,
        // inhabilita y habilita la segunda
        
        System.out.println("\nEjercicio 5.");
        
        MazoSorpresas mazo = new MazoSorpresas();
        
        // Creamos una sorpresa que permite evitar la carcel
        Sorpresa sorpresa1 = new Sorpresa(TipoSorpresa.SALIRCARCEL, mazo);
        
        // Creamos una sorpresa que envia a otra casilla
        Sorpresa sorpresa2 = new Sorpresa(TipoSorpresa.PORCASAHOTEL, 80, 
                                         "Paga por cada edificio que poseas");

        mazo.alMazo(sorpresa1);
        mazo.alMazo(sorpresa2);
        
        mazo.inhabilitarCartaEspecial(sorpresa2);         
        mazo.habilitarCartaEspecial(sorpresa2);

        // 6
        // Prueba métodos de la clase Diario. Aprovechamos y vemos todos los
        // mensajes acumulados de la instancia diario.
        
        System.out.println("\nEjercicio 6.");
        
        while(Diario.getInstance().eventosPendientes())
            System.out.println(Diario.getInstance().leerEvento());
        

        // 7
        // Crea un tablero, añadele casillas y comprueba que es el mismo que 
        // esperabas. Intenta provocar situaciones erróneas.
        // (ej: poscarcel > tamaño) Y comprueba que la gestión es correcta.
        // Haz varias tiradas y comprueba que se calcula correctamente la pos
        
        System.out.println("\nEjercicio 7.");
        
        Tablero tablero = new Tablero(3);
        Casilla casilla2 = new Casilla("2");
        Casilla casilla3 = new Casilla("3");
        
        tablero.añadeCasilla(casilla2);
        tablero.añadeCasilla(casilla3); //Debe añadirse la casilla Carcel
        tablero.añadeJuez();
        
        for(int i = 0 ; i < 5 ; i++){ //5 ya que son las casillas
            System.out.println(tablero.getCasilla(i).getNombre()); 
        }
        
        System.out.println(); //para que haya un espacio
        
        for(int i = 0 ; i < 5 ; i++){   //5 ya que es la máxima posición
            System.out.println("Posición inicial: " + i +
                                " y la posición final es: " +
                            tablero.nuevaPosicion(i,Dado.getInstance().tirar())
                            + " (tirada: " +     
                            Dado.getInstance().getUltimoResultado() +
                            " y es un mapa de 5 casillas)"); 
        }
        
    }
    */
}
