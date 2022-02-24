#encoding:utf-8
require_relative "jugador"

module Civitas
  class Especulador < Jugador    
    @@factor_especulador=2
        
    def initialize(jugador,fianza)
      @fianza = fianza      
      super(jugador.nombre, jugador.encarcelado, jugador.num_casilla_actual, jugador.puede_comprar, jugador.saldo, jugador.salvoconducto, jugador.propiedades)      
      
      nuevo_especulador(jugador)
    end
    
    def nuevo_especulador(jugador)
      if(@propiedades != nil)
        for i in 0..@propiedades.size-1
          @propiedades.at(i).actualiza_propietario_por_conversion(jugador)
        end
      end
    end
    
    def encarcelar(num_casilla_carcel)
      mover_a_casilla(num_casilla_carcel)
      
      if debe_ser_encarcelado
        if (!((@saldo-@fianza)> 0)) 
          Diario.instance.ocurre_evento("Se ha encarcelado a #{@nombre}")
          @encarcelado = true
        else
          @saldo = @saldo-@fianza
          Diario.instance.ocurre_evento("No se encarceló a #{@nombre} porque ha pagado la fianza")
        end
      end
      @encarcelado
    end
    
    def paga_impuesto(cantidad)
      !@encarcelado && paga(cantidad/2)
    end
    #Num de props: #{@propiedades.length },\n
    def to_s
      cad = "Nombre del especulador: #{@nombre},\n Saldo: #{@saldo},\n 
             Num de props: #{@propiedades.length},\n
             Num casilla actual: #{@num_casilla_actual}
             Fianza: #{@fianza} "

      cad
    end 
    
    def casas_max
      super.casas_max*@@factor_especulador
    end
    
    def hoteles_max
      super.hoteles_max*@@factor_especulador
    end
  end
end
