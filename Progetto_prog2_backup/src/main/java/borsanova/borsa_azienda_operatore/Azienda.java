package borsanova.borsa_azienda_operatore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

import borsanova.Azione;

/**
 * Classe mutabile per la rappresentazione di un'azienda.
 */
public class Azienda implements Iterable<Borsa>, Comparable<Azienda> {

    /**
     * Lista dei nomi già usati di aziende
     */
    private static final SortedSet<String> USED_NAMES = new TreeSet<>();

    /**
     * Nome dell'azienda.
     */
    public final String name;

    /**
     * Lista contenente i riferimenti alle borse in cui è quotata correntemente
     * l'azienda.
     */
    private final List<Borsa> quotatain = new ArrayList<>();


    /*
     * RI:
     * - name != null
     * - quotatain != null e non contiene duplicati
     * 
     * AF:
     * La classe rappresenta un'azienda in cui:
     * - il nome è rappresentato da name
     * - quotatain contiene le borse in cui è quotata l'azienda
     */

    /**
     * Metodo fabbricatore per la creazione di un'azienda.
     * <p>
     * Questo metodo prende un {@code nome}, che deve essere non {@code NULL} e
     * non vuoto ,come parametro e, dopo aver controllato la sua presenza nella
     * lista {@code USED_NAMES}, crea un' {@code Azienda} se il nome non è stato
     * utilizzato o lancia un' {@code IllegalArgumentException} altrimenti.
     * </p>
     *
     * @param name il nome dell'azienda.
     * @throws IllegalArgumentException se il nome è nullo, vuoto o già
     * utilizzato.
     * @return l'azienda creata.
     */
    public static Azienda of(final String name) throws IllegalArgumentException {
        if (Objects.requireNonNull(name, "Name must not be null.").isBlank()) {
            throw new IllegalArgumentException("Name must not be empty.");
        }
        if (USED_NAMES.contains(name)) {
            throw new IllegalArgumentException("Name for Azienda already used.");
        }
        USED_NAMES.add(name);
        return new Azienda(name);
    }

    /**
     * costruttore privato per la creazione di un'azienda.
     *
     * @param name il nome dell'azienda.
     */
    private Azienda(String name) {
        this.name = name;
    }

    /**
     * metodo che permette di quotare l'azienda in una borsa.
     *
     * <p>
     * Questo metodo permette ad una {@code azienda} di quotarsi in una
     * specifica borsa in cui non è correntemente quotata, più precisamente
     * aggiungerà la borsa scelta alla lista {@code quotatain} e chiamerà il
     * metodo {@link Borsa#quotaAzienda}.
     *
     * @param borsa la borsa in cui si vuole quotare l'azienda.
     * @param numero il numero di azioni da quotare.
     * @param prezzounitario il prezzo delle azioni emesse.
     *
     * @return l'azione emessa.
     *
     * @throws IllegalArgumentException se l'azienda è già quotata nella borsa
     * data, se il numero di azioni da emettere è negativo o se il prezzo
     * unitario è negativo.
     */
    public Azione quotaInBorsa(Borsa borsa, int numero, int prezzounitario) throws IllegalArgumentException {
        if (quotatain.contains(borsa)) {
            throw new IllegalArgumentException("Azienda già quotata in questa borsa");
        }
        if (numero < 0) {
            throw new IllegalArgumentException("Il numero di azioni non può essere negativo");
        }
        if (prezzounitario < 0) {
            throw new IllegalArgumentException("Il prezzo unitario non può essere negativo");
        }
        return borsa.quotaAzienda(this, numero, prezzounitario);
    }

    /**
     * metodo che permette di controllare se la borsa data è presente nella
     * lista delle borse in cui è quotata l'azienda.
     *
     * @param borsa la borsa che si vuole controllare.
     * @return true se l'azienda è quotata nella borsa data, false altrimenti.
     */
    public Boolean quotataIn(Borsa borsa) {
        return quotatain.contains(borsa);
    }

    /**
     * metodo protected per l'aggiunta delle borse in cui è quotata l'azienda.
     *
     * @param borsa la borsa in cui viene quotata l'azienda.
     *
     * @throws NullPointerException se la borsa è nulla.
     */
    protected void addBorsa(Borsa borsa) throws NullPointerException {
        if (borsa == null) {
            throw new NullPointerException("La borsa non può essere nulla");
        }
        quotatain.add(borsa);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Azienda other)) {
            return false;
        }
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int compareTo(Azienda other) {
        return name.compareTo(other.name);
    }

    @Override
    public Iterator<Borsa> iterator() {
        Collections.sort(quotatain);
        return Collections.unmodifiableList(quotatain).iterator();
    }

}
