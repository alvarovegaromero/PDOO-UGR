module Civitas

class Sorpresa_por_casa_hotel < Sorpresa

  def initialize(valor, texto)
    super(texto)
    
    @valor = valor
  end
  
# @brief Aplica la sorpresa de modificar el saldo multiplicar @valor con la
#   cantidad de casas y hoteles que el jugador posee
# @param actual, el indice del jugador
# @param todos, el vector de jugadores
# @return Es void
def aplicar_a_jugador_por_casa_hotel(actual,todos)
  if(jugador_correcto(actual,todos))
    informe(actual,todos)
    todos[actual].modificar_saldo(@valor * todos[actual].cantidad_casas_hoteles())
  end
end

end
end
