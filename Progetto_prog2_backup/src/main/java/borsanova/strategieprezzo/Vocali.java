package borsanova.strategieprezzo;

import borsanova.Azione;
import borsanova.StrategiaPoliticaPrezzo;

/**
 * Classe immutabile per la creazione della politica a variazione costante
 * 
 * <p>
 * La classe implementa l'interfaccia {@link StrategiaPoliticaPrezzo}, permette
 * di raddoppiare in caso di {@code acquisto} e dimezzare in caso di
 * {@code vendita} il prezzo di una {@code azione} se il nome dell'azienda o
 * della borsa da cui è emessa inizia con una vocale o con una
 * {@code lettera} data.
 * </p>
 */
public class Vocali implements StrategiaPoliticaPrezzo {

    /**
     * Lettera data.
     */
    private final String lettera;

    /**
     * Costruttore di default, costruisce una politica {@code Vocali} e inizializza
     * la lettera con cui fare il controllo.
     * 
     * @param lettera la lettera con cui inizia il nome dell'azienda o della borsa.
     */
    public Vocali(String lettera) {
        this.lettera = lettera.toLowerCase();
    }

    @Override
    public int modificaPrezzoAcquisto(int prezzo, Azione azione, int numeroazioniacquistate) {

        if (checkVocale(azione)) {
            return 2 * prezzo;
        }
        return prezzo;
    }

    @Override
    public int modificaPrezzoVendita(int prezzo, Azione azione, int numeroazionivendute) {
        if (checkVocale(azione)) {
            if (prezzo / 2 > 1) {
                return prezzo / 2;
            } else {
                return 1;
            }
        }
        return prezzo;
    }

    /**
     * Metodo per controllare se il nome dell'azienda o della borsa inizia con una
     * vocale o con la {@code lettera} data.
     * 
     * @param azione l'azione da controllare.
     * @return {@code true} se il nome dell'azienda o della borsa inizia con una
     *         vocale o con la {@code lettera} data, {@code false} altrimenti.
     */
    private boolean checkVocale(Azione azione) {
        String nomeAzienda = azione.getAzienda().name.toLowerCase();
        String nomeBorsa = azione.getBorsa().name.toLowerCase();
        return ((nomeAzienda.startsWith("a") || nomeAzienda.startsWith("e") || nomeAzienda.startsWith("i")
                || nomeAzienda.startsWith("o") || nomeAzienda.startsWith("u"))
                || (nomeBorsa.startsWith("a") || nomeBorsa.startsWith("e") || nomeBorsa.startsWith("i")
                        || nomeBorsa.startsWith("o") || nomeBorsa.startsWith("u"))
                || (nomeAzienda.startsWith(lettera) || nomeBorsa.startsWith(lettera)));
    }

}
