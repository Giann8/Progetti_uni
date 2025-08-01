package borsanova.strategieprezzo;

import borsanova.Azione;
import borsanova.StrategiaPoliticaPrezzo;

/**
 * Classe immutabile per la creazione della politica a variazione costante
 * 
 * <p>
 * La classe implementa l'interfaccia {@link StrategiaPoliticaPrezzo}, permette
 * di
 * aumentare o diminuire di un valore {@code costante} il prezzo di una
 * {@code azione}.
 * </p>
 */
public class VariazioneCostante implements StrategiaPoliticaPrezzo {

    /**
     * Costante con cui varia il prezzo.
     */
    private final int costante;

    /**
     * Costruttore di default.
     * 
     * @param costante la costante con la quale varia il prezzo.
     */
    public VariazioneCostante(int costante) {
        this.costante = Math.abs(costante);
    }

    @Override
    public int modificaPrezzoAcquisto(int prezzo, Azione azione, int numeroazioniacquistate) {
        return prezzo + costante;
    }

    @Override
    public int modificaPrezzoVendita(int prezzo, Azione azione, int numeroazionivendute) {
        if (prezzo > costante) {
            return prezzo - costante;
        }
        return 1;
    }

}
