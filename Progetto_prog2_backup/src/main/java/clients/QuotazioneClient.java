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

import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import borsanova.borsa_azienda_operatore.Azienda;
import borsanova.borsa_azienda_operatore.Borsa;

/** Client di test per alcune funzionalità relative alle <strong>quotazioni</strong>. */
public class QuotazioneClient {

  /** . */
  private QuotazioneClient() {}

  /*-
   * Scriva un {@code main} che legge dal flusso di ingresso una sequenza di
   * linee della forma
   *
   *    nome_azienda nome_borsa quantità prezzo
   *
   * Assuma che i nomi non contengano spazi. Dopo aver quotato le aziende nelle
   * borse emette nel flusso d'uscita
   *
   * - per ciascuna azienda, l'elenco delle borse in cui è quotata, poi
   * - per ciascuna borsa, l'elenco di aziende in essa quotate;
   *
   * I nomi delle borse e delle aziende devono essere uno per linea, in ordine
   * alfabetico; i nomi di borsa nel primo elenco e i azienda nel secondo devono
   * essere prefissati da "- ".
   */

   public static void main(String[] args) {
    Set<Borsa> borse = new TreeSet<>();
    Set<Azienda> aziende = new TreeSet<>();

    try (Scanner s = new Scanner(System.in)) {
        while (s.hasNext()) {
            Borsa borsa ;
            Azienda azienda ;
            String[] input = s.nextLine().split(" ");
            try {
                borsa = Borsa.of(input[1]);
                borse.add(borsa);
            } catch (IllegalArgumentException e) {
                borsa = getBorsa(input[1], borse);
            }

            try {
                azienda = Azienda.of(input[0]);
                aziende.add(azienda);
            } catch (IllegalArgumentException e) {
                azienda = getAzienda(input[0], aziende);
            }

            azienda.quotaInBorsa(borsa, Integer.parseInt(input[2]), Integer.parseInt(input[3]));

        }

        for (Azienda azienda : aziende) {
            System.out.println(azienda.name);
            for (Borsa borsa : azienda) {
                System.out.println("- " + borsa.name);
            }
        }

        for (Borsa borsa : borse) {
            System.out.println(borsa.name);
            for (Map.Entry<Azienda, Borsa.Quotazione> entry : borsa) {
                System.out.println("- " + entry.getKey().name);
            }
        }

    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
}

private static Borsa getBorsa(String string, Set<Borsa> borse) {
    for (Borsa borsa : borse) {
        if (borsa.name.equals(string)) {
            return borsa;
        }
    }
    return null;
}

private static Azienda getAzienda(String string, Set<Azienda> aziende) {
    for (Azienda azienda : aziende) {
        if (azienda.name.equals(string)) {
            return azienda;
        }
    }
    return null;
}
}
