package domain.server;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import domain.server.exceptions.AccountExistiertBereitsException;
import domain.server.exceptions.AccountExistiertNichtException;
import persistence.server.FilePersistenceManager;
import persistence.server.PersistenceManager;
import valueobjects.Account;
import valueobjects.Kunde;
import valueobjects.Mitarbeiter;


/**
 * Klasse zur Verwaltung von Accounts.
 * 
 */

public class Accountverwaltung {

	// Verwaltung der Accounts als Liste
	// Als Implementierung der Liste dient ein Vektor
	//TODO: Wieso ein Vector? Finde ich etwas exotisch. Eine ArrayList tut es auch ;)
	private List<Account> accountBestand = new Vector<Account>();
	
	
	// Persistenz-Schnittstelle, die fuer die Details des Dateizugriffs
	// verantwortlich ist .
	private PersistenceManager pm = new FilePersistenceManager(); //TODO: finde ich nicht sonderlich schÃ¶n, dass die Accountverwaltung sich ihren eigenen Persistenzmanager erzeugen muss. Wieso nicht einfach per Dependency Injection (Konstruktor Parameter reinreichen?)

	public Accountverwaltung( /*PersistenceManager persistenceManager*/ ) {
		//TODO: Leerer Konstruktor muss nicht angegeben werden. Wenn keiner angegeben ist, wird der Standardkonstruktor von Object verwendet. Also: AccountVerwaltung()
		//this.persistenceManager = persistenceManager;
	}

	
	/**
	 * Methode zum Einlesen der Kundendaten
	 * 
	 * @param datei Datei, die Accounts enthaelt
	 * @throws IOException
	 */
	
	public void liesKundendaten(String datei) throws IOException {
		// PersistenzManager fuer Lesevorgaenge oeffnen
		pm.openForReading(datei);

		//TODO: Das sollte meiner Meinung nach Aufgabe des Persistenzmanagers sein. Der sollte lediglich eine Liste an Kunden zurueckliefern die ihr dann in euren

		Kunde einKunde;
		do {
			// Account-Objekt einlesen
			einKunde = pm.ladeKunde();
			if (einKunde != null) {
				// Account in Liste einfuegen
				try {
					kundeEinfuegen(einKunde);
				} catch (AccountExistiertBereitsException e1) {
					// Kann hier eigentlich nicht auftreten,
					// daher auch keine Fehlerbehandlung...
					//TODO: sicher?
				}
			}
		} while (einKunde != null);

		// Persistenz-Schnittstelle wieder schlieï¿½en
		pm.close();
	}

	
	/**
	 * Methode zum Einlesen der Mitarbeiterdaten
	 * 
	 * @param datei
	 * @throws IOException
	 */
	
	public void liesMitarbeiterdaten(String datei) throws IOException {
		// PersistenzManager fuer Lesevorgaenge oeffnen
		pm.openForReading(datei);

		Mitarbeiter einMitarbeiter;
		do {
			// Account-Objekt einlesen
			einMitarbeiter = pm.ladeMitarbeiter();
			if (einMitarbeiter != null) {
				// Account in Liste einfï¿½gen
				try {
					MitarbeiterEinfuegen(einMitarbeiter);
				} catch (AccountExistiertBereitsException e1) {
					// Kann hier eigentlich nicht auftreten,
					// daher auch keine Fehlerbehandlung...
				}
			}
		} while (einMitarbeiter != null);

		// Persistenz-Schnittstelle wieder schlieï¿½en
		pm.close();

	}

	/**
	 * Methode zum Einfuegen eines Kunden-Accounts
	 *
	 * @param einKunde 
	 * @throws AccountExistiertBereitsException
	 */
	
	// Kunde einfuegen
	public void kundeEinfuegen(Kunde einKunde)
			throws AccountExistiertBereitsException {
		if (accountBestand.contains(einKunde)) {
			throw new AccountExistiertBereitsException(einKunde,
					" - in 'einfuegen()'");
		}

		accountBestand.add(einKunde);
	}
	
	/**
	 * Methode zum Einfuegen eines Mitarbeiter-Accounts
	 * 
	 * @param einMitarbeiter
	 * @throws AccountExistiertBereitsException
	 */
	
	// Mitarbeiter einfuegen
	public void MitarbeiterEinfuegen(Mitarbeiter einMitarbeiter) //TODO: siehe oben
			throws AccountExistiertBereitsException {
		if (accountBestand.contains(einMitarbeiter)) {
			throw new AccountExistiertBereitsException(einMitarbeiter,
					" - in 'einfuegen()'");
		}

		accountBestand.add(einMitarbeiter);
	}

	/**
	 * Methode um Account einzuloggen
	 * 
	 * @param name
	 * @param passwort
	 * @return user
	 * @throws AccountExistiertNichtException
	 */

	public Account loginAccount(String name, String passwort)
		throws AccountExistiertNichtException {	
			String username;
			for (Account user : accountBestand) {
				// Alle strings (Eingabe, gespeicherte Daten) in Kleinbuchstaben umwandeln
				name = name.toLowerCase();			
				username = user.getName().toLowerCase();
				if (username.equals(name)
						&& user.getPasswort().equals(passwort)) {
					return user;
				}
			}

		throw new AccountExistiertNichtException(name, passwort);
	}
		
	/**
	 * Methode um Account auszuloggen
	 * 
	 * @param name
	 * @param passwort
	 * @return user
	 * 
	 */
	
	public Account logoutAccount(String name, String passwort){
		for (Account user : accountBestand) {
			if (user.getName().equals(name) && user.getPasswort().equals(passwort)) {
				user = null;
				return user;
			}
		}
		return null;
	}

	/**
	 * Methode zum Schreiben der Kundendaten in eine Datei.
	 * 
	 * @param datei Datei, die Accounts enthaelt
	 * @throws IOException
	 */

	public void schreibeKundendaten(String datei) throws IOException {
		// PersistenzManager fuer Schreibvorgaenge oeffnen
		pm.openForWriting(datei);

		Iterator<Account> iter = accountBestand.iterator();
		while (iter.hasNext()) {
			Account person = iter.next();
			if (person instanceof Kunde) {
				//gäste werden nicht gespeichert
				if (person.getAccountNr() > 0) {
					pm.speichereKunde((Kunde) person);
				}
			}
		}

		// Persistenz-Schnittstelle wieder schliessen
		pm.close();
	}
	
	/**
	 * Methode zum Schreiben der Mitarbeiterdaten in eine Datei.
	 * 
	 * @param datei Datei, die Accounts enthï¿½lt
	 * @throws IOException
	 */

	public void schreibeMitarbeiterdaten(String datei) throws IOException {
		// PersistenzManager fuer Schreibvorgaenge oeffnen
		pm.openForWriting(datei);

		Iterator<Account> iter = accountBestand.iterator();
		while (iter.hasNext()) {
			Account person = iter.next();
			if (person instanceof Mitarbeiter)
				pm.speichereMitarbeiter((Mitarbeiter) person);
		}

		// Persistenz-Schnittstelle wieder schliessen
		pm.close();
	}
	
	// Getter und Setter
	
	/**
	 * Methode zum Abrufen des Accountbestands
	 * 
	 * @return accountbestand Accountbestand
	 */
	
	public List<Account> getAccountBestand() {
		return accountBestand;
	}
	
	/**
	 * Methode zum Abrufen des LoginStatus (Mitarbeiter oder Kunde)
	 * 
	 * @return loginStatus
	 */
	
	public boolean getLoginStatus(Account account) {
		// -> an Accountverwaltung
		
		return account.getLoginStatus();
	}
	
	public void setLoginStatus(Account account, boolean loginStatus){
		account.setLoginStatus(loginStatus);
	}
}