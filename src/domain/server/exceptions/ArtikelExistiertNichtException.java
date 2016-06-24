package domain.server.exceptions;

/**
 * Exception zur Signalisierung, dass ein Artikel nicht existiert (z.B. bei einem
 * Einfuegevorgang).
 */

public class ArtikelExistiertNichtException extends Exception {
	
	/**
	 * dadurch geht der gelbe Hinweis weg ;)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Konstruktor
	 * 
	 * @param nummer Artikelnummer
	 */
	public ArtikelExistiertNichtException(int nummer) {
		super("Artikel mit der Nummer " + nummer + " nicht gefunden.");
	}
}