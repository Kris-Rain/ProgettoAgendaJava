package codice;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	/* Da sistemare, meglio usare poi MatchNome e MatchData creati nella classe Appuntamento */
	public ArrayList<Appuntamento> searchAppuntamentoPerNome(String ... nomi) {	
		return searchAppuntamentoGenerico( appuntamento -> appuntamento.getNomePersone().containsAll(new ArrayList<>(Arrays.asList(nomi))));
	}
	
	public ArrayList<Appuntamento> searchAppuntamentoPerData(String data) {
		return searchAppuntamentoGenerico( appuntamento -> appuntamento.getData() == data);
	}
	

	@Override
	public Iterator<Appuntamento> iterator() {
		return new IteratoreAgenda();
	}
	
	
}
