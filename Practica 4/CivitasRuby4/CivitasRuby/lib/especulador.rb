#encoding:utf-8
require_relative "jugador"

module Civitas
  class Especulador < Jugador
    @@factor_especulador=2

    def initialize(jugador,fianza)
      @fianza = fianza
        
      nuevo_especulador(jugador)
    end
    
    def nuevo_especulador(jugador)
      copia(jugador)
      
      if(@propiedades != nil)
        for i in 0...@propiedades.size
          @propiedades.at(i).actualiza_propietario_por_conversion(jugador)
        end
      end
      
    end
    
    def encarcelar(num_casilla_carcel)
      mover_a_casilla(num_casilla_carcel)
      
      if debe_ser_encarcelado
        if ( @saldo < @fianza ) 
          Diario.instance.ocurre_evento("Se ha encarcelado al especulador #{@nombre}")
          @encarcelado = true
        else
          @saldo = @saldo-@fianza
          Diario.instance.ocurre_evento("No se encarcelo al especulador #{@nombre} porque ha pagado la fianza")
        end
      end
      
      @encarcelado
    end
    
    def paga_impuesto(cantidad)
      super(cantidad/2)
    end
    #Num de props: #{@propiedades.length },\n
    def to_s
      cad = super
      cad = cad + "Fianza: #{@fianza} "

      cad
    end 
    
    def casas_max
      super*@@factor_especulador
    end
    
    def hoteles_max
      super*@@factor_especulador
    end
  end
end
