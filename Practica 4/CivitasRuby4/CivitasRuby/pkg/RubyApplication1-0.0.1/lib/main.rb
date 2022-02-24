#encoding:utf-8
require_relative 'casilla'

require_relative'casilla_calle'
require_relative'casilla_impuesto'
require_relative'casilla_juez'
require_relative'casilla_sorpresa'

require_relative 'sorpresa'

require_relative 'sorpresa_ir_carcel'
require_relative 'sorpresa_ir_casilla'
require_relative 'sorpresa_pagar_cobrar'
require_relative 'sorpresa_por_casa_hotel'
require_relative 'sorpresa_por_jugador'
require_relative 'sorpresa_salir_carcel'
require_relative 'sorpresa_especulador'


require_relative 'civitas_juego'
require_relative 'especulador.rb'
require_relative 'controlador'
require_relative 'dado'
require_relative 'diario'
require_relative 'estados_juego'
require_relative 'gestiones_inmobiliarias'
require_relative 'gestor_estados'
require_relative 'jugador'
require_relative 'mazo_sorpresas'
require_relative 'operaciones_juego'
require_relative 'respuestas'
require_relative 'salidas_carcel'
require_relative 'tablero'
require_relative 'titulo_propiedad'
require_relative 'vista_textual'

module Civitas
  class Main
    def self.main
       vista = Vista_textual.new
        Dado.instance.set_debug(true)
        nombres = Array.new
        nombres << "Papi"
        nombres << "Franchu"
        
        juego = CivitasJuego.new(nombres)
        control = Controlador.new(juego,vista)
        control.juega
    end
    main
  end  
end
