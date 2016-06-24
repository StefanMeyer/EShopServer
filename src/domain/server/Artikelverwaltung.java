package domain.server;

import java.io.IOException;
import java.util.*;

import domain.server.exceptions.ArtikelExistiertBereitsException;
import domain.server.exceptions.ArtikelExistiertNichtException;
import domain.server.exceptions.BestandUeberschrittenException;
import persistence.server.FilePersistenceManager;
import persistence.server.PersistenceManager;
import valueobjects.Artikel;

/**
 * Klasse zur Verwaltung von Artikeln.
 *
 * 
 */

public class Artikelverwaltung {

	// Verwaltung des Artikelbestands als Liste
	// Als Implementierung der Liste dient ein Vektor

	private List<Artikel> artikelBestand = new ArrayList<>(); //TODO: siehe Accountverwaltung

	
	// Persistenz-Schnittstelle, die fuer die Details des Dateizugriffs
	// verantwortlich ist
	
	private PersistenceManager pm = new FilePersistenceManager(); //TODO: siehe Accountverwaltung

	public Artikelverwaltung() {
		//TODO: siehe Accountverwaltung
	}

	/**
	 * Methode zum Einlesen von Artikeldaten aus einer Datei.
	 * 
	 * @param datei Datei, die einzulesenden Artikelbestand enthaelt
	 * @throws IOException
	 */

	public void liesDaten(String datei) throws IOException {
		// PersistenzManager fuer Lesevorgaenge oeffnen
		pm.openForReading(datei);

		Artikel einArtikel;
		do {
			// Artikel-Objekt einlesen
			einArtikel = pm.ladeArtikel();

			if (einArtikel != null) {
				// Artikel in Liste einfuegen
				try {
					einfuegen(einArtikel);
				} catch (ArtikelExistiertBereitsException e1) {
					//
					// Kann hier eigentlich nicht auftreten,
					// daher auch keine Fehlerbehandlung...
					//
				}
			}
		} while (einArtikel != null);

		// Persistenz-Schnittstelle wieder schliessen
		pm.close();
	}
	
	/**
	 * Methode zum Schreiben der Artikeldaten in eine Datei.
	 * 
	 * @param datei Datei, in die der Artikelbestand geschrieben werden soll
	 * @throws IOException
	 */

	public void schreibeArtikel(String datei) throws IOException {
		
	}

	/**
	 * Methode, die einen Artikel an das Ende der Artikeliste einfuegt.
	 * 
	 * @param einArtikel der einzufuegende Artikel
	 * @throws ArtikelExistiertBereitsException wenn der Artikel bereits existiert
	 */

	//TODO Ereignisse hinzufuegen (extra txt-Datei), z.B. bei Bestand erhaelht usw. nach Tag filtern ...
	
	public void einfuegen(Artikel einArtikel)
			throws ArtikelExistiertBereitsException {

		// Vergleich der Artikelnummern aller vorhandenen Artikel mit der Nummer
		// des neu anzulegenden Artikels (testArtikel <-> einArtikel)

		for (Artikel testArtikel : artikelBestand) {
			if (testArtikel.getNummer() == einArtikel.getNummer()) {
				throw new ArtikelExistiertBereitsException();
			}
		}
		artikelBestand.add(einArtikel);
	}

	/**
	 * Methode die einen Artikel entfernt
	 * 
	 * @param gesuchteNummer die eingegebene Artikelnummer
	 * @return 
	 */

	public boolean entfernen(int gesuchteNummer) {

		for (Artikel testArtikel : artikelBestand) {
			if (testArtikel.getNummer() == gesuchteNummer) {
				artikelBestand.remove(testArtikel);
				return true;
			}
		}
		return false;
	}

	/**
	 * Methode, um den Bestand eines bereits existierenden Artikels zu �ndern.
	 * 
	 * @param artklNummer eingegebene Artikelnummer
	 * @param neuerBestand geaenderter Bestand
	 * @return
	 */

	public int aendereBestand(int artklNummer, int neuerBestand)
			throws ArtikelExistiertNichtException {

		for (Artikel testArtikel : artikelBestand) {
			if (testArtikel.getNummer() == artklNummer) {
				testArtikel.setBestand(neuerBestand);
				return testArtikel.getBestand();
			}
		}

		throw new ArtikelExistiertNichtException(artklNummer);

	}
	/** �ndert die Datein eines artikels
	 * 
	 * @param artikelname
	 * @param artikelnummer
	 * @param bestand
	 * @param preis
	 * @param packungsgroesse
	 */
	public void aendereArtikel(String artikelname, int artikelnummer, int bestand, float preis, int packungsgroesse) {		
		for (Artikel testArtikel : artikelBestand) {
			if (testArtikel.getNummer() == artikelnummer) {
				testArtikel.setArtname(artikelname);
				testArtikel.setPreis(preis);
				//TODO umwandeln von normalen artikel in masengutartikel
			/*	if (packungsgroesse > 0 ) {
					testArtikel.setMassengut(true);	
					testArtikel.setPackungsgroesse(packungsgroesse);
				} */
				testArtikel.setBestand(bestand);				
			}
		}			
	}
	/**
	 * Methode, die anhand eines Titels nach Artikeln sucht. Es wird eine Liste
	 * von Artikeln zurueckgegeben, welche mit dem exakten Titel uebereinstimmt.
	 * 
	 * @param artname Titel des gesuchten Artikels
	 * @return Liste der Artikel mit gesuchtem Titel (evtl. leer)
	 * 
	 */

	public List<Artikel> sucheArtikel(String artname) {
		List<Artikel> suchErgebnis = new ArrayList<>();
		String suchBegriff = artname.toLowerCase();

		for (Artikel artikel: getArtikelBestand()) {
			if (artikel.getName().toLowerCase().contains(suchBegriff)
					|| artikel.getArtname().toLowerCase().contains(suchBegriff)) {

				suchErgebnis.add(artikel);
			}
		}

		return suchErgebnis;
	}

	/**
	 * Methode, die den Artikelbestand als Vector zurueckgibt.
	 * 
	 * @return Liste aller Artikel im Artikelbestand
	 */
	public List<Artikel> getArtikelBestand() {
		return artikelBestand;
	}
	
	// nach Artikelnamen sortieren
	public List<Artikel> getSortierteArtikelnamen() {
		Collections.sort(artikelBestand, new Comparator<Artikel>() {
			public int compare(Artikel a1, Artikel a2) {
				return a1.getName().compareTo(a2.getName());
			}
		});
		return artikelBestand;
	}

	
	// nach Artikelnummer sortieren
	public List<Artikel> getSortierteArtikelnummern() {
		Collections.sort(artikelBestand, new Comparator<Artikel>() {
			public int compare(Artikel a1, Artikel a2) {
				return a1.getNummer() - a2.getNummer();
			}
		});
		return artikelBestand;
	}

	/**
	 * Methode zum Suchen der Artikel
	 * 
	 * @param gesuchteNummer
	 * @return
	 * @throws ArtikelExistiertNichtException
	 */

	public Artikel artikelSuchen(int gesuchteNummer)
			throws ArtikelExistiertNichtException {
		for (Artikel testArtikel : artikelBestand) {
			if (testArtikel.getNummer() == gesuchteNummer) {
				return testArtikel;
			}
		}
		throw new ArtikelExistiertNichtException(gesuchteNummer);

	}

	/**
	 * Methode zum Schreiben der Artikeldaten
	 * 
	 * @param datei Datei, in die der Artikelbestand geschrieben werden soll
	 * @throws IOException
	 */
	
	public void schreibeDaten(String datei) throws IOException {
		// PersistenzManager für Schreibvorgänge oeffnen
		pm.openForWriting(datei);

		Iterator<Artikel> iter = artikelBestand.iterator();
		while (iter.hasNext()) {
			Artikel a = iter.next();
			pm.speichereArtikel(a);
		}

		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}


}