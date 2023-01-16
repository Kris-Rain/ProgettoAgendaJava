package jbook.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcher {
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
