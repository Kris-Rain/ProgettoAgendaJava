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
		String orario = "19:59";
		assertDoesNotThrow(() -> Controllo.controlloData(data));
		assertDoesNotThrow(() -> Controllo.controlloData("29-02-2024"));
		assertThrows(AppuntamentoException.class, () -> Controllo.controlloData("29-02-2023"));
		assertThrows(AppuntamentoException.class, () -> Controllo.controlloData("31-04-2023"));
		assertDoesNotThrow(() -> Controllo.controlloData("27-07-1998"));
		assertDoesNotThrow(() -> Controllo.controlloOrario(orario));
		Appuntamento a1 = new Appuntamento("10-12-2021", "13-30", "30", "Milano", "Luca");
		assertEquals("10-12-2021", a1.getData());
		assertFalse(a1.matchPersone("Luca", "Giacomo"));
	}
	
	
	@Test
	void testSommaOrario() {
		assertEquals("14:00", new Appuntamento("10-12-2021", "13-30", "30", "Milano", "Luca").getOrarioFine());
		assertEquals("14:15", new Appuntamento("10-12-2021", "13-30", "45", "Milano", "Luca").getOrarioFine());
		assertEquals("14:30", new Appuntamento("10-12-2021", "13-30", "60", "Milano", "Luca").getOrarioFine());
		assertEquals("13:30", new Appuntamento("10-12-2021", "13-30", "0", "Milano", "Luca").getOrarioFine());
		assertEquals("13:32", new Appuntamento("10-12-2021", "13-30", "2", "Milano", "Luca").getOrarioFine());
		assertEquals("16:00", new Appuntamento("10-12-2021", "13-30", "150", "Milano", "Luca").getOrarioFine());
		
	}
}
