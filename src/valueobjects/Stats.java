package valueobjects;



/** statistik für Artikel
 */

public class Stats {
	
	public enum LagerEreignisTyp {
		NEU, BESTAND_VERAENDERT, GELOESCHT
	}
	
	private int arklnummer;
	private String Atklname;
	private int bestand;
	private String datum = null;
	private LagerEreignisTyp type = null;
	
	/** Erstellt ein Statistik objekt 
	 * 
	 * @param arklnummer
	 * @param bestand
	 */
	public Stats(int arklnummer,String name, int bestand,String datum, LagerEreignisTyp type) {
		this.arklnummer = arklnummer;
		this.Atklname = name;
		this.bestand = bestand;
		this.type = type;
		this.datum = datum;
	}
	public String toString() {
		return ("\n ArtikelNummer: " + this.arklnummer + " Datum: " + this.datum + " Bestand: " + this.bestand);
	}
	
	
	public String getDatum() {
		return this.datum;
	}
	public void setDatum(String datum) {
		this.datum = datum;
	}
	public int getArklnummer() {
		return this.arklnummer;
	}
	public void setArklnummer(int arklnummer) {
		this.arklnummer = arklnummer;
	}
	public int getBestand() {
		return this.bestand;
	}
	public void setBestand(int bestand) {
		this.bestand = bestand;
	}
	public LagerEreignisTyp getType() {
		return this.type;
	}
	public void setType(LagerEreignisTyp type) {
		this.type = type;
	}
	public String getAtklname() {
		return Atklname;
	}
	public void setAtklname(String atklname) {
		Atklname = atklname;
	}
}