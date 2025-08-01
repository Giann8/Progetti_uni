package borsanova.strategieprezzo;

import borsanova.Azione;

/**
 * Classe immutabile per la definizione della politica di decremento costante.
 * 
 * <p> Questa classe estende la classe {@link VariazioneCostante}
 */
public class DecrementoCostante extends VariazioneCostante {

    /**
     * Costruttore di default per la classe {@code DecrementoCostante}.
     * @param costante costante con cui decrementare il prezzo.
     */
    public DecrementoCostante(int costante) {
        super(costante);
    }

    @Override
    public int modificaPrezzoAcquisto(int prezzo, Azione azione, int numeroazioniacquistate) {
        return prezzo;
    }
}
