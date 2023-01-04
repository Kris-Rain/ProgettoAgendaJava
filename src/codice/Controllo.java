/**
 * @author Kristian Rigo
 * @author Nicolò Bianchetto
 */

package codice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class Controllo {
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

	private static void controlloGenerico(String regex, String match, String messaggio, int... flags)
			throws AppuntamentoException {
		PatternMatcher pm = new PatternMatcher(regex, match, flags);
		if (!pm.matcher.matches())
			throw new AppuntamentoException(messaggio);
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
	
	private static void isDataValida(String data) throws AppuntamentoException {

		try {
			DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT).parse(data);
		}
		catch(DateTimeParseException e) {
			throw new AppuntamentoException("Data non valida!");
		}
		
	}

	public static void controlloData(String data) throws AppuntamentoException {
		controlloGenerico("([0-2][0-9]|(3)[0-1])-(((0)[0-9])|((1)[0-2]))-\\d{4}", data, "Data non valida!",
				Pattern.CASE_INSENSITIVE);
		isDataValida(data);
	}

	public static void controlloOrario(String orario) throws AppuntamentoException {
		controlloGenerico("([0-1][0-9]|(2)[0-4]):([0-5][0-9])", orario, "Orario non valido!", Pattern.CASE_INSENSITIVE);
	}
}
