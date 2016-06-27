package commen.server;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import com.blogspot.debukkitsblog.Util.Datapackage;
import com.blogspot.debukkitsblog.Util.Executable;
import com.blogspot.debukkitsblog.Util.Server;

import domain.server.Shopverwaltung;
import domain.server.exceptions.AccountExistiertBereitsException;
import domain.server.exceptions.AccountExistiertNichtException;
import domain.server.exceptions.ArtikelExistiertBereitsException;
import domain.server.exceptions.ArtikelExistiertNichtException;
import domain.server.exceptions.BestandUeberschrittenException;
import valueobjects.Artikel;
import valueobjects.Kunde;
import valueobjects.Rechnung;
import valueobjects.Stats;
import valueobjects.Warenkorb;

public class ShopServer extends Server {
	private Shopverwaltung shop;
	
	
	public ShopServer(int port, boolean autoRegisterEveryClient, boolean keepConnectionAlive, Shopverwaltung shop) {
		super(port, autoRegisterEveryClient, keepConnectionAlive);
		this.shop = shop;
	}

	@Override
	public void preStart() {
		//shopobjekt
		registerMethod("GETSHOPDATA", new Executable() {
			@Override
			public void run(Datapackage data, Socket socket) {
				try {
					sendMessage(new Datapackage("SHOPDATA",new Shopverwaltung("Shop")), socket);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}			
		});
		//Alle Artikel an Client senden GIBALLEARLTIKEL
		registerMethod("GIBALLEARLTIKEL", new Executable() {
			@Override
			public void run(Datapackage data, Socket socket) {
				sendMessage(new Datapackage("DATA",shop.gibAlleArtikel()), socket);
			}			
		});
		//einen bestimmten Artikel an Client senden
		registerMethod("ARTIKELSUCHEN", new Executable() {
			@Override
			public void run(Datapackage data, Socket socket) {
				try {
					Artikel artikel = shop.artikelSuchen((int) data.get(1));
					sendMessage(new Datapackage("DATA", artikel), socket);
				} catch (BestandUeberschrittenException | ArtikelExistiertNichtException e) {
				
					e.printStackTrace();
				}	
			}			
		});
		registerMethod("SUCHENACHARTIKEL", new Executable() {
			@Override
			public void run(Datapackage data, Socket socket) {
				sendMessage(new Datapackage("DATA",shop.sucheNachArtikel((String) data.get(1))), socket);
			}			
		});
		//Artikel dem Warenkorb hinzuf�gen
		registerMethod("INWARENKORBEINFUEGEN", new Executable() {
			@Override
			public void run(Datapackage data, Socket socket) {
				try {
					sendMessage(new Datapackage("DATA",shop.inWarenkorbEinfuegen((Artikel) data.get(1), (int) data.get(2), (Kunde) data.get(3))), socket);
				} catch (BestandUeberschrittenException | ArtikelExistiertNichtException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}			
		});
		//einen Artikel aus dem Warenkorb entfernen
		registerMethod("AUSWARENKORBLOESCHEN", new Executable() {
			@Override
			public void run(Datapackage data, Socket socket) {
				sendMessage(new Datapackage("DATA",shop.ausWarenkorbloechen((Artikel) data.get(1), (Kunde) data.get(2))), socket);
			}			
		});
		registerMethod("AENDEREARTIKEL", new Executable() {
			@Override
			public void run(Datapackage data, Socket socket) {
				try {
					shop.aendereArtikel((String) data.get(1), (int) data.get(2), (int) data.get(3), (float) data.get(4), (int) data.get(5));
					//artikelliste ALLER clients Updaten
					broadcastMessage(new Datapackage("NEWARTIKELDATA",shop.gibAlleArtikel()));					
				} catch (ArtikelExistiertBereitsException | ArtikelExistiertNichtException e) {
					
					e.printStackTrace();
				}
				sendMessage(new Datapackage("REPLY - AENDEREARTIKEL"), socket);
			}			
		});
		
		registerMethod("ENTFERNEARTIKEL", new Executable() {
			@Override
			public void run(Datapackage data, Socket socket) {
				try {
					try {
						shop.entferneArtikel((int)data.get(1));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//artikelliste ALLER clients Updaten
					broadcastMessage(new Datapackage("NEWARTIKELDATA",shop.gibAlleArtikel()));					
				} catch (ArtikelExistiertNichtException e) {
					
					e.printStackTrace();
				}
				sendMessage(new Datapackage("REPLY - ENTFERNEARTIKEL"), socket);
			}			
		});

		registerMethod("KAUFABWICKELN", new Executable() {
			@Override
			public void run(Datapackage data, Socket socket) {
				try {	
					Rechnung rechnung = shop.rechnungErstellen((Kunde) data.get(1));
					Kunde user = shop.kaufAbwickeln((Kunde) data.get(1));
					sendMessage(new Datapackage("DATA",user,rechnung), socket);
					//artikelliste ALLER clients Updaten
					broadcastMessage(new Datapackage("NEWARTIKELDATA",shop.gibAlleArtikel()));
				} catch (IOException | ArtikelExistiertNichtException e) {
					
					e.printStackTrace();
				}
			}			
		});
		registerMethod("ERSTELLEGASTACCOUNT", new Executable() {
			@Override
			public void run(Datapackage data, Socket socket) {
				Kunde kunde = shop.erstelleGastAccount();
				sendMessage(new Datapackage("DATA",kunde), socket);
			}			
		});			
		registerMethod("FUEGEKUNDENACCOUNTEIN", new Executable() {
			@Override
			public void run(Datapackage data, Socket socket) {
				try {
					shop.fuegeKundenAccountEin((String) data.get(1),(String) data.get(2),(String) data.get(3), (int) data.get(4), (String) data.get(5));
				} catch (AccountExistiertBereitsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
		});
		registerMethod("LOGINACCOUNT", new Executable() {
			@Override
			public void run(Datapackage data, Socket socket) {
				try {
					sendMessage(new Datapackage("DATA", shop.loginAccount((String) data.get(1), (String) data.get(2))), socket);
				} catch (AccountExistiertNichtException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
		});
		registerMethod("PRUEFEKAUF", new Executable() {
			@Override
			public void run(Datapackage data, Socket socket) {
				sendMessage(new Datapackage("DATA",shop.pruefeKauf((Kunde) data.get(1))), socket); 
			}			
		});
		registerMethod("GETWARENKORB", new Executable() {
			@Override
			public void run(Datapackage data, Socket socket) {
				Warenkorb wk = shop.getWarenkorb((Kunde) data.get(1));
				sendMessage(new Datapackage("DATA", wk), socket);
			}			
		});
		registerMethod("SETWARENKORB", new Executable() {
			@Override
			public void run(Datapackage data, Socket socket) {
				Kunde kunde = shop.setWarenkorb((Kunde) data.get(1), (Warenkorb) data.get(2));
				sendMessage(new Datapackage("DATA",kunde), socket);
			}			
		});
		registerMethod("SCHREIBEARTIKELDATEN", new Executable() {
			@Override
			public void run(Datapackage data, Socket socket) {
				try {
					shop.schreibeArtikeldaten();
					sendMessage(new Datapackage("REPLY - SCHREIBEARTIKELDATEN"), socket);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
		});
		registerMethod("SCHREIBESTATSDATEN", new Executable() {
			@Override
			public void run(Datapackage data, Socket socket) {
				try {
					shop.schreibeStatsdaten();
					sendMessage(new Datapackage("REPLY - SCHREIBESTATSDATEN"), socket);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
		});
		registerMethod("SCHREIBEKUNDENDATEN", new Executable() {
			@Override
			public void run(Datapackage data, Socket socket) {
				try {
					shop.schreibeKundendaten();
					sendMessage(new Datapackage("REPLY - SCHREIBEKUNDENDATEN"), socket);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
		});
		registerMethod("STATSSUCHEN", new Executable() {
			@Override
			public void run(Datapackage data, Socket socket) {
				List<Stats> stats = shop.statsSuchen((int) data.get(1));
				sendMessage(new Datapackage("DATA",stats), socket);	
			}			
		});
		registerMethod("GIBALLESTATS", new Executable() {
			@Override
			public void run(Datapackage data, Socket socket) {
				List<Stats> stats = shop.gibAlleStats();
				sendMessage(new Datapackage("DATA",stats), socket);			
			}			
		});
	}
}
