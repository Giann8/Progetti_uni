package borsanova.strategieprezzo;

import borsanova.Azione;

/**
 * Classe immutabile per la definizione della politica di incremento costante.
 * 
 * <p>
 * Questa classe estende la classe {@link VariazioneCostante} andando a
 * modificare il metodo {@code modificaPrezzoVendita}.
 * </p>
 */
public class IncrementoCostante extends VariazioneCostante {

    /**
     * Costruttore di default per la classe {@code IncrementoCostante}.
     * 
     * @param costante costante con cui incrementare il prezzo.
     */
    public IncrementoCostante(int costante) {
        super(costante);
    }

    @Override
    public int modificaPrezzoVendita(int prezzo, Azione azione, int numeroazionivendute) {
        return prezzo;
    }

}
