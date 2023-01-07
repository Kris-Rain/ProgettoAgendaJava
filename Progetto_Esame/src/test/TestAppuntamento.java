
/**
 * @author Kristian Rigo
 * @author Nicolò Bianchetto
 */

package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import codice.Appuntamento;
import codice.Appuntamento.ControlloDati;
import codice.AppuntamentoException;


class TestAppuntamento {

	@Test
	void testCostruttoreCompleto() throws AppuntamentoException {
		String data = "10-12-2024";
		String orario = "19-59";
		assertTrue(ControlloDati.controlloData(data));
		assertTrue(ControlloDati.controlloData("29-02-2024"));
		assertFalse(ControlloDati.controlloData("29-02-2023"));
		assertFalse(ControlloDati.controlloData("31-04-2023"));
		assertTrue(ControlloDati.controlloData("27-07-1998"));
		assertTrue(ControlloDati.controlloOrario(orario));
		Appuntamento a1 = new Appuntamento("10-12-2021", "13-30", "30", "Milano", "Luca");
		assertEquals("10-12-2021", a1.getData());
		assertFalse(a1.matchPersona("Giacomo"));
	}
	
	@Test
	void testIsCompatible() throws AppuntamentoException {
		Appuntamento a1 = new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca");
		Appuntamento a2 = new Appuntamento("10-12-2021", "23-45", "30", "Milano", "Luca");
		Appuntamento a3 = new Appuntamento("11-12-2021", "13-30", "30", "Milano", "Luca");
		Appuntamento a4 = new Appuntamento("10-12-2021", "14-00", "30", "Milano", "Luca");
		Appuntamento a5 = new Appuntamento("10-12-2021", "13-45", "30", "Milano", "Luca");
		assertFalse(a1.isCompatible(a2));
		assertTrue(a1.isCompatible(a3));
		assertTrue(a1.isCompatible(a4));
		assertTrue(a1.isCompatible(a5));
	}
	
	
	@Test
	void testControlloDurata() throws AppuntamentoException {
		assertTrue(ControlloDati.controlloDurata("01"));
		assertTrue(ControlloDati.controlloDurata("0000001111"));
		assertTrue(ControlloDati.controlloDurata("1"));
		assertTrue(ControlloDati.controlloDurata("1000"));
		assertTrue(ControlloDati.controlloDurata("01000"));
		assertTrue(ControlloDati.controlloDurata("02361"));
		assertTrue(ControlloDati.controlloDurata("9999"));
		assertTrue(ControlloDati.controlloDurata("09999"));
		assertFalse(ControlloDati.controlloDurata("10000"));
		assertFalse(ControlloDati.controlloDurata("0000018532"));
		assertFalse(ControlloDati.controlloDurata("099999"));
		assertFalse(ControlloDati.controlloDurata("000000000"));
		
		assertEquals("1111", new Appuntamento("10-12-2021", "23-30", "000001111", "Milano", "Luca").getDurata());
		assertEquals("1000", new Appuntamento("10-12-2021", "23-30", "000001000", "Milano", "Luca").getDurata());
		assertEquals("1001", new Appuntamento("10-12-2021", "23-30", "000001001", "Milano", "Luca").getDurata());
	}
	
	@Test
	void testMatchSingoloNome() throws AppuntamentoException {
		assertTrue(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchPersona("LUCA"));
		assertTrue(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchPersona("luca"));
		assertTrue(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchPersona("Luca"));
		assertTrue(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchPersona("lUca"));
		assertTrue(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchPersona("luCA"));
		assertTrue(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchPersona("LUca"));

	}
}

