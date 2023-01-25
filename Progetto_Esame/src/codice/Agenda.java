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

import jbook.util.PatternMatcher;

/**
 * La classe {@code Agenda} rappresenta una semplice classe per gestire
 * un insieme di appuntamenti.
 * <p>
 * Ogni {@code Agenda} è formata da:<ul>
 * <li> un <strong>nome</strong> nel formato {@link String} che la identifica;<br>
 * <li> un {@code ArrayList<Appuntamento>}, ovvero una lista di oggetti {@link Appuntamento}.</ul>
 * <p>
 * Durante il suo ciclo di vita, ogni {@code Agenda} ordina automaticamente (in base alla data e all'orario di inizio)
 * gli appuntamenti al suo interno.
 * <p>
 * Un oggetto {@code Agenda} accetta un {@code Appuntamento} se e solo se quest'ultimo è compatibile con gli altri già presenti.
 * Per essere compatibile, un appuntamento non può esistere nel medesimo lasso temporale di un altro.<br>
 * Ad esempio, se un appuntamento dell'agenda inizia il {@code 20-04-2004} alle {@code 18-00} e dura {@code 60} minuti, non è possibile inserire
 * nell'agenda un altro appuntamento che inizia alle {@code 18-30} del medesimo giorno.
 * <p>
 * Inoltre, ogni {@code Agenda} può venir impostata su <em>salvata</em> o <em>non salvata</em>
 * a seconda se quest'ultima venga scritta su file oppure modificata durante l'esecuzione. Le agende non
 * importate da file vengono sempre inizializzate come <em>non salvate</em>.
 * <p>
 * A tal proposito, le agende condividono un <em>pathname</em> che indica il percorso in cui vengono
 * salvate su file di testo {@code txt}. Tale percorso, di base, è la cartella {@code Agende_testuali/}
 * presente nella directory di questo progetto. Se tale cartella non esiste può venire creata
 * tramite il metodo statico {@link #createPathToAgende} oppure direttamente durante un tentativo
 * di salvataggio su file. È anche possibile modificare tale percorso usando il metodo {@link #changePathToAgende}
 * durante l'esecuzione.
 * <p>
 * <strong>N.B. :</strong> le agende vengono salvate SOLO su file di tipo {@code txt}. È possibile importarle anche da file
 * di altri formati, ad esempio {@code md} o {@code docx}, tuttavia, in questo caso, verranno inizializzate come <em>non salvate</em>
 * nel rispetto di questo vincolo. <em>È stato introdotto questo limite per semplicità di progettazione, evitando così di dover
 * gestire la grande quantità di estensioni ormai esistenti ed evitare conflitti con file duplicati.</em>
 * <p>
 * La classe {@code Agenda} implementa l'interfaccia {@link Iterable}, che la rende iterabile sugli elementi di classe {@code Appuntamento}.
 * Mentre si itera sull'agenda, tuttavia, non è possibie modificarne il contenuto (rimuovendo o aggiungendo appuntamenti). Nel caso ciò dovesse
 * accadere, verrà sollevata un'eccezione {@link ConcurrentModificationException}.
 * <p>
 * Per ogni {@code Agenda} sono disponibili diversi metodi. Vi sono i metodi di aggiunta appuntamenti, rimozione appuntamenti,
 * ricerca appuntamenti e modifica appuntamenti. Le ricerche e le rimozioni sono possibili in diverse varianti. È possibile cercare e/o
 * eliminare tutti gli elementi che condividono lo stesso nome per la persona con cui si ha appuntamento, oppure che condividono la stessa data.<br>
 * È inoltre possibile effettuare ricerche e rimozioni più precise, tramite {@link #searchAppuntamentoPerDataOrario} e 
 * {@link #rimuoviPerDataOrario}, selezionando il singolo appuntamento identificato dalla data e orario passati come parametri.
 * <p>
 * Sono presenti ulteriori metodi, come {@link #isAgenda}, {@link #contains} e {@link #isCompatible}, 
 * utili a lavorare con oggetti di classe {@code Appuntamento}.
 * <p>
 * 
 * @see Appuntamento
 * @see ContenitoreAgende
 * 
 * @author Nicolò Bianchetto
 * @author Kristian Rigo
 */


public class Agenda implements Iterable<Appuntamento> {
	
	private String nomeAgenda;
	private ArrayList<Appuntamento> appuntamenti;
	private boolean saved;
	private static String textFilesPathName = "Agende_testuali/";
	
	
	private class IteratoreAgenda implements Iterator<Appuntamento> {
		private int indiceContatti;
		private int lastIndex;
		
		private IteratoreAgenda() {
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
	
	/**
	 * Crea una nuova Agenda assegnandole un nome e un arraylist di appuntamenti.
	 * <p>Se il nome è una stringa vuota, assegna all'agenda il nome di default <em>"Agenda"</em>.
	 * <p>Se l'arraylist non può identificare un'agenda (cioè vi sono appuntamenti non compatibili con gli altri), crea un'agenda vuota.
	 * <p>L'agenda viene creata come <em>non salvata</em>.
	 * 
	 * @param nomeAgenda stringa che identifica l'agenda
	 * @param appuntamenti arraylist che descrive l'insieme degli appuntamenti di un'agenda
	 */
	
	public Agenda(String nomeAgenda, ArrayList<Appuntamento> appuntamenti) {
		this.nomeAgenda = (nomeAgenda.isEmpty()) ? "Agenda": nomeAgenda;
		this.appuntamenti = (isAgenda(appuntamenti)) ? new ArrayList<>(appuntamenti) : new ArrayList<>();
		saved = false;
		ordinaAppuntamenti(this.appuntamenti);
	}
	
	/**
	 * Crea una nuova Agenda vuota assegnandole un nome.
	 * <p>Se il nome è una stringa vuota, assegna all'agenda il nome di default <em>"Agenda"</em>.
	 * <p>L'agenda viene creata come <em>non salvata</em>.
	 * 
	 * @param nomeAgenda stringa che identifica l'agenda
	 */
	
	public Agenda(String nomeAgenda) {
		this(nomeAgenda, new ArrayList<>());
	}
	
	/**
	 * Crea una nuova Agenda vuota assegnandole il nome di default <em>"Agenda"</em>.
	 * <p>L'agenda viene creata come <em>non salvata</em>.
	 * 
	 */
	
	public Agenda() {
		this("Agenda");
	}
	
	/**
	 * Crea una nuova Agenda assegnandole il nome di default <em>"Agenda"</em> e un arraylist di appuntamenti.
	 * <p>Se l'arraylist non può identificare un'agenda (cioè vi sono appuntamenti non compatibili con gli altri), crea un'agenda vuota.
	 * <p>L'agenda viene creata come <em>non salvata</em>.
	 * 
	 * @param appuntamenti {@code ArrayList} che descrive l'insieme degli appuntamenti di un'agenda.
	 */
	
	public Agenda(ArrayList<Appuntamento> appuntamenti) {
		this("Agenda", appuntamenti);
	}
	
	/**
	 * Crea una nuova {@code Agenda} importandola da un {@code File}.
	 * Assegna al nome dell'{@code Agenda} il nome del {@code File}, rimuovendo eventuali estensioni.
	 * Crea un'arraylist di appuntamenti leggendo le righe dal {@code File} 
	 * se sono nel corretto formato {@code dd-MM-uuuu|HH-mm|minuti|luogo|persona}
	 * <p>
	 * Se vi sono righe malformate, quest'ultime verranno ignorate. 
	 * Alla fine verrà stampato un messaggio d'errore nello {@code Standard Error}
	 * contenente le righe non lette.
	 * <p>
	 * <strong>Attenzione:</strong> le agende importate da file verranno impostate come
	 * <em>salvate</em> se e solo se sono state importate da un file di testo {@code txt}.
	 * Questo perché le agende vengono salvate su file <strong>soltanto</strong> in quel formato.
	 * 
	 * @param file la rappresentazione astratta del file da cui leggere.
	 * @throws IOException se si è verificata un'eccezione di I/O durante la lettura da file.
	 */

	public Agenda(File file) throws IOException {
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		nomeAgenda = stripExtension(file.getName());
		appuntamenti = new ArrayList<>();

		String stringaAppuntamento;
		String [] errString = new String[2];
		errString[0] = errString[1] = "Attenzione. Impossibile leggere dal file " + file.getName() + " le seguenti righe poiché malformate:\n";
		Consumer<String> appendErr = (errLine) -> errString[1] += "Riga: " + errLine + "\n";
		while((stringaAppuntamento = reader.readLine()) != null) {
			String[] parametri = stringaAppuntamento.split("((min)?\\|)");
				try {
					if(parametri.length != 5 || !aggiungiAppuntamento(new Appuntamento(parametri))) appendErr.accept(stringaAppuntamento);						
				} catch(AppuntamentoException e) { appendErr.accept(stringaAppuntamento); }	
			} 	
		reader.close();	
		ordinaAppuntamenti(appuntamenti);
		saved = (PatternMatcher.create(".*\\.txt$", file.getName()).matches()) ? true:false;

		if(!errString[1].equals(errString[0]))	System.err.println(errString[1] + "Agenda " + nomeAgenda + " creata ignorando le righe elencate.");
	}
	
	private static String stripExtension(String filename) {
	    return filename.lastIndexOf(".") > 0 ? filename.substring(0, filename.lastIndexOf(".")) : filename;
	}
	
	private static void ordinaAppuntamenti(ArrayList<Appuntamento> appuntamenti) {
		appuntamenti.sort( (first, second) -> first.getDataTimeInizio().compareTo(second.getDataTimeInizio()));
	}
	
	/**
	 * Crea, se non esiste, il Path per il salvataggio delle agende.
	 * 
	 * @return {@code true} se ha creato tutte le cartelle
	 * del Path, altrimenti {@code false}.
	 */

	public static boolean createPathToAgende() {
		return new File(textFilesPathName).mkdirs();
	}
	
	/**
	 * Cambia il Path per il salvataggio delle agende su file.
	 * <strong>Attenzione:</strong> ciò potrebbe portare a
	 * problemi di lettura/scrittura da file.
	 * 
	 * @param pathname stringa che descrive il Path.
	 * @return <strong>oldPath</strong>, il vecchio Path precedentemente memorizzato nel formato di stringa.
	 */
	
	public static String changePathToAgende(String pathname) {
		String oldPath = textFilesPathName;
		textFilesPathName = pathname;
		return oldPath;
	}
	
	/**
	 * Ritorna il Path per il salvataggio delle agende su file.
	 * Se questo non è stato cambiato, di base è la cartella <em>"Agende_testuali/"</em>
	 * presente nel percorso di questo progetto.
	 * 
	 * @return il nome del Path di tipo {@code String}.
	 */
	
	public static String getFilesPath() {
		return textFilesPathName;
	}
	
	/**
	 * Salva l'agenda su un file di testo nel formato {@code txt}.
	 * Imposta il valore dell'agenda come <em>salvata</em>.
	 * <p>
	 * Il salvataggio avviene all'interno del <strong>PathName</strong> impostato
	 * per le Agende. Per cui, se tale percorso non esiste, viene creato.
	 *
	 * @return {@code true} se il salvataggio è andato a buon fine.
	 * @throws IOException se si verifica un'eccezione di I/O durante il tentativo di salvataggio.
	 */
	
	public boolean salvaAgendaSuFile() throws IOException {
		if(!new File(textFilesPathName).exists()) createPathToAgende();
		File file = new File(textFilesPathName, nomeAgenda + ".txt");
		if(!file.exists()) file.createNewFile();
		BufferedWriter br = new BufferedWriter(new FileWriter(file));
		br.write(elencaAppuntamenti(appuntamenti));
		br.close();
		saved = true;
		return saved;
	}
	
	/**
	 * Verifica se una lista di appuntamenti potrebbe identificare un'agenda.
	 * Ritorna {@code true} se ogni appuntamento è compatibile con gli altri.
	 * 
	 * @param appuntamenti {@code ArrayList} di appuntamenti da testare.
	 * @return {@code true} se la lista di appuntamenti può identificare un'agenda, {@code false} altrimenti.
	 */
	
	public static boolean isAgenda(ArrayList<Appuntamento> appuntamenti) {
		ArrayList<Appuntamento> test = new ArrayList<>(appuntamenti);
		ordinaAppuntamenti(test);
		for(int i = 0; i < test.size() - 1; i++) {
			if(!test.get(i).isBefore(test.get(i+1))) return false;
		}
		return true;
	}
	
	/**
	 * Ritorna il nome dell'agenda.
	 * 
	 * @return il nome dell'agenda di tipo {@code String}.
	 */

	public String getNomeAgenda() {
		return nomeAgenda;
	}
	
	/**
	 * Modifica il nome dell'Agenda. Se la stringa passata come parametro è vuota,
	 * imposta il nome dell'agenda al default <em>"Agenda"</em>.
	 * <p>Imposta l'agenda come <em>non salvata</em>.
	 * 
	 * @param nomeAgenda nuovo nome dell'agenda di tipo {@code String}.
	 */

	public void setNomeAgenda(String nomeAgenda) {
		this.nomeAgenda = nomeAgenda.isEmpty() ? "Agenda" : nomeAgenda;
		saved = false;
	}
	
	/**
	 * Ritorna la lista degli appuntamenti.
	 * 
	 * @return una copia dell'{@code ArrayList} contenente gli appuntamenti.
	 */

	public ArrayList<Appuntamento> getAppuntamenti() {
		return new ArrayList<>(appuntamenti);
	}
	
	/**
	 * Ritorna la dimensione dell'agenda.
	 * 
	 * @return il numero {@code int} di appuntamenti dell'agenda 
	 */
	
	public int getDimensioneAgenda() {
		return appuntamenti.size();
	}

	private ArrayList<Appuntamento> searchAppuntamentoGenerico(Predicate <Appuntamento> predicato) {
		return (ArrayList<Appuntamento>) appuntamenti.stream().filter(predicato).collect(Collectors.toList());
	}
	
	/**
	 * Effettua una ricerca degli appuntamenti basata sul nome della persona con cui
	 * si ha l'appuntamento. Ritorna una lista degli appuntamenti trovati.<br>
	 * La lista sarà vuota se non è stato trovato alcun appuntamento.
	 * 
	 * @param nome il nome della persona con cui si ha l'appuntamento.
	 * @return un {@code ArrayList} degli appuntamenti trovati.
	 */
	
	public ArrayList<Appuntamento> searchAppuntamentoPerPersona(String nome) {	
		return searchAppuntamentoGenerico( appuntamento -> appuntamento.matchPersona(nome));
	}
	
	/**
	 * Effettua una ricerca degli appuntamenti basata sulla data degli stessi.
	 * Ritorna una lista degli appuntamenti trovati.<br>La lista sarà vuota se
	 * non è stato trovato alcun appuntamento.
	 * 
	 * @param data la data dell'appuntamento nel formato {@code dd-MM-uuuu}.
	 * @return un {@code ArrayList} degli appuntamenti trovati.
	 */
	
	public ArrayList<Appuntamento> searchAppuntamentoPerData(String data) {
		return searchAppuntamentoGenerico( appuntamento -> appuntamento.matchData(data));
	}
	
	/**
	 * Effettua una ricerca di un appuntamento basata sulla data e l'orario di quest'ultimo.
	 * <p>Siccome non possono esistere due appuntamenti che iniziano nel medesimo momento, questo
	 * metodo è utile per cercare con precisione un singolo appuntamento.<br>
	 * Ritorna una lista contenente al più un elemento. La lista sarà vuota se
	 * l'appuntamento cercato non esiste.
	 * 
	 * @param data la data dell'appuntamento nel formato {@code dd-MM-uuuu}.
	 * @param orario l'orario dell'appuntamento nel formato {@code HH-mm}.
	 * @return un {@code ArrayList} contenente l'appuntamento cercato se questo esiste, vuoto altrimenti.
	 */
	
	public ArrayList<Appuntamento> searchAppuntamentoPerDataOrario(String data, String orario) {
		return searchAppuntamentoGenerico( appuntamento -> appuntamento.matchDataOrario(data, orario));
	}
	
	/**
	 * Effettua una ricerca di un appuntamento basata sulla data e l'orario di quest'ultimo.<br>
	 * La data e l'orario sono identificati da un'unica stringa, dunque devono essere separati da
	 * almeno uno spazio. Eventuali ulteriori valori dopo l'orario (se separati da spazi) vengono ignorati.
	 * <p>Siccome non possono esistere due appuntamenti che iniziano nel medesimo momento, questo
	 * metodo è utile per cercare con precisione un singolo appuntamento.<br>
	 * Ritorna una lista contenente al più un elemento. La lista sarà vuota se
	 * l'appuntamento cercato non esiste.
	 * 
	 * @param dataOrario la data e l'orario dell'appuntamento nel formato {@code dd-MM-uuuu HH-mm}.
	 * @return un {@code ArrayList} contenente l'appuntamento cercato se questo esiste, vuoto altrimenti.
	 */
	
	public ArrayList<Appuntamento> searchAppuntamentoPerDataOrario(String dataOrario) {
		String[] parametri = dataOrarioSplit(dataOrario);
		return searchAppuntamentoGenerico( appuntamento -> appuntamento.matchDataOrario(parametri[0], parametri[1]));
	}
	
	/**
	 * Ritorna {@code true} se l'agenda contiene l'appuntamento passato come parametro.<br>
	 * Più precisamente, ritorna {@code true} se e solo se l'agenda contiene un appuntamento
	 * avente tutti i parametri uguali a quelli dell'appuntamento cercato, tale che 
	 * {@code Appuntamento.equals(Appuntamento other)} sia {@code true}.
	 * 
	 * @param appointment l'appuntamento da cercare nell'agenda.
	 * @return {@code true} se l'agenda contiene l'appuntamento cercato, {@code false} altrimenti.
	 */
	
	public boolean contains(Appuntamento appointment) {
		return appuntamenti.contains(appointment);
	}
	
	/**
	 * Ritorna {@code true} se vi è un appuntamento nell'agenda avente stessa data e orario
	 * passati come parametri.
	 * 
	 * @param data data dell'appuntamento da cercare nel formato {@code dd-MM-uuuu}.
	 * @param orario orario dell'appuntamento da cercare nel formato {@code HH-mm}.
	 * @return {@code true} se l'agenda contiene un appuntamento con stessa data e orario, {@code false} altrimenti.
	 */
	
	public boolean contains(String data, String orario) {
		return !searchAppuntamentoPerDataOrario(data, orario).isEmpty();
	}
	
	/**
	 * Ritorna {@code true} se vi è un appuntamento nell'agenda avente stessa data e orario
	 * passati come parametro. La data e l'orario devono essere separati da almeno uno spazio.
	 * <br>Eventuali ulteriori valori (se separati da spazio) vengono ignorati.
	 * 
	 * @param dataOrario data e orario dell'appuntamento da cercare nel formato {@code dd-MM-uuuu HH-mm}.
	 * @return {@code true} se l'agenda contiene un appuntamento con stessa data e orario, {@code false} altrimenti.
	 */
	
	public boolean contains(String dataOrario) {
		return !searchAppuntamentoPerDataOrario(dataOrario).isEmpty();
	}
	
	/**
	 * Verifica che un appuntamento sia compatibile con l'agenda. Ritorna {@code true} se
	 * esso non è in conflitto con nessun appuntamento in agenda.
	 * 
	 * @param appointment l'appuntamento da testare
	 * @return {@code true} se l'appuntamento è compatibile con l'agenda, {@code false} altrimenti.
	 */
	
	public boolean isCompatible(Appuntamento appointment) {
		return appuntamenti.stream().allMatch( elemento -> elemento.isCompatible(appointment) );
	}


	private String elencaAppuntamenti(ArrayList<Appuntamento> lista) {
		String stringaAppuntamenti = "";
		for(Appuntamento appointment: lista) stringaAppuntamenti+= appointment.toString();
		return stringaAppuntamenti;
	}
	
	/**
	 * Ritorna, nel formato di stringa, un elenco degli appuntamenti dell'agenda, se il nome cercato
	 * combacia con il nome della persona salvato nell'appuntamento.<br>La
	 * stringa sarà vuota se nessun appuntamento è stato trovato.
	 * 
	 * @param nome il nome della persona da cui elencare gli appuntamenti
	 * @return l'elenco degli appuntamenti trovati di tipo {@code String}.
	 */
	
	public String elencaPerPersona(String nome) {
		return elencaAppuntamenti(searchAppuntamentoPerPersona(nome));
	}
	
	/**
	 * Ritorna, nel formato di stringa, un elenco degli appuntamenti dell'agenda, se il data cercata
	 * combacia con la data dell'appuntamento.<br>
	 * La stringa sarà vuota se nessun appuntamento è stato trovato.
	 * 
	 * @param data la data da cui elencare gli appuntamenti
	 * @return l'elenco degli appuntamenti trovati di tipo {@code String}.
	 */
	
	public String elencaPerData(String data) {
		return elencaAppuntamenti(searchAppuntamentoPerData(data));
	}
	
	/**
	 * Ritorna una rappresentazione in stringa dell'agenda.<br>
	 * La stringa descriverà solo il nome dell'agenda se quest'ultima è vuota.
	 * 
	 * @return una rappresentazione dell'agenda di tipo {@code String}.
	 */
	
	@Override
	public String toString() {
		return "Agenda: " + nomeAgenda + "\n" + elencaAppuntamenti(appuntamenti);
	}
	

	/**
	 * Prova ad aggiungere un appuntamento nell'agenda. Se l'appuntamento non è
	 * compatibile con quest'ultima, non verrà aggiunto.<br>
	 * Se l'aggiunta va a buon fine, l'agenda viene impostata a <em>non salvata</em> e ritorna {@code true}, 
	 * altrimenti ritorna {@code false}. 
	 * 
	 * @param appointment l'appuntamento da aggiungere
	 * @return {@code true} se l'aggiunta ha avuto esito positivo, {@code false} altrimenti.
	 */
	
	public boolean aggiungiAppuntamento(Appuntamento appointment) {
		if(!this.isCompatible(appointment)) return false;
		appuntamenti.add(appointment);
		ordinaAppuntamenti(appuntamenti);
		saved = false;
		return true;
	}
	
	/**
	 * Prova ad aggiungere un appuntamento nell'agenda creandolo nuovo a partire dai parametri ricevuti.<br>
	 * Se i parametri non sono validi oppure l'appuntamento non è compatibile con l'agenda, non verrà aggiunto.<br>
	 * Se l'aggiunta va a buon fine, l'agenda viene impostata a <em>non salvata</em> e ritorna {@code true}, 
	 * altrimenti ritorna {@code false}. 
	 * 
	 * <em><p>Per maggiori informazioni sui controlli dei parametri vedi anche:</em><br>
	 * {@link Appuntamento.ControlloDati#controlloPer(Appuntamento.ControlloDati.TipoControllo, String)}<br>
	 * 
	 * @param data la data dell'appuntamento da aggiungere nel formato {@code dd-MM-uuuu}.
	 * @param orario l'orario dell'appuntamento da aggiungere nel formato {@code HH-mm}.
	 * @param durata la durata dell'appuntamento da aggiungere di tipo {@code String}.
	 * @param luogo il luogo nell'appuntamento da aggiungere di tipo {@code String}.
	 * @param nomePersona il nome della persona con cui si ha l'appuntamento di tipo {@code String}.
	 * 
	 * @return {@code true} se l'aggiunta ha avuto esito positivo, {@code false} altrimenti.
	 */
	
	
	public boolean aggiungiAppuntamento(String data, String orario, String durata, String luogo, String nomePersona) {
		try {
			return aggiungiAppuntamento(new Appuntamento(data, orario, durata, luogo, nomePersona));
		}
		catch(AppuntamentoException e) { 
			return false; 
		}
	}
	
	
	/**
	 * Prova ad aggiungere un appuntamento nell'agenda creandolo nuovo a partire dai parametri ricevuti.<br>
	 * Se i parametri non sono validi oppure l'appuntamento non è compatibile con l'agenda, non verrà aggiunto.<br>
	 * Se l'aggiunta va a buon fine, l'agenda viene impostata a <em>non salvata</em> e ritorna {@code true}, 
	 * altrimenti ritorna {@code false}. 
	 * <p>
	 * La data e l'orario sono identificati da un'unica stringa, per cui devono essere separati da almeno
	 * uno spazio. Eventuali altri valori (se separati da spazi) vengono ignorati.
	 * 
	 * <em><p>Per maggiori informazioni sui controlli dei parametri vedi anche:</em><br>
	 * {@link Appuntamento.ControlloDati#controlloPer(Appuntamento.ControlloDati.TipoControllo, String)}<br>
	 * 
	 * @param dataOrario la data e l'orario dell'appuntamento da aggiungere nel formato {@code dd-MM-uuuu HH-mm}.
	 * @param durata la durata dell'appuntamento da aggiungere di tipo {@code String}.
	 * @param luogo il luogo nell'appuntamento da aggiungere di tipo {@code String}.
	 * @param nomePersona il nome della persona con cui si ha l'appuntamento di tipo {@code String}.
	 * 
	 * @return {@code true} se l'aggiunta ha avuto esito positivo, {@code false} altrimenti.
	 */
	
	
	public boolean aggiungiAppuntamento(String dataOrario, String durata, String luogo, String nomePersona) {
		String[] splittati = dataOrarioSplit(dataOrario);
		return aggiungiAppuntamento(splittati[0], splittati[1], durata, luogo, nomePersona);
	}
	
	private boolean rimuoviGenerico(BooleanSupplier removeBy) {
		boolean risultato = removeBy.getAsBoolean();
		if(risultato) saved = false;
		return risultato;
	}
	
	/**
	 * Rimuove gli appuntamenti che condividono la stessa persona identificata
	 * dalla stringa passata come parametro.<br>Ritorna {@code true} e imposta l'agenda a
	 * <em>non salvata</em> se la rimozione ha avuto esito positivo.
	 * 
	 * @param nome il nome della persona da cui eliminare gli appuntamenti.
	 * @return {@code true} se è stato eliminato almeno un appuntamento, {@code false} altrimenti.
	 */
	
	public boolean rimuoviPerPersona(String nome) {
		return rimuoviGenerico( () -> appuntamenti.removeAll(searchAppuntamentoPerPersona(nome)));
	}
	
	/**
	 * Rimuove gli appuntamenti che condividono la stessa data identificata
	 * dalla stringa passata come parametro.<br>Ritorna {@code true} e imposta l'agenda a
	 * <em>non salvata</em> se la rimozione ha avuto esito positivo.
	 * 
	 * @param data la data degli appuntamenti da eliminare nel formato {@code dd-MM-uuuu}.
	 * @return {@code true} se è stato eliminato almeno un appuntamento, {@code false} altrimenti.
	 */
	
	public boolean rimuoviPerData(String data) {
		return rimuoviGenerico( () -> appuntamenti.removeAll(searchAppuntamentoPerData(data)));
	}
	
	/**
	 * Rimuove l'appuntamento identificato dalla data e dall'orario passati come parametri.
	 * <br>Ritorna {@code true} e imposta l'agenda a <em>non salvata</em> se la rimozione ha avuto esito positivo.
	 * 
	 * @param data la data dell'appuntamento da eliminare nel formato {@code dd-MM-uuuu}.
	 * @param orario l'orario dell'appuntamento da eliminare nel formato {@code HH-mm}.
	 * @return {@code true} se l'appuntamento è stato eliminato, {@code false} altrimenti.
	 */
	
	public boolean rimuoviPerDataOrario(String data, String orario) {
		return rimuoviGenerico( () -> appuntamenti.removeAll(searchAppuntamentoPerDataOrario(data, orario)));
	}
	
	/**
	 * Rimuove l'appuntamento identificato dalla data e dall'orario passati come unico parametro.
	 * <br>Ritorna {@code true} e imposta l'agenda a <em>non salvata</em> se la rimozione ha avuto esito positivo.
	 * 
	 * <p>La data e l'orario sono identificati da un'unica stringa, per cui devono essere separati da almeno
	 * uno spazio. Eventuali altri valori (se separati da spazi) vengono ignorati.
	 * 
	 * @param dataOrario la data e l'orario dell'appuntamento da eliminare nel formato {@code dd-MM-uuuu HH-mm}.
	 * @return {@code true} se l'appuntamento è stato eliminato, {@code false} altrimenti.
	 */
	
	public boolean rimuoviPerDataOrario(String dataOrario) {
		return rimuoviGenerico( () -> appuntamenti.removeAll(searchAppuntamentoPerDataOrario(dataOrario)));
	}
	
	/**
	 * Rimuove tutti gli appuntamenti dall'agenda.<br>
	 * Imposta l'agenda a <em>non salvata</em> e ritorna {@code true} a meno che
	 * l'agenda non sia vuota.
	 * 
	 * @return {@code true} se è stato eliminato almeno un appuntamento, {@code false} se l'agenda è vuota.
	 */
	
	public boolean rimuoviTutto() {
		return rimuoviGenerico( () -> appuntamenti.removeAll(appuntamenti));
	}

	
	private int testModifica(Appuntamento oldApp, String newValue, String flag) {
		try {
			appuntamenti.remove(oldApp);
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
	
	/**
	 * Tenta di modificare un appuntamento presente nell'agenda.
	 * <p>Questo metodo riceve come parametri la data e l'orario 
	 * dell'appuntamento da identificare, il nome del parametro 
	 * da modificare <em>(data, orario, durata, luogo, persona)</em> e il
	 * nuovo valore da inserire.
	 * <p>Se la modifica è andata a buon fine, imposta l'agenda come <em>non salvata</em>.
	 * <p>Per far sì che la modifica vada a buon fine devono essere verificate le seguenti condizioni:<ul>
	 * <li> L'appuntamento deve esistere;<br>
	 * <li> Il nome del parametro deve essere valido (il controllo è <em>case-insensitive</em> e ignora eventuali spazi
	 * all'inizio e alla fine);<br>
	 * <li> La modifica non deve rendere l'appuntamento incompatibile con l'agenda;<br>
	 * <li> Il nuovo valore deve avere il formato corretto a seconda del parametro che va a modificare.</ul>
	 * <em><br>Per maggiori informazioni sui controlli dei parametri vedi anche:</em><br>
	 * {@link Appuntamento.ControlloDati#controlloPer(Appuntamento.ControlloDati.TipoControllo, String)}<br>
	 * 
	 * 
	 * @param dataApp data dell'appuntamento da modificare nel formato {@code dd-MM-uuuu}
	 * @param orarioApp orario dell'appuntamento da modificare nel formato {@code HH-mm}
	 * @param parametroDaModificare nome del parametro da modificare di tipo {@code String}.
	 * @param newValue nuovo valore da inserire di tipo {@code String}.
	 * 
	 * @return {@code 1} se la modifica è andata a buon file.<br>
	 * {@code 0} se non è stato possibile trovare l'appuntamento da modificare.<br>
	 * {@code -1} se la modifica non è possibile poiché porterebbe l'appuntamento a non essere compatibile con l'agenda.<br>
	 * {@code -2} se il nuovo valore è in un formato non corretto.<br>
	 * {@code -3} se il nome del <em>parametroDaModificare</em> non identifica alcun campo.
	 * 
	 */
	
	public int modificaAppuntamento(String dataApp, String orarioApp, String parametroDaModificare, String newValue) {
		ArrayList<Appuntamento> risultato = searchAppuntamentoPerDataOrario(dataApp, orarioApp);
		if(risultato.isEmpty()) return 0;
		Appuntamento vecchioAppuntamento = risultato.get(0);
		
		String parametro = parametroDaModificare.toLowerCase().strip();
		switch(parametro){
			case "data", "orario", "durata", "luogo", "persona" -> {
				return testModifica(vecchioAppuntamento, newValue, parametro);
			}
			default -> { return -3; }
		}
	}
	
	
	/**
	 * Tenta di modificare un appuntamento presente nell'agenda.
	 * <p>Questo metodo riceve come parametri una singola stringa che descrive la data e l'orario 
	 * dell'appuntamento da identificare, il nome del parametro 
	 * da modificare <em>(data, orario, durata, luogo, persona)</em> e il
	 * nuovo valore da inserire.
	 * <p>Se la modifica è andata a buon fine, imposta l'agenda come <em>non salvata</em>.
	 * <p><strong>N.B. : </strong> la data e l'orario devono essere separati da almeno uno spazio.<br>
	 * Eventuali altri valori dopo l'orario (se separati da spazi) vengono ignorati.
	 * <p>Per far sì che la modifica vada a buon fine devono essere verificate le seguenti condizioni:<ul>
	 * <li> L'appuntamento deve esistere;<br>
	 * <li> Il nome del parametro deve essere valido (il controllo è <em>case-insensitive</em> e ignora eventuali spazi
	 * all'inizio e alla fine);<br>
	 * <li> La modifica non deve rendere l'appuntamento incompatibile con l'agenda;<br>
	 * <li> Il nuovo valore deve avere il formato corretto a seconda del parametro che va a modificare.</ul>
	 * <em><br>vedi anche:</em><br>
	 * {@link Appuntamento.ControlloDati#controlloPer(Appuntamento.ControlloDati.TipoControllo, String)}<br>
	 * 
	 * 
	 * @param dataOrarioApp data e orario dell'appuntamento da modificare nel formato {@code dd-MM-uuuu HH-mm}
	 * @param parametroDaModificare nome del parametro da modificare di tipo {@code String}.
	 * @param newValue nuovo valore da inserire di tipo {@code String}.
	 * 
	 * @return {@code 1} se la modifica è andata a buon file.<br>
	 * {@code 0} se non è stato possibile trovare l'appuntamento da modificare.<br>
	 * {@code -1} se la modifica non è possibile poiché porterebbe l'appuntamento a non essere compatibile con l'agenda.<br>
	 * {@code -2} se il nuovo valore è in un formato non corretto.<br>
	 * {@code -3} se il nome del <em>parametroDaModificare</em> non identifica alcun campo.
	 * 
	 */
	
	public int modificaAppuntamento(String dataOrarioApp, String parametroDaModificare, String newValue) {
		String[] splittati = dataOrarioSplit(dataOrarioApp);
		return modificaAppuntamento(splittati[0], splittati[1], parametroDaModificare, newValue);
	}
	
	/**
	 * Ritorna {@code true} se l'agenda è salvata su file di testo {@code txt}.
	 * 
	 * @return {@code true} se l'agenda è salvata su file, {@code false} altrimenti.
	 */
	
	public boolean isSaved() {
		return saved;
	}
	
	/**
	 * Ritorna un nuovo IteratoreAgenda per iterare su ogni
	 * oggetto {@code Appuntamento}.
	 * <p>{@code IteratoreAgenda} implementa l'interfaccia {@link Iterator}.
	 * 
	 * @return un {@code IteratoreAgenda} di quest'agenda.
	 */
	
	@Override
	public Iterator<Appuntamento> iterator() {
		return new IteratoreAgenda();
	}
	
	
	/**
	 * Crea una copia esatta di quest'agenda.<br>
	 * Ciò vuol dire che la nuova agenda avrà stesso nome e
	 * stessi appuntamenti (con valori uguali sui medesimi campi) di quest'agenda.
	 * 
	 * @return un nuovo oggetto {@code Agenda} copia di quest'agenda.
	 */
	
	@Override
	public Agenda clone() {
		return new Agenda(nomeAgenda, appuntamenti);
	}
	
	/**
	 * Verifica che l'oggetto passato come parametro sia uguale
	 * a quest'agenda.
	 * <p>Più precisamente:<ul>
	 * <li> l'oggetto non deve essere {@code null};
	 * <li> la classe dell'oggetto deve essere {@code Agenda};
	 * <li> il nome dell'agenda deve essere il medesimo;
	 * <li> gli appuntamenti devono essere i medesimi.</ul>
	 * 
	 * <p>Ritorna {@code true} se queste condizioni sono soddisfatte.
	 * 
	 * @param object il riferimento all'oggetto da comparare.
	 * 
	 * @return {@code true} se l'oggetto è uguale a quest'agenda, {@code false} altrimenti.
	 */
	
	@Override
	public boolean equals(Object object) {
		if(object == null || object.getClass() != this.getClass()) return false;
		Agenda other = (Agenda) object;
		return (other.getNomeAgenda().equals(this.nomeAgenda) && this.appuntamenti.equals(other.getAppuntamenti()));
	}
	
	
	private String[] dataOrarioSplit(String dataOrario) {
		String[] splitted = dataOrario.split("(\\s)+");
		return splitted.length == 1 ? new String[] {dataOrario, ""} : splitted;
	}
	
}
