package codice;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * 
 * @author Nicolò Bianchetto
 * @author Kristian Rigo
 * 
 * 
 * 
 */

public class Agenda implements Iterable<Appuntamento> {
	
	private String nomeAgenda;
	private ArrayList<Appuntamento> appuntamenti;
	
	private static int numAgende = 0;
	
	
	public class IteratoreAgenda implements Iterator<Appuntamento> {
		private int indiceContatti;
		private int lastIndex;
		
		public IteratoreAgenda() {
			indiceContatti = 0;
			lastIndex = appuntamenti.size();
		}
		
		@Override
		public boolean hasNext() {
			return indiceContatti < lastIndex;
		}

		@Override
		public Appuntamento next() throws ConcurrentModificationException {
			if(lastIndex != appuntamenti.size()) throw new ConcurrentModificationException();
			return appuntamenti.get(indiceContatti++);
		}
	}
	
	
	public Agenda(String nomeAgenda, ArrayList<Appuntamento> appuntamenti) {
		this.nomeAgenda = nomeAgenda;
		this.appuntamenti = appuntamenti;
		ordinaAppuntamenti();
		numAgende++;
	}
	
	public Agenda(String nomeAgenda) {
		this(nomeAgenda, new ArrayList<>());
	}
	
	
	public Agenda() {
		this("Agenda " + (numAgende+1));
	}
	
	public Agenda(FileReader file) {
		
		try {
			BufferedReader reader = new BufferedReader(file);
			/*
				https://www.digitalocean.com/community/tutorials/java-read-file-line-by-line
				codice lettura da file
			*/
			reader.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}

	public String getNomeAgenda() {
		return nomeAgenda;
	}

	public void setNomeAgenda(String nomeAgenda) {
		this.nomeAgenda = nomeAgenda;
	}

	public ArrayList<Appuntamento> getAppuntamenti() {
		return appuntamenti;
	}

	public static int getNumAgende() {
		return numAgende;
	}
	
	
	public static void clear() {
		numAgende = 0;
	}
	
	private ArrayList<Appuntamento> searchAppuntamentoGenerico(Predicate <Appuntamento> predicato) {
		return (ArrayList<Appuntamento>) appuntamenti.stream().filter(predicato).collect(Collectors.toList());
	}
	
	public ArrayList<Appuntamento> searchAppuntamentoPerPersona(String nome, String ... extraNomi) {	
		return searchAppuntamentoGenerico( appuntamento -> appuntamento.matchPersone(nome, extraNomi));
	}
	
	public ArrayList<Appuntamento> searchAppuntamentoPerData(String data) {
		return searchAppuntamentoGenerico( appuntamento -> appuntamento.matchData(data));
	}
	
	/**
	 * Ordino prima per orario e poi per data, in modo che alla fine ho gli appuntamenti ordinati
	 * prima per data e poi per orario
	 * La Giannini li vuole elencati per data, però secondo me ha senso ordinarli anche per orario a sto punto
	 * Nel sorting multicriterio bisogna andare in senso opposto a quello per cui vuoi effettivamente ordinare ... ad esempio:
	 * (2, 5) (1, 3) (5, 1) (1, 2)
	 * se si vuole ordinare le tuple per i primi numeri e poi per i secondi, si sorta prima per i secondi e poi per i primi:
	 * ordino per i secondi:
	 * (5, 1) (1, 2) (1, 3) (2, 5)
	 * poi i primi:
	 * (1, 2) (1, 3) (2, 5) (5, 1)
	 * 
	 * so sta cosa perché l'aveva detto Guazzone a lezione di algoritmi
	 */
	
	private void ordinaAppuntamenti() {
		appuntamenti.sort( (first, second) -> first.getOrario().compareTo(second.getOrario()));
		appuntamenti.sort( (first, second) -> first.getData().compareTo(second.getData()));
	}
	
	@Override
	public String toString() {
		String stringaAppuntamenti = "";
		for(Appuntamento appointment: this) stringaAppuntamenti+= appointment.toString();
		return stringaAppuntamenti;
	}
	
	public int aggiungiAppuntamento(Appuntamento appointment) {
		//Qui codice aggiungi.. magari ritorni -1 se l'appuntamento non puoi inserirlo per problemi di sovrapposizione di data/orario
		//magari ritorni 0 se invece l'appuntamento già esiste nell'agenda
		return 1;
	}
	

	@Override
	public Iterator<Appuntamento> iterator() {
		return new IteratoreAgenda();
	}
	
	
}
