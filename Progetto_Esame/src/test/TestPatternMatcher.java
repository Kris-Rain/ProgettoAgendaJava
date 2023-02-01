package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.regex.Pattern;
import jbook.util.PatternMatcher;

import org.junit.jupiter.api.Test;

/**
 * @author Nicolò Bianchetto (matr. 20026606)
 * @author Kristian Rigo (matr. 20046665)
 */

class TestPatternMatcher {

	@Test
	void testVari() {
		assertTrue(PatternMatcher.create("Hello, World", "hello, world", Pattern.CASE_INSENSITIVE).matches());
		assertFalse(PatternMatcher.create("Hello, World", "hello, world").matches());
		assertTrue(PatternMatcher.create("HeLLo, World ! # ignora questo commento", "hello,world!", Pattern.CASE_INSENSITIVE, Pattern.COMMENTS).matches());
		assertFalse(PatternMatcher.create("HeLLo, World ! .", "hello,world!\n", Pattern.CASE_INSENSITIVE, Pattern.COMMENTS).matches());
		assertTrue(PatternMatcher.create("HeLLo, World ! .", "hello,world!\n", Pattern.CASE_INSENSITIVE, Pattern.COMMENTS, Pattern.DOTALL).matches());
		assertFalse(PatternMatcher.create("Þ þ î Î", "Þ Þ î î", Pattern.CASE_INSENSITIVE).matches());
		assertTrue(PatternMatcher.create("Þ þ î Î", "Þ Þ î î", Pattern.CASE_INSENSITIVE, Pattern.UNICODE_CASE).matches());
	}

}
