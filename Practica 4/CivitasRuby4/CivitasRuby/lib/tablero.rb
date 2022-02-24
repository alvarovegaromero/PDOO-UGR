#encoding:utf-8 

module Civitas
  
class Tablero
  attr_reader :num_casilla_carcel, :casillas, :por_salida, :tiene_juez
  
  @@POSPORDEFECTOCARCEL = 1
  @@TABLEROERRONEO = -1
  
  
  # Constructor con un parametro
  # @param num_casilla_carcel indice de la casilla de la carcel
  def initialize(num_casilla_carcel)
    if num_casilla_carcel > @@POSPORDEFECTOCARCEL  
      @num_casilla_carcel = num_casilla_carcel 
    else 
      @num_casilla_carcel = @@POSPORDEFECTOCARCEL  
    end
    
    @casillas  = []      # Contenedor de las casillas del juego
    @por_salida = 0       # Numero de veces que se ha pasado por la salida en un turno
    @tiene_juez = false   # Si el tablero dispone de la casilla juez
    
    salida = Casilla.new("Salida")
    añade_casilla(salida)
  end
  
  
  private # Los siguientes metodos de instancia seran privados
  
  # @brief Metodo para comprobar si se puede jugar en el tablero
  # @return true si se puede jugar, false en caso contrario
  # Dos metodos no pueden llamarse igual en ruby, por eso aniadimos el 0
  def correcto0
   if @casillas.length > @num_casilla_carcel && @tiene_juez
    true
   else
    false
   end
  end
  
  # @brief Comprueba si se puede jugar y el parametro es valido para acceder a @casillas
  # @param num_casilla indice de la casilla que se desea comprobar si es valida 
  # @return true si se puede jugar y num_casilla es correcta, false en caso contrario
  def correcto(num_casilla)
    if correcto0 && @casillas.length > num_casilla
      true
    else
      false
    end
  end
  
    
  public  
  
  # @brief Resta uno al valor de por_salida si es mayor que cero
  # @return el valor de por_salida cuando se hace la llamada
  def get_por_salida 
    if @por_salida > 0
      @por_salida -= 1
      @por_salida+1
    else 
      @por_salida
    end
  end
  
  
  # @brief Metodo para aniadir casillas al tablero
  # @param casilla instancia de la clase Casilla a agregar
  # @return void
  def añade_casilla(casilla) 
    if @casillas.length == @num_casilla_carcel
      @casillas << Casilla.new("Carcel")
    end
    
    @casillas << casilla
    
    if @casillas.length == @num_casilla_carcel
      @casillas << Casilla.new("Carcel")
    end
  end 
  
  
  # @brief Metodo para agregar un Juez al tablero
  # @allows que no haya dos casillas de Juez en el tablero
  def añade_juez
    if !@tiene_juez
      @casillas << CasillaJuez.new(@num_casilla_carcel, "Juez")
      @tiene_juez = true
    end
  end
  
  
  # @brief Devuelve la casilla que ocupa la posicion pasada como parametro
  # @param posicion a buscar
  # @return casilla en la posicion num_casilla si es valida, null en otro caso
  def get_casilla(num_casilla)
    casilla = nil
      
      if correcto(num_casilla)
        casilla = @casillas.at(num_casilla)
      end
      
      return casilla
    #@casillas[num_casilla] if correcto(num_casilla)
  end
  
  
  # @brief Metodo para calcular la posicion de un jugador tras tirar el dado
  # @param actual posicion actual del jugador
  # @param tirada cuantas posiciones se avanzan (numero que ha salido en el dado)
  # @return casilla donde debe colocarse el jugador (-1 si no se puede ir a esa casilla)
  def nueva_posicion(actual, tirada)
    casilla_final = (actual + tirada) % @casillas.length
    
    if correcto(casilla_final)
      if casilla_final != actual + tirada
        @por_salida+=1
      end
      
      casilla_final
    else 
      @@TABLEROERRONEO
    end
    
  end
  
  
  # @brief Metodo para calcular cuantas casillas faltan para llegar a una determinada casilla
  # @param origen posicion donde se encuentra el jugador
  # @param destino lugar hasta el que queremos calcular el numero de casillas
  # @return el numero de casillas desde origen hasta destino (en el sentido del juego, no la geodesica) 
  def calcular_tirada(origen, destino)
    faltan = destino - origen
    
    if faltan <= 0       
      faltan += @casillas.length
    end
    
    faltan
  end

  
  # @brief Metodo para mostrar un tablero
  def to_s
    print "Casilla de la carcel: ", @num_casilla_carcel,
       
          "\nCaillas: "
    @casillas.each{|n| print "#{n.to_s}, "}
    
    print "\nSe ha pasado por la salida ", @por_salida, " veces"
    
    if @tiene_juez
      puts "\nTiene Juez"
    else
      puts "\nNo tiene Juez"
    end
  end
  
end # Fin class Tablero
end # Fin module Civitas
