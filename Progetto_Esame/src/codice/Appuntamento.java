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

import codice.Appuntamento.ControlloDati.PatternMatcher;

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
			
			//Sistematina a patternmatcher
			public boolean matches() {
				return matcher.matches();
			}
			
			public static PatternMatcher create(String regex, String match, int ... flags) {
				return new PatternMatcher(regex, match, flags);
			}

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
		
		private static boolean controlloGenerico(String regex, String match, int ... flags){
			return PatternMatcher.create(regex, match, flags).matches();
		}
		
		public static boolean controlloData(String data){
			if(!controlloGenerico("([0-2][0-9]|(3)[0-1])-(((0)[0-9])|((1)[0-2]))-\\d{4}", data)) return false;
			return isDataValida(data);
		}
		public static boolean controlloOrario(String orario){
			return controlloGenerico("([0-1][0-9]|(2)[0-3])-([0-5][0-9])", orario);
		}
		
		//Cambiato la regex, la durata non accetta 0 e basta oppure 000 o 00000 perché un appuntamento che non dura nulla non ha senso
		public static boolean controlloDurata(String durata){
			return controlloGenerico("(0*[1-9][0-9]{0,3})", durata);
		}
		public static boolean controlloNome(String nome){
			return controlloGenerico("[a-z0-9]{1,20}", nome, Pattern.CASE_INSENSITIVE);
		}
		public static boolean controlloLuogo(String luogo){
			return controlloGenerico("[a-z]{1,20}", luogo, Pattern.CASE_INSENSITIVE);
		}
	}
	
	public Appuntamento(String data, String orario, String durata, String luogo, String nomePersona) throws AppuntamentoException{
		testParametri(data, orario, durata, luogo, nomePersona);
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

	private void testParametri(String data, String orario, String durata, String luogo, String nomePersona) throws AppuntamentoException {
		if( !(ControlloDati.controlloData(data) &&
			  ControlloDati.controlloOrario(orario) &&
			  ControlloDati.controlloNome(nomePersona) &&
			  ControlloDati.controlloLuogo(luogo) &&
			  ControlloDati.controlloDurata(durata)) )	throw new AppuntamentoException("Dati non validi!");
	}

	
	//Compattato
	public boolean matchPersona(String nome) {
		return PatternMatcher.create(nome, this.nomePersona, Pattern.CASE_INSENSITIVE).matches();

	}

	public boolean matchData(String data) {
		return this.getData().contains(data);
	}
	
	public boolean matchDataOrario(String data, String orario) {
		return (this.getData() + " " + this.getOrario()).contains(data + " " + orario);
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
}
