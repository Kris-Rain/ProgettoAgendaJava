/**
 * @author Kristian Rigo
 * @author Nicolò Bianchetto
 */

package codice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Controllo{
	public static class PatternMatcher {
		private Pattern pattern;
		private Matcher matcher;
		
		private PatternMatcher(String regex, String match, int ... flags) {
			int resFlag = 0;
			for(int i = 0; i < flags.length; i++) resFlag |= flags[i];

			pattern = Pattern.compile(regex, resFlag);
			matcher = pattern.matcher(match);
		}
	}
	private static void controlloGenerico(String regex, String match, String messaggio, int ... flags) throws AppuntamentoException {
		PatternMatcher pm = new PatternMatcher(regex, match, flags);
		if(!pm.matcher.matches()) throw new AppuntamentoException(messaggio);	
	}
	public static void controlloData(String data) throws AppuntamentoException {
		controlloGenerico("([0-2][0-9]|(3)[0-1])-(((0)[0-9])|((1)[0-2]))-\\d{4}", data, "Data non valida!", Pattern.CASE_INSENSITIVE);
	}
	public static void controlloOrario(String orario) throws AppuntamentoException {
		controlloGenerico("([0-1][0-9]|(2)[0-4]):([0-5][0-9])", orario, "Orario non valido!", Pattern.CASE_INSENSITIVE);
	}
}
