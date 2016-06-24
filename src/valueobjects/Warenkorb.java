package valueobjects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Klasse zur Repraesentation des Warenkorbs.
 */

//Ereignisse 
// -> wer
// -> wann
// -> was (Artikel)
// -> ggf. wie viel
// -> typ -> Enumeration (ANGELEGT, BESTAND_ERHOET, ... )

public class Warenkorb implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -624627391885907928L;
	// Verwaltung des Warenkorbbestands als Liste
	private HashMap<Artikel, Integer> inhalt = new HashMap<Artikel, Integer>();


	public void einfuegen(Artikel a, int anzahl) {
		//bevor artikel kaufen: pruefe ob massenartikel
		inhalt.put(a, anzahl);
	}

	public void loeschen(Artikel artikel) {
		inhalt.remove(artikel);
	}

	public void leeren() {
		inhalt.clear();
	}

	public HashMap getInhalt() {
		return inhalt;
	}

	public void setWarenkorbInhalt(HashMap Inhalt) {
		this.inhalt = Inhalt;
	}
}