#encoding:utf-8

module Civitas
  
class Sorpresa

  attr_reader :texto
  
# @brief Constructor de la clase. 
def initialize(texto)
  @texto = texto
end
  

####################################################################
## Resto de métodos
####################################################################

# @brief Aplica una sorpresa
# @param actual, el indice del jugador
# @param todos, el vector de jugadores
# @return Es void
def aplicar_a_jugador(actual, todos) end


# @brief Informa de una sorpresa al jugador todos.[actual]
# @param actual, el indice del jugador
# @param todos, el vector de jugadores
# @return Es void
# @pre Debe existir el jugador
def informe(actual,todos)
  if(jugador_correcto(actual,todos))
    Diario.instance.ocurre_evento("Se ha aplicado al jugador " + 
                                  todos[actual].nombre +
                                  " la sorpresa\n" +
                                  self.to_s
                                 )
  end
end

# @brief Comprueba si un indice existe en un vector de jugadores
# @param actual, el indice a comprobar
# @param todos, el vector
# @return True si es un indice valido para acceder a los elementos de "todos"
def jugador_correcto(actual, todos)
  resultado = false
  
  if(todos.length > actual && actual >= 0) #primera pos del vector
    resultado = true
  end
end


# @brief Devuelve el nombre de la sorpresa(tipo)
# @return Un string con el nombre
def to_s()
  "Texto: " + @texto
end

def usada() end
def salir_del_mazo() end
    
end # Fin class Sorpresa
end # Fin module Civitas
