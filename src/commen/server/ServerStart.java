package commen.server;

import java.io.IOException;

import domain.server.Shopverwaltung;

public class ServerStart {
    public ServerStart() throws IOException {
    	//server Starten
    	ShopServer server = new ShopServer(8788, true, true, new Shopverwaltung("Shop"));
    }
}
