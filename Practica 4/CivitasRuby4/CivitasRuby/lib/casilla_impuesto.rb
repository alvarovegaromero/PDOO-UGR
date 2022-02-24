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
  
end #fin clase
end #fin modulo
