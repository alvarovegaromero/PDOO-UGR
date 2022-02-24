/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civitasjava;

import civitasjava.CivitasJuego;
import civitasjava.Dado;
import java.util.ArrayList;
import juegotexto.Controlador;
import juegotexto.VistaTextual;

/**
 *
 * @author Francisco Prados Abad
 */
public class mainPruebas {
        
    public static void main(String[] args){
        System.out.println("Juguemos!");
        
        ArrayList<String> Jugadores = new ArrayList<>();
        Jugadores.add("Fran");
        Jugadores.add("Alvaro");
        //Jugadores.add("Juank");
        
        VistaTextual vista      = new VistaTextual();
        CivitasJuego juego      = new CivitasJuego(Jugadores);
        Dado.getInstance().setDebug(true);
        Controlador controlador = new Controlador(juego, vista);
        
        controlador.juega();
        
    }
}
