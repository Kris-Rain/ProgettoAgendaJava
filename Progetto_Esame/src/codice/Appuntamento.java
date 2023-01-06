/**
 * @author Kristian Rigo
 * @author Nicolò Bianchetto
 */

package codice;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Appuntamento {
	private DataOrario dataTimeInizio;
	private DataOrario dataTimeFine;
	private String durata;
	private String luogo;
	private String nomePersona;
	
	public static class ControlloDati{
		//regex con controllo data e orario
		//Controllo.java
		public static class PatternMatcher {
			private Pattern pattern;
			private Matcher matcher;

			private PatternMatcher(String regex, String match, int... flags) {
				int resFlag = 0;
				for (int i = 0; i < flags.length; i++)
					resFlag |= flags[i];

				pattern = Pattern.compile(regex, resFlag);
				matcher = pattern.matcher(match);
			}
		}
		
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
		
		private static boolean isDataValida(String data){
			try {
				DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT).parse(data);
			}
			catch(DateTimeParseException e) {
				return false;
			}
			return true;
		}
		private static boolean controlloGenerico(String regex, String match, String messaggio, int... flags){
			PatternMatcher pm = new PatternMatcher(regex, match, flags);
			return pm.matcher.matches();
		}
		public static boolean controlloData(String data){
			if(!controlloGenerico("([0-2][0-9]|(3)[0-1])-(((0)[0-9])|((1)[0-2]))-\\d{4}", data, "Data non valida!")) return false;
			return isDataValida(data);
		}
		public static boolean controlloOrario(String orario){
			return controlloGenerico("([0-1][0-9]|(2)[0-4])[:-]([0-5][0-9])", orario, "Orario non valido!");
		}
		public static boolean controlloDurata(String durata){
			return controlloGenerico("[0-9]{1,4}", durata, "Durata non valida!");
		}
		public static boolean controlloNome(String nome){
			return controlloGenerico("[a-z0-9]{1,20}", nome, "Nome non valido!", Pattern.CASE_INSENSITIVE);
		}
	}
	
	public Appuntamento(String data, String orario, String durata, String luogo, String nomePersona) {
		if(!ControlloDati.controlloData(data));
		this.dataTimeInizio = new DataOrario(data, orario);
		this.durata=durata;
		this.dataTimeFine = dataTimeInizio.plusMinuti(durata);
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
	private DataOrario getDataTimeFine() {
		return dataTimeFine;
	}
	private DataOrario getDataTimeInizio() {
		return dataTimeInizio;
	}
	
	/**
	 * 
	 * @param nome
	 * @return
	 * 
	 * Faccio il contains per ogni persona dell'arraylist... mi basta che anche solo una persona contenga la sequenza
	 */
	
	public boolean matchPersona(String nome) {
		Pattern pat = Pattern.compile(nome, Pattern.CASE_INSENSITIVE);
		Matcher mat = pat.matcher(this.nomePersona);
		return mat.matches();

	}

	public boolean matchData(String data) {
		return this.getData().contains(data);
	}
	public boolean matchDataOrario(String data, String orario) {
		return (this.getData() + " " + this.getOrario()).contains(data + " " + orario);
	}
	public boolean isCompatible(Appuntamento newAppuntamento) {
		if(newAppuntamento.getDataTimeInizio().compareTo(dataTimeInizio)<0 
		&& newAppuntamento.getDataTimeFine().compareTo(dataTimeInizio)<=0 
		|| newAppuntamento.getDataTimeInizio().compareTo(dataTimeFine)>=0) return true;
		return false;
	}
}
