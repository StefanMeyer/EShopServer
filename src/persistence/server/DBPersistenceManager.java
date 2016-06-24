package persistence.server;

import java.io.IOException;

import valueobjects.Artikel;
import valueobjects.Kunde;
import valueobjects.Mitarbeiter;
import valueobjects.Stats;

public class DBPersistenceManager implements PersistenceManager {

	
	@Override
	public boolean close() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openForReading(String datenquelle) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void openForWriting(String datenquelle) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Artikel ladeArtikel() throws IOException {
		// TODO Auto-generated method stub
		return null;
		}

	@Override
	public boolean speichereArtikel(Artikel a) throws IOException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Kunde ladeKunde() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean speichereKunde(Kunde k) throws IOException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Mitarbeiter ladeMitarbeiter() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean speichereMitarbeiter(Mitarbeiter m) throws IOException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Stats ladeStats() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean speichereStats(Stats s) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

}