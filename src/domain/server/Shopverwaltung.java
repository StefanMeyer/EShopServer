package domain.server;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import domain.server.Artikelverwaltung;
import domain.server.exceptions.AccountExistiertBereitsException;
import domain.server.exceptions.AccountExistiertNichtException;
import domain.server.exceptions.ArtikelExistiertBereitsException;
import domain.server.exceptions.ArtikelExistiertNichtException;
import domain.server.exceptions.BestandUeberschrittenException;
import domain.server.exceptions.StatExistiertBereitsException;
import valueobjects.Account;
import valueobjects.Artikel;
import valueobjects.Kunde;
import valueobjects.Massengutartikel;
import valueobjects.Mitarbeiter;
import valueobjects.Rechnung;
import valueobjects.Stats;
import valueobjects.Warenkorb;
import valueobjects.Stats.LagerEreignisTyp;

public class Shopverwaltung implements Serializable{
	private static final long serialVersionUID = 8845844992442630247L;
	// Verwaltungsklassen fuer Artikel, Accounts etc.
	private Artikelverwaltung meineArtikel;
	private Accountverwaltung meineAccounts;
	private Rechnungsverwaltung meineRechnungen;
	private StatsVerwaltung meineStats;
	// Namen der Dateien, in der die Daten des Shops gespeichert sind
	private String datei = "";

	// Konstrukter
	public Shopverwaltung(String datei) throws IOException {
		this.datei = datei;

		//Artikelbestand einlesen
		meineArtikel = new Artikelverwaltung(); //TODO: hier auf jeden Fall Dependency Injection verwenden. So ist das ganze kaum testbar per Unit-Test
		meineArtikel.liesDaten(datei+"_A.txt");
		//Accounts einlesen
		meineAccounts = new Accountverwaltung();  //TODO: hier auf jeden Fall Dependency Injection verwenden. So ist das ganze kaum testbar per Unit-Test
		meineAccounts.liesKundendaten(datei+"_Kunde.txt"); //TODO: so etwas gehoert nicht in den Konstruktor
		meineAccounts.liesMitarbeiterdaten(datei+"_Mitarbeiter.txt"); //TODO: so etwas gehoert nicht in den Konstruktor
		// Rechnungen einlesen
		meineRechnungen = new Rechnungsverwaltung();//TODO: hier auf jeden Fall Dependency Injection verwenden. So ist das ganze kaum testbar per Unit-Test
		//TODO Rechnungskrams?!
		
		//Statistik
		meineStats = new StatsVerwaltung();//TODO: hier auf jeden Fall Dependency Injection verwenden. So ist das ganze kaum testbar per Unit-Test
		meineStats.liesDaten(datei+"_S.txt");//TODO: so etwas gehoert nicht in den Konstruktor
	}	
	public List<Artikel> gibAlleArtikel() {
		// -> an Artikelverwaltung
		return meineArtikel.getArtikelBestand();
	}
	public List<Stats> statsSuchen(int artikelnummer) {
		// -> an Artikelverwaltung
		return meineStats.statsSuchen(artikelnummer);
	}
	
	public List<Stats> gibAlleStats() {
		// -> an Artikelverwaltung
		return meineStats.getStats();
	}	
	public boolean fuegeMassengutEin(String artname, int artnr, int artbestand, float artpreis, int packung) throws ArtikelExistiertBereitsException {

		Massengutartikel a = new Massengutartikel(artname, artnr, artbestand, artpreis, packung);
		meineArtikel.einfuegen(a);
		return false;
	}	
	// Fuegt Artikel ein
	public boolean fuegeArtikelEin(String artname, int artnr, int artbestand, float preis, int packungsgroesse) throws ArtikelExistiertBereitsException{
		Artikel a = new Artikel(artname, artnr, artbestand, preis);
		meineArtikel.einfuegen(a);
		return true;
	}
	
	/**
	 * Methode zur Artikelsuche anhand des Artikelnamens
	 */
	public List<Artikel> sucheNachArtikel(String artname) {
		//delegieren an meineArtikel (Artikelverwaltung)
		return meineArtikel.sucheArtikel(artname);
	}
	
	/**
	 * Methode zur Artikelsuche anhand des Artikelnamens
	 */
	public List<Artikel> sucheNachArtikelNummer(String artnummer) {
		//delegieren an meineArtikel (Artikelverwaltung)
		return meineArtikel.sucheArtikel(artnummer);
	}
	
	//Fuege Mitarbeiter Account ein	
	public boolean fuegeMitarbeiterAccountEin(String name, String passwort) throws AccountExistiertBereitsException{
			
			// Accountnummer wird zugewiesen (AccountBestand + 1)
			int accnummer = meineAccounts.getAccountBestand().size()+1;
			
			Mitarbeiter mitarbeiter = new Mitarbeiter(name, passwort, accnummer);
			try {
				meineAccounts.MitarbeiterEinfuegen(mitarbeiter);
			} catch (AccountExistiertBereitsException e) {
				return false;
			}
			return true;
		}
	
	//Fuege Kunden Account ein	
	public boolean fuegeKundenAccountEin(String name, String passwort, String strasse, int plz, String ort) throws AccountExistiertBereitsException {
		
		// Accountnummer wird zugewiesen (AccountBestand + 1)
		int accnummer = meineAccounts.getAccountBestand().size()+1;
		
		Kunde kunde = new Kunde(name, passwort, accnummer, strasse, plz, ort);
		try {
			meineAccounts.kundeEinfuegen(kunde);
		} catch (AccountExistiertBereitsException e) {
			return false;
		}
		return true;
	}
	//Fuege Kunden Account ein	
	public Kunde erstelleGastAccount() {
		int kundennummer = (int) (Math.random() * 90000000) + 10000000;
		Kunde kunde = new Kunde("Gast_"+ kundennummer, "gast", (kundennummer * -1), "none", 12345, "none");
		try {
			meineAccounts.kundeEinfuegen(kunde);
			return kunde;
		} catch (AccountExistiertBereitsException e) {
			// TODO Auto-generated catch block
		}
		return null;
	}	
	//Methode zur Ueberpruefung des Warenkorbs zum Kauf (Bestandsabfragen etc.)
	
	public HashMap<Artikel, Integer> pruefeKauf(Kunde user) {
		HashMap<Artikel, Integer> fehlerliste = new HashMap<Artikel, Integer>();
		HashMap<Artikel, Integer> pruefkorb = user.getWarenkorb().getInhalt();
		if (!pruefkorb.isEmpty()){
			Set<Artikel> articles = pruefkorb.keySet();
			for(Artikel artikel : articles) {
				int anzahl = (Integer) pruefkorb.get(artikel);
				if ((artikel.getBestand() - anzahl) < 0) {
					//user.getWarenkorb().loeschen(artikel);
					fehlerliste.put(artikel, anzahl);
				}
				else {
					if (artikel.getPackungsgroesse() > 0 && anzahl%artikel.getPackungsgroesse() != 0) {
						//user.getWarenkorb().loeschen(artikel);
						fehlerliste.put(artikel, anzahl);
					}
				}				
			}
			if (!fehlerliste.isEmpty()) {
				Set<Artikel> articlos = fehlerliste.keySet();
				for(Artikel artikel : articlos) {
					user.getWarenkorb().loeschen(artikel);
				}
			}
		}	
		return fehlerliste;		
	}

	//Methode zur Kaufabwicklung
	public Rechnung kaufAbwickeln(Kunde kaeufer) throws IOException{
		
		// aus Lager abbuchen (pruefen!)
		
		Rechnung rechnung = new Rechnung(kaeufer);

		// Warenkorb leeren

		return rechnung;
	}
	
	//Artikel entfernen
	public boolean entferneArtikel(int artnr) throws ArtikelExistiertNichtException, IOException {
		// delegieren nach Artikelverwaltung
		Artikel data = meineArtikel.artikelSuchen(artnr);
		meineStats.statupdate(artnr,data.getName(), data.getBestand(), LagerEreignisTyp.GELOESCHT);
		schreibeStatsdaten();
		return meineArtikel.entfernen(artnr);
	}

	//login Status
	public boolean getLoginStatus(Account account) {
		// -> an Accountverwaltung
			return meineAccounts.getLoginStatus(account);
	}
	
	public void setLoginStatus(Account account, boolean loginStatus){
		meineAccounts.setLoginStatus(account, loginStatus);
	}
	
	//Sortiere nach Artikelnamen
	public List<Artikel> getSortierteArtikelnamen() {
		// -> an Artikelverwaltung
		return meineArtikel.getSortierteArtikelnamen();
	}
	
	//Sortiere nach Artikelnummern
	public List<Artikel> getSortierteArtikelnummern() {
		// -> an Artikelverwaltung
		return meineArtikel.getSortierteArtikelnummern();
	}
	
	//Einloggen eines Accounts
	public Account loginAccount(String name, String passwort) throws AccountExistiertNichtException {
		return meineAccounts.loginAccount(name, passwort);		
	}
	
	//Ausloggen eines Accounts
	public Account logoutAccount(String name, String passwort) {
		return meineAccounts.logoutAccount(name, passwort);
	}

	//Bestand aendern
	public int aendereBestand(int artklnummer, int newBestand1) throws ArtikelExistiertNichtException {
		Artikel data = meineArtikel.artikelSuchen(artklnummer);
		meineStats.statupdate(artklnummer,data.getName(), newBestand1, LagerEreignisTyp.BESTAND_VERAENDERT);
		return meineArtikel.aendereBestand(artklnummer, newBestand1);		
	}
	//Bestand aendern
	public void aendereArtikel(String artikelname, int artikelnummer,int  bestand,float preis, int packungsgroesse) throws ArtikelExistiertBereitsException, ArtikelExistiertNichtException {
		Artikel data = meineArtikel.artikelSuchen(artikelnummer);
		//bestand verändert? an stats weiterleiten
		if (data.getBestand() != bestand) meineStats.statupdate(artikelnummer,data.getName(), bestand, LagerEreignisTyp.BESTAND_VERAENDERT);
		//artikeldaten ändern (lassen)
		meineArtikel.aendereArtikel(artikelname,artikelnummer,bestand,preis,packungsgroesse);
				
	}	
	
	//Warenkorn einfuegen
	public Kunde inWarenkorbEinfuegen(Artikel art, int anzahl, Kunde kunde) throws BestandUeberschrittenException, ArtikelExistiertNichtException {
		Warenkorb warenkorb = kunde.getWarenkorb();		
		warenkorb.einfuegen(art, anzahl);
		kunde.setWarenkorb(warenkorb);
		return kunde;
	}
	//aus Warenkorb löschen
	public Kunde ausWarenkorbloechen(Artikel art, Kunde kunde) {
		Warenkorb warenkorb = kunde.getWarenkorb();		
		warenkorb.loeschen(art);
		kunde.setWarenkorb(warenkorb);
		return kunde;
	}	
	// Artikel suchen.
	public Artikel artikelSuchen(int gesuchteNummer) throws BestandUeberschrittenException, ArtikelExistiertNichtException {
		return meineArtikel.artikelSuchen (gesuchteNummer);
	}
	
	// Artikel aus dem Warenkorb ausgeben
	public HashMap<Artikel, Integer> gibAlleArtikelAusWarenkorb(Kunde kunde) {
		Warenkorb warenkorb = kunde.getWarenkorb();
		return warenkorb.getInhalt();
	}

	//schreibe Artikeldaten
	public void schreibeArtikeldaten() throws IOException {
		meineArtikel.schreibeDaten(datei+"_A.txt");
	}
	
	//Schreibe Mitarbeiterdaten
	public void schreibeMitarbeiterdaten() throws IOException {
		meineAccounts.schreibeMitarbeiterdaten(datei+"_Mitarbeiter.txt");
	}
	
	//Kundendaten
	public void schreibeKundendaten() throws IOException {
		meineAccounts.schreibeKundendaten(datei+"_Kunde.txt");
	}
	
	//schreibe Statistikdaten
	public void schreibeStatsdaten() throws IOException {
		meineStats.schreibeDaten(datei+"_S.txt");
	}
	public Warenkorb getWarenkorb(Kunde kunde) {
		return kunde.getWarenkorb();
	}
	public Kunde setWarenkorb(Kunde kunde, Warenkorb warenkorb) {
		kunde.setWarenkorb(warenkorb);
		return kunde;
	}
}