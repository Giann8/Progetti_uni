package borsanova;

/**
 * Interfaccia per definire le politiche di prezzo da utilizzare nella borsa.
 */
public interface StrategiaPoliticaPrezzo {

    /**
     * Metodo che determina il nuovo prezzo in caso di acquisto
     * 
     * @param prezzo                 prezzo corrente delle azioni
     * @param azione                 azione acquistata
     * @param numeroazioniacquistate numero azioni acquistate
     * @return nuovo prezzo delle azioni
     */
    public int modificaPrezzoAcquisto(int prezzo, Azione azione, int numeroazioniacquistate);

    /**
     * Metodo che determina il nuovo prezzo in caso di vendita
     * 
     * @param prezzo              prezzo corrente delle azioni
     * @param azione              azione venduta
     * @param numeroazionivendute numero azioni vendute
     * @return nuovo prezzo delle azioni
     */
    public int modificaPrezzoVendita(int prezzo, Azione azione, int numeroazionivendute);
}
