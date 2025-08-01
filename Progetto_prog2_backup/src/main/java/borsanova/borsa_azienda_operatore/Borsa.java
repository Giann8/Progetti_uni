package borsanova.borsa_azienda_operatore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import borsanova.Allocazione;
import borsanova.Azione;
import borsanova.StrategiaPoliticaPrezzo;

/**
 * Classe mutabile rappresentante una borsa, iterabile sulle aziende.
 */
public class Borsa implements Iterable<Map.Entry<Azienda, Borsa.Quotazione>>, Comparable<Borsa> {

    /**
     * Nome della borsa attuale
     */
    public final String name;

    /**
     * Lista privata per il salvataggio dei nomi già usati per le borse
     */
    private static final SortedSet<String> USED_NAMES = new TreeSet<>();

    /**
     * La politica di prezzo adottata per questa borsa, se è {@code null}
     * nessuna particolare politica è stata adottata e il prezzo delle azioni
     * non varia in nessun caso.
     */
    private StrategiaPoliticaPrezzo politica = null;

    /**
     * Mappa contenente le quotazioni delle aziende in ordine alfabetico.
     */
    private final Map<Azienda, Quotazione> quotazioniaziende = new HashMap<>();

    /**
     * Mappa delle allocazioni delle azioni agli operatori.
     */
    private final Map<Operatore, List<Allocazione>> operatori = new HashMap<>();


    /*
    * RI:
    * - name != null
    * - politica == null o una delle politiche di prezzo definite in strategieprezzo
    * - quotazioniaziende != null e non contiene duplicati
    * - operatori != null
    * 
    * AF:
    * La classe rappresenta una borsa azionaria in cui:
    * - il nome è rappresentato da name
    * - la politica di prezzo da applicare è rappresentata da politica
    * - le quotazioni delle varie aziende quotate sono contenute in quotazioniaziende
    * - le allocazioni degli operatori sono contenute in operatori
    */

    /**
     * Metodo fabbricatore per la creazione di una borsa.
     *
     * <p>
     * Questo metodo prende un {@code nome}, che deve essere non {@code NULL} e
     * non vuoto, come parametro e , dopo aver controllato la sua presenza nella
     * lista {@code USED_NAMES}, crea una {@code Borsa} se il nome non è stato
     * utilizzato o lancia un'{@code IllegalArgumentException} altrimenti.
     *
     * @param name il nome della borsa.
     * @return la borsa creata.
     * @throws IllegalArgumentException se il nome è nullo, vuoto o già
     * utilizzato.
     */
    public static Borsa of(final String name) throws IllegalArgumentException {
        if (Objects.requireNonNull(name, "Name must not be null.").isBlank()) {
            throw new IllegalArgumentException("Name must not be empty.");
        }
        if (USED_NAMES.contains(name)) {
            throw new IllegalArgumentException("Name for Borsa already used.");
        }
        USED_NAMES.add(name);
        return new Borsa(name);
    }

    /**
     * Costruttore di default
     *
     * @param name il nome della borsa.
     */
    private Borsa(String name) {
        this.name = name;
    }

    /**
     * Metodo pubblico per la modifica della {@link Borsa#politica}.
     *
     * @param politica la politica di prezzo che si vuole usare per questa
     * {@code borsa}.
     */
    public void changePolitica(StrategiaPoliticaPrezzo politica) {
        this.politica = politica;
    }

    /**
     * Inner class mutabile rappresentante la quotazione di una {@code Azienda}
     * nella {@code borsa} corrente
     */
    public class Quotazione {

        /**
         * Azienda a cui appartiene la quotazione
         */
        private final Azienda azienda;

        /**
         * Valore corrente della quotazione
         */
        private int valore;

        /**
         * Quantità delle azioni emesse
         */
        private final int numerototaleazioni;

        /**
         * Numero azioni disponibili, inizialmente è uguale a
         * {@code numerototaleazioni}
         */
        private int numeroazionidisponibili;
        
        /*
         * RI:
         * - azienda != null
         * - valore >= 1
         * - numerototaleazioni > 0
         * - numeroazionidisponibili >= 0
         * 
         * AF:
         * La classe rappresenta una quotazione di un'azienda in cui:
         * - l'azienda è rappresentata da azienda
         * - il valore della quotazione è rappresentato da valore
         * - il numero totale di azioni emesse è rappresentato da numerototaleazioni 
         * - il numero di azioni disponibili all'acquisto è rappresentato da numeroazionidisponibili
         */
        

        /**
         * Costruttore di default
         *
         * @param azienda l'azienda a cui è associata la quotazione
         * @param valore il valore della quotazione
         * @param numerototaleazioni il numero di azioni emesse
         *
         * @throws NullPointerException se l'azienda è nulla.
         */
        public Quotazione(Azienda azienda, int valore, int numerototaleazioni) throws NullPointerException {
            Objects.requireNonNull(azienda, "L'azienda non può essere nulla");

            this.azienda = azienda;
            this.valore = valore;
            this.numerototaleazioni = numerototaleazioni;
            this.numeroazionidisponibili = numerototaleazioni;
        }

        /**
         * Metodo per la restituzione del nome dell'azienda a cui appartiene la
         * quotazione.
         *
         * @return il nome dell'azienda a cui appartiene la quotazione.
         */
        public String geNomeAzienda() {
            return azienda.name;
        }

        /**
         * Metodo che restituisce il valore della quotazione corrente.
         *
         * @return il valore della quotazione.
         */
        public int getValoreQuotazione() {
            return valore;
        }

        /**
         * Restituisce il numero di azioni emesse.
         *
         * @return il numero di azioni totali emesse.
         */
        public int getNumeroAzioniEmesse() {
            return numerototaleazioni;
        }

        /**
         * Metodo per la restituzione del numero di azioni correntemente
         * disponibili.
         *
         * @return il numero di azioni disponibili.
         */
        public int getNumeroAzioniAcquistabili() {
            // cambiare in getRestanti
            return numeroazionidisponibili;
        }

        /**
         * Metodo privato per la modifica del {@code numero di azioni}
         * disponibili.
         *
         * @param numero il nuovo numero di azioni disponibili.
         *
         * @throws IllegalArgumentException se {@code numero} è negativo.
         */
        private void setNumeroAzioni(int numero) throws IllegalArgumentException {
            if (numero < 0) {
                throw new IllegalArgumentException("Il numero di azioni non può essere negativo");
            }
            this.numeroazionidisponibili = numero;
        }

        /**
         * Metodo privato per la modifica del {@code valore} della
         * {@code quotazione}.
         *
         * @param nuovovalore il nuovo valore da associare a {@code quotazione}
         */
        private void setValoreAzioni(int nuovovalore) {

            this.valore = nuovovalore;
        }
    }

    /**
     * Metodo per la gestione della vendita di un'azione.
     *
     * <p>
     * Questo metodo controlla che il numero di azioni che {@code operatore}
     * vuole vendere sia minore o uguale al numero di azioni attualmente
     * disponibili, effettua poi la vendita effettiva, andando a modificare il
     * valore della quotazione di {@code azienda} e restituendo {@code True} se
     * non vi sono più {@code allocazioni} di tale azione associate
     * all'operatore dato o {@code false} altrimenti.
     * </p>
     *
     * @param azienda - l'azienda a cui appartengono la o le azioni
     * @param numero - quantità delle azioni da vendere.
     * @param operatore - nome dell'operatore che sta vendendo la/le azione/i.
     *
     * @return true se un operatore termina le allocazioni dell'azione venduta,
     * false altrimenti.
     *
     * @throws IllegalArgumentException se l'operatore non possiede azioni nella
     * borsa attuale o se il numero di azioni da vendere è maggiore di quelle
     * possedute dall'operatore.
     *
     * @throws NullPointerException se {@code azienda} o {@code operatore} sono
     * nulli.
     */
    public Boolean venditaAzione(Azienda azienda, int numero, Operatore operatore)
            throws IllegalArgumentException, NullPointerException {

        if (Objects.isNull(operatore) || Objects.isNull(azienda)) {
            throw new NullPointerException("Azienda e operatore non possono essere nulli");
        }

        Quotazione quotazione = quotazioniaziende.get(azienda);
        if (!operatori.containsKey(operatore) || operatori.get(operatore).isEmpty()
                || operatori.get(operatore) == null) {
            throw new IllegalArgumentException("L'operatore non possiede azioni in questa borsa.");
        }

        Allocazione allocazione = getAllocazione(operatore, azienda);
        if (numero > allocazione.numeroazioni()) {
            throw new IllegalArgumentException("Il numero di azioni da vendere è maggiore di quelle possedute");
        }

        quotazione.setNumeroAzioni(quotazione.getNumeroAzioniAcquistabili() + numero);
        operatore.modificaBudget(operatore.getBudget() + numero * quotazione.getValoreQuotazione());

        if (politica != null) {
            quotazione.setValoreAzioni(
                    politica.modificaPrezzoVendita(quotazione.getValoreQuotazione(), allocazione.azione(), numero));
        }


        if (allocazione.numeroazioni() == numero) {
            operatori.get(operatore).remove(allocazione);
            operatore.rimuoviAzione(allocazione.azione());
            return true;
        } else {
            operatori.get(operatore).add(new Allocazione(allocazione.operatore(), allocazione.azione(),
                    allocazione.numeroazioni() - numero));
            operatori.get(operatore).remove(allocazione);
            return false;
        }
    }

    /**
     * Metodo per la gestione dell'acquisto di {@code Azione}.
     *
     * <p>
     * Il metodo identifica il valore e la quantità di azioni acquistabili
     * dall'operatore, in caso di esito positivo modifica in
     * {@link Borsa#operatori} l'allocazione dell'azione acquistata aumentandone
     * la quantità. Inoltre il metodo modifica il valore della quotazione
     * attraverso l'uso di una {@code politica di prezzo}.
     * </p>
     *
     * @param azienda - l'azienda di cui viene comprata l'azione.
     * @param prezzototale - il prezzo massimo pagabile dall'operatore.
     * @param operatore - l'operatore che effettua l'acquisto.
     *
     *
     * @throws IllegalArgumentException se {@code prezzototale} è minore del
     * valore della quotazione dell'azienda o se il numero di azioni emesse è
     * {@code 0}
     * @throws NullPointerException se {@code azienda} o {@code operatore} sono
     * nulli.
     */
    public void acquistoAzione(Azienda azienda, int prezzototale, Operatore operatore)
            throws IllegalArgumentException, NullPointerException {


        if (azienda == null || operatore == null) {
            throw new NullPointerException("Azienda e operatore non devono essere nulli");
        }

        Quotazione quotazione = quotazioniaziende.get(azienda);

        if (quotazione == null) {
            throw new IllegalArgumentException("L'azienda richiesta non è quotata in questa borsa");
        }

        int azionipossedute = 0;
        int numeroazioniacquistabili;

        if (prezzototale < quotazione.getValoreQuotazione()) {
            throw new IllegalArgumentException("Il prezzo inserito è troppo basso");
        }
        if (quotazione.getNumeroAzioniAcquistabili() == 0) {
            throw new IllegalArgumentException("Non ci sono azioni disponibili");
        }

        if (operatori.get(operatore) == null) {
            operatori.put(operatore, new ArrayList<>());
        }

        numeroazioniacquistabili = prezzototale / quotazione.getValoreQuotazione();

        if (quotazione.getNumeroAzioniAcquistabili() <= numeroazioniacquistabili) {
            numeroazioniacquistabili = quotazione.getNumeroAzioniAcquistabili();
        }

        for (Allocazione allocazione : operatori.get(operatore)) {
            if (allocazione.azione().getAzienda().equals(azienda)) {
                operatori.get(operatore).remove(allocazione);
                azionipossedute = allocazione.numeroazioni();
                break;
            }
        }

        operatori.get(operatore).add(new Allocazione(operatore, new Azione(azienda, this), numeroazioniacquistabili + azionipossedute));

        operatore.modificaBudget(operatore.getBudget() - quotazione.getValoreQuotazione() * numeroazioniacquistabili);

        Azione azione = new Azione(azienda, this);

        if(!operatore.hasAzione(azione))
            operatore.aggiungiAzione(azione);

        quotazione.setNumeroAzioni(quotazione.getNumeroAzioniAcquistabili() - numeroazioniacquistabili);

        if (politica != null) {
            quotazione.setValoreAzioni(politica.modificaPrezzoAcquisto(quotazione.getValoreQuotazione(),
                    new Azione(azienda, this), numeroazioniacquistabili));
        }

    }

    /**
     * Metodo protetto per l'aggiunta delle quotazioni di ogni azienda
     *
     * <p>
     * Questo metodo prende un'{@code azienda} non nulla e dati {@code numero}
     * di azioni da emettere e {@code prezzo}, crea la sua quotazione e la salva
     * in {@code quotazioniaziende}.
     * </p>
     *
     * @param azienda l'azienda quotata.
     * @param numero numero delle azioni da emettere.
     * @param prezzo prezzo unitario delle azioni emesse.
     *
     * @return l'azione emessa dalla borsa.
     *
     * @throws NullPointerException se l'azienda data è nulla.
     * @throws IllegalArgumentException se il numero di azioni da emettere o il
     * loro prezzo è negativo
     */
    public Azione quotaAzienda(Azienda azienda, int numero, int prezzo) throws NullPointerException {

        if (Objects.isNull(azienda)) {
            throw new NullPointerException("L'azienda non può essere nulla");
        }
        if (numero < 0) {
            throw new IllegalArgumentException("Il numero di azioni non può essere negativo");
        }
        if (prezzo < 0) {
            throw new IllegalArgumentException("Il prezzo unitario non può essere negativo");
        }

        quotazioniaziende.put(azienda, new Quotazione(azienda, prezzo, numero));
        azienda.addBorsa(this);

        return new Azione(azienda, this);
    }

    /**
     * Restituisce la quotazione di una specifica {@code Azienda}.
     *
     * @param azienda l'azienda di cui si vuole conoscere la quotazione.
     * @return la quotazione dell'azienda.
     */
    public Quotazione getQuotazione(Azienda azienda) {
        return quotazioniaziende.get(azienda);
    }

    /**
     * Restituisce una copia di una specifica lista di allocazioni.
     *
     * @param operatore l'operatore a cui appartiene l'allocazione.
     * @return la lista delle azioni allocate.
     *
     * @throws IllegalArgumentException se l'operatore non possiede azioni in
     * questa borsa.
     */
    public List<Allocazione> getListaAllocazioni(Operatore operatore) throws IllegalArgumentException {
        if (!operatori.containsKey(operatore) || operatori.get(operatore).isEmpty()
                || operatori.get(operatore) == null) {
            throw new IllegalArgumentException("L'operatore non possiede azioni in questa borsa.");
        }
        return new ArrayList<>(operatori.get(operatore));
    }

    /**
     * Metodo che restituisce una specifica allocazione di un'operatore data
     * un'azienda
     *
     * @param operatore l'operatore a cui appartiene l'allocazione, non può
     * essere nullo.
     * @param azienda l'azienda alla quale fanno riferimento le azioni.
     * @return L'allocazione dell'operatore che presenta azioni dell'azienda
     * data
     *
     * @throws IllegalArgumentException se l'operatore non possiede azioni in
     * questa borsa.
     * @throws NullPointerException se l'operatore o l'azienda sono nulli.
     */
    public Allocazione getAllocazione(Operatore operatore, Azienda azienda) throws IllegalArgumentException, NullPointerException {
        if (Objects.isNull(operatore) || Objects.isNull(azienda)) {
            throw new NullPointerException("Operatore e azienda non possono essere nulli");
        }

        if (!operatori.containsKey(operatore) || operatori.get(operatore).isEmpty()
                || operatori.get(operatore) == null) {
            throw new IllegalArgumentException("L'operatore non possiede azioni in questa borsa.");
        }
        for (Allocazione allocazione : operatori.get(operatore)) {
            if (allocazione.azione().getAzienda().equals(azienda)) {
                return allocazione;
            }
        }
        return null;
    }

    @Override
    public Iterator<Map.Entry<Azienda, Quotazione>> iterator() {
        return new TreeMap<>(quotazioniaziende).entrySet().iterator();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Borsa other)) {
            return false;
        }
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int compareTo(Borsa other) {
        return name.compareTo(other.name);
    }

}
