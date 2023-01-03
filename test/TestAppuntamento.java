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
		String data="10-12-2023";
		String orario="19:59";
		assertDoesNotThrow(() -> Controllo.controlloData(data));
		assertDoesNotThrow(() -> Controllo.controlloOrario(orario));
		Appuntamento a1=new Appuntamento("10-12-2021", "13-30", "30", "Milano", "Luca");
		assertEquals("10-12-2021", a1.getData());
		assertEquals("Luca", a1.getNome());
	}
}
