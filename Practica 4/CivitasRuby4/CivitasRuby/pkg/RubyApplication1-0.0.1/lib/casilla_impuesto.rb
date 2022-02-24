module Civitas
  
class CasillaImpuesto < Casilla 
# @brief Constructor de la clase. Casilla impuesto
# @param cantidad, La cantidad a pagar
# @param nombre, el nombre de la casilla
  def initialize(cantidad, nombre)
    super(nombre)
    @importe = cantidad
  end

# @brief El jugador dado(si existe), paga un impesto
# @param actual, el indice del jugador
# @param todos, el vector de jugador
# @return Es void
def recibe_jugador(actual,todos)
  if(jugador_correcto(actual,todos))
    super(actual,todos)
    todos[actual].paga_impuesto(@importe)
  end
end

# @brief Comprueba si un indice existe en un vector de jugadores
# @param actual, el indice a comprobar
# @param todos, el vector
# @return True si es un indice valido para acceder a los elementos de "todos"
def jugador_correcto(actual, todos)
  super(actual,todos)
end
  
end #fin clase
end #fin modulo
