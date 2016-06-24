package domain.server.exceptions;

/**
 * Exception zur Signalisierung, dass ein Artikel nicht existiert (z.B. bei einem
 * Einfuegevorgang).
 */

public class ArtikelExistiertNichtException extends Exception {
	private static final long serialVersionUID = 7501391787959597997L;

	/**
	 * Konstruktor
	 * 
	 * @param nummer Artikelnummer
	 */
	public ArtikelExistiertNichtException(int nummer) {
		super("Artikel mit der Nummer " + nummer + " nicht gefunden.");
		System.out.println("Artikel mit der Nummer " + nummer + " nicht gefunden.");
	}
}