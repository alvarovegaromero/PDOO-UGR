#encoding:utf-8
require "singleton"

module Civitas
class Dado
  
  attr_reader :random, :ultimo_resultado, :debug
  include Singleton
  
  @@salida_carcel  = 5 #Atributo de clase privado
  
  # Constructor por defecto
  def initialize
    @ultimo_resultado = 0    
    @debug           = false
  end
  
  
  # @brief Metodo que genera un numero aleatorio y lo almacena en @ultimoResultado
  # @return Un natural entre 1 y 6 si debug esta desactivado, 1 en otro caso
  def tirar
    caras_dado = 6
    if !@debug
      @ultimo_resultado = rand(caras_dado) + 1
    else 
      @ultimo_resultado = 1
    end
  end
  

  # @brief Tiramos el dado, si obtenemos el numero SalidaCarcel, sale de la carcel
  # @return true si sale de la carcel, false en caso contrario
  def salgo_de_la_carcel
    tirada = tirar
    
    if tirada == @@salida_carcel
      true
    else
      false
    end
  end
  
  
  # @brief Metodo para decidir que jugador empieza el juego
  # @param n numero de jugadores
  # @return entero entre 0 y n-1. Indice del jugador que empieza
  def quien_empieza(n)
    rand(n)
  end
  
  # @brief Modificador del atributo debug
  # @param d valor que tomara el atributo debug
  # @allows dejar constancia en el diario
  def set_debug(d)
    @debug = d
    Diario.instance.ocurre_evento("Debug se ha puesto a " + d.to_s)
  end
  
  
end # Fin class Dado
end # Fin module Civitas
