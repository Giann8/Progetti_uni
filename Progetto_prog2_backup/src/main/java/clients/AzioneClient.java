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

import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

import borsanova.Azione;
import borsanova.borsa_azienda_operatore.Azienda;
import borsanova.borsa_azienda_operatore.Borsa;

/**
 * Client di test per alcune funzionalità relative alle <strong>azioni</strong>.
 */
public class AzioneClient {

    /**
     * .
     */
    private AzioneClient() {
    }

    /*-
   * Scriva un [@code main} che, ricevuto un nome di borsa come parametro sulla
   * linea di comando, legge dal flusso in ingresso una sequenza di linee della
   * forma
   *
   *     nome_azienda numero prezzo_unitario
   *
   * e dopo aver quotato l'azienda nella borsa specificata come parametro
   * produce le azioni quotate emettendo per ciascuna di esse, nel flusso
   * d'uscita, il nome dell'azienda (in ordine alfabetico) seguito da prezzo e
   * numero (separati da virgole). Assuma che il nome dell'azienda non contenga
   * spazi.
     */
    public static void main(String[] args) {
        SortedSet<Azione> azioni = new TreeSet<>();
        Borsa borsa = Borsa.of(args[0]);
        try (Scanner s = new Scanner(System.in)) {
            while (s.hasNextLine()) {
                String[] input = s.nextLine().split(" ");
                Azienda azienda = Azienda.of(input[0]);
                azioni.add(azienda.quotaInBorsa(borsa, Integer.parseInt(input[1]), Integer.parseInt(input[2])));
            }
        }
        for (Azione azione : azioni) {
            System.out.println(azione.getAzienda().name + ", " + azione.getPrezzoCorrente() + ", " + azione.getNumeroTotaleAzioniEmesse());
        }
    }
}
