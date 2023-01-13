package codice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
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
	private boolean saved;
	public static final String PATHNAME = "Agende_testuali/";
	
	
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
		saved = false;
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

	public Agenda(File file) throws IOException {
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		nomeAgenda = stripExtension(file.getName());
		appuntamenti = new ArrayList<>();

		String stringaAppuntamento;
		String [] errString = new String[2];
		errString[0] = errString[1] = "Attenzione. Impossibile leggere dal file " + file.getName() + " le seguenti righe poiché malformate:\n";
		while((stringaAppuntamento = reader.readLine()) != null) {
			Consumer<String> appendErr = (errLine) -> errString[1] += "Riga: " + errLine + "\n";
			String[] parametri = stringaAppuntamento.split("((min)?\\|)");
				try {
					if(parametri.length != 5 || !aggiungiAppuntamento(new Appuntamento(parametri))) appendErr.accept(stringaAppuntamento);						
				} catch(AppuntamentoException e) { appendErr.accept(stringaAppuntamento); }	
			} 	
		reader.close();	
		ordinaAppuntamenti(appuntamenti);
		saved = true;
		/* Nel peggiore dei casi, se non riesco a leggere nessuna riga, creo una Agenda vuota. 
		 * Lancio un'eccezione solo se il file non esiste o ho avuto IOException
		 */
		if(!errString[1].equals(errString[0]))	System.err.println(errString[1] + "Agenda " + nomeAgenda + " creata ignorando le righe elencate.");
	}
	
	private static String stripExtension(String filename) {
	    return filename.lastIndexOf(".") > 0 ? filename.substring(0, filename.lastIndexOf(".")) : filename;
	}

	public static void createPathToAgende() {
		new File(PATHNAME).mkdirs();
	}
	
	public boolean salvaAgendaSuFile() throws IOException {
		if(!new File(PATHNAME).exists()) createPathToAgende();
		File file = new File(PATHNAME, nomeAgenda + ".txt");
		if(!file.exists()) file.createNewFile();
		BufferedWriter br = new BufferedWriter(new FileWriter(file));
		br.write(elencaAppuntamenti(appuntamenti));
		br.close();
		saved = true;
		return saved;
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
		saved = false;
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
	
	private boolean rimuoviGenerico(BooleanSupplier removeBy) {
		boolean risultato = removeBy.getAsBoolean();
		if(risultato) saved = false;
		return risultato;
	}
	
	public boolean rimuoviPerPersona(String nome) {
		return rimuoviGenerico( () -> appuntamenti.removeAll(searchAppuntamentoPerPersona(nome)));
	}
	
	public boolean rimuoviPerData(String data) {
		return rimuoviGenerico( () -> appuntamenti.removeAll(searchAppuntamentoPerData(data)));
	}
	
	public boolean rimuoviPerDataOrario(String data, String orario) {
		return rimuoviGenerico( () -> appuntamenti.removeAll(searchAppuntamentoPerDataOrario(data, orario)));
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
				appuntamenti.add(oldApp);
				ordinaAppuntamenti(appuntamenti);
				return -1;
			}
			
			return 1;
		}
		catch(AppuntamentoException e) {
			appuntamenti.add(oldApp);
			ordinaAppuntamenti(appuntamenti);
			return -2;
		}
	}
	
	
	public int modificaAppuntamento(String dataApp, String orarioApp, String parametroDaModificare, String newValue) {
		ArrayList<Appuntamento> risultato = searchAppuntamentoPerDataOrario(dataApp, orarioApp);
		if(risultato.isEmpty()) return 0;
		Appuntamento vecchioAppuntamento = risultato.get(0);
		appuntamenti.remove(vecchioAppuntamento);
		
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
	
	public boolean isSaved() {
		return saved;
	}
	
	@Override
	public Iterator<Appuntamento> iterator() {
		return new IteratoreAgenda();
	}
	
	
	@Override
	public Agenda clone() {
		return new Agenda(nomeAgenda, appuntamenti);
	}
	
	@Override
	public boolean equals(Object object) {
		if(object == null || object.getClass() != this.getClass()) return false;
		Agenda other = (Agenda) object;
		return (other.getNomeAgenda().equals(this.nomeAgenda) && this.appuntamenti.equals(other.getAppuntamenti()));
	}
	
	
}
