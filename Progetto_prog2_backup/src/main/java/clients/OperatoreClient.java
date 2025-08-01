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
import java.util.Set;
import java.util.TreeSet;

import borsanova.Azione;
import borsanova.borsa_azienda_operatore.Azienda;
import borsanova.borsa_azienda_operatore.Borsa;
import borsanova.borsa_azienda_operatore.Operatore;

/** Client di test per alcune funzionalità relative agli <strong>operatori</strong>. */
public class OperatoreClient {

  /** . */
  private OperatoreClient() {}

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
   *     ... [oppure]
   *     nome_operatore d valore
   *     ... [oppure] nome_operatore w valore
   *
   * Assuma che i nomi non contengano spazi. In base al contenuto del primo
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
   * - d deposita denaro (secondo il valore specificato),
   * - w preleva denaro (secondo il valore specificato).
   *
   * Osservi che l'acquisto può determinare un resto, nel caso in cui il prezzo
   * totale non sia un multiplo esatto del prezzo dell'azione; tale resto rimane
   * a disposizione dell'operatore per eventuali operazioni successive.
   *
   * Al termine della lettura il programma emette nel flusso d'uscita l'elenco
   * degli operatori coinvolti (in ordine alfabetico) ciascuno dei quali seguito
   * (sulla stessa linea e separato da virgole) dal suo budget finale e dalla
   * somma del valore delle azioni che possiede, ogni operatore è poi seguito
   * dall'elenco delle azioni che possiede, ciascuna azione va descritta
   * emettendo il nome della borsa (in ordine alfabetico, preceduto da -)
   * seguito da quello dell'azienda e dal numero di azioni possedute (separati
   * da virgole).
   */

   public static void main(String[] args) {
    Set<Operatore> operatori = new TreeSet<>();
    List<Azienda> aziende = new ArrayList<>();
    Set<Borsa> borse = new TreeSet<>();
    try (Scanner s = new Scanner(System.in)) {
        int i = 0;
        while (s.hasNextLine()) {
            String riga = s.nextLine();
            if (riga.isEmpty()) {
                break;
            }
            if (riga.equals("--")) {
                i++;
                continue;
            }
            String[] input = riga.split(" ");
            switch (i) {
                case 0 -> {
                    Azienda azienda = getAzienda(input[0], aziende);

                    azienda.quotaInBorsa(getBorsa(input[1], borse), Integer.parseInt(input[2]),
                            Integer.parseInt(input[3]));
                    break;
                }
                case 1 -> {
                    try {
                        operatori.add(Operatore.of(input[0], Integer.parseInt(input[1])));
                    } catch (IllegalArgumentException e) {
                    }
                    break;
                }
                case 2 -> {
                    switch (input[1].charAt(0)) {
                        case 'b' -> {
                            getOperatore(input[0], operatori).compraAzione(getBorsa(input[2], borse),
                                    getAzienda(input[3], aziende),
                                    Integer.parseInt(input[4]));
                            break;
                        }
                        case 's' -> {
                            getOperatore(input[0], operatori).vendiAzione(getBorsa(input[2], borse),
                                    getAzienda(input[3], aziende),
                                    Integer.parseInt(input[4]));
                            break;
                        }
                        case 'd' -> {
                            getOperatore(input[0], operatori).deposito(Integer.parseInt(input[2]));
                            break;
                        }
                        case 'w' -> {
                            getOperatore(input[0], operatori).prelievo(Integer.parseInt(input[2]));
                            break;
                        }
                    }
                    break;
                }
            }
        }

        for (Operatore operatore : operatori) {
            if (operatori.contains(operatore)) {
                System.out.println(
                        operatore.name + ", " + operatore.getBudget() + ", " + operatore.getTotaleAzioni());
                for (Borsa borsa : borse) {
                    for (Azione action : operatore) {
                        if (action.getBorsa().equals(borsa))
                            System.out.println("- " + action.getBorsa().name + ", " + action.getAzienda().name + ", "
                                            + action.getNumeroAzioniPossedute(operatore));
                    }
                }
            }
        }

    } catch (Exception e) {
        System.out.println(e.getMessage());
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

public static Borsa getBorsa(String nome, Set<Borsa> borse) {
    Borsa borsa = null;
    try {
        borsa = Borsa.of(nome);
        borse.add(borsa);
    } catch (IllegalArgumentException e) {
        for (Borsa b : borse) {
            if (b.name.equals(nome)) {
                return b;
            }
        }
    }
    return borsa;
}
}
