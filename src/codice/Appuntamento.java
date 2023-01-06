/**
 * @author Kristian Rigo
 * @author Nicolò Bianchetto
 */

package codice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Appuntamento {
	private String orarioFine;
	private String durata;
	private String luogo;
	private ArrayList<String> persone;
	private DataOrario dataTime;
	
	public static class ControlloDati{
		//regex con controllo data e orario
		//Controllo.java
	}
	
	public Appuntamento(String data, String orario, String durata, String luogo, String nomePersona) {
		this(data, orario, durata, luogo, new ArrayList<String>(Arrays.asList(nomePersona)));
	}
	public Appuntamento(String data, String orario, String durata, String luogo, ArrayList<String> persone) {
		this.dataTime = new DataOrario(data, orario);
		this.durata=durata;
		this.orarioFine = dataTime.plusMinuti(durata).toString();
		this.luogo=luogo;
		this.persone=persone;
	}
	public String getData() {
		return dataTime.getDataToString();
	}
	public ArrayList<String> getPersone() {
		return persone;
	}
	public String getOrario() {
		return dataTime.getOrarioToString();
	}
	public String getDurata() {
		return durata;
	}
	public String getLuogo() {
		return luogo;
	}
	public String getOrarioFine() {
		return orarioFine;
	}
	
	/*private String sommaOrario(String durata) {
		String[] split = orario.split("[:-]");
		int somma = Integer.valueOf(split[0]) * 60 + Integer.valueOf(split[1]) + Integer.parseInt(durata);
		String minuti = (( somma % 60 ) < 10) ? ("0"+somma%60) : Integer.toString(somma%60);
		if((int)Math.floor(somma/60) >= 24) return "23:59";
		return (int)Math.floor(somma/60) + ":" + minuti;
	}*/
	
	/**
		Ho provato a definire matchPersone e matchData perché mi servivano per i metodi di Agenda ... 
		Sei libero di modificarli come vuoi
		Purtroppo contains è case sensitive, se vogliamo fare che l'utente ricerca per nome case-insensitive
		ci tocca usare il pattern-matcher (ho guardato su stackoverflow pare l'unico modo) ... 
		
		Una roba del genere
		Pattern pat = Pattern.compile("Marco", Pattern.CASE_INSENSITIVE);
		for(String nome: persone) {
			Matcher mat = pat.matcher(nome);
			if(mat.matches()) return true;
		}
		return false;
		
		... oppure ce ne sbattiamo le palle e basta
	*/
	
	
	/**
	 * 
	 * @param nome
	 * @return
	 * 
	 * Faccio il contains per ogni persona dell'arraylist... mi basta che anche solo una persona contenga la sequenza
	 */
	
	private boolean matchSingoloNome(String nome) {
		for(String persona: persone) {
			if(persona.contains(nome)) return true;
		}
		return false;
	}

	/**
	 * 
	 * @param primoNome
	 * @param extraNomi
	 * @return
	 * 
	 * Ho messo primoNome e poi extraNomi perché voglio almeno un parametro String obbligatorio
	 * Se ho più di un nome vuol dire che voglio che per ogni nome della ricerca
	 * ci sia almeno una persona nell'arraylist che soddisfi il "contains"
	 * 
	 */
	
	public boolean matchPersone(String primoNome, String ... extraNomi) {
		ArrayList<String> elencoNomi = new ArrayList<>(Arrays.asList(primoNome));
		Collections.addAll(elencoNomi, extraNomi);
		for(String nome : elencoNomi) 	if(!matchSingoloNome(nome))	return false;
		return true;
	}
	public boolean matchData(String data) {
		return this.getData().contains(data);
	}
	public boolean matchDataOrario(String data, String orario) {
		return (this.getData() + " " + this.getOrario()).contains(data + " " + orario);
	}
	public boolean isCompatible(Appuntamento newAppuntamento) {
		//true se compatibile false se non compatibile
		if(dataTime.compareTo(newAppuntamento.getData(), newAppuntamento.getOrario())==0) {
			if(newAppuntamento.getOrarioFine().compareTo(this.getOrario()) <= 0 
			|| newAppuntamento.getOrario().compareTo(this.getOrarioFine()) >= 0) return true;
			return false;
		}
		return true;
	}
}
