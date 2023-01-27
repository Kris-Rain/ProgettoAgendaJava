package test;

import static org.junit.jupiter.api.Assertions.*;
import java.time.format.DateTimeParseException;
import org.junit.jupiter.api.Test;

import jbook.util.DataOrario;

/**
 * @author Nicolò Bianchetto (matr. 20026606)
 * @author Kristian Rigo (matr. 20046665)
 */

class TestDataOrario {

	@Test
	void testCostruttore() {
		assertEquals("12-02-2022", new DataOrario("12-02-2022", "12-31").getDataToString());
		assertEquals("14-30", new DataOrario("01-02-1998", "14-30").getOrarioToString());
		assertDoesNotThrow(() -> new DataOrario("01-02-1998", "12-30"));
		assertDoesNotThrow(() -> new DataOrario("29-02-2020", "23-30"));
		assertDoesNotThrow(() -> new DataOrario("05-07-2020", "00-00"));
		assertThrows(DateTimeParseException.class, () -> new DataOrario("03-02-1998", "12:30"));
		assertThrows(DateTimeParseException.class, () -> new DataOrario("29-02-2021", "07-45"));
		assertThrows(DateTimeParseException.class, () -> new DataOrario("28/02/2021", "15-00"));
		assertThrows(DateTimeParseException.class, () -> new DataOrario("05-15-2021", "08-00"));
	}
	
	@Test
	void testPlusMinuti() {
		assertEquals("01-01-2023 00-30", new DataOrario("31-12-2022", "23-30").plusMinuti("60").toString());
		assertEquals("01-03-2001 00-30", new DataOrario("28-02-2001", "23-30").plusMinuti("60").toString());
		assertEquals("01-03-2020 00-00", new DataOrario("29-02-2020", "23-30").plusMinuti("30").toString());
		assertEquals("01-01-2000 00-00", new DataOrario("31-12-1999", "23-30").plusMinuti("30").toString());
		assertNotEquals("01-01-1900 00-00", new DataOrario("31-12-1999", "23-30").plusMinuti("30").toString());
	}
	
	@Test
	void testComparazione() {
		assertEquals(-1, new DataOrario("28-02-2001", "23-30").compareTo(new DataOrario("01-03-2001", "00-30")));
		assertEquals(1, new DataOrario("28-02-2001", "00-00").compareTo(new DataOrario("27-02-2001", "23-59")));
		assertEquals(0, new DataOrario("28-02-2001", "00-00").compareTo(new DataOrario("28-02-2001", "00-00")));
		assertEquals(-1, new DataOrario("28-02-2001", "00-00").compareTo(new DataOrario("28-02-2001", "00-01")));
		assertEquals(-1, new DataOrario("31-12-2020", "23-59").compareTo(new DataOrario("01-01-2021", "00-00")));
		assertEquals(1, new DataOrario("01-01-2021", "00-00").compareTo(new DataOrario("31-12-2020", "23-59")));
		assertEquals(1, new DataOrario("01-01-2021", "00-00").compareTo("31-12-2020", "23-59"));
		assertEquals(-1, new DataOrario("29-02-2020", "23-00").compareTo("01-03-2020", "00-01"));
		assertEquals(-1, new DataOrario("31-12-1999", "23-59").compareTo("01-01-2000", "00-00"));
	}
	
	@Test
	void testToString() {
		assertEquals("28-02-2001 23-30", new DataOrario("28-02-2001", "23-30").toString());
	}
	
	@Test
	void testEquals() {
		assertTrue(new DataOrario("28-02-2001", "23-30").equals(new DataOrario("28-02-2001", "23-30")));
		assertTrue(new DataOrario("29-02-2020", "23-30").equals(new DataOrario("29-02-2020", "23-30")));
		assertFalse(new DataOrario("31-12-1999", "23-30").equals(new DataOrario("01-01-1900", "23-30")));
		assertFalse(new DataOrario("01-01-2000", "00-00").equals(new DataOrario("01-01-2001", "00-00")));
	}
}
