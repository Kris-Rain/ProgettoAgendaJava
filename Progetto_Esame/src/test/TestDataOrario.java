package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import codice.DataOrario;

class TestDataOrario {


	@Test
	void testCostruttore() {
		DataOrario datatime = new DataOrario("12-02-2022", "12-31");
		assertEquals("12-02-2022", datatime.getDataToString());
		assertEquals("12-31", datatime.getOrarioToString());
	}
	
	@Test
	void testPlusMinuti() {
		assertEquals("01-01-2023 00-30", new DataOrario("31-12-2022", "23-30").plusMinuti("60").toString());
		assertEquals("01-03-2001 00-30", new DataOrario("28-02-2001", "23-30").plusMinuti("60").toString());
	}
	
	
	@Test
	void testComparazione() {
		assertEquals(-1, new DataOrario("28-02-2001", "23-30").compareTo(new DataOrario("01-03-2001", "00-30")));
		assertEquals(1, new DataOrario("28-02-2001", "00-00").compareTo(new DataOrario("27-02-2001", "23-59")));
		assertEquals(0, new DataOrario("28-02-2001", "00-00").compareTo(new DataOrario("28-02-2001", "00-00")));
		assertEquals(-1, new DataOrario("28-02-2001", "00-00").compareTo(new DataOrario("28-02-2001", "00-01")));
		assertEquals(-1, new DataOrario("31-12-2020", "23-59").compareTo(new DataOrario("01-01-2021", "00-00")));
		assertEquals(1, new DataOrario("01-01-2021", "00-00").compareTo(new DataOrario("31-12-2020", "23-59")));
	}

}
