module Civitas

class Sorpresa_pagar_cobrar < Sorpresa
  
  def initialize(valor, texto)
    super(texto)
    @valor = valor
  end
 
  # @param actual, el indice del jugador
  # @param todos, el vector de jugadores
  # @brief Aplica la sorpresa de modificar el saldo en funcion de @valor
  # @return void
  def aplicar_a_jugador(actual,todos)

    if(jugador_correcto(actual,todos))
      informe(actual,todos)
      todos[actual].modificar_saldo(@valor)
    end

  end

end
end
