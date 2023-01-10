
/**
 * @author Kristian Rigo
 * @author Nicolò Bianchetto
 */

package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import codice.Appuntamento;
import codice.Appuntamento.ControlloDati;
import codice.Appuntamento.ControlloDati.TipoControllo;
import codice.AppuntamentoException;


class TestAppuntamento {

	@Test
	void testCostruttoreCompleto() throws AppuntamentoException {
		String data = "10-12-2024";
		String orario = "19-59";
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DATA, data));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DATA, "29-02-2024"));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DATA, "29-02-2023"));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DATA, "31-04-2023"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DATA, "27-07-1998"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_ORARIO, orario));
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
	void testControlloOrario() {
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_ORARIO, "4567"));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_ORARIO, ""));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_ORARIO, "%&(3"));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_ORARIO, "111-21"));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_ORARIO, "13-255"));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_ORARIO, "12-60"));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_ORARIO, "24-00"));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_ORARIO, "orario"));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_ORARIO, "12:30"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_ORARIO, "12-30"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_ORARIO, "00-00"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_ORARIO, "03-45"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_ORARIO, "02-12"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_ORARIO, "09-59"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_ORARIO, "07-23"));
	}
	
	
	@Test
	void testControlloNome() {
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_NOME, "  "));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_NOME, " "));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_NOME, ""));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_NOME, "$(!$)!"));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_NOME, "NomeTroppoLungoLungoooo CognomeTroppoLungoLungo"));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_NOME, "1234"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_NOME, "A"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_NOME, "Luca"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_NOME, "Paolo Bonolis"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_NOME, "Luca98 Bianchi"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_NOME, "MARCO"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_NOME, "Giovanni1234"));
	}
	
	
	@Test
	void testControlloLuogo() {
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_LUOGO, ""));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_LUOGO, " "));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_LUOGO, "1245"));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_LUOGO, "!($(%"));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_LUOGO, "Aereo123"));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_LUOGO, "Aereo!!!"));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_LUOGO, "DescrizioneLuogo DecisamenteTroppoLunga"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_LUOGO, "A"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_LUOGO, "Ufficio"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_LUOGO, "Via Roma 36"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_LUOGO, "Avenue Street 487"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_LUOGO, "Sala 2"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_LUOGO, "Palazzo Chigi"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_LUOGO, "Casa Paolo"));
	}
	
	@Test
	void testControlloDurata() throws AppuntamentoException {
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DURATA, "01"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DURATA, "0000001111"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DURATA, "1"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DURATA, "1000"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DURATA, "01000"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DURATA, "02361"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DURATA, "9999"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DURATA, "09999"));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DURATA, "10000"));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DURATA, "0000018532"));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DURATA, "099999"));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DURATA, "000000000"));
		
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

