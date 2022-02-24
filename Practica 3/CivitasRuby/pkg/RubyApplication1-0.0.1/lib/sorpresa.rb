#encoding:utf-8
  
require_relative 'diario.rb'
require_relative 'tipo_sorpresa.rb'
require_relative 'mazo_sorpresas.rb'
require_relative 'tablero.rb'
require_relative 'jugador.rb'
require_relative 'casilla.rb'

module Civitas
  
class Sorpresa

# @brief Constructor de la clase.
# @param tipo, el tipo de sorpresa
# @param texto, el nombre de la sorpresa
# @return Un objeto de clase sorpresa  
def initialize(texto, tipo)
  init()
  @tipo = tipo
  @texto = texto
end
  
# @brief Método que hace de Constructor de la clase. Resto de sorpresas
# @param tipo, el tipo de sorpresa
# @return Un objeto de clase sorpresa  
def self.new_valor(tipo, valor, texto)
  @valor = valor
  new(texto, tipo)
end

# @brief Método que hace de Constructor de la clase. Evitar la cárcel
# @param tipo, el tipo de sorpresa
# @param mazo, el mazo con en el que se juega
# @return Un objeto de clase sorpresa  
def self.new_mazo(tipo, mazo)
  @mazo = mazo
  new("MAZO", tipo)
end

# @brief Método que hace de Constructor de la clase. Enviar a Cárcel
# @param tipo, el tipo de sorpresa
# @param tablero, el tablero en el que juega
# @return Un objeto de clase sorpresa  
def self.new_tablero(tipo, tablero)
  @tablero = tablero
  new("A la cárcel!", tipo)
end

# @brief Método que hace de Constructor de la clase. Enviar jugador a otra cas
# @param tipo, el tipo de sorpresa
# @param tablero, el tablero en el que juega
# @param valor, el valor de la sorpresa
# @param texto, el nombre de la sorpresa
# @return Un objeto de clase sorpresa
def self.new_tablero_valor(tipo, tablero, valor, texto)
  @valor = valor
  @tablero = tablero
  new(texto, tipo)
end

####################################################################
## Resto de métodos
####################################################################

# @brief Aplica una sorpresa en funcion del autrbuto de instancia @tipo
# @param actual, el indice del jugador
# @param todos, el vector de jugadores
# @return Es void
public
def aplicar_a_jugador(actual, todos)
    case @tipo
      when TipoSorpresa::IRCARCEL
        aplicar_a_jugador_ir_carcel(actual,todos)
      when TipoSorpresa::IRCASILLA
        aplicar_a_jugador_ir_a_casilla(actual,todos)
      when TipoSorpresa::SALIRCARCEL
        aplicar_a_jugador_salir_carcel(actual,todos)
      when TipoSorpresa::PAGARCOBRAR
        aplicar_a_jugador_pagar_cobrar(actual,todos)
      when TipoSorpresa::PORCASAHOTEL
        aplicar_a_jugador_por_casa_hotel(actual,todos)
      when TipoSorpresa::PORJUGADOR
        aplicar_a_jugador_por_jugador(actual,todos)
    end
end

# @brief Modifica la posición del jugador, actualizándose la casilla donde está
# @param actual, el indice del jugador
# @param todos, el vector de jugadores
# @return Es void
private
def aplicar_a_jugador_ir_a_casilla(actual,todos)
  if(jugador_correcto(actual,todos))
    informe(actual,todos)
    casilla_actual = todos[actual].num_casilla_actual
    tirada = @tablero.calcular_tirada(casilla_actual,@tablero.carcel)
    casilla_final = casilla_actual+@valor
    
    if (casilla_final <= 0)
      tirada = tirada +@tablero.size
    end
    
    nueva_posicion = @tablero.nueva_posicion(casilla_actual, tirada)
    todos.at(actual).mover_a_casilla(nueva_posicion)
    @tablero.get_casilla(nueva_posicion).recibe_jugador(actual, todos)
  end
end

# @brief Aplica la sorpresa de encarcelar usando el método de jugador encarcelar
# @param actual, el indice del jugador
# @param todos, el vector de jugadores
# @return Es void
private
def aplicar_a_jugador_ir_carcel(actual, todos)
    if(jugador_correcto(actual,todos))
      informe(actual,todos)
      todos[actual].encarcelar(@tablero.num_casilla_carcel)
    end
end

# @brief Aplica la sorpresa de modificar el saldoe enfuncion de @valor
# @param actual, el indice del jugador
# @param todos, el vector de jugadores
# @return Es void
private
def aplicar_a_jugador_pagar_cobrar(actual,todos)
  if(jugador_correcto(actual,todos))
    informe(actual,todos)
    todos[actual].modificar_saldo(@valor)
  end
end

# @brief Aplica la sorpresa de modificar el saldo multiplicar @valor con la
#   cantidad de casas y hoteles que el jugador posee
# @param actual, el indice del jugador
# @param todos, el vector de jugadores
# @return Es void
private
def aplicar_a_jugador_por_casa_hotel(actual,todos)
  if(jugador_correcto(actual,todos))
    informe(actual,todos)
    todos[actual].modificar_saldo(@valor * todos[actual].cantidad_casas_hoteles())
  end
end

# @brief Llama al método pagar y cobrar y todos los jugadores le dan @valor
#   al jugador actual, y el jugador actual recibe @valor * num_jugadores 
# @param actual, el indice del jugador
# @param todos, el vector de jugadores
# @return Es void
private
def aplicar_a_jugador_por_jugador(actual,todos)
  if(jugador_correcto(actual,todos))
    informe(actual,todos)
    
    a_cobrar = Sorpresa.new_valor(TipoSorpresa::PAGARCOBRAR,(todos.length-1)* @valor, "cobrar")
    a_pagar = Sorpresa.new(TipoSorpresa::PAGARCOBRAR,-1*@valor , "pagar")
    
    for i in (0..todos.length)
      if(i != actual) #El jugador "actual" no paga
        a_pagar.aplicar_a_jugador(i,todos)
      end
    end
   
    a_cobrar.aplicar_a_jugador(actual,todos) #Llama a modifica saldo 
    
  end
end

# @brief Si nadie tiene la sorpresa para evitar la carcel, la obtine el
#   jugador actual
# @param actual, el indice del jugador
# @param todos, el vector de jugadores
# @return Es void  
private
def aplicar_a_jugador_salir_carcel(actual,todos)
  if(jugador_correcto(actual,todos))
    informe(actual,todos)
  end
end

# @brief Informa de una sorpresa al jugador todos.[actual]
# @param actual, el indice del jugador
# @param todos, el vector de jugadores
# @return Es void
# @pre Debe existir el jugador
private
def informe(actual,todos)
  if(jugador_correcto(actual,todos))
    Diario.instance.ocurre_evento("Se ha aplicado una sorpresa a " + todos[actual].nombre)
  end
end

# @brief Inicia los atributos de instancia de valor, tablero y mazo
private
def init()
  @valor = -1
  @tablero = nil
  @mazo = nil
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

# @brief Si la sorpresa es la que evita la carcél, inhabilita la carta especial 
#   en el mazo de sorpresa
# @return Es void
public
def salir_del_mazo()
  if (@tipo == TipoSorpresa::SALIRCARCEL)
    @mazo.inhabilitar_carta_especial(this)
  end
end

# @brief Devuelve el nombre de la sorpresa(tipo)
# @return Un string con el nombre
public
def to_s()
  "TipoSorpresa: " + @tipo.to_s + "\nValor: " + @valor.to_s + "\nTexto: "+@texto
end

# @brief Si la sorpresa es la que evita la carcél, habilita la carta especial 
#   en el mazo de sorpresa
# @return Es void
public
def usada()
  if (@tipo == TipoSorpresa::SALIRCARCEL)
    @mazo.habilitar_carta_especial(this)
  end
end
    
end # Fin class Sorpresa
end # Fin module Civitas
