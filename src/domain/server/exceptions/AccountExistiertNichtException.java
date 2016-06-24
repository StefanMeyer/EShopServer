package domain.server.exceptions;

/**
 * Exception zur Signalisierung, dass ein Account nicht existiert (z.B. bei einem
 * Einfuegevorgang).
 */

public class AccountExistiertNichtException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public AccountExistiertNichtException(String name, String pwd) {
		super("Account mit Name" + name + " oder Passwort nicht gefunden!");
	}
}