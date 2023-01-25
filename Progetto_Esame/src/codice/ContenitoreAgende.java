
package codice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;
import jbook.util.PatternMatcher;


/**
 * La classe {@code ContenitoreAgende} è una classe per la gestione di più
 * oggetti {@link Agenda}.<br>
 * Essa è formata da un unico campo {@code ArrayList<Agenda>}, ovvero una lista di agende, e 
 * numerosi metodi per gestire tale lista.
 * <p>
 * È possibile gestire un numero pressoché illimitato di agende, con l'unico limite che non ci possono
 * essere due agende con lo stesso nome nel medesimo {@code ContenitoreAgende}.<br>
 * Tale limite ha lo scopo di evitare possibili ambiguità tra le agende esistenti.
 * <p>
 * Dato che ogni {@code Agenda} può essere importata da file e quest'ultimi possono presentare nel nome
 * caratteri maiuscoli, minuscoli, speciali e spazi ripetuti <em>(ricordiamo che il nome del file
 * è il nome che identifica l'agenda)</em>, il {@code ContenitoreAgende} non farà alcun 
 * controllo su di questi. Questo lascia molta libertà di scelta sul nome per l'agenda, tuttavia rende il controllo
 * sulla non ripetitività del nome <em>case-sensitive</em> e <strong>sensibile anche a eventuali spazi a inizio/fine stringa.</strong>
 * <p>
 * Le azioni possibili sulle agende sono: <ul>
 * <li> cercarle e selezionarle, tramite il metodo {@link #selezionaAgenda};
 * <li> aggiungerne di nuove, possibile grazie ai metodi {@link #aggiungiAgenda(Agenda)} e {@link #aggiungiAgenda(String)};
 * <li> rimuoverle tramite {@link #removeAgenda};
 * <li> modificarne il nome con {@link #modificaNomeAgenda}.</ul>
 * <p>
 * Per la lettura/scrittura delle agende da file, è possibile caricarle/salvarle una ad una con {@link #caricaAgendaDaFile} e {@link #salvaAgendaSuFile},
 * oppure tutte insieme da un'unica cartella con {@link #caricaMultiAgendeDaFiles} e {@link #salvaContenitoreSuFile}.<br>
 * La directory presa in considerazione, di default, è quella standard dichiarata nel Path per le agende 
 * <em>(vedi {@link Agenda#getFilesPath})</em>; a meno che non venga cambiato <em>run-time</em> tramite {@link Agenda#changePathToAgende}.
 * <p>
 * {@code ContenitoreAgende} implementa l'interfaccia {@link Iterable}, che lo rende iterabile sugli elementi di tipo {@code Agenda}.<br>
 * Non è però possibile rimuovere o aggiungere agende mentre si itera. Nel caso si tentasse di farlo, verrà lanciata un'eccezione di tipo
 * {@link ConcurrentModificationException}.
 * 
 * @see Agenda
 * @see Appuntamento
 * 
 * 
 * @author Nicolò Bianchetto
 * @author Kristian Rigo
 */


public class ContenitoreAgende implements Iterable<Agenda> {
	
	private ArrayList<Agenda> agende;
	
	/**
	 * Costruttore per un nuovo {@link ContenitoreAgende}.
	 * Ques'ultimo viene creato <em>vuoto.</em>
	 */
	public ContenitoreAgende() {
		agende = new ArrayList<>();
	}
	
	/**
	 * Ritorna la dimensione del {@code ContenitoreAgende}.
	 * 
	 * @return il numero {@code int} delle agende presenti nel contenitore.
	 */
	public int getDimensione() {
		return agende.size();
	}
	
	/**
	 * Ritorna una copia della lista di agende presenti in questo contenitore.
	 * 
	 * @return una copia della lista di agende di tipo {@code ArrayList<Agenda>}.
	 */
	
	public ArrayList<Agenda> getAgende() {
		return new ArrayList<>(agende);
	}
	

	private class IteratoreContenitoreAgende implements Iterator<Agenda> {
		private int indiceAgende;
		private int lastIndex;
		
		private IteratoreContenitoreAgende() {
			indiceAgende = 0;
			lastIndex = agende.size();
		}
		
		@Override
		public boolean hasNext() {
			return indiceAgende < lastIndex;
		}

		@Override
		public Agenda next() throws ConcurrentModificationException {
			if(lastIndex != agende.size()) throw new ConcurrentModificationException();
			return agende.get(indiceAgende++);
		}
	}
	
	/**
	 * Importa una singola {@code Agenda} da file e l'aggiunge al contenitore. L'importazione va a buon fine se la lettura
	 * da file ha successo e non esiste nel contenitore un'altra {@code Agenda} con lo stesso nome di quella
	 * letta dal file.<br>
	 * Il file viene letto all'interno della cartella specificata da {@link Agenda#getFilesPath}.
	 * 
	 * @param fileName il nome del file da cui caricare l'agenda.
	 * @return {@code true} se l'agenda è stata aggiunta al contenitore, {@code false} altrimenti.
	 * @throws IOException se si è verificata un'eccezione di I/O durante la lettura da file.
	 */
	
	public boolean caricaAgendaDaFile(String fileName) throws IOException {
		File file = new File(Agenda.getFilesPath(), fileName);	
		Agenda newAgenda = new Agenda(file);
		return (this.contains(newAgenda.getNomeAgenda())) ? false : agende.add(newAgenda);
	}
	
	/**
	 * Carica ogni {@code Agenda} salvata su file di tipo {@code txt} presente all'interno della cartella
	 * specificata da {@link Agenda#getFilesPath}.<br>
	 * Eventuali sottocartelle vengono ignorate.
	 * <p>Un'agenda non viene importata se condivide il nome con un'altra già presente nel contenitore.
	 * 
	 * @return {@code true} <em>se e solo se</em> vengono caricate <strong>tutte</strong> le agende presenti nella directory, {@code false} altrimenti.
	 * @throws IOException se si è verificata un'eccezione di I/O durante la lettura dai files.
	 */
	
	public boolean caricaMultiAgendeDaFiles() throws IOException {
		File cartella = new File(Agenda.getFilesPath());
		if(!cartella.exists()) throw new FileNotFoundException("Impossibile trovare la cartella per il caricamento delle Agende.");
		File[] listaFile = cartella.listFiles();
		boolean risultatoFinale = true;
		for(File file: listaFile) {
			if(!file.isDirectory() && PatternMatcher.create(".*\\.txt$", file.getName()).matches()) {
				if(!caricaAgendaDaFile(file.getName())) risultatoFinale = false;
			}
		}
		return risultatoFinale;
	}
	
	/**
	 * Salva una singola agenda del contenitore su un file {@code txt} all'interno 
	 * della directory indicata da {@link Agenda#getFilesPath}.
	 * <p>Tenta di salvare un'agenda anche se questa è impostata come <em>salvata</em>. Ciò
	 * rende questo metodo più sicuro rispetto a {@link #salvaContenitoreSuFile}.
	 * 
	 * @param nomeAgenda il nome dell'agenda da salvare su file.
	 * @return {@code true} se l'agenda è stata salvata su file, {@code false} se l'agenda non esiste.
	 * @throws IOException se si è verificata un'eccezione di I/O durante il salvataggio su file.
	 */
	
	public boolean salvaAgendaSuFile(String nomeAgenda) throws IOException {
		Agenda toSave;
		try {
			toSave = selezionaAgenda(nomeAgenda);
		} catch(NoSuchElementException e) { return false; }
		
		return toSave.salvaAgendaSuFile();
	}
	
	/**
	 * Salva l'intero contenitore su files di tipo {@code txt}, uno per ogni agenda, all'interno
	 * della directory indicata da {@link Agenda#getFilesPath}.
	 * <p>Se un'agenda è impostata come <em>salvata</em>, viene ignorata.
	 * 
	 * @return {@code true} se il salvataggio su files del contenitore è riuscito.
	 * @throws IOException se si è verificata un'eccezione di I/O durante il salvataggio su files.
	 */
	
	public boolean salvaContenitoreSuFile() throws IOException {
		for(Agenda elemento: this) {
			if(!elemento.isSaved()) elemento.salvaAgendaSuFile();
		}
		return true;
	}
	
	/**
	 * Controlla che tutte le agende presenti nel contenitore siano salvate su file.
	 * 
	 * @return {@code true} <em>se e solo se</em> ogni {@code Agenda} è impostata come <em>salvata</em>, {@code false} altrimenti.
	 */
	
	public boolean allSaved() {
		return agende.stream().allMatch( agenda -> agenda.isSaved() );
	}
	
	/**
	 * Rimuove ogni {@code Agenda} presente in questo {@code ContenitoreAgende}.
	 * 
	 * @return {@code true} se è stata rimossa almeno un'agenda, {@code false} se il contenitore era già vuoto.
	 */
	
	public boolean clear() {
		return agende.removeAll(agende);
	}
	
	/**
	 * Cerca un'agenda nel contenitore a partire da un nome ricevuto come parametro. Ritorna
	 * un riferimento a tale {@code Agenda} se trovata.
	 * 
	 * @param nomeAgenda il nome, di tipo {@code String}, con cui effettuare la ricerca.
	 * @return un riferimento all' {@code Agenda} trovata.
	 * @throws NoSuchElementException se non è stata trovata alcuna agenda a partire dal nome.
	 */
	
	public Agenda selezionaAgenda(String nomeAgenda) throws NoSuchElementException {
		for(Agenda elemento: this) {
			if(elemento.getNomeAgenda().equals(nomeAgenda)) return elemento;
		}
		throw new NoSuchElementException("Agenda non esiste!");
	}
	
	
	/**
	 * Cerca, a partire dal nome, un'agenda nel contenitore e ritorna {@code true} se questa è presente.
	 * @param nomeAgenda il nome dell'agenda di cui verificare la presenza.
	 * @return {@code true} se un'agenda col nome corrispondente è presente, {@code false} altrimenti.
	 */
	
	public boolean contains(String nomeAgenda) {
		return agende.stream().anyMatch( agenda -> agenda.getNomeAgenda().equals(nomeAgenda) );
	}
	
	/**
	 * Tenta di modificare il nome di un'agenda presente nel contenitore. Se il nuovo nome identifica
	 * un'altra agenda già presente, la modifica non viene eseguita.
	 * 
	 * @param nomeAgenda il nome dell'agenda da modificare.
	 * @param newName il nuovo nome da assegnare all'agenda.
	 * @return {@code true} se la modifica è avvenuta con successo, {@code false} se l'agenda da modificare non esiste oppure
	 * se il nuovo nome è già presente nel contenitore.
	 */
	
	public boolean modificaNomeAgenda(String nomeAgenda, String newName) {
		if(!this.contains(newName)) {
			try {
				Agenda agenda = selezionaAgenda(nomeAgenda);
				agenda.setNomeAgenda(newName);
				return true;
			} catch(NoSuchElementException e) { return false; }
		}
		return false;
	}
	
	
	private boolean actionToAgenda(String nomeAgenda, Predicate<Agenda> actionIfFound, Predicate<Agenda> actionIfNotFound) {
		try {
			return actionIfFound.test(selezionaAgenda(nomeAgenda));
		}
		catch(NoSuchElementException e) {
			return actionIfNotFound.test(new Agenda(nomeAgenda));
		}
	}
	
	/**
	 * Aggiunge una nuova {@code Agenda} al {@code ContenitoreAgende} assegnandoli il nome
	 * ricevuto come parametro. Se la stringa è vuota, si tenterà di aggiungere al contenitore
	 * un'agenda con il nome di default <em>"Agenda"</em>.<br>
	 * La nuova agenda viene creata vuota. 
	 * <p>Se esiste già un elemento con il medesimo nome all'interno del contenitore, l'operazione non viene eseguita.
	 * 
	 * @param nomeAgenda il nome da assegnare alla nuova agenda da aggiungere.
	 * @return {@code true} se l'aggiunta è avvenuta con successo, {@code false} altrimenti.
	 */
	
	public boolean aggiungiAgenda(String nomeAgenda) {
		return actionToAgenda(nomeAgenda, agenda -> false, agenda -> agende.add(agenda));
	}
	
	/**
	 * Aggiunge l' {@code Agenda} ricevuta come parametro al {@code ContenitoreAgende}.
	 * <p>L'operazione viene annullata se esiste già un'agenda con lo stesso nome all'interno del contenitore.
	 * 
	 * @param toAdd l' {@code Agenda} da aggiungere.
	 * @return {@code true} se l'operazione ha successo, {@code false} altrimenti.
	 */
	
	public boolean aggiungiAgenda(Agenda toAdd) {
		return (this.contains(toAdd.getNomeAgenda())) ? false : agende.add(toAdd);
	}
	
	/**
	 * Rimuove un' {@code Agenda} dal {@code ContenitoreAgende} a partire dal nome.<br>
	 * La rimozione ha successo <em>se e solo se</em> esiste nel contenitore un'agenda il cui nome combacia
	 * perfettamente con la stringa passata come parametro.
	 * 
	 * @param nomeAgenda il nome dell'agenda da rimuovere.
	 * @return {@code true} se la rimozione ha successo, {@code false} se l'agenda non esiste.
	 */
	
	
	public boolean removeAgenda(String nomeAgenda) {
		return actionToAgenda(nomeAgenda, agenda -> agende.remove(agenda), agenda -> false);
	}
	
	

	private String makeString(Function<Agenda, String> getString)  {
		ArrayList<Agenda> elenco = new ArrayList<>(agende);
		String finalString = "";
		elenco.sort( (first, second) -> first.getNomeAgenda().compareTo(second.getNomeAgenda()));
		for(Agenda elemento: elenco) finalString += getString.apply(elemento);
		return finalString;
	}
	
	/**
	 * Ritorna un elenco dei nomi delle agende presenti nel {@code ContenitoreAgende}. Se l'agenda è <em>non salvata</em>,
	 * viene aggiunto un asterisco identificativo a fine nome. I nomi delle agende sono elencati in ordine alfabetico.
	 * 
	 * @return un elenco, di tipo {@code String}, dei nomi delle agende presenti in questo {@code ContenitoreAgende}.
	 */
	
	public String elencaNomiAgende() {
		return makeString( agenda -> "- "+agenda.getNomeAgenda() + (agenda.isSaved() ? "":"*") + "\n");
	}
	
	/**
	 * Ritorna una rappresentazione in stringa di questo {@code ContenitoreAgende}. Le agende sono elencate in ordine
	 * alfabetico.
	 * 
	 * @return una rappresentazione di tipo {@code String} di questo {@code ContenitoreAgende}.
	 */
	
	@Override
	public String toString() {
		return makeString( agenda -> agenda.toString() + "\n------------------------------------------\n\n");
	}

	/**
	 * Ritorna un {@code IteratoreContenitoreAgende} per questo {@code ContenitoreAgende}, utile a iterare su ogni
	 * {@code Agenda}.
	 * <p>{@code IteratoreContenitoreAgende} implementa l'interfaccia {@link Iterator}.
	 * 
	 * @return un nuovo {@code IteratoreContenitoreAgende} di questo contenitore.
	 */
	
	@Override
	public Iterator<Agenda> iterator() {
		return new IteratoreContenitoreAgende();
	}

}
