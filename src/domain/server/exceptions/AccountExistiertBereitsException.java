package domain.server.exceptions;

import valueobjects.Account;

/**
 * Exception zur Signalisierung, dass ein Account bereits existiert (z.B. bei einem
 * Einfuegevorgang).
 */
public class AccountExistiertBereitsException extends Exception {

	private static final long serialVersionUID = 1L;
	String zusatzMsg = "Bitte wiederholen Sie den Registrierungsvorgang.";

	/**
	 * Konstruktor
	 * 
	 * @param account nicht existierender Account
	 * @param zusatzMsg zusätzlicher Text fuer die Fehlermeldung
	 */
	public AccountExistiertBereitsException(Account account, String zusatzMsg) {
		super("Account mit Titel " + account.getName() + " existiert bereits. " + zusatzMsg);
	}
}