#encoding:utf-8

#PARA USAR COMO MAIN, DESCOMENTAR Y relative diario.rb para no errores
require_relative 'jugador'

require_relative 'vista_textual'
require_relative 'civitas_juego'
require_relative 'controlador'
require_relative 'dado'

module Civitas
  
  class TituloPropiedad

    attr_reader :nombre, :num_casas, :num_hoteles, :propietario, :precio_edificar, :precio_compra , :hipotecado , :hipoteca_base

    @@FACTORINTERESHIPOTECA = 1.1 

  # Constructor de la clase. Es único
  # @param nombre de la propiedad
  # @param precio base del alquiler de la propiedad
  # @param factor de revalorización de la propiedad
  # @param el precio base de la hipoteca
  # @param precio de compra de la propiedad
  # @param precio de edificar
  # @return un objeto de la clase TituloPropiedad  
  def initialize(nombre, precio_base_alquiler, factor_revalorizacion, hipoteca_base, precio_compra, precio_edificar)
      @alquiler_base = precio_base_alquiler 
      @factor_revalorizacion = factor_revalorizacion
      @hipoteca_base = hipoteca_base
      @hipotecado = false
      @nombre = nombre
      @num_casas = 0
      @num_hoteles = 0
      @precio_compra = precio_compra
      @precio_edificar = precio_edificar
      @propietario = nil
  end

  ####################################################################
  ## Resto de métodos
  ####################################################################

  # @brief Actualiza el propietario de una propiedad
  # @param jugador, el nuevo propietario
  # @return Es una funcion void
  public
  def actualiza_propietario_por_conversion(jugador)
    @propietario = jugador
  end

  # @brief Metodo para deshipotecar una propiedad
  # @param jugador, el propietario de la propiedad
  # @return true si cancela la hipoteca, false en otro caso
  public
  def cancelar_hipoteca(jugador)
    result = false

    if(@hipotecado && es_este_el_propietario(jugador))
      paga(importe_cancelar_hipoteca())
      @hipotecado = false
      result = true
    end

    result
  end

  # @brief Calcula la suma de hoteles y casas
  # @return la suma previa
  public
  def cantidad_casas_hoteles()
    @num_casas+@num_hoteles
  end


  # @brief Metodo para comprobar una propiedad.
  # @param El jugador que la va a comprar
  # @return true si la compra, false en otro caso
  public
  def comprar(jugador)
    result = false

    if(!tiene_propietario)
      @propietario = jugador
      result = true
      @propietario.paga(@precio_compra)
    end

    result
  end

  # @brief Metodo para constuir una casa en una propiedad.
  # @param El jugador que debe ser el dueño
  # @return true si se construye la casa, false en otro caso
  public
  def construir_casa(jugador)
    result = false

    if(es_este_el_propietario(jugador))
      @propietario.paga(@precio_edificar)
      @num_casas = @num_casas + 1
      result = true
    end

    result
  end

  # @brief Metodo para constuir un hotel en una propiedad.
  # @param El jugador que debe ser el dueño
  # @return true si se construye el hotel, false en otro caso
  public
  def construir_hotel(jugador)
    result = false

    if(es_este_el_propietario(jugador))
      @propietario.paga(@precio_edificar)
      @num_hoteles = @num_hoteles + 1
      result = true
    end

    result
  end

  # @brief Decrementa, si es posible, el contador de casas construidas en 
  #         n unidades.     
  # @param n, el número de casas a eliminar
  # @param jugador, el propietario de la casilla
  # @return un valor booleano. True si se ha podido decrementar
  # @pre El jugador debe ser el propietario de la casilla
  # @pre Se debe de disponer de n o más casas (previamente)
  public
  def derruir_casas(n, jugador)
    resultado = false

    if(@num_casas >= n && es_este_el_propietario(jugador))
      @num_casas = @num_casas - n
      resultado = true
    end
  end  

  # @brief Verifica si un jugador es el propìetario
  # @param jugador, el jugador que se verá si es propietario
  # @return true si es el propietario, false si no lo es
  private
  def es_este_el_propietario(jugador)
    (@propietario.nombre == jugador.nombre)
  end

  # @brief Calcula el valor que se recibiria al hipotecar una prop.
  # @return El valor a recibir si hipotecas una propiedad
  public
  def importe_cancelar_hipoteca()
    @hipoteca_base*@@FACTORINTERESHIPOTECA
  end  

  # @brief Devuelve el valor de hipoteca
  # @return El valor base de la hipoteca
  private
  def get_importe_hipoteca()
    @hipoteca_base
  end

  # @brief Devuelve el precio del alquiler de la propiedad
  # @return El valor a pagar si caes en una propiedad
  # @pre Que no este hipotecada la propiedad
  private
  def get_precio_alquiler
    precio_alquiler = 0
    if(!@hipotecado or propietario_encarcelado)
      precio_alquiler = @alquiler_base*(1+(@num_casas*0.5)+(@num_hoteles*2.5))
    end
  end

  # @brief Calcula el precio de venta 
  # @return la suma del precio de compra con el precio de edificar las
  #   casas y hoteles que tenga, multiplicado éste último por el factor 
  #   de revalorización.private
  private
  def get_precio_venta()
    @precio_compra+(@num_casas+5*@num_hoteles)*@precio_edificar*@factor_revalorizacion
  end


  # @brief Metodo para hipotecar una propiedad.
  # @param jugador, el jugador que quiere hipoteca la propiedad
  # @return true si se hipoteca, false en otro caso
  
  public
  def hipotecar(jugador)
    salida = false

    if(!@hipotecado && es_este_el_propietario(jugador))
      @propietario.recibe(get_importe_hipoteca)
      @hipotecado = true
      salida = true
    end

    salida
  end

  # @brief Dice sin el propietario de una propiedad esta encarcelado
  # @return Un valor booleano true si lo está, false si no está en la cárcel
  private
  def propietario_encarcelado()
    @propietario.encarcelado
  end 

  # @brief Verifica si una propiedad tiene dueño
  # @return true si tiene propietario, false en otro caso
  public
  def tiene_propietario()
    resultado = false

    if (@propietario != nil)
      resultado = true
    end

    resultado  
  end

  # @brief Expresa el estado completo de un objeto
  # @return Un string con los valores de los atributos de instancia
  public
  def to_s
      "Nombre: "                +@nombre+
      "\nAlquilerBase: "        +@alquiler_base.to_s
      "\nFactorRevalorizacion: "+@factor_revalorizacion.to_s+
      "\nHipotecaBase: "        +@hipoteca_base.to_s+
      "\nPrecioCompra: "        +@precio_compra.to_s+
      "\nPrecioEdificar: "      +@precio_edificar.to_s+
      "\nHipotecado: "          +@hipotecado.to_s+
      "\nNumCasas: "            +@num_casas.to_s+
      "\nNumHoteles: "          +@num_hoteles.to_s+

      if(@propietario != nil)
        "\nPropietario: "+@propietario.nombre
      else
        "\nNo tiene propietario "
      end
  end

  # @brief Tramita el alquiler de una casilla cuando un jugador cae en ella
  # @param El jugador que cae a la casilla
  # @return void
  # @pre La propiedad debe tener propietario
  public
  def tramitar_alquiler(jugador)
      if(tiene_propietario && !es_este_el_propietario(jugador))
        propietario.recibe(get_precio_alquiler)
        jugador.paga_alquiler(get_precio_alquiler)
      end
  end  

  # @brief Vende una propiedad, reembolsandose el dinero. Se eliminan casas,
  #     hoteles y se desvincula al propietario de la propiedad
  # @param el jugador que quiere vender la propiedad
  # @pre El jugador pasado como parámetro debe ser el propietario
  # @pre La propiedad no puede estar hipotecada
  public
  def vender(jugador)
    resultado = false

    if(!@hipotecado and es_este_el_propietario(jugador))
      jugador.recibe(get_precio_venta)
      @propietario = nil
      @num_casas = 0
      @num_hoteles = 0
      resultado = true
    end

    resultado
  end

  end #Fin clase TituloPropiedad
end #Fin Modulo Civitas