package jbook.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@code PatternMatcher} è una semplice classe che facilita
 * l'utilizzo delle classi {@link Pattern} e {@link Matcher}. Questo 
 * specialmente nei casi ove è necessario creare un singolo {@code Pattern}
 * per un singolo {@code Matcher}. Negli altri casi, è ovviamente preferibile
 * dichiarare tali classi separatamente.
 * 
 *<p><strong>Questa classe è immutabile</strong>.
 * 
 * 
 * @author Nicolò Bianchetto (matr. 20026606)
 * @author Kristian Rigo (matr. 20046665)
 */

public class PatternMatcher {
	private Pattern pattern;
	private Matcher matcher;
	
	/**
	 * Verifica che il {@code Matcher} soddisfi la <em>regex</em> assegnata
	 * al {@code Pattern}.
	 * 
	 * @return {@code true} se e solo se il matcher soddisfa la regex, {@code false} altrimenti.
	 */
	
	public boolean matches() {
		return matcher.matches();
	}
	
	/**
	 * Crea un nuovo {@code PatternMatcher} tramite una <em>regex</em>, un <em>matcher</em>
	 * ed eventuali <em>flags</em>. Quest'ultime sono opzionali e devono essere le stesse flags dichiarate
	 * nella classe {@code Pattern}.
	 * 
	 * @param regex la sequenza da compilare ed assegnare al {@code Pattern}.
	 * @param match la sequenza da assegnare al {@code Matcher} e verificare se soddisfa la <em>regex.</em>
	 * @param flags eventuali flags di controllo. Queste devono essere le stesse flags dichiarate in {@code Pattern}, ovvero:
	 * {@link Pattern#CASE_INSENSITIVE}, {@link Pattern#MULTILINE}, {@link Pattern#DOTALL},
     * {@link Pattern#UNICODE_CASE}, {@link Pattern#CANON_EQ}, {@link Pattern#UNIX_LINES},
     * {@link Pattern#LITERAL}, {@link Pattern#UNICODE_CHARACTER_CLASS}
     * e {@link Pattern#COMMENTS}.
	 * @return un nuovo {@link PatternMatcher}.
	 */
	
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
