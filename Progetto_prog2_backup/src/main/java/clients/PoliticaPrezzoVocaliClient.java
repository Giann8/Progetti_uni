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

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import borsanova.Azione;
import borsanova.borsa_azienda_operatore.Azienda;
import borsanova.borsa_azienda_operatore.Borsa;
import borsanova.borsa_azienda_operatore.Operatore;
import borsanova.strategieprezzo.Vocali;

/**
 * Client di test per alcune funzionalità relative alle <strong>borse</strong>.
 */
public class PoliticaPrezzoVocaliClient {

    /**
     * .
     */
    private PoliticaPrezzoVocaliClient() {
    }

    /*-
   * Scriva un [@code main} che riceve come parametri sulla linea di comando
   *
   *      nome_borsa lettera nome_operatore budget_iniziale
   *
   * il secondo parametro è una lettera che determina la politica di prezzo
   * della borsa come segue: le azioni "coinvolte" sono quelle la cui azienda o
   * borsa hanno un nome che inizia per vocale o coincide (a meno di maiuscole o
   * minuscole) con tale lettera; la politica è che il prezzo delle azioni
   * "coinvolte" raddoppia se acquistate e viene diviso per due (ma senza
   * scendere mai sotto 1) se vendute.
   *
   * Il programma quindi procede esattamente come nel caso della classe
   * PoliticaPrezzoClient, ossia: legge dal flusso in ingresso una sequenza di
   * due gruppi di linee (separati tra loro dalla linea contenente solo --)
   * ciascuno della forma descritta di seguito:
   *
   *     nome_azienda numero prezzo_unitario
   *     ...
   *     --
   *     b nome_azienda prezzo_totale
   *     ... [oppure]
   *     s nome_azienda numero_azioni
   *
   * in base al contenuto del primo blocco, quota le azioni delle aziende
   * specificate nella borsa (definita dal primo parametro sulla linea di
   * comando) secondo il numero e prezzo unitario specificati, in base al
   * contenuto del secondo blocco — una volta creato un operatore (di nome
   * e budget iniziale specificati dal terzo e quarto parametro sulla
   * linea di comando) — esegue le operazioni a seconda che il carattere che
   * segue il nome dell'operatore sia:
   *
   * - b compra azioni (dell'azienda specificata, impegnano il prezzo totale
   *   specificato),
   * - s vende azioni (dell'azienda specificata, nel numero specificato).
   *
   * Al termine della lettura il programma emette nel flusso d'uscita l'elenco
   * delle azioni (in ordine alfabetico) seguite dal prezzo (separato da una
   * virgola).
     */
    public static void main(String[] args) {
        Operatore operatore = Operatore.of(args[2], Integer.parseInt(args[3]));
        Borsa borsa = Borsa.of(args[0]);

        List<Azienda> aziende = new ArrayList<>();

        borsa.changePolitica(new Vocali(args[1]));

        try (Scanner s = new Scanner(System.in)) {
            int i = 0;
            while (s.hasNextLine()) {
                String riga = s.nextLine();
                if (riga.equals("--")) {
                    i++;
                    continue;
                }
                String[] input = riga.split(" ");
                switch (i) {
                    case 0 -> {
                        Azienda azienda = getAzienda(input[0], aziende);
                        azienda.quotaInBorsa(borsa, Integer.parseInt(input[1]),
                                Integer.parseInt(input[2]));
                        break;
                    }
                    case 1 -> {
                        switch (input[0].charAt(0)) {
                            case 'b' -> {
                                operatore.compraAzione(borsa, getAzienda(input[1], aziende), Integer.parseInt(input[2]));
                                break;
                            }
                            case 's' -> {
                                operatore.vendiAzione(borsa, getAzienda(input[1], aziende), Integer.parseInt(input[2]));
                                break;
                            }
                        }
                        break;
                    }
                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        for (Azione azione : operatore) {
            System.out.println(azione.getAzienda().name + ", " + azione.getPrezzoCorrente());

        }
    }

    private static Azienda getAzienda(String nome, List<Azienda> aziende) {
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
}
