package domain.server;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import domain.server.exceptions.ArtikelExistiertNichtException;
import domain.server.exceptions.StatExistiertBereitsException;
import domain.server.exceptions.StatsExistiertNichtException;
import persistence.server.FilePersistenceManager;
import persistence.server.PersistenceManager;
import valueobjects.Artikel;
import valueobjects.Stats;
import valueobjects.Stats.LagerEreignisTyp;

public class StatsVerwaltung {
	// Verwaltung des Artikelbestands als Liste
	// Als Implementierung der Liste dient ein Vektor

	private List<Stats> alleStats = new Vector<Stats>();

	
	// Persistenz-Schnittstelle, die fuer die Details des Dateizugriffs
	// verantwortlich ist
	
	private PersistenceManager pm = new FilePersistenceManager();

	public StatsVerwaltung() {
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
		Stats eineStat;
		do {
			// Artikel-Objekt einlesen
			eineStat = pm.ladeStats();
			if (eineStat != null) {
				einfuegen(eineStat);
			}
		} while (eineStat != null);
		// Persistenz-Schnittstelle wieder schliessen
		pm.close();
	}
	/**
	 * Methode, die dalle Stats als Vector zurueckgibt.
	 * 
	 * @return Alle Stats als vecotr
	 */
	public List<Stats> getStats() {
		return getSortierteArtikelnummern();
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

		Iterator<Stats> iter = alleStats.iterator();
		while (iter.hasNext()) {
			Stats s = iter.next();
			pm.speichereStats(s);
		}
		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}
	/** F�gt ein Statistikteil hinzu
	 * @param artklnummer Nummer des Artikels	
	 * @param name name des Artikels
	 * @param bestand neuer bestand
	 * @param type Welche art von statistik
	 * 
	 */
	public void statupdate(int artklnummer,String name, int bestand, LagerEreignisTyp type) {
		//statistik daten einf�gen
		//datum als string einf�gen
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date today = Calendar.getInstance().getTime();        
		String datum = df.format(today);
		Stats stat = new Stats(artklnummer,name, bestand,datum,type);
		einfuegen(stat);
	}
	/**
	 * Methode zum Suchen von Statistikteilen
	 * 
	 * @param artikelnummer
	 * @return
	 * @throws ArtikelExistiertNichtException
	 */

	public List<Stats> statsSuchen(int artikelnummer) {
		List<Stats> bestimmtestats = new Vector<Stats>();
		for (Stats eineStat : alleStats) {
			if (eineStat.getArklnummer() == artikelnummer) {
				bestimmtestats.add(eineStat);
			}
		}
		return bestimmtestats;
	}
	// nach Artikelnummer sortieren
	public List<Stats> getSortierteArtikelnummern() {
		Collections.sort(alleStats, new Comparator<Stats>() {
			public int compare(Stats a1, Stats a2) {
				return a1.getArklnummer() - a2.getArklnummer();
			}
		});
		return alleStats;
	}
	
	
	/**
	 * Methode, die eine Statistik an das Ende der Statistikliste einfuegt.
	 * @param einStat die einzuf�gende Statistik
	 */
	public void einfuegen(Stats eineStat) {
		alleStats.add(eineStat);
	}

}
