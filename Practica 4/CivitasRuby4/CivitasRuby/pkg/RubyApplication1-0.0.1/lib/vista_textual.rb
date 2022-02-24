#encoding:utf-8

require 'io/console'

module Civitas

  class Vista_textual
   attr_accessor :juegoModel
   attr_reader :iGestion, :iPropiedad
=begin
  /**
   * Método para mostrar por pantalla un string
   * 
   * @param estado string a mostrar
   */
=end
    def mostrar_estado(estado)
      puts estado
    end

    
    def pausa
      print "Pulsa una tecla"
      STDIN.getch
      print "\n"
    end

=begin
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
=end
    def lee_entero(max,msg1,msg2)
      ok = false
      begin
        print msg1
        cadena = gets.chomp
        begin
          if (cadena =~ /\A\d+\Z/)
            numero = cadena.to_i
            ok = true
          else
            raise IOError
          end
        rescue IOError
          puts msg2
        end
        if (ok)
          if (numero >= max)
            ok = false
          end
        end
      end while (!ok)

      return numero
    end

=begin
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
=end
    def menu(titulo,lista)
      tab = "  "
      puts titulo
      index = 0
      lista.each { |l|
        puts tab+index.to_s+"-"+l
        index += 1
      }

      opcion = lee_entero(lista.length,
                          "\n"+tab+"Elige una opcion: ",
                          tab+"Valor erróneo")
      return opcion
    end

=begin
  /**
   * Método para preguntar al usuario de qué manera desea abandonar la cárcel
   * 
   * @return Opción de salida, PAGANDO o TIRANDO
   */
=end
    def salir_carcel
      opcion = self.menu("Elige la forma para intentar salir de la carcel",
                          ["Pagando", "Tirando el dado"] )
                 
      lista_salidas_carcel = [Salidas_carcel::PAGANDO, Salidas_carcel::TIRANDO]
      
      lista_salidas_carcel[opcion] 
    end
    
=begin   
  /**
   * Método para preguntar al usuario si desea comprar la calle a la que ha llegado
   * 
   * @return SI si se quiere comprar NO en otro caso
   */
=end
    def comprar
      casilla_actual = @juegoModel.get_casilla_actual.to_s
      opcion = self.menu("Has llegado a la casilla\n #{casilla_actual}
                        \nQuieres comprarla?",
                        ["SI","NO"] ) 
      
      lista_Respuestas = [Respuestas::SI, Respuestas::NO]
      
      lista_Respuestas[opcion] 
    end

=begin  
  /**
   * Método para preguntar al cliente que gestión desea realizar y sobre qué propiedad
   * 
   * @post Se actualizan los atributos de iGestion e iPropiedad
   */
=end
    def gestionar
      opcion_gest = self.menu("Que numero de gestión inmobiliara quieres realizar?",
                                ["-> VENDER", 
                                 "-> HIPOTECAR",
                                 "-> CANCELAR_HIPOTECA", 
                                 "-> CONSTRUIR_CASA", 
                                 "-> CONSTRUIR_HOTEL",  
                                 "-> TERMINAR"
                                ]
                            )
                                         
      if ( opcion_gest != 5 )
        num_props   = @juegoModel.get_jugador_actual.propiedades.length
        opcion_prop = self.lee_entero(num_props, 
                               "Dime sobre que propiedad quieres hacer la gestion", 
                               "Valor Erroneo")

        @iPropiedad = opcion_prop
      end
      
      @iGestion   = opcion_gest
    end

=begin
  /**
   * Método para mostrar por pantalla la siguiente operación que va a realizar el juego
   * 
   * @param operacion siguiente que va a realizar el juego
   */
=end
    def mostrar_siguiente_operacion(operacion)
      self.mostrar_estado("La siguiente operacion que va a realizar 
                           el juego es " + operacion.to_s)
    end
=begin
  /**
   * Método para mostrar por consola todos los eventos pendientes del diario
   */
=end
    def mostrar_eventos
      while( Diario.instance.eventos_pendientes )
          self.mostrar_estado( Diario.instance.leer_evento )
      end
    end

=begin   
  /**
   * Método para inicializar el atributo juegoModel
   * 
   * @param civitas el modelo, para que lo conozca la vista y pueda consultarlo directamente
   * 
   * @pre Este metodo muestra el estado actial
   */
=end
    def set_civitas_juego(civitas)
        @juegoModel=civitas
        self.actualizar_vista
    end

=begin
  /**
   * Metodo para mostrar información del jugador actual, sus propiedades y la casilla actual
   */
=end
    def actualizar_vista
      propiedades = ""
      
      if (@juegoModel.get_jugador_actual.propiedades.empty?)
        propiedades = "No tiene propiedades"
      
      else
        for i in 0...@juegoModel.get_jugador_actual.propiedades.length
          propiedades += propiedades + @juegoModel.get_jugador_actual.propiedades[i].to_s
        end
      end
   
      
      self.mostrar_estado( "\nEs el turno de " +
                          @juegoModel.get_jugador_actual.to_s +
                           "\nCuyas propiedades son:\n"  +  propiedades + "\n" +
                          "Actualmente se encuentra en la casilla: " +
                           @juegoModel.get_casilla_actual.nombre + "\n" +
                           "Tiene salvoconducto: "+@juegoModel.get_jugador_actual.tiene_salvoconducto.to_s
                         )
    end

    
  end
end
