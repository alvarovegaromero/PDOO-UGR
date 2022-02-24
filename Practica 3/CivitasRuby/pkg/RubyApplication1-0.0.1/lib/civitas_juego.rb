#encoding:utf-8

require_relative 'diario'
require_relative 'sorpresa'
require_relative 'mazo_sorpresas'
require_relative 'tipo_sorpresa'
require_relative 'titulo_propiedad'
require_relative 'jugador'
require_relative 'gestor_estados'
require_relative 'dado'
require_relative 'casilla'

require 'io/console'

module Civitas
  class CivitasJuego
    
    attr_reader :indice_jugador_actual
    
  # CONSTRUCTOR  
  def initialize(nombres)
    @jugadores = Array.new
    for i in (0..nombres.length-1)  #&& (i < 4) Máximo 4 jugadores
      jugador = Jugador.new_nombre (nombres[i])
      @jugadores << jugador     
    end

    @gestor_estados = Gestor_estados.new
    @estado = @gestor_estados.estado_inicial
    @indice_jugador_actual = Dado.instance.quien_empieza(@jugadores.length)
    @mazo = MazoSorpresas.new

    inicializar_tablero(@mazo)      
    inicializar_mazo_sorpresas(@tablero) 
  end

  ####################################################################
  ## Resto de métodos
  ####################################################################  

  private
  def avanza_jugador()
    jugador_actual = @jugadores[@indice_jugador_actual]
    posicion_actual = jugador_actual.num_casilla_actual
    tirada = Dado.instance.tirar
    posicion_nueva = @tablero.nueva_posicion(posicion_actual,tirada)
    casilla = @tablero.get_casilla(posicion_nueva)

    contabilizar_pasos_por_salida(jugador_actual)
    jugador_actual.mover_a_casilla(posicion_nueva)
    casilla.recibe_jugador(@indice_jugador_actual, @jugadores)
    contabilizar_pasos_por_salida(jugador_actual)
  end

  public  
  def cancelar_hipoteca(ip)
    @jugadores[@indice_jugador_actual].cancelar_hipoteca(ip)
  end

  public
  def construir_casa(ip)
    @jugadores[@indice_jugador_actual].construir_casa(ip)
  end

  public
  def construir_hotel(ip)
    @jugadores[@indice_jugador_actual].construir_hotel(ip)
  end

  # @brief Método para comprar la casilla en la que el jugador actual ha caido
  # @return true si puede comprarla, false en otro caso
  public
  def comprar
    jugador_actual = @jugadores[indice_jugador_actual]
    num_casilla_actual = jugador_actual.num_casilla_actual
    casilla = @tablero.casillas[num_casilla_actual]
    titulo = casilla.titulo_propiedad
    res = jugador_actual.comprar(titulo)
  end

  # @brief El jugador pasado como parametro cobra por todas las veces que
  #        ha pasado por la salida en su turno actual
  # @param jugadorActual
  # @return void
  private
  def contabilizar_pasos_por_salida(jugador)
    cont = @tablero.get_por_salida
    while (cont > 0)
      jugador.pasa_por_salida
      cont = @tablero.get_por_salida
    end
  end

  # @brief Metodo para comprobar si el juego ha terminado
  # @return TRUE si algun jugador esta en bancarrota, FALSE en otro caso
  public
  def final_del_juego()
    se_acabo = false
      
      for j in @jugadores
        if (j.en_bancarrota)
          se_acabo = true
        end
      end
      
      return se_acabo
  end 

  # @brief Metodo para encontrar la casilla en la que se encuentra
  #       el jugador actual
  # @return objeto Casilla donde se encuentra el jugador actual
  public
  def get_casilla_actual()
    @tablero.get_casilla(@jugadores[@indice_jugador_actual].num_casilla_actual)  
  end

  # @brief Metodo para encontrar el jugador que esta desarrollando su actividad
  # @return objeto Jugador activo en el juego actualmente
  public
  def get_jugador_actual
    @jugadores[@indice_jugador_actual]
  end

  # @brief Metodo para hipotecar una propiedad para obtener un beneficio
  # @param ip indice de propiedad a hipotecar
  # @return TRUE si se consigue hipotecar la propiedad, FALSE en otro caso
  public
  def hipotecar(ip)
    @jugadores[@indice_jugador_actual].hipotecar(ip)  
  end

  # @brief Metodo para iniciar el mazo de sorpresas
  # @param tablero necesario para crear las sorpresas
  # @return void
  private
  def inicializar_mazo_sorpresas(tablero)
    @mazo.al_mazo(Sorpresa.new_tablero(TipoSorpresa::IRCARCEL,tablero));
    @mazo.al_mazo(Sorpresa.new_tablero_valor(TipoSorpresa::IRCASILLA,tablero, 8, "A la casilla numero 7!"))
    @mazo.al_mazo(Sorpresa.new_valor(TipoSorpresa::PAGARCOBRAR, 150, "A cobrar 150!") )
    @mazo.al_mazo(Sorpresa.new_valor(TipoSorpresa::PAGARCOBRAR, -150, "A pagar 150!") )
    @mazo.al_mazo(Sorpresa.new_valor(TipoSorpresa::PORCASAHOTEL, 50, "A pagar 50 por cada casa u hotel ):") )
    @mazo.al_mazo(Sorpresa.new_valor(TipoSorpresa::PORJUGADOR,  100, "A pagar 100 a cada jugador!") )
    @mazo.al_mazo(Sorpresa.new_mazo(TipoSorpresa::SALIRCARCEL,@mazo));
  end
  
  # @brief Metodo para iniciar el tablero
  # @param mazo de sorpresas necesario para crear las sorpresas del tablero
  # @return void
  private
  def inicializar_tablero(mazo)
    @tablero = Tablero.new(9)
    precio_impuesto = 500
    
    @tablero.añade_casilla(Casilla.new_titulo(TituloPropiedad.new("Calle Nispero",50,1.1,50,100 ,60)))
    @tablero.añade_casilla(Casilla.new_titulo(TituloPropiedad.new("Calle Chispas",50,1.1,50,100 ,60)))
    @tablero.añade_casilla(Casilla.new_mazo(mazo,"SORPRESA"))
    @tablero.añade_juez
    @tablero.añade_casilla(Casilla.new_titulo(TituloPropiedad.new("Calle Dendi",50,1.1,50,100 ,60)))
    @tablero.añade_casilla(Casilla.new_titulo(TituloPropiedad.new("Calle Ambi",60 ,1.1 ,60 ,120 ,70 )))
    @tablero.añade_casilla(Casilla.new_mazo(mazo,"SORPRESA"))
    @tablero.añade_casilla(Casilla.new_cantidad(precio_impuesto,"IMPUESTO CIRCULACIÓN"))
    @tablero.añade_casilla(Casilla.new_titulo(TituloPropiedad.new("Calle Liada",60 ,1.1 ,60 ,120 ,70)))
   
    @tablero.añade_casilla(Casilla.new_titulo(TituloPropiedad.new("Calle Alexa",60 ,1.1 ,60 ,120 ,70)))
    @tablero.añade_casilla(Casilla.new_titulo(TituloPropiedad.new("Calle Tiempo",70, 1.1 ,70,140 ,90)))
    @tablero.añade_casilla(Casilla.new_titulo(TituloPropiedad.new("Calle Seya",70, 1.1 ,70,140 ,90)))
    @tablero.añade_casilla(Casilla.new_mazo(mazo,"SORPRESA"))
    @tablero.añade_casilla(Casilla.new_cantidad(precio_impuesto*2,"IMPUESTO MEDIOAMBIENTAL"))
    @tablero.añade_casilla(Casilla.new_titulo(TituloPropiedad.new("Calle Yeyiii",70, 1.1 ,70,140 ,90)))
    @tablero.añade_casilla(Casilla.new_titulo(TituloPropiedad.new("Calle Brasa",90,1.1 ,90,160,120)))
    @tablero.añade_casilla(Casilla.new_nombre("PARKING"))
    @tablero.añade_casilla(Casilla.new_mazo(mazo,"SORPRESA"))
    @tablero.añade_casilla(Casilla.new_titulo(TituloPropiedad.new("Calle Lastima",90, 1.1 ,90,160,120)))
  end

  # @brief Metodo para cambiar adecuadamente el indice del jugador actual
  # @return void
  private
  def pasar_turno()
     if @indice_jugador_actual < @jugadores.length-1
        @indice_jugador_actual = @indice_jugador_actual + 1
     else
        @indice_jugador_actual=0
     end
  end

  # @brief Metodo para ordenar los jugadores de la partida atendiendo a su saldo
  # @return Lista ordenada de jugadores en funcion de su saldo
  public
  def ranking()
    @jugadores.sort! { |a,b| a.saldo <=> b.saldo }
  end

  # @brief Metodo para que un jugador salga de la carcel tirando los dados
  # @return TRUE si el jugador sale de la carcel FALSE en otro caso 
  public
  def salir_carcel_tirando()
    @jugadores[@indice_jugador_actual].salir_carcel_tirando
  end

  # @brief Metodo para que un jugador salga de la carcel con su saldo
  # @return TRUE si el jugador sale de la carcel, FALSE en otro caso
  public
  def salir_carcel_pagando()
    @jugadores[@indice_jugador_actual].salir_carcel_pagando  
  end

  # @brief Hace el siguiente paso del jugador en funcion de su estado
  # @return la operacion
  public
  def siguiente_paso()
    jugador_actual = @jugadores[@indice_jugador_actual];
    operacion = @gestor_estados.operaciones_permitidas(jugador_actual, @estado)

    if(operacion==Operaciones_juego::PASAR_TURNO)
      pasar_turno
      siguiente_paso_completado(operacion)

    elsif(operacion==Operaciones_juego::AVANZAR)
      avanza_jugador
      siguiente_paso_completado(operacion)
    end

    operacion
  end

  # @brief Actualizar el estado del juego
  #        Modifica adecuadamente el valor del atributo estado
  # @param operacion 
  # @return void
  public
  def siguiente_paso_completado(operacion)
   @estado = @gestor_estados.siguiente_estado(@jugadores[@indice_jugador_actual], @estado, operacion)
  end

  # @brief Metodo para que un jugador venda una de sus propiedades
  # @param ip indice de propiedad a vender
  # @return TRUE si se realiza la venta, FALSE en otro caso
  public
  def vender(ip)
    @jugadores[@indice_jugador_actual].vender(ip)  
  end 

  end #Fin clase
end #Fin módulo
