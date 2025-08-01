package borsanova;

import borsanova.borsa_azienda_operatore.Azienda;
import borsanova.borsa_azienda_operatore.Borsa;
import borsanova.borsa_azienda_operatore.Operatore;

/**
 * Record per la rappresentazione di un'azione.
 */
public class Azione implements Comparable<Azione> {

    /**
     * Azienda a cui appartiene l'azione corrente.
     */
    private final Azienda azienda;

    /**
     * Borsa da cui viene emessa l'azione corrente
     */
    private final Borsa borsa;


    /*
     * RI:
     * - azienda != null
     * - borsa != null
     * 
     * AF:
     * La classe rappresenta un'azione in cui:
     * - l'azienda a cui appartiene è rappresentata da azienda
     * - la borsa da cui è emessa è rappresentata da borsa
     */

    /**
     * Costruttore di default.
     *
     * @param azienda azienda a cui appartiene l'azione.
     * @param borsa   borsa dalla quale viene emessa l'azione.
     *
     * @throws NullPointerException se l'azienda o la borsa sono nulli.
     */
    public Azione(Azienda azienda, Borsa borsa) throws NullPointerException, IllegalArgumentException {
        if (azienda == null || borsa == null) {
            throw new NullPointerException("Azienda e borsa non possono essere nulli");
        }
        this.borsa = borsa;
        if (!azienda.quotataIn(borsa)) {
            throw new IllegalArgumentException("Azienda non quotata in questa borsa");
        }
        this.azienda = azienda;
    }

    /**
     * Metodo che restituisce l'azienda a cui appartiene l'azione.
     *
     * @return l'azienda dell'azione corrente.
     */
    public Azienda getAzienda() {
        return azienda;
    }

    /**
     * Metodo che restituisce la borsa dalla quale viene emessa l'azione corrente.
     *
     * @return la borsa dalla quale viene emessa l'azione corrente.
     */
    public Borsa getBorsa() {
        return borsa;
    }

    /**
     * Metodo che restituisce il prezzo corrente dell'azione.
     *
     * @return il prezzo corrente dell'azione, equivale al valore della
     *         {@link Borsa.Quotazione} dell'{@code azienda} in {@code borsa}.
     */
    public int getPrezzoCorrente() {
        return borsa.getQuotazione(this.azienda).getValoreQuotazione();
    }

    /**
     * Metodo che restituisce il numero totale di azioni emesse.
     *
     * @return il numero totale di azioni emesse.
     */
    public int getNumeroTotaleAzioniEmesse() {
        return borsa.getQuotazione(this.azienda).getNumeroAzioniEmesse();
    }

    /**
     * Metodo che restituisce il numero di azioni che possono essere acquistate.
     *
     * @return numero azioni ancora presenti.
     */
    public int getNumeroAzioniAcquistabili() {
        return borsa.getQuotazione(this.azienda).getNumeroAzioniAcquistabili();
    }

    /**
     * Metodo che dato un certo {@code Operatore} restituisce la quantità delle
     * azioni correnti da lui possedute.
     * 
     * <p>
     * Questo metodo fa riferimento a {@link Borsa#getAllocazione} e a
     * {@link Allocazione#numeroazioni}, restituisce {@code 0} se non è stata
     * trovata alcuna allocazione associata all'operatore dato
     * </p>
     * 
     * @param operatore l'operatore
     * @return il numero di azioni allocate correntemente all'operatore.
     * 
     * @throws IllegalArgumentException se non è stato ricevuto alcun operatore.
     */
    public int getNumeroAzioniPossedute(Operatore operatore) throws IllegalArgumentException {
        if (operatore == null)
            throw new IllegalArgumentException("Operatore necessario");
        Allocazione allocazione = borsa.getAllocazione(operatore, this.azienda);
        if (allocazione == null) {
            return 0;
        }
        return allocazione.numeroazioni();
    }

    @Override
    public final boolean equals(Object arg0) {
        if (!(arg0 instanceof Azione other)) {
            return false;
        }
        return azienda.equals(other.azienda) && borsa.equals(other.borsa);
    }

    @Override
    public final int hashCode() {
        return azienda.hashCode() + borsa.hashCode();
    }

    @Override
    public int compareTo(Azione o) {
        return azienda.compareTo(o.azienda) + borsa.compareTo(o.borsa);
    }

}
