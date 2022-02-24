module Civitas

class Sorpresa_salir_carcel < Sorpresa
  
# @brief Constructor de la clase
# @param mazo, el mazo con en el que se juega
def initialize(mazo)
  super("Evita la carcel")

  @mazo = mazo
end


# @brief Si nadie tiene la sorpresa para evitar la carcel, la obtiene el
#   jugador actual
# @param actual, el indice del jugador
# @param todos, el vector de jugadores
# @return Es void  
def aplicar_a_jugador(actual,todos)      
  if (jugador_correcto(actual,todos))
    informe(actual,todos)
    Diario.instance.ocurre_evento("Se procede a pedir el salvoconducto")
  end

  for i in 0..todos.size-1 do
    if (todos.at(i).tiene_salvoconducto)
      salvoconducto = true
    end
  end

  if (!salvoconducto)
    todos.at(actual).obtener_salvoconducto(self)
    salir_del_mazo
  end

  return salvoconducto
end


# @brief Inhabilita la carta especial en el mazo de sorpresas
# @return Es void
def salir_del_mazo()
   @mazo.inhabilitar_carta_especial(self)
end

# @brief Habilita la carta especial en el mazo de sorpresas
# @return Es void
def usada()
  @mazo.habilitar_carta_especial(self)
end
  

end

end