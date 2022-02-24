module Civitas

class Sorpresa_por_jugador < Sorpresa
  def initialize(valor, texto)
    super(texto)
    
    @valor = valor
  end

# @brief Llama al método pagar y cobrar y todos los jugadores le dan @valor
#   al jugador actual, que recibe @valor * num_jugadores 
# @param actual, el indice del jugador
# @param todos, el vector de jugadores
# @return Es void
def aplicar_a_jugador(actual,todos)
  if(jugador_correcto(actual,todos))
    informe(actual,todos)
    
    a_cobrar = Sorpresa_pagar_cobrar.new((todos.length-1) * @valor, "cobrar")
    a_pagar  = Sorpresa_pagar_cobrar.new(              -1 * @valor, "pagar" )
    
    for i in (0..todos.length)
      if(i != actual) #El jugador "actual" no paga
        a_pagar.aplicar_a_jugador(i,todos)
      end
    end
   
    a_cobrar.aplicar_a_jugador(actual,todos) #Llama a modifica saldo 
    
  end
  
end

end

end