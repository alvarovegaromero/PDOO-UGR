#encoding:utf-8
 
require_relative 'jugador.rb'
require_relative 'tipo_casilla.rb'
require_relative 'titulo_propiedad.rb'
require_relative 'tipo_casilla.rb'
require_relative 'sorpresa.rb'  
require_relative 'mazo_sorpresas.rb'

module Civitas
  
class Casilla

@@carcel = 10 #SIEMPRE QUE APAREZCA, PONER EN POS 10

attr_reader :nombre , :titulo_propiedad

# @brief Constructor de la clase.
# @param tipo, el tipo de la casilla
# @param texto, el nombre de la casilla
# @return Un objeto de la clase
def initialize(tipo, nombre, cantidad, mazo, titulo)
  @nombre = nombre
  @importe = cantidad
  @titulo_propiedad = titulo
  @mazo = mazo
  @sorpresa = nil
  @tipo = tipo
end


# @brief Método que hace de Constructor de la clase. Casilla descanso
# @param nombre, el nombre de la casilla
# @return Un objeto de clase casilla
def self.new_nombre(nombre)
  tipo = TipoCasilla::DESCANSO
  new(tipo, nombre, 0, nil, nil)
end

# @brief Método que hace de Constructor de la clase. Casilla calle
# @param titulo, Un objeto de titulo_propiedad
# @return Un objeto de clase casilla
def self.new_titulo(titulo)
    tipo = TipoCasilla::CALLE
    new(tipo,titulo.nombre, 0, nil, titulo)
  end

# @brief Método que hace de Constructor de la clase. Casilla impuesto
# @param cantidad, La cantidad a pagar
# @param nombre, el nombre de la casilla
# @return Un objeto de clase casilla
def self.new_cantidad(cantidad, nombre)
    tipo = TipoCasilla::IMPUESTO
    new(tipo, nombre, cantidad, nil, nil)
end

# @brief Método que hace de Constructor de la clase. Casilla juez
# @param carcel, El número de la casilla cácel
# @param nombre, el nombre de la casilla
# @return Un objeto de clase casilla
def self.new_carcel(carcel, nombre)
    tipo = TipoCasilla::JUEZ
    @@carcel = carcel
    new(tipo, nombre,0, nil, nil)
end

# @brief Método que hace de Constructor de la clase. Casilla sorpresa
# @param mazo, Un mazo de sorpresas
# @param nombre, el nombre de la casilla
# @return Un objeto de clase casilla
def self.new_mazo(mazo, nombre)
  tipo = TipoCasilla::SORPRESA
  new(tipo, nombre,0,nil,mazo)
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

# @brief Inicia los atributos de instancia del objeto. 
private
def init()
  @nombre = ""
  @importe = 0
  @titulo_propiedad = nil
  @mazo = nil
  @sorpresa = nil
  @tipo = nil
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
  case @tipo
    when TipoCasilla::CALLE
      recibe_jugador_calle(iactual,todos)
    when TipoCasilla::IMPUESTO
      recibe_jugador_impuesto(iactual,todos)
    when TipoCasilla::JUEZ
      recibe_jugador_juez(iactual,todos)
    when TipoCasilla::SORPRESA
      recibe_jugador_sorpresa(iactual,todos)
    else
      informe(iactual,todos)
   end
end


# @brief El jugador dado(si existe), permite que compre la casilla o
#        si tiene dueño y no está hipotecada, paga el alquiler
# @param iactual, el indice del jugador
# @param todos, el vector de jugadores
# @return Es void
private
def recibe_jugador_calle(actual,todos)
  if(jugador_correcto(actual,todos))
    informe(actual,todos)
    jugador = todos[actual]
    
    if(!@titulo_propiedad.tiene_propietario)
      jugador.puede_comprar_casilla
    else
      @titulo_propiedad.tramitar_alquiler(jugador)
    end
  end
end

# @brief El jugador dado(si existe), paga un impesto
# @param actual, el indice del jugador
# @param todos, el vector de jugador
# @return Es void
private
def recibe_jugador_impuesto(actual,todos)
  if(jugador_correcto(actual,todos))
    informe(actual,todos)
    todos[actual].paga_impuesto(@importe)
  end
end

# @brief Encarcela a el jugador dado(si existe)
# @param actual, el indice del jugador
# @param todos, el vector de jugador
# @return Es void
private
def recibe_jugador_juez(actual,todos)
  if(jugador_correcto(actual,todos))
    informe(actual,todos)
    todos[actual].encarcelar(@@carcel)
  end
end

# @brief Aplica una sorpresa a un jugador que cae en una casilla sorpresa
# @param actual, el indice del jugador
# @param todos, el vector de jugador
# @return void
private
def recibe_jugador_sorpresa(actual,todos)
  if(jugador_correcto(actual,todos))
    sorpresa = @mazo.siguiente
    informe(actual,todos)
    sorpresa.aplicar_a_jugador(actual, todos)
  end
end

# @brief Devuelve el estado del objeto en forma de un string
# @return Un string con la información
public
def to_s()
    cad = "Casilla tipo: " + @tipo.to_s + "\nNombre: " + @nombre
    if(@importe != 0)
        cad += "\nImporte: " + @importe.to_s
    end
    if(@titulo_propiedad != nil)
      cad += "\nTituloPropiedad" + @titulo_propiedad.to_s
    end
    if(@nombre == "juez")
      cad += "\nCasilla carcel: " + @@carcel.to_s
    end
    return cad
end

end # Fin class Casilla
end # Fin module Civitas