#encoding:utf-8

require_relative 'diario.rb'
require_relative 'sorpresa.rb'
require_relative 'mazo_sorpresas.rb'
require_relative 'tipo_sorpresa.rb'
require_relative 'titulo_propiedad.rb'

module Civitas  
  
class Jugador
  
attr_reader :nombre, :saldo, :casas_max, :hoteles_max, :casas_por_hotel,:num_casilla_actual,
            :precio_libertad,:paso_por_salida,:propiedades,:puede_comprar,:encarcelado

@@casas_max = 4
@@casas_por_hotel = 4
@@hoteles_max = 4
@paso_por_salida = 1000.0
@@precio_libertad = 200.0
@saldo_inicial = 7500

# Constructor
def initialize (nombre, encarcelado, num_casilla_actual, 
                puede_comprar, saldo, salvoconducto, propiedades)
  @nombre = nombre
  @encarcelado = encarcelado
  @num_casilla_actual = num_casilla_actual
  @puede_comprar = puede_comprar
  @saldo = saldo
  @propiedades = propiedades
  @salvoconducto = salvoconducto #Objeto de Sorpresa
end

# Constructor
def self.new_nombre(nombre)
  new(nombre,false,0,false,@saldo_inicial,nil,Array.new)
end

# Constructor de copia
protected
def self.new_copia(jugador)
  new(jugador.nombre,jugador.encarcelado, jugador.num_casilla_actual,
      jugador.puede_comprar, jugador.saldo, jugador.salvoconducto,
      jugador.propiedades)
end
  
####################################################################
## Resto de métodos
####################################################################  


# @brief Metodo para deshipotecar una propiedad
# @param ip, el número de la propiedad en el vector de propiedades
# @return true si cancela la hipoteca, false en otro caso
public
def cancelar_hipoteca(ip)
  result = false
  
  if(!@encarcelado && existe_la_propiedad(ip))
    propiedad = propiedades[ip]
    cantidad = propiedad.get_importe_cancelar_hipoteca()
    
    puedo_gastar = puedo_gastar(cantidad)
    
    if(puedo_gastar)
      result = cancelar_hipoteca(jugador)
      if(result)
        Diario.instance.ocurre_evento("El jugador #{@nombre} cancela la hipoteca de la propiedad #{ip}")      
      end
    end
  end
  
  result
end


# @brief Calcula el numero total de casas y hoteles que posee un jugador
# @return Numero total de edificios que tiene el jugador
public
def cantidad_casas_hoteles 
  suma = 0
  for i in(0..@propiedades.legth)
    suma += @propiedades[i].cantidad_casas_hoteles 
  end
  
  suma
end


# @brief Metodo para comprobar si un objeto tiene un orden mayor o menor
#   que el objeto que realiza la llamada
# @param otro jugador con el que se compara
# @post Este metodo es reciproco, a.compareTo(b) = - b.compareTo(a)
# @return 0 si ambos ojetos tienen el mismo orden (no hay uno mayor que otro)
#         un numero entero negativo (-1) si el objeto actual es menor que el pasado como parametro
#         un numero entero positivo (+1) si el objeto actual es mayor que el pasado como parametro
public
def compare_to(otro)
  @saldo<=>otro.saldo # -1 si a < b, 0 si a == b, 1 si a > b
end

# @brief Metodo para comprobar una propiedad.
# @param La propiedad a comprar
# @return true si la compra, false en otro caso
public
def comprar(titulo)
  result = false
  
  if(!@encarcelado && @puede_comprar)
    precio = titulo.precio_compra
    
    if(puedo_gastar(precio))
      result = titulo.comprar(self)
      if(result)
        @propiedades<<titulo
        Diario.instance.ocurre_evento("El jugador #{@nombre} compra la propiedad #{titulo.nombre}")
      end
      @puede_comprar = false
    end
  end
  
  result
end

# @brief Metodo para constuir una casa en una propiedad.
# @param ip, el indice de la propiedad en el vector de propiedades
# @return true si se construye la casa, false en otro caso
public
def construir_casa(ip)
  result = false
  puedo_edificar_casa = false
  
  if(!encarcelado)
    existe = existe_la_propiedad(ip)
    
    if(existe)
      propiedad = @propiedades[ip]
      puedo_edificar_casa = puedo_edificar_casa(propiedad)
      precio = propiedad.precio_edificar
      
      if(puedo_gastar(precio) && propiedad.num_casas < @@casas_max)
        puedo_edificar_casa = true
      end
      
      if(puedo_edificar_casa)
        result = propiedad.construir_casa(self)
        
        if(result)
          Diario.instance.ocurre_evento("El jugador #{@nombre} construye una casa #{ip}")
        end
      end
    end
  end
  
  result
end


# @brief Metodo para constuir un hotel en una propiedad.
# @param ip, el indice de la propiedad en el vector de propiedades
# @return true si se construye el hotel, false en otro caso
public
def construir_hotel(ip)
  result = false
  
  if(!@encarcelado && existe_la_propiedad(ip))
    propiedad = @propiedades[ip]
    puedo_edificar_hotel = puedo_edificar_hotel(propiedad)
    puedo_edificar_hotel = false
    precio = propiedad.precio_edificar()
    
    if(puedo_gastar(precio) && propiedad.num_hoteles < @@hoteles_max && propiedad.num_casas >= @@casas_por_hotel)
      puedo_edificar_hotel = true
    end
    
    if(puedo_edificar_hotel)
      result = propiedad.construir_hotel(self)
      
      casas_por_hotel = @@casas_por_hotel
      propiedad.derruir_casas(casas_por_hotel, self)
      Diario.instance.ocurre_evento("El jugador #{@nombre} construye un hotel en #{ip}")
    end
  end
  
  result
end

# @brief Metodo para comprobar si un jugador tiene que ir a la carcel
# @post false si el jugador ya estaba encarcelado
# @post si se usa la carta de librarse de la cacel esta se consume
# @return TRUE si hay que encarcelar al jugador, FALSE en otro caso
protected
def debe_ser_encarcelado
  if(@encarcelado)
    resultado = false
  
  elsif tiene_salvoconducto
    perder_salvoconducto
    resultado = true
    Diario.instance.ocurre_evento("#{@nombre} se salvó de la cárcel por la carta Evitar Cárcel")

  else
    resultado = true
  end
  
  resultado
end


# @brief Metodo para comprobar si un jugador tiene dinero
# @return TRUE si tiene dinero, FALSE si esta en numeros rojos
public
def en_bancarrota
  resultado = false
  
  if @saldo < 0
    resultado = true
  end
  
  resultado
end

# @brief Si un jugador tiene que ser encarcelado, se mueve a la carcel
# @param num_casilla_carcel
# @pre el parametro num_casilla_carcel debera ser la casilla correcta 
# @return TRUE si se ha encarcelado al jugador FALSE en otro caso
public
def encarcelar(num_casilla_carcel)
  if debe_ser_encarcelado
    if mover_a_casilla(num_casilla_carcel)
      Diario.instance.ocurre_evento("Se ha encarcelado a #{@nombre}")
      @encarcelado = true
    else
      Diario.instance.ocurre_evento("No se pudo encarcelar a #{@nombre}")
    end
  end
  
  @encarcelado
end

# @brief Metodo para comprobar si hay una propiedad @ip
# @param ip numero de propiedad a evaluar
# @return TRUE si hay una propiedad @ip, FALSE en otro caso
private
def existe_la_propiedad(ip)
  resultado = false
  
  #Supongo que ip es el indice de la propiedad a buscar
  if (@propiedades.length > ip)
     resultado = true
  end
  
  resultado
end

# @brief Metodo para hipotecar una propiedad.
# @param ip, el indice de la propiedad en el vector de propiedades
# @return true si se hipoteca la propiedad, false en otro caso
public
def hipotecar(ip)
  result = false
  
  if(!@encarcelado && existe_la_propiedad(ip))
    propiedad = @propiedades[ip];
    result = propiedad.hipotecar(self)
    
    if(result)
      Diario.instance.ocurre_evento("El jugador #{@nombre} ha hipotecado la propiedad #{ip}")
    end
  end
  
  result
end

# @brief Incrementa el saldo @cantidad dinero
# @param cantidad de dinero a sumar al jugador
# @return TRUE en cualquier caso
public
def modificar_saldo(cantidad)
  @saldo += cantidad
  Diario.instance.ocurre_evento("El nuevo saldo de #{@nombre} es (#{@saldo})")
  true
end

# @brief Metodo para desplazar a un jugador a una determinada casilla
# @param numCasilla casilla a la que se desplazara el jugador
# @return TRUE si se realiza la operacion FALSE en otro caso
public
def mover_a_casilla(num_casilla)
  resultado = false
  
  if !@encarcelado
    @num_casilla_actual = num_casilla
    @puede_comprar = false
      Diario.instance.ocurre_evento("La nueva posición de #{@nombre} es "+ num_casilla.to_s)
    resultado = true
  end
  
  resultado
end

# @brief Metodo para hacer que un jugador consiga un salvoconducto
# @pre el parametro debe ser una sorpresa de tipo SALIRCARCEL
# @param sorpresa
# @post el jugador no obtendra salvoconducto si esta encarcelado
# @return TRUE si el jugador obtiene el salvoconducto FALSE en otro caso
public
def obtener_salvoconducto(sorpresa)
  resultado = false
  if !@encarcelado
    resultado = true
    @salvoconducto = sorpresa
  end
  
  resultado
end

# @brief Metodo para que un jugador pierda @cantidad dinero
# @param cantidad de dinero que se descuenta
# @see this.modificar_saldo(float cantidad)
# @return TRUE si se modifica el saldo FALSE si no (siempre devuelve TRUE)
public
def paga(cantidad)
  tmp = (-1 * cantidad)
  resultado = modificar_saldo(tmp)
  
  resultado
end


# @brief Metodo para restar @cantidad dinero a un jugador por un ALQUILER 
# @param cantidad de dinero a restar
# @return TRUE si se realiza la operacion FALSE en otro caso (siempre TRUE)
public
def paga_alquiler(cantidad)
  resultado = false
  if !@encarcelado
    paga(cantidad)
    resultado = paga(cantidad)
  end
  
  resultado
end


# @brief Metodo para restar @cantidad dinero a un jugador por un IMPUESTO
# @param cantidad de dinero a restar
# @return TRUE si se realiza la operacion FALSE en otro caso (siempre TRUE)
public
def paga_impuesto(cantidad)
  resultado = false
  if !@encarcelado
    paga(cantidad)
    resultado = paga(cantidad)
  end
  
  resultado
end


# @brief Indica que el jugador ha pasado por la salida y el cobra el premio
# @return TRUE en cualquier caso 
public
def pasa_por_salida 
  modificar_saldo(@paso_por_salida)
  Diario.instance.ocurre_evento("#{@nombre} ha pasado por la salida")
  true
end


# @brief Metodo para indicar que un jugador usa un salvoconducto
# @post El jugador pierde el salvoconducto tras usarlo
private
def perder_salvoconducto 
  @salvoconducto.usada
  @salvoconducto = nil
end


# @brief Metodo para compobar si un jugador puede comprar una casilla
# @post el resultado se almacenara en el atributo puedeComprar
# @return TRUE si puede comprar, FALSE en otro caso
public
def puede_comprar_casilla 
  if @encarcelado
    @puede_comprar = false
  else
    @puede_comprar = true
  end
  
  @puede_comprar
end


# @brief Metodo para comprobar si un jugador tiene suficiente 
#   dinero para librarse de la carcel
# @return TRUE si tiene suficiente saldo, FALSE en otro caso
private
def puede_salir_carcel_pagando 
  resultado = false

  if(@saldo >= @@precio_libertad)
    resultado = true
  end
  
  resultado
end

# @brief Metodo para comprobar si se cumplen los requisitios para construir casas
# @param propiedad donde comprobar si se puede edificar
# @return 
private
def puedo_edificar_casa(propiedad)
  (propiedad.num_casas<@@casas_max && !@encarcelado)
end


# @brief Metodo para comprobar si se cumplen los requisitios para construir hoteles
# @param propiedad donde comprobar si se puede edificar
# @return 
private
def puedo_edificar_hotel(propiedad)
  resultado = false
  if(propiedad.num_hoteles<@@hoteles_max && propiedad.num_casas == @casas_por_hotel && !@encarcelado )
    resultado = true
  end
  resultado
end


# @brief Metodo para comprobar si un jugador tiene al menos @precio dinero
# @param precio a comparar
# @post Aunque tenga suficiente dinero, si esta encarcelado devuelve FALSE 
# @return TRUE si hay suficiente dinero FALSE en otro caso
private
def puedo_gastar(precio)
  resultado = false
  if !@encarcelado
    if(saldo >= precio)
      resultado = true
    end
  end

  resultado
end

# @brief Metodo para que un jugador reciba @cantidad dinero
# @param cantidad de dinero que se incrementa
# @see this.modificar_saldo(float cantidad)
# @return TRUE si se modifica el saldo FALSE si no (siempre devuelve TRUE)
public
def recibe(cantidad)
  resultado = false
  if !@encarcelado
    resultado = modificar_saldo(cantidad)
  end
  
  resultado
end

# @brief Metodo para que un jugador salga de la carcel con su saldo
# @return TRUE si el jugador sale de la carcel, FALSE en otro caso
public
def salir_carcel_pagando 
  resultado = false

  if (@encarcelado && puede_salir_carcel_pagando )
    paga(@@precio_libertad)
    @encarcelado = false
    Diario.instance.ocurre_evento("#{@nombre} ha salido de la carcel pagando")
  end
  
  resultado
end

# @brief Metodo para que un jugador salga de la carcel tirando los dados
# @return TRUE si el jugador sale de la carcel FALSE en otro caso 
public
def salir_carcel_tirando 
  resultado = false
  if (Dado.instance.salgo_de_la_carcel)
    @encarcelado = false
    Diario.instance.ocurre_evento("#{@nombre} ha salido de la carcel con el dado")
    resultado = true
  end
  
  resultado
end

# @brief Metodo para comprobar si un jugador tiene, al menos, una propiedad
# @return TRUE si tiene alguna propiedad, FALSE en otro caso
public
def tiene_algo_que_gestionar 
  (@propiedades.length > 0 )
end

# @brief Metodo para consultar si un jugador tiene un salvoconducto
# @return TRUE si tiene un salvoconducto, FALSE en otro caso
public
def tiene_salvoconducto 
  (@salvoconducto != nil)
end

# @brief Metodo para convertir la informacion de un jugador a String
# @return String con la informacion 
public
def toString
  
  cad = "Nombre: #{@nombre},\n Saldo: #{@saldo},\n 
         Num de props: #{@propiedades.length },\n
         Num casilla actual: #{@num_casilla_actual}"
  
  cad
end

# @brief Metodo para perder una propiedad
# @param ip propiedad a perder
# @return TRUE si se realiza la venta, FALSE en otro caso
public
def vender(ip)
  resultado = false
  
  if !@encarcelado
    if(existe_la_propiedad(ip) && !@propiedades[ip].hipotecado)
      resultado = @propiedades[ip].vender(self)
      Diario.instance.ocurre_evento("#{@nombre} ha vendido la propiedad #{@propiedades[ip].nombre}")
      @propiedades.delete_at(ip)
    end
  end
  
  resultado
end

end #fin de la clase

end #fin del módulo
