module Civitas
 
# @brief Constructor de la clase. Enviar a Cárcel
# @param tablero, el tablero en el que juega 
class Sorpresa_ir_carcel < Sorpresa
  def initialize(tablero)
    super("Envia a la carcel")
    @tablero = tablero
  end
 

# @brief Aplica la sorpresa de encarcelar usando el método de jugador encarcelar
# @param actual, el indice del jugador
# @param todos, el vector de jugadores
# @return Es void
def aplicar_a_jugador(actual, todos)
    if(jugador_correcto(actual,todos))
      informe(actual,todos)
      todos[actual].encarcelar(@tablero.num_casilla_carcel)
    end
end

end

end
