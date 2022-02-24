#encoding:utf-8

module Civitas
  
class MazoSorpresas
  attr_reader :ultima_sorpresa     
  public  
  
  # Constructor con un parametro
  # @param valor para el atributo debug
  def initialize(debug=false)
    Diario.instance.ocurre_evento("Debug se ha puesto a " + debug) if debug
    @sorpresas        = Array.new    # Almacena las cartas Sorpresa
    @barajada         = false # Indica si ha el mazo sido barajado
    @usadas           = 0     # Numero de cartas del mazo que ya han sido utilizadas
    @cartas_especiales = Array.new    # Almacenara la carta SALIRCARCEL cuando la tenga un jugador
    @debug = debug  # Activa o desactiva el modo depuración
    @ultima_sorpresa = nil # Guarda la ultima sorpresa que ha salido
  end
  
  
  # @brief Metodo para aniadir sorpresas al mazo si no ha sido barajado
  # @param s sorpresa a agregar
  # @return void
  def al_mazo(s)
     @sorpresas << s if !@barajada
  end
  
  # @brief Metodo para coger una carta del mazo
  # @return Sorpresa activa en en ese momento para el juego
  def siguiente
    if (!@debug && (!@barajada || @usadas == @sorpresas.length))
      @sorpresas = @sorpresas.shuffle # shuffle devuelve un array mezclado aleatoriamente
      @barajada = true
      @usadas   = 0
    end
    
    @usadas += 1
    sorpresa = @sorpresas.shift              # Quitamos la primera carta
    @sorpresas.push(sorpresa) # Copiamos la primera carta al final
    @ultima_sorpresa = sorpresa
    
    @ultima_sorpresa
  end
   
  # @brief Quita la sorpresa pasada como parametro del mazo para 
  #        que no se use y la guarda en cartas_especiales
  # @param sorpresa a desactivar
  # @see habilirarCartaEspecial(sorpresa)
  # @return void
  def inhabilitar_carta_especial(sorpresa)
      if(@cartas_especiales.include?(sorpresa))
        @sorpresas.push(sorpresa)
        @cartas_especiales.delete(sorpresa)
        Diario.instance.ocurre_evento("Carta inhabilitada")
      end
  end
  
  
  # @brief Se aniade al final de @sorpresas la sorpresa pasada
  #        como parametro y se saca de cartas_especiales
  # @param sorpresa a activar
  # @see inhabilitar_carta_especial(sorpresa)
  # @return void
  def habilitar_carta_especial(sorpresa)
    if @cartas_especiales.include?(sorpresa) 
            
      indice = @cartas_especiales.index(sorpresa) # No se hace @cartas_especiales.delete(sorpresa) 
      @cartas_especiales.delete_at(indice)        # por si hay dos cartas iguales que no borre las dos
      
      @sorpresas.push(sorpresa)
      
      Diario.instance.ocurre_evento("Habilitamos carta " + sorpresa.nombre)
    end
  end
  
  
  # @brief Metodo para mostrar el mazo
  # @return void
  def to_s
    
    print "Cartas del mazo: "
          @sorpresas.each{|n| print "#{n.to_s}, "}
    
    if @barajada
      print "\nEl mazo ha sido barajado" 
    else
      print "\nEl mazo no ha sido barajado"
    end
    
    print "\nAhora mismo se han usado ", @usadas, " cartas"
   
    print "\nEl modo debug esta "
    if @debug
      print " activado, por lo tanto no se barajara el mazo"
    else 
      print " desactivado, asi que se barajara el mazo"
    end
    
    #cartas_especiales
    
    print "\nLa ultima carta que ha salido es ", @ultima_sorpresa, "\n"
  end
  
end # Fin de MazoSorpresas
end # Fin de ModuleCivitas