package borsanova;

import borsanova.borsa_azienda_operatore.Operatore;

/**
 * Record per la rappresentazione di un'allocazione di azioni.
 * 
 * @param operatore    operatore che ha allocato le azioni.
 * @param azione       azione allocata.
 * @param numeroazioni numero di azioni allocate.
 */
public record Allocazione(Operatore operatore, Azione azione, int numeroazioni) {

    /**
     * Costruttore di default
     *
     * @throws IllegalArgumentException se il numero di azioni allocate è negativo.
     */
    public Allocazione {
        if (numeroazioni < 0) {
            throw new IllegalArgumentException("Il numero di azioni allocate non può essere negativo");
        }
    }

}
