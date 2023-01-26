package codice;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import jbook.util.PatternMatcher;

/**
 * La classe {@code Appuntamento} descrive una serie di metodi per la gestione di un singolo appuntamento.
 * <p>
 * 
 * Ogni {@code Appuntamento} è costituito da:<ul>
 * <li> una <strong>data</strong> di tipo {@link String} che identifica il giorno dell'appuntamento, 
 * espressa nel formato <strong>{@code dd-MM-uuuu}</strong>;
 * 
 * <li> un <strong>orario</strong> di tipo {@link String} espresso nel formato <strong>{@code HH-mm}</strong>;
 * 
 * <li> una <strong>durata</strong> di tipo {@link String} espressa in <strong>{@code minuti}</strong> ed un massimo di quattro cifre;
 * 
 * <li> un <strong>luogo</strong> di tipo {@link String} espresso in <strong>{@code caratteri alfanumerici}</strong> 
 * ove il numero può essere inserito solo alla fine;
 * 
 * <li> un <strong>nome persona</strong> di tipo {@link String} espresso in <strong>{@code caratteri alfanumerici}</strong> 
 * tuttavia non può essere espresso solo con caratteri numerici.
 * </ul><p>
 * 
 * Durante la creazione di un {@code Appuntamento}, all'utente viene chiesto di inserire questi parametri indispensabili, 
 * che verranno controllati attraverso dei test privati e pubblici per verificare la correttezza dei dati.<br>
 * 
 * In particolare:<ul>
 * <li>la <strong>data</strong> deve rispettare il formato <strong>{@code dd-MM-uuuu}</strong>
 * e nel caso l'utente inserisca una data non congrua con il calendario, verrà sollevata un'eccezione {@link AppuntamentoException}. E.g.:
 * <blockquote><pre><em>"25-12-2023"</em>, <em><s>"30-15-1998"</s></em>, <em><s>"29-02-2023"</s></em>;</pre></blockquote>
 * 
 * <li>l'<strong>orario</strong> deve rispettare il formato <strong>{@code HH-mm}</strong>
 * e nel caso l'utente inserisca un orario che superi le 24 ore, verrà sollevata un'eccezione {@link AppuntamentoException}. E.g.:
 * <blockquote><pre><em>"23-30"</em>, <em>"00-00"</em>, <em><s>"27-30"</s></em>;</pre></blockquote>
 * 
 * <li>la <strong>durata</strong> deve essere espressa in <strong>{@code minuti}</strong>
 * e deve rispettare la regex <em>^(0*[1-9][0-9]{0,3})$</em>, altrimenti verrà sollevata un'eccezione {@link AppuntamentoException}. E.g.:
 * <blockquote><pre><em>"120"</em>, <em>"1000"</em>, <em><s>"0"</s></em>, <em><s>"10000"</s></em>;</pre></blockquote>
 * 
 * <li>il <strong>luogo</strong> deve essere espresso in <strong>{@code caratteri alfanumerici}</strong>
 * e deve rispettare la regex <br><em>^[a-z]{1,20}(\\s)?[a-z]{0,20}(\\s[0-9]{0,4})?$</em>,
 * altrimenti verrà sollevata un'eccezione {@link AppuntamentoException}. E.g.:
 * <blockquote><pre><em>"Milano"</em>, <em>"Via Roma 46"</em>, <em><s>"36 Piazza Cavour"</s></em>;</pre></blockquote>
 * 
 * <li>il <strong>nome persona</strong> deve essere espresso in <strong>{@code caratteri alfanumerici}</strong>
 * e deve rispettare la regex <br><em>^(?![0-9]+$)[a-z0-9]{1,20}(\\s)?[a-z0-9]{0,20}$</em>,
 * altrimenti verrà sollevata un'eccezione {@link AppuntamentoException}. E.g.:
 * <blockquote><pre><em>"Luca99"</em>, <em>"Marco Rossi"</em>, <em><s>"1998"</s></em>;</pre></blockquote></ul>
 * 
 * Inoltre all'interno del costruttore sono presenti <em>{@code dataTimeInizio}</em> e <em>{@code dataTimeFine}</em> di tipo {@link DataOrario},
 * che equivalgono rispettivamente alla <strong>data</strong> e all'<strong>orario</strong> iniziale, impostato dall'utente e alla
 * <strong>data</strong> e all'<strong>orario</strong> finale dopo l'aggiunta della durata all'orario.<br>
 * Nel caso l'orario finale superi la mezzanotte, la data finale verrà aggiornata col giorno successivo. E.g.:
 * <blockquote><pre>
 * {@code String data = "31-12-1999";
 * String orario = "23-30";
 * String durata = 45;
 * 
 * DataOrario dataTimeInizio -> "31-12-1999 23-30";
 * DataOrario dataTimeFine -> "01-01-2000 00-15";}</pre></blockquote>
 * 
 * {@code dataTimeInizio} e {@code dataTimeFine} sono utilizzati per controllare la <em>compatibilità</em> confrontando
 * il nuovo appuntamento da aggiungere con questo {@code appuntamento} tramite i metodi:<ul>
 * <li>{@link #isAfter(Appuntamento other)};
 * <li>{@link #isBefore(Appuntamento other)};
 * <li>{@link #isCompatible(Appuntamento other)};</ul>
 * 
 * Per recuperare i dati di un appuntamento nel tipo {@link String}, sono presenti dei metodi {@code getter}. In particolare:<ul>
 * <li>{@link #getData()};
 * <li>{@link #getOrario()};
 * <li>{@link #getDurata()};
 * <li>{@link #getLuogo()};
 * <li>{@link #getPersona()};
 * <li>{@link #getDataTimeInizio()};
 * <li>{@link #getDataTimeFine()};</ul>
 * 
 * Sono presenti tre metodi di confronto a seconda del parametro:<ul>
 * <li>{@link #matchData(String data)} -> confronta questa <strong>data</strong> con la data passata per argomento;
 * <li>{@link #matchDataOrario(String data, String orario)} -> confronta questa <strong>data-orario</strong> con il data-orario passato per argomento;
 * <li>{@link #matchPersona(String nome)} -> confronta questo <strong>nome persona</strong> con il nome persona passato per argomento.</ul>
 * 
 * Inoltre è possibile rappresentare l'appuntamento attraverso il metodo {@link #toString} e confrontare questo
 * {@code Appuntamento} con un altro, tramite il metodo {@link #equals}.
 * 
 * @see Agenda
 * @see ContenitoreAgende
 * @see AppuntamentoException
 * @see DataOrario
 * 
 * @author Kristian Rigo
 * @author Nicolò Bianchetto
 */

public class Appuntamento {
	private DataOrario dataTimeInizio;
	private DataOrario dataTimeFine;
	private String durata;
	private String luogo;
	private String nomePersona;
	
	/**
	 * Una classe membro statica che si occupa di gestire il controllo dei parametri
	 * attraverso dei metodi privati e pubblici prima di essere utilizzati
	 * per la creazione di un {@code Appuntamento}.<br>
	 * In particolare, il metodo {@link #controlloPer} verifica se il parametro passato
	 * come argomento è valido per la creazione di un {@code Appuntamento}, dato un {@link TipoControllo};<br>
	 * Se i parametri superano questa fase di controllo, allora l'appuntamento verrà creato con questi dati.
	 */
	
	public static class ControlloDati{
		private static class ControlloMappato {
			private Predicate<String> controllo;
			private AppuntamentoException exception;
			
			private ControlloMappato(TipoControllo tc) {
				switch(tc) {
					case CONTROLLO_DATA -> {
						controllo = ControlloDati::isDataValida;
						exception = new AppuntamentoException("Data non valida!");
					}
					case CONTROLLO_ORARIO -> {
						controllo = ControlloDati::isOrarioValido;
						exception = new AppuntamentoException("Orario non valido!");
					}
					case CONTROLLO_DURATA -> {
						controllo = durata -> PatternMatcher.create("^(0*[1-9][0-9]{0,3})$", durata).matches();
						exception = new AppuntamentoException("Durata non valida!");
					}
					case CONTROLLO_LUOGO -> {
						controllo = luogo -> PatternMatcher.create("^[a-z]{1,20}(\\s)?[a-z]{0,20}(\\s[0-9]{0,4})?$", luogo, Pattern.CASE_INSENSITIVE).matches();
						exception = new AppuntamentoException("Luogo non valido!");
					}
					case CONTROLLO_NOME -> {
						controllo = nome -> PatternMatcher.create("^(?![0-9]+$)[a-z0-9]{1,20}(\\s)?[a-z0-9]{0,20}$", nome, Pattern.CASE_INSENSITIVE).matches();
						exception = new AppuntamentoException("Nome non valido!");
					}
					
				}
			}
			
			private boolean test(String stringa) {
				return controllo.test(stringa);
			}
			
			private AppuntamentoException getAppuntamentoException() {
				return exception;
			}
		}
		
		/**
		 * Una semplice enumarazione di tutti i tipi di controllo che
		 * verranno utilizzati per validare i parametri, durante la creazione
		 * di un nuovo {@code Appuntamento}.<p>
		 * A seconda del tipo di controllo selezionato:<ul>
		 * <li> {@link #CONTROLLO_DATA} -> nel caso di validazione della data;
		 * <li> {@link #CONTROLLO_ORARIO} -> nel caso di validazione dell'orario;
		 * <li> {@link #CONTROLLO_DURATA} -> nel caso di validazione della durata;
		 * <li> {@link #CONTROLLO_LUOGO} -> nel caso di validazione del luogo;
		 * <li> {@link #CONTROLLO_NOME} -> nel caso di validazione del nome della persona;</ul>
		 */
		
		public enum TipoControllo {
			/**
			 * Indica che si vuole verificare il parametro come se fosse la <strong>data</strong>. In particolare:<ul>
			 * <li>la <strong>data</strong> deve rispettare il formato <strong>{@code dd-MM-uuuu}</strong>;
			 * <li>nel caso l'utente inserisca una data non congrua con il calendario, verrà sollevata un'eccezione {@link AppuntamentoException}. E.g.:</ul>
			 * <blockquote><pre>
			 * Accettati: <em>"25-12-2023"</em>, <em>"29-02-2020"</em>;
			 * Non accettati: <em>"30-15-1998"</em>, <em>"29-02-2023"</em>, <em>"03/11/2023"</em>.</pre></blockquote>
			 */
			CONTROLLO_DATA,
			/**
			 * Indica che si vuole verificare il parametro come se fosse l'<strong>orario</strong>. In particolare:<ul>
			 * <li>deve rispettare il formato <strong>{@code HH-mm}</strong>;
			 * <li>nel caso l'utente inserisca un orario che superi le 24 ore, verrà sollevata un'eccezione {@link AppuntamentoException}. E.g.:</ul>
			 * <blockquote><pre>
			 * Accettati: <em>"23-30"</em>, <em>"00-00"</em>;
			 * Non accettati: <em>"27-30"</em>, <em>"24-30"</em>, <em>"00:30"</em>.</pre></blockquote>
			 */
			CONTROLLO_ORARIO,
			/**
			 * Indica che si vuole verificare il parametro come se fosse la <strong>durata</strong>. In particolare:<ul>
			 * <li>deve essere espressa in <strong>{@code minuti}</strong>;
			 * <li>deve rispettare la regex <em>^(0*[1-9][0-9]{0,3})$</em>, altrimenti verrà sollevata un'eccezione {@link AppuntamentoException}. E.g.:</ul>
			 * <blockquote><pre>
			 * Accettati: <em>"120"</em>, <em>"1000"</em>;
			 * Non accettati: <em>"0"</em>, <em>"10000"</em>.</pre></blockquote>
			 */
			CONTROLLO_DURATA,
			/**
			 * Indica che si vuole verificare il parametro come se fosse il <strong>luogo</strong>. In particolare:<ul>
			 * <li>deve essere espresso in <strong>{@code caratteri alfanumerici}</strong>
			 * <li>deve rispettare la regex <em>^[a-z]{1,20}(\\s)?[a-z]{0,20}(\\s[0-9]{0,4})?$</em>,
			 * altrimenti verrà sollevata un'eccezione {@link AppuntamentoException}. E.g.:</ul>
			 * <blockquote><pre>
			 * Accettati: <em>"Milano"</em>, <em>"Via Roma 46"</em>;
			 * Non accettati: <em>"36 Piazza Cavour"</em>, <em>"!($(%"</em>, <em>"Novara123"</em>.</pre></blockquote>
			 */
			CONTROLLO_LUOGO,
			/**
			 * Indica che si vuole verificare il parametro come se fosse il <strong>nome persona</strong>. In particolare:<ul>
			 * <li>deve essere espresso in <strong>{@code caratteri alfanumerici}</strong>
			 * <li>deve rispettare la regex <em>^(?![0-9]+$)[a-z0-9]{1,20}(\\s)?[a-z0-9]{0,20}$</em>,
			 * altrimenti verrà sollevata un'eccezione {@link AppuntamentoException}. E.g.:</ul>
			 * <blockquote><pre>
			 * Accettati: <em>"Luca99"</em>, <em>"Marco Rossi"</em>;
			 * Non accettati: <em>"1998"</em>, <em>"$(!$)!"</em>, <em>"1234"</em>.</pre></blockquote>
			 */
			CONTROLLO_NOME
		}
		
		private static HashMap<TipoControllo, ControlloMappato> creaControlliMappati() {
			HashMap<TipoControllo, ControlloMappato> controlliMappati = new HashMap<>();
			controlliMappati.put(TipoControllo.CONTROLLO_DATA, new ControlloMappato(TipoControllo.CONTROLLO_DATA));
			controlliMappati.put(TipoControllo.CONTROLLO_ORARIO, new ControlloMappato(TipoControllo.CONTROLLO_ORARIO));
			controlliMappati.put(TipoControllo.CONTROLLO_DURATA, new ControlloMappato(TipoControllo.CONTROLLO_DURATA));
			controlliMappati.put(TipoControllo.CONTROLLO_LUOGO, new ControlloMappato(TipoControllo.CONTROLLO_LUOGO));
			controlliMappati.put(TipoControllo.CONTROLLO_NOME, new ControlloMappato(TipoControllo.CONTROLLO_NOME));
			return controlliMappati;
		}
		
		private static boolean isDataTimeValid(String format, String dataTime){
			try {
				DateTimeFormatter.ofPattern(format).withResolverStyle(ResolverStyle.STRICT).parse(dataTime);
			}
			catch(DateTimeParseException e) {
				return false;
			}
			return true;
		}
		
		private static boolean isDataValida(String data) {
			return isDataTimeValid("dd-MM-uuuu", data);
		}
		
		private static boolean isOrarioValido(String orario) {
			return isDataTimeValid("HH-mm", orario);
		}
		
		/**
		 * Dato un {@link TipoControllo}, verifica se la {@code stringa} passata 
		 * come argomento è valida come parametro per la creazione di
		 * un {@code Appuntamento} attraverso:<ul>
		 * <li> {@link PatternMatcher} che confronta la stringa con la regex assegnata,
		 * nel caso della <strong>durata</strong>, <strong>luogo</strong> e <strong>nome persona</strong>;
		 * <li> {@link DataOrario} che utilizza la libreria {@link DateTimeFormatter} 
		 * che nel caso della data, controlla se è conforme col formato <strong>{@code dd-MM-uuuu}</strong> 
		 * e se è congrua con il calendario e nel caso dell'orario, se è conforme
		 * col formato <strong>{@code HH-mm}</strong> e controlla se supera le 24 ore.</ul>
		 * 
		 * @param tc che descrive il tipo di controllo da effettuare sulla stringa.
		 * @param stringa sulla quale verrà effettuato il controllo.
		 * @return {@code true} se il {@code parametro} è valido, {@code false} altrimenti.
		 */
		
		public static boolean controlloPer(TipoControllo tc, String stringa) {
			return new ControlloMappato(tc).test(stringa);
		}
		
		private static void testParametri(String data, String orario, String durata, String luogo, String nome) throws AppuntamentoException {
			HashMap<TipoControllo, ControlloMappato> controlliMappati = creaControlliMappati();
			String[] parametri = { data, orario, durata, luogo, nome };
			int indice = 0;
			for(TipoControllo controllo: TipoControllo.values()) {
				if(!controlliMappati.get(controllo).test(parametri[indice++])) throw controlliMappati.get(controllo).getAppuntamentoException();
			}
		}
	}
	
	/**
	 * Crea un nuovo {@code Appuntamento} assegnando data, orario, durata, luogo e nome della persona:<ul>
	 * <li> Verifica i parametri e lancia un eccezione {@link AppuntamentoException} 
	 * se non sono validi;
	 * <li> Crea un nuovo {@link DataOrario} passando la data e l'orario come argomenti 
	 * e assegnandolo alla variabile {@code dataTimeInizio};
	 * <li> Viene sommata la {@code durata} a {@code dataTimeInizio} e assegnandola
	 * alla variabile {@code dataTimeFine};</ul>
	 * Quando verrà aggiunto un nuovo {@code Appuntamento} nell'{@link Agenda},
	 * {@code dataTimeInizio} e {@code dataTimeFine} verranno confrontati per 
	 * verificare la compatibilità fra tutti gli appuntamenti tramite {@link #isCompatible}.
	 * 
	 * @param data stringa che identifica la data
	 * @param orario stringa che identifica l'orario
	 * @param durata stringa che identifica la durata
	 * @param luogo stringa che identifica il luogo
	 * @param nomePersona stringa che identifica il nome della persona
	 * @throws AppuntamentoException se i parametri passati come argomento non superano i controlli.
	 */
	
	public Appuntamento(String data, String orario, String durata, String luogo, String nomePersona) throws AppuntamentoException {
		ControlloDati.testParametri(data, orario, durata, luogo, nomePersona);
		this.dataTimeInizio = new DataOrario(data, orario);
		this.durata=durata.replaceFirst("^0*", "");
		this.dataTimeFine = dataTimeInizio.plusMinuti(this.durata);
		this.luogo=luogo;
		this.nomePersona=nomePersona;
	}
	
	/**
	 * Crea un nuovo {@code Appuntamento} attraverso gli elementi dell'array <strong>{@code parametri}</strong>.
	 * 
	 * @param parametri di tipo {@code String[]} che descrive l'insieme dei dati per la creazione di un {@code Appuntamento}.
	 * @throws AppuntamentoException se i parametri passati come argomento non superano i controlli.
	 */
	
	public Appuntamento(String[] parametri) throws AppuntamentoException {
		this(parametri[0], parametri[1], parametri[2], parametri[3], parametri[4]);
	}
	
	/**
	 * Restituisce la data dell'appuntamento.
	 * 
	 * @return la data dell'appuntamento nel tipo {@link String}.
	 */

	public String getData() {
		return dataTimeInizio.getDataToString();
	}
	
	/**
	 * Restituisce il nome della persona dell'appuntamento.
	 * 
	 * @return il nome della persona dell'appuntamento nel tipo {@link String}.
	 */
	
	public String getPersona() {
		return nomePersona;
	}
	
	/**
	 * Restituisce l'orario dell'appuntamento.
	 * 
	 * @return l'orario dell'appuntamento nel tipo {@link String}.
	 */
	
	public String getOrario() {
		return dataTimeInizio.getOrarioToString();
	}
	
	/**
	 * Restituisce la durata dell'appuntamento.
	 * 
	 * @return la durata dell'appuntamento nel tipo {@link String}.
	 */
	
	public String getDurata() {
		return durata;
	}
	
	/**
	 * Restituisce il luogo dell'appuntamento.
	 * 
	 * @return il luogo dell'appuntamento nel tipo {@link String}.
	 */
	
	public String getLuogo() {
		return luogo;
	}
	
	/**
	 * Restituisce il data-orario iniziale dell'appuntamento.
	 * 
	 * @return il data-orario iniziale dell'appuntamento nel tipo {@link DataOrario}.
	 */
	
	public DataOrario getDataTimeFine() {
		return dataTimeFine;
	}
	
	/**
	 * Restituisce il data-orario finale dell'appuntamento.
	 * 
	 * @return il data-orario finale dell'appuntamento nel tipo {@link DataOrario}.
	 */
	
	public DataOrario getDataTimeInizio() {
		return dataTimeInizio;
	}
	
	/**
	 * Crea un {@link PatternMatcher} confrontando il nome persona di quest'appuntamento
	 * con la stringa {@code nome} passata come argomento, utilizzando la flag {@code CASE_INSENSITIVE}
	 * in modo tale che le stringhe vengano confrontate non considerando le maiuscole.<br>
	 * 
	 * @param nome che identifica il nome della persona che si vuole confrontare.
	 * @return {@code true} se sono uguali, {@code false} altrimenti.
	 */

	public boolean matchPersona(String nome) {
		return PatternMatcher.create(this.nomePersona, nome, Pattern.CASE_INSENSITIVE).matches();
	}
	
	/**
	 * Crea un {@link PatternMatcher} confrontando la data di quest'appuntamento
	 * con la stringa {@code data} passata come argomento.<br>
	 * 
	 * @param data che identifica la data che si vuole confrontare.
	 * @return {@code true} se sono uguali, {@code false} altrimenti.
	 */
	
	public boolean matchData(String data) {
		return PatternMatcher.create(this.getData(), data).matches();
	}
	
	/**
	 * Crea un {@link PatternMatcher} confrontando il {@code dataTimeInizio} di quest'appuntamento
	 * con le stringhe {@code data} e {@code orario} passate come argomento.<br>
	 * 
	 * @param data che identifica la data che si vuole confrontare.
	 * @param orario che identifica l'orario che si vuole confrontare.
	 * @return {@code true} se sono uguali, {@code false} altrimenti.
	 */
	
	public boolean matchDataOrario(String data, String orario) {
		return PatternMatcher.create(this.getDataTimeInizio().toString(), data + " " + orario).matches();
	}
	
	/**
	 * Controlla se questo {@code Appuntamento} inizia dopo la fine dell'appuntamento 
	 * passato come argomento, per controllare la compatibilità prima di essere aggiunto
	 * nell'{@link Agenda}.<br>
	 * 
	 * @param other appuntamento da aggiungere che deve terminare prima di questo {@code Appuntamento}.
	 * @return {@code true} se questo {@code Appuntamento} inizia dopo other, {@code false} altrimenti.
	 */
	
	public boolean isAfter(Appuntamento other) {
		//Se inizio dopo che l'altro finisca, ritorno true
		return (this.dataTimeInizio.compareTo(other.getDataTimeFine()) >= 0);
	}
	
	/**
	 * Controlla se questo {@code Appuntamento} termina prima dell'inizio dell'appuntamento 
	 * passato come argomento, per controllare la compatibilità prima di essere aggiunto
	 * nell'{@link Agenda}.<br>
	 * 
	 * @param other appuntamento da aggiungere che deve iniziare dopo la fine di questo {@code Appuntamento}.
	 * @return {@code true} se questo {@code Appuntamento} termina prima di other, {@code false} altrimenti.
	 */
	
	public boolean isBefore(Appuntamento other) {
		//Se finisco prima che l'altro inizi, ritorno true
		return (this.dataTimeFine.compareTo(other.getDataTimeInizio()) <= 0);
	}
	
	/**
	 * Controlla se questo {@code Appuntamento} termina prima o inizia dopo l'appuntamento 
	 * passato come argomento, per controllare la compatibilità prima di essere aggiunto
	 * nell'{@link Agenda}.<br>
	 * 
	 * @param other appuntamento da aggiungere che deve iniziare dopo la fine di questo {@code Appuntamento}
	 * o terminare prima di questo {@code Appuntamento}.
	 * @return {@code true} se questo {@code Appuntamento} termina prima o inizia dopo other, {@code false} altrimenti.
	 */
	
	public boolean isCompatible(Appuntamento other) {
		return this.isAfter(other) || this.isBefore(other);
	}
	
	/**
	 * Restituisce una rappresentazione in stringa dell'appuntamento.
	 * 
	 * @return una rappresentazione dell'appuntamento nel formato {@code String}.
	 */

	@Override
	public String toString() {
		return this.getData()+"|"+this.getOrario()+"|"+this.getDurata()+"min|"+this.getLuogo()+"|"+this.getPersona()+"\n";
	}
	
	/**
	 * Verifica che l'oggetto passato come parametro sia uguale
	 * a questo appuntamento.
	 * <p>Più precisamente:<ul>
	 * <li> l'oggetto non deve essere {@code null};
	 * <li> la classe dell'oggetto deve essere {@code Appuntamento};
	 * <li> il {@code dataTimeInizio} dell'appuntamento deve essere il medesimo;
	 * <li> il {@code dataTimeFine} dell'appuntamento deve essere il medesimo;
	 * <li> la {@code durata} dell'appuntamento deve essere il medesimo;
	 * <li> il {@code luogo} dell'appuntamento deve essere il medesimo;
	 * <li> il {@code nome persona} dell'appuntamento deve essere il medesimo;</ul>
	 * <p>Restituisce {@code true} se queste condizioni sono soddisfatte.
	 * 
	 * @param object il riferimento all'oggetto da comparare.
	 * @return {@code true} se l'oggetto è uguale a questo appuntamento, {@code false} altrimenti.
	 */
	
	@Override
	public boolean equals(Object object) {
		if(object==null || object.getClass() != this.getClass()) return false;
		Appuntamento other = (Appuntamento) object;
		return (other.getDataTimeInizio().equals(this.dataTimeInizio)) 
				&& (other.getDataTimeFine().equals(this.dataTimeFine)) 
				&& (other.getDurata().equals(this.durata))
				&& (other.getLuogo().equals(this.luogo))
				&& (other.getPersona().equals(this.nomePersona));
	}
}
