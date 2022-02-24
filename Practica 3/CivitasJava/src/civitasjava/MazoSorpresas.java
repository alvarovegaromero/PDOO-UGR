
package civitasjava;

import java.util.ArrayList;
import java.util.Collections; //para barajar el vector

public class MazoSorpresas {
    
    // Atributos de instancia
    private ArrayList<Sorpresa> sorpresas;
    private boolean barajada;
    private int usadas;
    private boolean debug;
    private ArrayList<Sorpresa> cartasEspeciales;
    private Sorpresa ultimaSorpresa;
   
// @brief Metodo privado para inicializar algunos atributos de la clase
// @return void
    private void init(){
        usadas           = 0; 
        barajada         = false;
        sorpresas        = new ArrayList<>(); 
        cartasEspeciales = new ArrayList<>();
    }
    
// Constructor con un parametro
// @param valor para el atributo debug
    MazoSorpresas (boolean debug){
        this.debug = debug;
        init();
        
        if(debug)
            Diario.getInstance().ocurreEvento("Se encuentra activado el "
                                              + "modo debug");

    }
    
// Constructor sin parámetros. Llama al método init y fija el valor
// de debug a false.
    MazoSorpresas(){
        debug = false;
        init();    
    }
    
// @brief Metodo para aniadir sorpresas al mazo si no ha sido barajado
// @param s sorpresa a agregar
// @return void
    void alMazo(Sorpresa s){
        if(!barajada)
            sorpresas.add(s);
    }
    
// @brief Metodo para coger una carta del mazo
// @return Sorpresa activa en en ese momento para el juego
    Sorpresa siguiente(){
        if((!barajada || usadas == sorpresas.size()) && !debug){
            Collections.shuffle(sorpresas); //Barajamos con la función shuffle
            usadas = 0;
            barajada = true;
        }
        
        usadas++;
        ultimaSorpresa = sorpresas.get(0); //Primer elemento del vector
        sorpresas.remove(0);    
        return ultimaSorpresa;
    }
    
// @brief Quita la sorpresa pasada como parametro del mazo para 
//        que no se use y la guarda en cartasEspeciales
// @param sorpresa a desactivar
// @see habilirarCartaEspecial(sorpresa)
// @return void
    void inhabilitarCartaEspecial(Sorpresa sorpresa){
        if(sorpresas.contains(sorpresa))
        {   
            sorpresas.remove(sorpresa);
            cartasEspeciales.add(sorpresa);
            Diario.getInstance().ocurreEvento("Se ha inhabilitado una carta");
        }
    }
    
// @brief Se aniade al final de @sorpresas la sorpresa pasada
//        como parametro y se saca de cartasEspeciales
// @param sorpresa a activar
// @see inhabilitarCartaEspecial(sorpresa)
// @return void
    void habilitarCartaEspecial(Sorpresa sorpresa)
    {
      if(cartasEspeciales.contains(sorpresa)){
            sorpresas.add(sorpresa);
            cartasEspeciales.remove(sorpresa);
            Diario.getInstance().ocurreEvento("Se ha habilitado una carta");         
      }
    }
    
}
