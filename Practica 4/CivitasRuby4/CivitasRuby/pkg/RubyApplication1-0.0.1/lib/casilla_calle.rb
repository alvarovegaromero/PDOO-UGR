module Civitas
  
class CasillaCalle < Casilla
  
attr_reader :titulo_propiedad  
  
# @brief Método que hace de Constructor de la clase. Casilla calle
# @param titulo, Un objeto de titulo_propiedad
# @return Un objeto de clase casilla
  def initialize(titulo)
    super(titulo.nombre)
    @titulo_propiedad = titulo
  end

# @brief El jugador dado(si existe), permite que compre la casilla o
#        si tiene dueño y no está hipotecada, paga el alquiler
# @param iactual, el indice del jugador
# @param todos, el vector de jugadores
# @return Es void
public
def recibe_jugador(actual,todos)
  if(jugador_correcto(actual,todos))
    super(actual,todos)
    jugador = todos[actual]
    
    if(!@titulo_propiedad.tiene_propietario)
      jugador.puede_comprar_casilla
    else
      @titulo_propiedad.tramitar_alquiler(jugador)
    end
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
