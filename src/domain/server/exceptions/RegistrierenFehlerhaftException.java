package domain.server.exceptions;

public class RegistrierenFehlerhaftException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public RegistrierenFehlerhaftException(String name, String passwort, String strasse, int plz, String ort) {
		super("Bitte füllen Sie alle Felder aus!");
	}
}