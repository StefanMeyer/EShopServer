package domain.server.exceptions;

import valueobjects.Artikel;

/**
 * Exception zur Signalisierung, dass ein Artikel bereits existiert (z.B. bei einem
 * Einfuegevorgang).
 */
public class StatExistiertBereitsException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor
	 * 
	 * @param artikel Der bereits existierende Artikel
	 * @param zusatzMsg zusaetzlicher Text fuer die Fehlermeldung
	 */
	public StatExistiertBereitsException(/*Artikel artikel*/) {

		super("Statistik existiert bereits.");
	}
}