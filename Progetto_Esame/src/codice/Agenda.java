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
		this.nomeAgenda = (nomeAgenda.isEmpty()) ? "Agenda": nomeAgenda;
		this.appuntamenti = (isAgenda(appuntamenti)) ? new ArrayList<>(appuntamenti) : new ArrayList<>();
		ordinaAppuntamenti(this.appuntamenti);
	}
	
	public Agenda(String nomeAgenda) {
		this(nomeAgenda, new ArrayList<>());
	}
	
	
	public Agenda() {
		this("Agenda");
	}
	
	public Agenda(ArrayList<Appuntamento> appuntamenti) {
		this("Agenda", appuntamenti);
	}

	public Agenda(File file) throws FileNotFoundException, IOException {
		
		nomeAgenda = file.getName();
		appuntamenti = new ArrayList<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));

		String stringaAppuntamento;
		while((stringaAppuntamento = reader.readLine()) != null) {
			String[] parametri = stringaAppuntamento.split("///");
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
	
	/*Creo una copia e faccio il test sulla copia e non sugli appuntamenti originali per rispettare la regola
	 * "Un metodo deve fare solo una cosa e deve fare solo quella"
	 * Quindi isAgenda ti dice se l'arraylist può essere un agenda o meno, ma non deve anche ordinare gli appuntamenti
	 * Perché magari, per qualche motivo strano, io non gli voglio ordinati in ordine di data e orario
	*/
	public static boolean isAgenda(ArrayList<Appuntamento> appuntamenti) {
		ArrayList<Appuntamento> test = new ArrayList<>(appuntamenti);
		ordinaAppuntamenti(test);
		for(int i = 0; i < test.size() - 1; i++) {
			if(!test.get(i).isBefore(test.get(i+1))) return false;
		}
		return true;
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
	
	public int getDimensioneAgenda() {
		return appuntamenti.size();
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
	
	public ArrayList<Appuntamento> searchAppuntamentoPerDataOrario(String data, String orario) {
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
	
	
	private static void ordinaAppuntamenti(ArrayList<Appuntamento> appuntamenti) {
		appuntamenti.sort( (first, second) -> first.getDataTimeInizio().compareTo(second.getDataTimeInizio()));
	}
	
	private String elencaAppuntamenti(ArrayList<Appuntamento> lista) {
		String stringaAppuntamenti = "";
		for(Appuntamento appointment: lista) stringaAppuntamenti+= appointment.toString();
		return stringaAppuntamenti;
	}
	
	@Override
	public String toString() {
		return "Agenda: " + nomeAgenda + "\n" + elencaAppuntamenti(appuntamenti);
	}
	
	
	public String elencaPerPersona(String nome) {
		return elencaAppuntamenti(searchAppuntamentoPerPersona(nome));
	}
	
	
	public String elencaPerData(String data) {
		return elencaAppuntamenti(searchAppuntamentoPerData(data));
	}
	

	public boolean aggiungiAppuntamento(Appuntamento appointment) {
		if(!this.isCompatible(appointment)) return false;
		appuntamenti.add(appointment);
		ordinaAppuntamenti(appuntamenti);
		return true;
	}
	
	
	public boolean aggiungiAppuntamento(String data, String orario, String durata, String luogo, String nomePersona) {
		try {
			return aggiungiAppuntamento(new Appuntamento(data, orario, durata, luogo, nomePersona));
		}
		catch(AppuntamentoException e) { 
			return false; 
		}
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
	
	/*
	public int modificaAppuntamento(String dataApp, String orarioApp, String parametroDaModificare, String newValue) {
		ArrayList<Appuntamento> risultato = searchAppuntamentoPerDataOrario(dataApp, orarioApp);
		if(risultato.isEmpty()) return 0;
		Appuntamento vecchioAppuntamento = risultato.get(0);
		
		String[] parametri = {
				vecchioAppuntamento.getData(),
				vecchioAppuntamento.getOrario(),
				vecchioAppuntamento.getDurata(),
				vecchioAppuntamento.getLuogo(),
				vecchioAppuntamento.getPersona()
		};

		switch(parametroDaModificare.toLowerCase().strip()) {
			case "data" -> parametri[0] = newValue;
			case "orario" -> parametri[1] = newValue; 
			case "durata" -> parametri[2] = newValue; 
			case "luogo" -> parametri[3] = newValue; 
			case "persona" -> parametri[4] = newValue; 
			default -> { return -3; }
		}

		try {	
			Appuntamento nuovoAppuntamento = new Appuntamento(parametri[0], parametri[1], parametri[2], parametri[3], parametri[4]);
			appuntamenti.set(appuntamenti.indexOf(vecchioAppuntamento), nuovoAppuntamento);
			if(!isAgenda(appuntamenti))  {
				appuntamenti.set(appuntamenti.indexOf(nuovoAppuntamento), vecchioAppuntamento);
				ordinaAppuntamenti(appuntamenti);
				return -1;
			}
				
			return 1;
		} 
		catch(AppuntamentoException e) {
			return -2;
		}
	}
	*/
	
	private int testModifica(Appuntamento oldApp, String newValue, String flag) {
		try {
			Appuntamento newApp = new Appuntamento(
					(flag.equals("data")) ? newValue : oldApp.getData(),
					(flag.equals("orario")) ? newValue : oldApp.getOrario(),
					(flag.equals("durata")) ? newValue : oldApp.getDurata(),
					(flag.equals("luogo")) ? newValue : oldApp.getLuogo(), 
					(flag.equals("persona")) ? newValue : oldApp.getPersona());
			
			if(!aggiungiAppuntamento(newApp)) {
				aggiungiAppuntamento(oldApp);
				return -1;
			}
			return 1;
		}
		catch(AppuntamentoException e) {
			aggiungiAppuntamento(oldApp);
			return -2;
		}
	}
	
	
	
	public int modificaAppuntamento(String dataApp, String orarioApp, String parametroDaModificare, String newValue) {
		ArrayList<Appuntamento> risultato = searchAppuntamentoPerDataOrario(dataApp, orarioApp);
		if(risultato.isEmpty()) return 0;
		Appuntamento vecchioAppuntamento = risultato.get(0);
		rimuoviPerDataOrario(dataApp, orarioApp);
		
		String parametro = parametroDaModificare.toLowerCase().strip();
		switch(parametro){
			case "data", "orario", "durata", "luogo", "persona" -> {
				return testModifica(vecchioAppuntamento, newValue, parametro);
			}
			default -> { 
				aggiungiAppuntamento(vecchioAppuntamento);
				return -3;
			}
		}
	}
	
	

	@Override
	public Iterator<Appuntamento> iterator() {
		return new IteratoreAgenda();
	}
	
	
	@Override
	public Agenda clone() {
		return new Agenda(nomeAgenda, appuntamenti);
	}
	
	
}
