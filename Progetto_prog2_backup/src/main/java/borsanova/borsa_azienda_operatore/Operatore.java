package borsanova.borsa_azienda_operatore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import borsanova.Azione;

/**
 * classe rappresentante un operatore e contenente il {@code nome} e il
 * {@code budget} dell'operatore.
 *
 * <p>
 * L'operatore può depositare o prelevare denaro dal proprio budget, può
 * comprare o vendere azioni di un'azienda in una borsa specifica.
 * </p>
 */
public class Operatore implements Iterable<Azione>, Comparable<Operatore> {

    /**
     * Set contenente i nomi degli operatori già utilizzati.
     */
    private static final SortedSet<String> USED_NAMES = new TreeSet<>();

    /**
     * Nome dell'operatore.
     */
    public final String name;

    /**
     * Budget operatore, inizialmente inizializzato a zero.
     */
    private int budget = 0;

    /**
     * Lista delle azioni attualmente possedute.
     */
    private final Set<Azione> azioni = new TreeSet<>();


    /*
     * RI:
     * - name != null
     * - budget >=0
     * - azioni != null e senza valori duplicati
     *
     * AF:
     * La classe rappresenta un operatore azionario in cui:
     * - il nome è rappresentato da name
     * - il denaro posseduto è rappresentato da budget
     * - azioni contiene le azioni correntemente possedute dall'operatore 
     */



    /**
     * Metodo che permette di creare un'istanza di operatore.
     *
     * @param name nome dell'operatore.
     * @param budget budget dell'operatore.
     * @return l'operatore creato.
     * @throws NullPointerException se il nome è nullo.
     * @throws IllegalArgumentException se il nome è vuoto.
     */
    public static Operatore of(final String name, int budget) throws NullPointerException, IllegalArgumentException {
        if (Objects.requireNonNull(name, "Name must not be null.").isBlank()) {
            throw new IllegalArgumentException("Name must not be empty.");
        }
        if (USED_NAMES.contains(name)) {
            throw new IllegalArgumentException("Name already used.");
        }
        USED_NAMES.add(name);
        return new Operatore(name, budget);
    }

    /**
     * Costruttore privato per la creazione di un operatore.
     *
     * @param nome Nome dell'operatore.
     * @param budget Budget iniziale dell'operatore.
     * @throws NullPointerException se il nome è nullo.
     * @throws IllegalArgumentException se il nome è vuoto o se il budget è negativo.
     */
    private Operatore(String nome, int budget) throws NullPointerException, IllegalArgumentException {
        if (Objects.requireNonNull(nome).isBlank()) {
            throw new IllegalArgumentException("Il nome dell'operatore non può essere nullo");
        }
        if (budget < 0) {
            throw new IllegalArgumentException("Il budget non può essere negativo");
        }
        this.name = nome;
        this.budget = budget;
    }

    /**
     * restituisce il budget corrente.
     *
     * @return il budget corrente dell'operatore.
     */
    public int getBudget() {
        return budget;
    }

    /**
     * Permette di depositare denaro per aumentare il {@code budget} corrente.
     *
     * @param denaro denaro da depositare.
     * @throws IllegalArgumentException se il denaro da depositare è un numero
     * negativo.
     */
    public void deposito(int denaro) throws IllegalArgumentException {
        if (denaro < 0) {
            throw new IllegalArgumentException("Il denaro depositato non può essere negativo");
        }
        this.budget += denaro;
    }

    /**
     * Permette di ritirare denaro dal {@code budget} corrente.
     *
     * @param denaro denaro da ritirare.
     * @throws IllegalArgumentException se si ritira una somma di denaro
     * maggiore del budget oppure se la somma richiesta è un numero negativo.
     */
    public void prelievo(int denaro) throws IllegalArgumentException {
        if (denaro < 0) {
            throw new IllegalArgumentException("Il denaro da prelevare non può essere negativo");
        }
        if (budget < denaro) {
            throw new IllegalArgumentException("Non puoi prelevare più di quanto hai nel budget");
        }
        this.budget -= denaro;
    }

    /**
     * Restituisce il valore totale dato dal {@code valore delle azioni}
     * possedute e dal {@code budget} corrente
     *
     * @return il capitale totale corrente
     */
    public int getCapitaleTotale() {
        return getBudget() + getTotaleAzioni();
    }

    /**
     * Metodo per l'acquisto delle azioni di un'azienda in una borsa specifica
     *
     * <p>
     * Il metodo vende un certo numero di azioni Aggiunge alla lista delle
     * azioni possedute dall'operatore la o le azioni acquistate.
     * </p>
     *
     * @param borsa - la borsa.
     * @param azienda - l'azienda quotata nella borsa.
     * @param prezzototale - prezzo che si vuole pagare.
     * @throws IllegalArgumentException se il prezzo totale inserito è negativo
     * o se il budget è troppo basso.
     *
     * @throws NullPointerException se l'azienda o la borsa sono nulli.
     */
    public void compraAzione(Borsa borsa, Azienda azienda, int prezzototale)
            throws IllegalArgumentException, NullPointerException {


        if (azienda == null || borsa == null) {
            throw new NullPointerException("Azienda e borsa non possono essere nulli");
        }

        if (prezzototale < 0) {
            throw new IllegalArgumentException("Il prezzo totale non può essere negativo");
        }
        if (budget < prezzototale) {
            throw new IllegalArgumentException("budget troppo basso per " + this.name + ": " + this.getBudget());
        }

        borsa.acquistoAzione(azienda, prezzototale, this);

    }

    /**
     * Metodo per la vendita di azioni di un'azienda in una borsa specifica.
     *
     * <p>
     * Con questo metodo l'operatore può vendere un certo numero di azioni
     * appartenenti ad un'azienda quotata in una certa borsa.
     * </p>
     *
     * @param borsa - borsa in cui vogliamo acquistare le azioni.
     * @param azienda - azienda a cui appartengono le azioni.
     * @param numeroazioni - numero di azioni da vendere.
     *
     * @throws IllegalArgumentException se il numero di azioni da vendere è
     * negativo.
     * @throws NullPointerException se l'azienda o la borsa sono nulli.
     */
    public void vendiAzione(Borsa borsa, Azienda azienda, int numeroazioni)
            throws IllegalArgumentException, NullPointerException {
        if (azienda == null || borsa == null) {
            throw new NullPointerException("Azienda e borsa non possono essere nulli");
        }

        Azione azione = new Azione(azienda, borsa);

        if (numeroazioni < 0) {
            throw new IllegalArgumentException("Il numero di azioni da vendere non può essere negativo");
        }

        if (!azioni.contains(azione)) {
            throw new IllegalArgumentException(
                    "L'operatore non possiede azioni di questa azienda nella borsa specificata");
        }

        borsa.venditaAzione(azienda, numeroazioni, this);

    }

    /**
     * Delega di {@link Set#contains(Object)}
     *
     * @param azione l'azione da controllare.
     * @return true se l'operatore possiede l'azione, false viceversa.
     */
    public boolean hasAzione(Azione azione) {
        return azioni.contains(azione);
    }

    /**
     * Metodo che restituisce il valore totale delle azioni possedute
     * dall'operatore.
     *
     * @return il valore totale delle azioni possedute correntemente.
     */
    public int getTotaleAzioni() {
        int totale = 0;
        for (Azione azione : azioni) {
            totale += azione.getPrezzoCorrente() * azione.getNumeroAzioniPossedute(this);
        }
        return totale;
    }

    /**
     * Metodo protected che permette di aggiungere un'azione alla lista delle
     * azioni.
     *
     * @param azione l'azione da aggiungere.
     * @throws NullPointerException se l'azione è nulla.
     */
    protected void aggiungiAzione(Azione azione) throws NullPointerException {
        Objects.requireNonNull(azione, "l'azione non può essere nulla");
        azioni.add(azione);
    }

    /**
     * Metodo protected che permette di rimuovere un'azione dalla lista delle azioni.
     * @param azione l'azione da rimuovere.
     * @throws NullPointerException se l'azione è nulla.
     */
    protected void rimuoviAzione(Azione azione) throws NullPointerException {
        Objects.requireNonNull(azione, "l'azione non può essere nulla");
        azioni.remove(azione);
    }

    /**
     * Metodo protected che permette di modificare il budget.
     *
     * @param nuovobudget il nuovo budget.
     * @throws IllegalArgumentException se il nuovobudget è negativo.
     */
    protected void modificaBudget(int nuovobudget) throws IllegalArgumentException {
        if (nuovobudget < 0) {
            throw new IllegalArgumentException("Il denaro non può essere negativo");
        }
        budget = nuovobudget;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Operatore other)) {
            return false;
        }
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int compareTo(Operatore other) {
        return name.compareTo(other.name);
    }

    @Override
    public Iterator<Azione> iterator() {
        return new ArrayList<>(azioni).iterator();
    }

}
