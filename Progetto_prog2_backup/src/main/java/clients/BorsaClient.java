/*

Copyright 2024 Massimo Santini

This file is part of "Programmazione 2 @ UniMI" teaching material.

This is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This material is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this file.  If not, see <https://www.gnu.org/licenses/>.

 */
package clients;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import borsanova.Allocazione;
import borsanova.Azione;
import borsanova.borsa_azienda_operatore.Azienda;
import borsanova.borsa_azienda_operatore.Borsa;
import borsanova.borsa_azienda_operatore.Operatore;

/**
 * Client di test per alcune funzionalità relative alle <strong>borse</strong>.
 */
public class BorsaClient {

    /**
     * .
     */
    private BorsaClient() {
    }

    /*-
   * Scriva un [@code main} che legge dal flusso in ingresso una sequenza di tre
   * gruppi di linee (separati tra loro dalla linea contenente solo --) ciascuno
   * della forma descritta di seguito:
   *
   *     nome_azienda nome_borsa numero prezzo_unitario
   *     ...
   *     --
   *     nome_operatore budget_iniziale
   *     ...
   *     --
   *     nome_operatore b nome_borsa nome_azienda prezzo_totale
   *     ... [oppure]
   *     nome_operatore s nome_borsa nome_azienda numero_azioni
   *
   * Assuma che i nomi non contengano spazi. Iqn base al contenuto del primo
   * blocco, quota le azioni delle aziende nelle borse secondo il numero e
   * prezzo unitario specificati, in base al secondo blocco crea gli operatori
   * specificati con il budget iniziale specificato e in base al terzo blocco
   * esegue le operazioni, a seconda che il carattere che segue il nome
   * dell'operatore sia:
   *
   * - b compra azioni (quotate nella borsa e dell'azienda specificata,
   *   impegnano il prezzo totale specificato),
   * - s vende azioni (quotate nella borsa e dell'azienda specificata, nel
   *   numero specificato).
   *
   * Osservi che l'acquisto può determinare un resto, nel caso in cui il prezzo
   * totale non sia un multiplo esatto del prezzo dell'azione; tale resto rimane
   * a disposizione dell'operatore per eventuali operazioni successive.
   *
   * Al termine della lettura il programma emette nel flusso d'uscita (una per
   * linea) l'elenco delle borse coinvolte (in ordine alfabetico), per ogni
   * borsa emette l'elenco delle azioni in essa quotate (in ordine alfabetico,
   * prefissate da - e seguite dal numero di azioni ancora disponibili), e per
   * ognuna di esse i nomi degli operatori e delle quantità che ne possiedono
   * (in ordine alfabetico, prefissati da =).
     */
    public static void main(String[] args) {
        Map<Borsa, Set<Azione>> borse = new TreeMap<>();
        Set<Operatore> operatori = new TreeSet<>();
        Set<Azienda> aziende = new TreeSet<>();

        try (Scanner s = new Scanner(System.in)) {
            int i = 0;
            while (s.hasNextLine()) {
                String riga = s.nextLine();

                if (riga.equals("--")) {
                    i++;
                    continue;
                }
                if (riga.isBlank()) {
                    break;
                }
                String[] input = riga.split(" ");

                switch (i) {
                    case 0 -> {
                        Borsa borsa = getBorsa(input[1], borse);
                        Azienda azienda = getAzienda(input[0], aziende);

                        Azione azione = borsa.quotaAzienda(azienda, Integer.parseInt(input[2]),
                                Integer.parseInt(input[3]));

                        borse.get(borsa).add(azione);
                        break;
                    }

                    case 1 -> {
                        operatori.add(Operatore.of(input[0], Integer.parseInt(input[1])));
                        break;
                    }

                    case 2 -> {
                        switch (input[1].charAt(0)) {
                            case 'b' -> {
                                getBorsa(input[2], borse).acquistoAzione(getAzienda(input[3], aziende), Integer.parseInt(input[4]), getOperatore(input[0], operatori));
                                break;
                            }
                            case 's' -> {
                                getBorsa(input[2], borse).venditaAzione(getAzienda(input[3], aziende), Integer.parseInt(input[4]), getOperatore(input[0], operatori));
                                break;
                            }
                        }
                        break;
                    }
                }
            }

            for (Borsa borsa : borse.keySet()) {
                System.out.println(borsa.name);

                for (Azione azione : borse.get(borsa)) {
                    System.out.println("- " + azione.getAzienda().name + " " + azione.getNumeroAzioniAcquistabili());
                    for (Operatore operatore : operatori) {
                        if (!operatore.hasAzione(azione)) {
                            continue;
                        }
                        List<Allocazione> allocazioni = borsa.getListaAllocazioni(operatore);
                        for (Allocazione allocazione : allocazioni) {
                            if (allocazione.azione().equals(azione)) {
                                System.out.println("= " + operatore.name + " " + allocazione.numeroazioni());
                                break;
                            }
                        }

                    }
                }
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static Operatore getOperatore(String nome, Set<Operatore> operatori) {
        for (Operatore operatore : operatori) {
            if (operatore.name.equals(nome)) {
                return operatore;
            }
        }
        return null;
    }

    public static Azienda getAzienda(String nome, Set<Azienda> aziende) {
        Azienda azienda = null;
        try {
            azienda = Azienda.of(nome);
            aziende.add(azienda);
        } catch (IllegalArgumentException e) {
            for (Azienda a : aziende) {
                if (a.name.equals(nome)) {
                    return a;
                }
            }
        }
        return azienda;
    }

    public static Borsa getBorsa(String nome, Map<Borsa, Set<Azione>> borse) {
        Borsa borsa = null;
        try {
            borsa = Borsa.of(nome);
            borse.put(borsa, new TreeSet<>());
        } catch (IllegalArgumentException e) {
            for (Borsa b : borse.keySet()) {
                if (b.name.equals(nome)) {
                    return b;
                }
            }
        }
        return borsa;
    }
}
