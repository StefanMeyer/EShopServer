package valueobjects;

import java.io.Serializable;

/**
 * Klasse zur Repräsentation der Accounts.
 */

public class Account implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2549836699167815837L;
	//Attribute zur Beschreibung des Accounts
	protected int accountNr;
	protected String accountname;
	protected String accountpasswort;
	
	private boolean loginStatus = false;
	

	public Account(String name, String passwort, int accnummer) {
		this.accountname = name;
		this.accountpasswort = passwort;
		this.accountNr = accnummer;	
	}

	//Überprüfe ob der Account schon vorhanden ist	
	public boolean equals(Object obj) {
		if (obj instanceof Account) {
			Account anderesAccount = (Account) obj;
			return this.accountname.equals(anderesAccount.accountname);
		}
		return false;
	}

	//Getter und Setter
	public int getAccountNr() {
		return accountNr;
	}


	public void setAccountNr(int accountNr) {
		this.accountNr = accountNr;
	}

	
	public String getName() {
		return accountname;
	}


	public void setName(String name) {
		this.accountname = name;
	}


	public String getPasswort() {
		return accountpasswort;
	}	

	public void setPasswort(String passwort) {
		this.accountpasswort = passwort;
	}
	
	public boolean getLoginStatus() {
		return loginStatus;
	}
	
	public void setLoginStatus(boolean loginStatus) {
		this.loginStatus = loginStatus;
	}
}