module Civitas

# @brief Constructor de la clase. Enviar jugador a otra cas
# @param tablero, el tablero en el que juega
# @param valor, el valor de la sorpresa
# @param texto, el nombre de la sorpresa
class Sorpresa_ir_casilla < Sorpresa
  def initialize(tablero, valor, texto)
    super(texto)
    
    @tablero = tablero
    @valor   = valor
  end
  
  
# @brief Modifica la posición del jugador, actualizándose la casilla donde está
# @param actual, el indice del jugador
# @param todos, el vector de jugadores
# @return Es void
def aplicar_a_jugador(actual,todos)
  if( jugador_correcto(actual,todos) )
    informe(actual,todos)
    
    casilla_actual = todos[actual].num_casilla_actual
    
    # Cuantas posiciones nos faltan hasta llegar a la casilla valor
    tirada = @tablero.calcular_tirada(casilla_actual, @valor)
    
    # Calculamos la posicion donde nos encontramos al desplazarnos
    nueva_posicion = @tablero.nueva_posicion(casilla_actual, tirada)

    # Movemos al jugador a esa nueva posicion
    todos.at(actual).mover_a_casilla(nueva_posicion)
    
    # Indicamos a la casilla que reciba al jugador
    @tablero.get_casilla(nueva_posicion).recibe_jugador(actual, todos)
    
#    Parece innecesario usar calcularTirada y nuevaPosicion pq
#    sorpresa.valor guarda la casilla donde hay que ir, pero
#    hace falta para guardar si se pasara por la salida (:
  end
end

end

end
