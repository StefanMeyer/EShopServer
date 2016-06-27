package commen.server;

import java.io.IOException;

import domain.server.Shopverwaltung;

public class Server_Launcher {
//MAIN
    public static void main(String[] args) throws IOException {
    	//server Starten
    	ShopServer server = new ShopServer(8788, true, true, new Shopverwaltung("Shop"));
    }
}