/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civitasjava;

/**
 *
 * @author juanc
 */
public class Especulador extends Jugador{
    private int fianza;
    private static int factorEspeculador = 2;
    protected final static int CasasMax= 8;
    protected final static int HotelesMax = 4;
    
    public Especulador(Jugador otro, int fianza) {
        super(otro);
        this.fianza = fianza;
        
        if(this.propiedades != null)
            for (int i = 0; i < this.propiedades.size(); i++) 
                this.propiedades.get(i).actualizaPropietarioPorConversion(otro);
        
    }
    
    @Override
    boolean encarcelar(int numCasillaCarcel) {
        this.moverACasilla(numCasillaCarcel); 
     
        if( debeSerEncarcelado() ){
            if ( (this.saldo-this.fianza) < 0 ) {
                this.encarcelado = true;
                Diario.getInstance().ocurreEvento("El jugador " + this.getNombre() 
                                                  + " ha sido encarcelado");
            }
            else {
                this.saldo -= this.fianza;
            }
        }
        
        return this.encarcelado;
    }
    
    @Override
    boolean pagaImpuesto(float cantidad){
        return super.paga(cantidad/2);
    }
    
    @Override
    public String toString(){
     
        String numPropiedades;
        if( this.getPropiedades() != null )
            numPropiedades =  "" + this.propiedades.size();
        else 
            numPropiedades = "0";
        
        
        String info = ( "Nombre: "          + this.getNombre() + " (Especulador)"
              + "\nSaldo: "                 + this.getSaldo()
              + "\nNumero de propiedades "  + numPropiedades
              + "\nCasilla actual: "        + this.getNumCasillaActual()
              + "\nFianza: "                + this.fianza);
        
        return info;
    }
}
