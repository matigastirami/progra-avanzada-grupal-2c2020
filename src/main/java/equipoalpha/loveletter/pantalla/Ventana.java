package equipoalpha.loveletter.pantalla;

import com.google.gson.JsonObject;
import equipoalpha.loveletter.client.LoveLetter;
import equipoalpha.loveletter.common.ComandoTipo;
import equipoalpha.loveletter.util.Drawable;

import javax.swing.*;
import java.awt.*;

public class Ventana {
    private final JFrame ventana;
    private Drawable panelActual;

    public Ventana() {
        this.ventana = new JFrame("Love Letter");
        this.ventana.setSize(1024, 768);
        this.ventana.setPreferredSize(new Dimension(1024, 768));
        PanelElegirNombre panelElegirNombre = new PanelElegirNombre();
        this.ventana.add(panelElegirNombre);
        this.panelActual = panelElegirNombre;
        this.ventana.pack();
        this.ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.ventana.setLocationRelativeTo(null);
        this.ventana.setResizable(false);
        this.ventana.setVisible(true);
        panelElegirNombre.setVisible(true);
        this.ventana.setFocusable(true);
        this.ventana.requestFocusInWindow();
    }

    public void onLogin() {
        PanelMenuPrincipal panelMenuPrincipal = new PanelMenuPrincipal(this);
        this.ventana.add(panelMenuPrincipal);
        this.ventana.pack();
        ((JPanel) this.panelActual).setVisible(false);
        LoveLetter.handler.removeDrawableObject(panelActual);
        this.panelActual = panelMenuPrincipal;
        panelMenuPrincipal.setVisible(true);
    }

    public void onCrearSala() {
        PanelSala panelSala = new PanelSala();
        this.ventana.add(panelSala);
        this.ventana.pack();
        ((JPanel) this.panelActual).setVisible(false);
        LoveLetter.handler.removeDrawableObject(this.panelActual);
        this.panelActual = panelSala;
        panelSala.setVisible(true);
    }

    public void onSalirSala() {
        Container container = ventana.getContentPane();
        for (Component component : container.getComponents()) {
            if (component instanceof PanelMenuPrincipal) {
                ((JPanel) this.panelActual).setVisible(false);
                LoveLetter.handler.removeDrawableObject(this.panelActual);
                component.setVisible(true);
                this.panelActual = (Drawable) component;
                LoveLetter.handler.addDrawableObject((Drawable) component);
                return;
            }
        }
    }

    public void onConfirmarInicio() {
        int seleccion = JOptionPane.showConfirmDialog(this.ventana, // o this.panelActual
                "El creador selecciono para empezar la partida" +
                        ".\n¿Estas listo?",
                "Partida empezando",
                JOptionPane.YES_NO_OPTION);
        if (seleccion == JOptionPane.YES_OPTION) {
            LoveLetter.getInstance().getCliente().getJugadorCliente().confirmarInicio();
        } else {
            LoveLetter.getInstance().getCliente().getJugadorCliente().cancelarInicio();
        }
    }

    public void onPartidaEmpezada() {
        PanelPartida panelPartida = new PanelPartida();
        ventana.add(panelPartida);
        ventana.pack();
        ((JPanel) this.panelActual).setVisible(false);
        LoveLetter.handler.removeDrawableObject(this.panelActual);
        this.panelActual = panelPartida;
        panelPartida.setVisible(true);
    }

    public void onRondaEmpezada() {
        ((PanelPartida) this.panelActual).animandoAIR = true;
    }

    public void onPartidaTerminadaMsg() {
        int seleccion = JOptionPane.showConfirmDialog(this.ventana,
                "La partida termino, el ganador es: " +
                        LoveLetter.getInstance().getCliente().getJugadorCliente().getPartidaActual().jugadorEnTurno.nombre +
                        ".\n¿Volver a jugar?",
                "Partida terminada",
                JOptionPane.YES_NO_OPTION);
        if (seleccion == JOptionPane.YES_OPTION) {
            LoveLetter.getInstance().getCliente().send(ComandoTipo.ConfirmarVolverAJugar, new JsonObject());
        } else {
            LoveLetter.getInstance().getCliente().send(ComandoTipo.CancelarVolverJugar, new JsonObject());
        }
    }

    public void onPartidaTerminada() {
        Container container = ventana.getContentPane();
        for (Component component : container.getComponents()) {
            if (component instanceof PanelSala) {
                ((JPanel) this.panelActual).setVisible(false);
                LoveLetter.handler.removeDrawableObject(this.panelActual);
                component.setVisible(true);
                this.panelActual = (Drawable) component;
                LoveLetter.handler.addDrawableObject((Drawable) component);
                return;
            }
        }
    }

    public Drawable getPanelActual() {
        return panelActual;
    }
}
