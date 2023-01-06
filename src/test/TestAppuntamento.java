/**
 * @author Kristian Rigo
 * @author Nicolò Bianchetto
 */

package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import codice.Appuntamento;
import codice.AppuntamentoException;
import codice.Controllo;

class TestAppuntamento {

	@Test
	void testCostruttoreCompleto() throws AppuntamentoException {
		String data = "10-12-2024";
		String orario = "19-59";
		assertDoesNotThrow(() -> Controllo.controlloData(data));
		assertDoesNotThrow(() -> Controllo.controlloData("29-02-2024"));
		assertThrows(AppuntamentoException.class, () -> Controllo.controlloData("29-02-2023"));
		assertThrows(AppuntamentoException.class, () -> Controllo.controlloData("31-04-2023"));
		assertDoesNotThrow(() -> Controllo.controlloData("27-07-1998"));
		assertDoesNotThrow(() -> Controllo.controlloOrario(orario));
		Appuntamento a1 = new Appuntamento("10-12-2021", "13-30", "30", "Milano", "Luca");
		assertEquals("10-12-2021", a1.getData());
		assertFalse(a1.matchPersona("Giacomo"));
	}
	
	@Test
	void testIsCompatible() {
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
	void testMatchSingoloNome() {
		assertTrue(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchPersona("LUCA"));
		assertTrue(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchPersona("luca"));
		assertTrue(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchPersona("Luca"));
		assertTrue(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchPersona("lUca"));
		assertTrue(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchPersona("luCA"));
		assertTrue(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchPersona("LUca"));

	}
}
