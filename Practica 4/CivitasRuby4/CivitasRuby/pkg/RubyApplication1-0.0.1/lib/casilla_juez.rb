module Civitas
  
class CasillaJuez < Casilla
  
# @brief Método que hace de Constructor de la clase. Casilla juez
# @param carcel, El número de la casilla cácel
# @param nombre, el nombre de la casilla
# @return Un objeto de clase casilla
  def initialize(carcel, nombre)
    super(nombre)
    Casilla.carcel=carcel
    @@carcel = carcel
  end

# @brief Encarcela a el jugador dado(si existe)
# @param actual, el indice del jugador
# @param todos, el vector de jugador
# @return Es void
public
def recibe_jugador(actual,todos)
  if(jugador_correcto(actual,todos))
    super(actual,todos)
    todos[actual].encarcelar(@@carcel)
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

end
end