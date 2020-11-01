package equipoalpha.loveletter.partida.eventos;

import equipoalpha.loveletter.jugador.EstadosJugador;
import equipoalpha.loveletter.jugador.Jugador;
import equipoalpha.loveletter.partida.Partida;

import java.util.ArrayList;
import java.util.List;

public class ConfirmarInicioEvento implements EventoObservado {
    private final Partida partida;
    private List<Jugador> observadores;

    public ConfirmarInicioEvento(Partida partida) {
        this.partida = partida;
    }

    @Override
    public void notificar(List<Jugador> observadores) {
        this.observadores = new ArrayList<>(observadores);
        for (Jugador jugador : observadores) {
            jugador.getEstado().setEstadoActual(EstadosJugador.CONFIRMANDOINICIO);
        }
    }

    @Override
    public void removerObservador(Jugador jugador) {
        observadores.remove(jugador);
        jugador.getEstado().setEstadoActual(EstadosJugador.ESPERANDO);
        if (observadores.isEmpty())
            partida.initPartida();
    }

    @Override
    public void cancelar() {
        for (Jugador jugador : observadores) {
            jugador.getEstado().setEstadoActual(EstadosJugador.ESPERANDO);
        }
        this.observadores = null;
    }
}