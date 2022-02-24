module Civitas

class CasillaSorpresa < Casilla
    
# @brief Método que hace de Constructor de la clase. Casilla sorpresa
# @param mazo, Un mazo de sorpresas
# @param nombre, el nombre de la casilla
# @return Un objeto de clase casilla
  def initialize(mazo, nombre)
    super(nombre)
    @mazo = mazo
    @sorpresa = nil

  end

# @brief Aplica una sorpresa a un jugador que cae en una casilla sorpresa
# @param actual, el indice del jugador
# @param todos, el vector de jugador
# @return void
public
def recibe_jugador(actual, todos)
  if(jugador_correcto(actual,todos))
    sorpresa = @mazo.siguiente
    super(actual,todos)
    sorpresa.aplicar_a_jugador(actual, todos)
  end
end

# @brief Comprueba si un indice existe en un vector de jugadores
# @param actual, el indice a comprobar
# @param todos, el vector
# @return True si es un indice valido para acceder a los elementos de "todos"
public
def jugador_correcto(actual, todos)
  super(actual,todos)
end

end #fin clase
end #fin modulo
