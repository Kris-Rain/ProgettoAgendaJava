/**
 * @author Kristian Rigo
 * @author Nicolò Bianchetto
 */

package codice;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import jbook.util.PatternMatcher;

public class Appuntamento {
	private DataOrario dataTimeInizio;
	private DataOrario dataTimeFine;
	private String durata;
	private String luogo;
	private String nomePersona;
	
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
					/* Ho fatto una modifichina alle regex. Ho aggiunto a inizio e fine stringa i simboli ^ e $ a tutti
					 * ^ non indica solo "esclusione" ma anche inizio stringa, quando è messa fuori dalle quadrate
					 * [^a-z] -> esclusione / ^[a-z] -> l'inizio deve essere una lettera a-z
					 * $ indice fine stringa.
					 * Dovrebbe renderle "più precise" ho letto... perché indichi proprio la stringa intera e non solo un suo contenuto
					 * 
					 * A fine CONTROLLO_LUOGO ho messo (\\s[0-9]{0,4}).. così facendo il luogo può anche essere volendo un indirizzo come Via Roma 36
					 * Ho messo lo spazio obbligatorio in questo caso... boh in realtà solo perché Via Roma36 è bruttino.
					 * 
					 * Ah e poi ho aggiunto a CONTROLLO_NOME all'inizio (?![0-9]+$)
					 * Perché volevo far sì che il nome potesse contenere numeri, però NON solo numeri.
					 * Spiegato in breve:
					 * ?! indica "Negative Lookahead" ... poi esistono anche Positive Lookahead (?=), Positive Lookbehind (?<=), Negative Lookbehind (?<!)
					 * Lookhead vuol dire "che segue", Lookbehind vuol dire "che precede".
					 * Positive vuol dire che accetto quello che segue/precede, Negative vuol dire che non lo accetto.
					 * 
					 * Quindi...
					 * ^(?![0-9]+$) vuol dire
					 * "L'inizio della stringa (^) NON deve essere seguito da [0-9] ripetuti da 1 a infinite volte (+) fino alla fine della stringa ($)"
					 * In parole più semplici, quindi, una stringa non può essere formata solo da cifre ripetute.
					 */
				}
			}
			
			private boolean test(String stringa) {
				return controllo.test(stringa);
			}
			
			private AppuntamentoException getAppuntamentoException() {
				return exception;
			}
			
		}
		
		public enum TipoControllo {
			CONTROLLO_DATA,
			CONTROLLO_ORARIO,
			CONTROLLO_DURATA,
			CONTROLLO_LUOGO,
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

		//regex con controllo data e orario
		//Controllo.java	
		
		/**
		 * 
		 * @param data
		 * @throws AppuntamentoException
		 * 
		 * Dopo la regex, faccio questo test per verificare che la data abbia senso.
		 * 
		 * DataTimeFormatter è un "Formatter" per stampa/parse (analisi) di DataTime
		 * al metodo ofPattern() gli do il pattern, in questo caso dd-MM-uuuu (giorni-mesi-anni)
		 * ResolverStyle è un enumerazione di valori. Mi indica i diversi stili di parsing
		 * 
		 * Ci sono Lenient, Smart e Strict
		 * 
		 * Lenient
		 * accetto tutto
		 * anche robe come 30 febbraio volendo
		 * 
		 * Smart
		 * se la data è 30 febbraio la trasforma in 28 oppure 29 febbraio se bisestile
		 * 
		 * Strict (quello che uso)
		 * non accetto proprio roba come
		 * 31 Aprile
		 * 30 Febbraio
		 * 
		 */
		
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
				
		
		public static boolean controlloPer(TipoControllo tc, String stringa) {
			HashMap<TipoControllo, ControlloMappato> controlliMappati = creaControlliMappati();
			return controlliMappati.get(tc).test(stringa);
		}
		
				
		public static void testParametri(String data, String orario, String durata, String luogo, String nome) throws AppuntamentoException {
			HashMap<TipoControllo, ControlloMappato> controlliMappati = creaControlliMappati();
			String[] parametri = { data, orario, durata, luogo, nome };
			int indice = 0;
			for(TipoControllo controllo: TipoControllo.values()) {
				if(!controlliMappati.get(controllo).test(parametri[indice++])) throw controlliMappati.get(controllo).getAppuntamentoException();
			}
		}
		/*
		public static boolean controlloData(String data){
			return(controlloGenerico("([0-2][0-9]|(3)[0-1])-(((0)[0-9])|((1)[0-2]))-\\d{4}", data) && isDataValida(data));
		}
		public static boolean controlloOrario(String orario){
			return controlloGenerico("([0-1][0-9]|(2)[0-3])-([0-5][0-9])", orario);
		}
		
		//Cambiato la regex, la durata non accetta 0 e basta oppure 000 o 00000 perché un appuntamento che non dura nulla non ha senso
		public static boolean controlloDurata(String durata){
			return controlloGenerico("(0*[1-9][0-9]{0,3})", durata);
		}
		public static boolean controlloNome(String nome){
			return controlloGenerico("[a-z0-9]{1,20}(\\s)?[a-z0-9]{0,20}", nome, Pattern.CASE_INSENSITIVE);
		}
		public static boolean controlloLuogo(String luogo){
			return controlloGenerico("[a-z_]{1,20}", luogo, Pattern.CASE_INSENSITIVE);
		}
		*/
	}
	
	public Appuntamento(String data, String orario, String durata, String luogo, String nomePersona) throws AppuntamentoException {
		ControlloDati.testParametri(data, orario, durata, luogo, nomePersona);
		this.dataTimeInizio = new DataOrario(data, orario);
		this.durata=durata.replaceFirst("^0*", "");
		this.dataTimeFine = dataTimeInizio.plusMinuti(this.durata);
		this.luogo=luogo;
		this.nomePersona=nomePersona;
	}
		

	public String getData() {
		return dataTimeInizio.getDataToString();
	}
	public String getPersona() {
		return nomePersona;
	}
	public String getOrario() {
		return dataTimeInizio.getOrarioToString();
	}
	public String getDurata() {
		return durata;
	}
	public String getLuogo() {
		return luogo;
	}
	
	//Gli ho resi pubblici perché servono ad Agenda per ordinare gli appuntamenti per data
	public DataOrario getDataTimeFine() {
		return dataTimeFine;
	}
	public DataOrario getDataTimeInizio() {
		return dataTimeInizio;
	}

	/*
	public HashMap<Predicate<String>, Supplier<AppuntamentoException>> creaHashControlli() {
		HashMap<Predicate<String>, Supplier<AppuntamentoException>> controlli = new HashMap<>();
		controlli.putIfAbsent(data -> ControlloDati.controlloData(data), () -> new AppuntamentoException("Data non valida!"));
		controlli.putIfAbsent(orario -> ControlloDati.controlloOrario(orario), () -> new AppuntamentoException("Orario non valido!"));
		controlli.putIfAbsent(durata -> ControlloDati.controlloDurata(durata), () -> new AppuntamentoException("Durata non valida!"));
		controlli.putIfAbsent(luogo -> ControlloDati.controlloLuogo(luogo), () -> new AppuntamentoException("Luogo non valido!"));
		controlli.putIfAbsent(nome -> ControlloDati.controlloNome(nome), () -> new AppuntamentoException("Nome non valido!"));
		return controlli;
	}
	
	private void testParametri(String data, String orario, String durata, String luogo, String nomePersona) throws AppuntamentoException {
		HashMap<Predicate<String>, Supplier<AppuntamentoException>> controlli = creaHashControlli();
		ArrayList<String> parametri = new ArrayList<>(Arrays.asList(data, orario, durata, luogo, nomePersona));
		parametri.stream().for
		for(Map.Entry<Predicate<String>, Supplier<AppuntamentoException>> set: controlli.entrySet()) {
			parametri.stream()
		}
	}
	*/
	

	//Compattato
	public boolean matchPersona(String nome) {
		return PatternMatcher.create(this.nomePersona, nome, Pattern.CASE_INSENSITIVE).matches();

	}

	public boolean matchData(String data) {
		return PatternMatcher.create(this.getData(), data).matches();
	}
	
	public boolean matchDataOrario(String data, String orario) {
		return PatternMatcher.create(this.getData() + " " + this.getOrario(), data + " " + orario).matches();
	}
	
	
	/*
	 * Ho "diviso" in due perché mi sembra più leggibile il codice così ... boh sostanzialemente non cambia nulla in realtà
	 */
	
	public boolean isAfter(Appuntamento other) {
		//Se inizio dopo che l'altro finisca, ritorno true
		return (this.dataTimeInizio.compareTo(other.getDataTimeFine()) >= 0);
	}
	
	public boolean isBefore(Appuntamento other) {
		//Se finisco prima che l'altro inizi, ritorno true
		return (this.dataTimeFine.compareTo(other.getDataTimeInizio()) <= 0);
	}
	
	//metto this almeno si capisce di più a cosa mi riferisco
	public boolean isCompatible(Appuntamento other) {
		return this.isAfter(other) || this.isBefore(other);
	}
	
	@Override
	public String toString() {
		return this.getData()+" "+this.getOrario()+" "+this.getDurata()+" "+this.getLuogo()+" "+this.getPersona()+"\n";
	}
	
}
