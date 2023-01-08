package codice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
	
	public Agenda(File file) throws FileNotFoundException, IOException {
		
		nomeAgenda = file.getName();
		appuntamenti = new ArrayList<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));

		String stringaAppuntamento;
		while((stringaAppuntamento = reader.readLine()) != null) {
			String[] parametri = stringaAppuntamento.split(" ");
			if(parametri.length == 5) {	//se i parametri non sono 5, ignoro la riga
				try {
					aggiungiAppuntamento(new Appuntamento(parametri[0], parametri[1], parametri[2], parametri[3], parametri[4]));	
					//Provo ad aggiungere, aggiungiAppuntamento testa anche se compatibile.
				} catch(AppuntamentoException e) {
					//do nothing. Ignoro la riga
				}	
			}
		}
		reader.close();	
		/* Nel peggiore dei casi, se non riesco a leggere nessuna riga, creo una Agenda vuota. 
		 * Lancio un'eccezione solo se il file non esiste o ho avuto IOException
		 */
	}
	
	
	public void salvaAgendaSuFile() throws FileNotFoundException, IOException {
		File file = new File("/pathname/"+nomeAgenda);
		file.createNewFile();
		BufferedWriter br = new BufferedWriter(new FileWriter(file));
		br.write(this.toString());
		br.close();
		
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
	
	public ArrayList<Appuntamento> searchAppuntamentoPerPersona(String nome) {	
		return searchAppuntamentoGenerico( appuntamento -> appuntamento.matchPersona(nome));
	}
	
	public ArrayList<Appuntamento> searchAppuntamentoPerData(String data) {
		return searchAppuntamentoGenerico( appuntamento -> appuntamento.matchData(data));
	}
	
	private ArrayList<Appuntamento> searchAppuntamentoPerDataOrario(String data, String orario) {
		return searchAppuntamentoGenerico( appuntamento -> appuntamento.matchDataOrario(data, orario));
	}
	
	
	public boolean contains(Appuntamento app) {
		return appuntamenti.contains(app);
	}
	
	public boolean contains(String data, String orario) {
		return !searchAppuntamentoPerDataOrario(data, orario).isEmpty();
	}
	
	public boolean isCompatible(Appuntamento appointment) {
		for(Appuntamento elemento: this) {
			if(!appointment.isCompatible(elemento))	return false;
		}
		return true;
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
	
	public void ordinaAppuntamenti() {
		appuntamenti.sort( (first, second) -> first.getDataTimeInizio().compareTo(second.getDataTimeInizio()));
	}
	
	@Override
	public String toString() {
		String stringaAppuntamenti = "";
		for(Appuntamento appointment: this) stringaAppuntamenti+= appointment.toString() + "\n";
		return stringaAppuntamenti;
	}

	public boolean aggiungiAppuntamento(Appuntamento appointment) {
		if(!this.isCompatible(appointment)) return false;
		appuntamenti.add(appointment);
		ordinaAppuntamenti();
		return true;
	}
	
	
	public boolean aggiungiAppuntamento(String data, String orario, String durata, String luogo, String nomePersona) throws AppuntamentoException {
		return aggiungiAppuntamento(new Appuntamento(data, orario, durata, luogo, nomePersona));
	}
	
	public boolean rimuoviPerPersona(String nome) {
		return appuntamenti.removeAll(searchAppuntamentoPerPersona(nome));
	}
	
	public boolean rimuoviPerData(String data) {
		return appuntamenti.removeAll(searchAppuntamentoPerData(data));
	}
	
	public boolean rimuoviPerDataOrario(String data, String orario) {
		return appuntamenti.removeAll(searchAppuntamentoPerDataOrario(data, orario));
	}
	

	public boolean modificaAppuntamento(String dataApp, String orarioApp, String parametroDaModificare, String newValue) {
		ArrayList<Appuntamento> risultato = searchAppuntamentoPerDataOrario(dataApp, orarioApp);
		if(risultato.isEmpty()) return false;
		Appuntamento vecchioAppuntamento = risultato.get(0);
		
		String[] parametri = {
				vecchioAppuntamento.getData(),
				vecchioAppuntamento.getOrario(),
				vecchioAppuntamento.getDurata(),
				vecchioAppuntamento.getLuogo(),
				vecchioAppuntamento.getPersona()
		};


		switch(parametroDaModificare.toLowerCase().strip()) {
			case "data": parametri[0] = newValue; break;
			case "orario": parametri[1] = newValue; break;
			case "durata": parametri[2] = newValue; break;
			case "luogo": parametri[3] = newValue; break;
			case "persona": parametri[4] = newValue; break;
			default: return false;
		}

		
		try {
			Appuntamento nuovoAppuntamento = new Appuntamento(parametri[0], parametri[1], parametri[2], parametri[3], parametri[4]);
			if(!this.isCompatible(nuovoAppuntamento)) return false;
			appuntamenti.set(appuntamenti.indexOf(vecchioAppuntamento), nuovoAppuntamento);
			ordinaAppuntamenti();
		} 
		catch(AppuntamentoException e) {
			return false;
		}
		
		return true;
	}
	
	

	@Override
	public Iterator<Appuntamento> iterator() {
		return new IteratoreAgenda();
	}
	
	
}
