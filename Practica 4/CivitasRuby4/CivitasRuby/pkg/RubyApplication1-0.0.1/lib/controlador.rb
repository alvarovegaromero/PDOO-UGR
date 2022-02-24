#encoding:utf-8

module Civitas
  class Controlador
    def initialize(juego,vista)
      @juego = juego
      @vista = vista
    end
    
    def juega
    @vista.set_civitas_juego(@juego)
    
    while (!@juego.final_del_juego) do
      @vista.pausa
      operacion = @vista.juegoModel.siguiente_paso
      
      if (operacion != Operaciones_juego::PASAR_TURNO)
        @vista.mostrar_eventos
        #@vista.actualizar_vista
      else
        @vista.mostrar_estado("Pasando turno")
      end
      
      if (!@juego.final_del_juego)
        case operacion
          when Operaciones_juego::COMPRAR
            respuesta = @vista.comprar
          
            if (respuesta == Respuestas::SI)
              @juego.comprar
            end
            @juego.siguiente_paso_completado(operacion)
            
          when Operaciones_juego::GESTIONAR
            @vista.gestionar
            lista_gestiones = [Gestiones_inmobiliarias::VENDER,Gestiones_inmobiliarias::HIPOTECAR,
                Gestiones_inmobiliarias::CANCELAR_HIPOTECA,Gestiones_inmobiliarias::CONSTRUIR_CASA,
                Gestiones_inmobiliarias::CONSTRUIR_HOTEL,Gestiones_inmobiliarias::TERMINAR]
            gestion = lista_gestiones[@vista.iGestion]
            
          case gestion
            when Gestiones_inmobiliarias::CANCELAR_HIPOTECA
                @juego.cancelar_hipoteca(@vista.iPropiedad)
              when Gestiones_inmobiliarias::CONSTRUIR_CASA
                @juego.construir_casa(@vista.iPropiedad)
              when Gestiones_inmobiliarias::CONSTRUIR_HOTEL
                @juego.construir_hotel(@vista.iPropiedad)
              when Gestiones_inmobiliarias::HIPOTECAR
                @juego.hipotecar(@vista.iPropiedad)
              when Gestiones_inmobiliarias::TERMINAR
                @juego.siguiente_paso_completado(operacion)
              when Gestiones_inmobiliarias::VENDER
                @juego.vender(@vista.iPropiedad)
            end
          when Operaciones_juego::SALIR_CARCEL
            salida = @vista.salir_carcel

            if (salida == Salidas_carcel::PAGANDO)
              @juego.salir_carcel_pagando
            elsif (salida == Salidas_carcel::TIRANDO)
              @juego.salir_carcel_tirando
            end

            @juego.siguiente_paso_completado(operacion)
         end
       end 
     end
     
    puts "\n---RANKING---"
      ranking = @juego.ranking

      for i in 0..@juego.ranking-1
        puts ranking.at(i) + "\n"
      end
   end
  end
 end
