package borsanova.strategieprezzo;

import borsanova.Azione;
import borsanova.StrategiaPoliticaPrezzo;

/**
 * Classe immutabile per la definizione della politica con soglia.
 * 
 * <p>
 * Questa classe implementa l'interfaccia {@link StrategiaPoliticaPrezzo} e
 * permette di modificare il prezzo dell'azione raddoppiando il valore in caso
 * di {@code acquisto} e dimezzandolo in caso di {@code vendita} se {@code numeroazioniacquistate} o {@code numerozionivendute} è minore di {@code soglia}.
 * </p>
 */
public class PoliticaSoglia implements StrategiaPoliticaPrezzo {

    /** Soglia definita tramite costruttore */
    private final int soglia;

    /**
     * Costruttore di default
     * 
     * @param soglia la soglia data.
     */
    public PoliticaSoglia(int soglia) {
        this.soglia = soglia;
    }

    @Override
    public int modificaPrezzoAcquisto(int prezzo, Azione azione, int numeroazioniacquistate) {
        if (numeroazioniacquistate > soglia) {
            return 2 * prezzo;
        }
        return prezzo;
    }

    @Override
    public int modificaPrezzoVendita(int prezzo, Azione azione, int numeroazionivendute) {
        if (numeroazionivendute > soglia) {
            if (prezzo / 2 > 1) {
                return prezzo / 2;
            } else {
                return 1;
            }
        }
        return prezzo;
    }

}
