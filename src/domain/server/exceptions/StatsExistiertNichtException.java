package domain.server.exceptions;

public class StatsExistiertNichtException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor
	 * 
	 * @param artikel Der bereits existierende Artikel
	 * @param zusatzMsg zusaetzlicher Text fuer die Fehlermeldung
	 */
	public StatsExistiertNichtException(/*Artikel artikel*/) {

		super("Statistik existiert bereits.");
	}
}
