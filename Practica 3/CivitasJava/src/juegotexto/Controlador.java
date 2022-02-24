/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegotexto;

/**
 *
 * @author juanc
 */
import civitasjava.CivitasJuego;
import civitasjava.Diario;
import civitasjava.GestionesInmobiliarias;
import civitasjava.Jugador;
import civitasjava.OperacionInmobiliaria;
import civitasjava.OperacionesJuego;
import civitasjava.Respuestas;
import civitasjava.SalidasCarcel;
import java.util.ArrayList;

public class Controlador {
    private CivitasJuego juego;
    private VistaTextual vista;
    
    public Controlador(CivitasJuego juego, VistaTextual vista) {
        this.juego = juego;
        this.vista = vista;
    }
    
    public void juega() {
       
        vista.setCivitasJuego(juego);
        
        while(!juego.finalDelJuego()) {
            
            vista.pausa();
            OperacionesJuego operacion = vista.juegoModel.siguientePaso(); 
            this.vista.mostrarSiguienteOperacion(operacion); 
            
            if (operacion != OperacionesJuego.PASAR_TURNO){ 
                this.vista.mostrarEventos();
                this.vista.actualizarVista();
            }
    
            
            if (!juego.finalDelJuego()) {
                switch(operacion) {
                    case COMPRAR:
                        Respuestas respuesta = vista.comprar();
                        
                        if (respuesta == Respuestas.SI) 
                            this.juego.comprar();
                            
                        this.juego.siguientePasoCompletado(operacion);
                        
                        break;
                        
                    case GESTIONAR:
                        vista.gestionar();
                        GestionesInmobiliarias gestion = GestionesInmobiliarias.values()[vista.getGestion()];
                        OperacionInmobiliaria op_inmobiliaria = new OperacionInmobiliaria(gestion,vista.getPropiedad());
                        
                        switch (op_inmobiliaria.getGestion()) {
                            case CANCELAR_HIPOTECA:
                                this.juego.cancelarHipoteca(vista.getPropiedad());
                                break;
                            case CONSTRUIR_CASA:
                                this.juego.construirCasa(vista.getPropiedad());
                                break;
                            case CONSTRUIR_HOTEL:
                                this.juego.construirHotel(vista.getPropiedad());
                                break;
                            case HIPOTECAR:
                                this.juego.hipotecar(vista.getPropiedad());
                                break;
                            case TERMINAR:
                                juego.siguientePasoCompletado(operacion);
                                break;
                            case VENDER:
                                this.juego.vender(vista.getPropiedad());
                                break;
                        }
                        break;
                    case SALIR_CARCEL:
                        SalidasCarcel salida = vista.salirCarcel();
                        
                        if (salida == SalidasCarcel.PAGANDO) {
                            this.juego.salirCarcelPagando();
                        }
                        else if (salida == SalidasCarcel.TIRANDO) {
                            this.juego.salirCarcelTirando();
                        }
                        
                        this.juego.siguientePasoCompletado(operacion);
                        break;
                }
            }
            
        }
        
        System.out.println("\n---RANKING---ºn");
        ArrayList<Jugador> ranking = this.juego.ranking();

        for (int i = 0; i < ranking.size(); i++) {
            System.out.println(ranking.get(i) + "\n");
        }
        
    }
}
