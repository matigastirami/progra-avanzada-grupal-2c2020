package equipoalpha.loveletter.jugador;

import equipoalpha.loveletter.carta.Carta;
import equipoalpha.loveletter.carta.CartaTipo;
import equipoalpha.loveletter.partida.eventos.EventosPartida;
import equipoalpha.loveletter.server.JugadorServer;

public class JugadorFacade {
    /**
     * el jugador al que pertenece esta instancia
     */
    private final JugadorServer jugador;
    /**
     * estado actual del jugador
     */
    private EstadosJugador estadoActual;
    private Carta cartaDescartada;
    private JugadorServer jugadorElegido;
    private CartaTipo cartaAdivinada;
    private Carta cartaViendo;

    public JugadorFacade(JugadorServer jugador) {
        this.estadoActual = EstadosJugador.ESPERANDO;
        this.jugador = jugador;
    }

    public EstadosJugador getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(EstadosJugador estadoActual) {
        this.estadoActual = estadoActual;
        jugador.sincronizar();
    }

    public void cartaDescartada(Carta cartaDescartada) {
        this.cartaDescartada = cartaDescartada;
        enviarMensaje(jugador + " descarta " + cartaDescartada);
        this.cartaDescartada.descartar(this.jugador);
    }

    public void jugadorElegido(JugadorServer jugadorElegido) {
        this.jugadorElegido = jugadorElegido;
        enviarMensaje(jugador+ " elige al jugador " + jugadorElegido);
        this.cartaDescartada.jugadorElegido(this.jugador, this.jugadorElegido);
    }

    public void cartaAdivinada(CartaTipo cartaAdivinada) {
        this.cartaAdivinada = cartaAdivinada;
        enviarMensaje(jugador + " intenta adivinar con " + cartaAdivinada.nombre);
        this.cartaDescartada.cartaAdivinada(this.jugador, this.jugadorElegido, this.cartaAdivinada);

    }

    public void viendoCarta(Carta carta) {
        this.cartaViendo = carta;
        this.estadoActual = EstadosJugador.VIENDOCARTA;
    }

    public void terminarDeVer() {
        if (cartaDescartada == null || cartaDescartada.getTipo() == CartaTipo.BARON)
            jugador.salaActual.eventos.removerObservador(EventosPartida.VIENDOCARTA, jugador);
        else
            this.jugador.rondaJugando.onFinalizarDescarte(this.jugador);

    }

    public Carta getCartaDescartada() {
        return cartaDescartada;
    }

    public JugadorServer getJugadorElegido() {
        return jugadorElegido;
    }

    public CartaTipo getCartaAdivinada() {
        return cartaAdivinada;
    }

    public Carta getCartaViendo() {
        return cartaViendo;
    }

    public void resetElecciones() {
        this.cartaDescartada = null;
        this.jugadorElegido = null;
        this.cartaAdivinada = null;
        this.cartaViendo = null;
    }

    public Jugador getJugador() {
        return jugador;
    }

    private void enviarMensaje(String mensaje) {
        jugador.salaActual.chat.nuevoMensaje(mensaje);
    }

}
