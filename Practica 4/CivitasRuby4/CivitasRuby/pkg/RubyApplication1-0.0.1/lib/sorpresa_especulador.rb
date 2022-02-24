require_relative "especulador"

module Civitas
  class Sorpresa_especulador < Sorpresa 
    def initialize(valor, texto)
      super(texto)

      @valor = valor
    end

  # @brief Convierte al jugador actual en jugador especulador
  # @param actual, el indice del jugador
  # @param todos, el array de jugadores
  # @return void

    def aplicar_a_jugador(actual, todos)
      if(jugador_correcto(actual,todos))
        informe(actual, todos)
        Diario.instance.ocurre_evento("Se procede a convertir en Especulador")

        jugador = todos.at(actual)
        
        especulador = Especulador.new(jugador, @valor)

        # Guardamos pos actual para que sigan el mismo orden tras la conversion
        indice = todos.index(jugador)

        todos.delete(jugador)

        todos.insert(indice, especulador)
     end
    end
  end
end