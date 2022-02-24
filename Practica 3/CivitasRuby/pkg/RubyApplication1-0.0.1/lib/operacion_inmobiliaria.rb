# encoding:utf-8

module Civitas
  attr_reader :num_propiedad, :gestion
  
  class OperacionInmobiliaria
    def initialize(gest, ip)
      @num_propiedad = ip
      @gestion       = gest
    end
  end
end
