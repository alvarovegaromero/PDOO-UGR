#encoding:utf-8

module Civitas
  
class Casilla

attr_reader :nombre
attr_accessor :carcel

# @brief Constructor de la clase. Casilla descanso
# @param nombre, el nombre de la casilla
# @return Un objeto de clase casilla
def initialize(nombre)
  @nombre  = nombre
end

####################################################################
## Resto de métodos
####################################################################

# @brief Informa de que el jugador todos.[actual] ha caido en una casilla
# @param actual, el indice del jugador
# @param todos, el vector de jugadores
# @return Es void
# @pre Debe existir el jugador
private
def informe(actual,todos)
  if(jugador_correcto(actual,todos))
    Diario.instance.ocurre_evento(todos[actual].nombre + " ha caido en " + @nombre)
  end
end

# @brief Comprueba si un indice existe en un vector de jugadores
# @param actual, el indice a comprobar
# @param todos, el vector
# @return True si es un indice valido para acceder a los elementos de "todos"
public
def jugador_correcto(actual, todos)
  resultado = false
  
  if(todos.length > actual && actual >= 0) #primera pos del vector
    resultado = true
  end
end

# @brief La propiedad recibe a un jugador que ha caido sobre ella
# @param actual, el indice del jugador
# @param todos, el vector de jugadores
# @return Es void
public
def recibe_jugador(iactual, todos)
  informe(iactual,todos)
end

# @brief Devuelve el estado del objeto en forma de un string
# @return Un string con la información
public
def to_s()
    cad = " \nCasilla: Nombre: " + @nombre
    return cad
end

def self.carcel=pos
  @@carcel = pos
end


end # Fin class Casilla
end # Fin module Civitas