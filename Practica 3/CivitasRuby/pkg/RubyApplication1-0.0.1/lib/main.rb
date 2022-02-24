#encoding:utf-8
require_relative "vista_textual"
require_relative "dado"
require_relative "civitas_juego"
require_relative "controlador"
require_relative "casilla"
require_relative "titulo_propiedad"

module Civitas
  class Main
    def self.main
       vista = Vista_textual.new
        Dado.instance.set_debug(true)
        nombres = Array.new
        nombres << "Juan"
        nombres << "Antonio"
        
        juego = CivitasJuego.new(nombres)
        control = Controlador.new(juego,vista)
        control.juega
    end
    main
  end  
end
