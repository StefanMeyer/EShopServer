package valueobjects;

import java.io.Serializable;

public class Mitarbeiter extends Account implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2034374609656688481L;

	public Mitarbeiter(String name, String passwort, int accnummer) {
		super(name, passwort, accnummer);
	}
}