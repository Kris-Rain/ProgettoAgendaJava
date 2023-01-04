/**
 * @author Kristian Rigo
 * @author Nicolò Bianchetto
 */

package codice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Appuntamento {
	private String data;
	private String orario;
	private String durata;
	private String luogo;
	private ArrayList<String> persone;
	
	public Appuntamento(String data, String orario, String durata, String luogo, String nomePersona) {
		this.data=data;
		this.orario=orario;
		this.durata=durata;
		this.luogo=luogo;
		persone=new ArrayList<String>(Arrays.asList(nomePersona));
		/* Solo una piccola modifica, ho tolto nome e lasciato loro l'arraylist dei nomi di persone 
		  per "compattare" il codice il resto è uguale .. puoi cancellare poi questo commento */
		
		/* Piccolo appunto, ho guardato per la data, forse ci conviene, e magari è più semplice da gestire,
		 * utilizzare SimpleDateFormat per la data... che comunque converte la data sempre in una stringa
		 * ti lascio dei link per andarti a vedere un po' ... non sembra troppo difficile e forse è anche più comodo.. poi boh
		 * https://www.tutorialspoint.com/how-to-compare-two-dates-in-java
		 * https://www.digitalocean.com/community/tutorials/java-simpledateformat-java-date-format
		 * 
		 * a sto punto forse anche l'orario ... più che altro perché poi in agenda devo aggiungere l'appuntamento non solo se
		 * soddisfa i requisiti standard di data, orario, durata etc.. ma anche se non ci sono sovrapposizioni con altre date/orari.
		 * 
		 * magari possiamo aggiungere un parametro orarioFine ... ricavato da orarioInizio + durata
		 * Beh vedremo poi bene sta cosa
		 * */
	}
	public String getData() {
		return data;
	}
	public ArrayList<String> getPersone() {
		return persone;
	}
	public String getOrario() {
		return orario;
	}
	public String getDurata() {
		return durata;
	}
	public String getLuogo() {
		return luogo;
	}
	
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
		List<String> elencoNomi = Arrays.asList(primoNome);
		Collections.addAll(elencoNomi, extraNomi);
		for(String nome : elencoNomi) 	if(!matchSingoloNome(nome))	return false;
		return true;
	}
	
	// Ho tolto "if(true) return true; else return false" e ho compattato ... così la Giannini non si trigghera
	public boolean matchData(String data) {
		return this.data.contains(data);
	}
}
