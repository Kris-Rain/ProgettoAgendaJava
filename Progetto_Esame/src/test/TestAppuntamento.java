package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import codice.Appuntamento;
import codice.Appuntamento.ControlloDati;
import codice.Appuntamento.ControlloDati.TipoControllo;
import codice.AppuntamentoException;

/**
 * @author Nicolò Bianchetto (matr. 20026606)
 * @author Kristian Rigo (matr. 20046665)
 */

class TestAppuntamento {

	@Test
	void testCostruttoreCompleto() throws AppuntamentoException {
		assertDoesNotThrow(() -> new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca"));
		assertDoesNotThrow(() -> new Appuntamento("05-08-1997", "20-30", "15", "Via Novara 2", "Marco Rossi"));
		assertDoesNotThrow(() -> new Appuntamento("28-06-2430", "15-00", "60", "New York", "John"));
		assertThrows(AppuntamentoException.class, () -> new Appuntamento("08/05-2023", "23-30", "30", "Milano", "Luca"));
		assertThrows(AppuntamentoException.class, () -> new Appuntamento("10-12-2021", "23/30", "001", "Milano", "Luca"));
		assertThrows(AppuntamentoException.class, () -> new Appuntamento("10-12-2021", "23-30", "30", "Milano", "999"));

		Appuntamento a1 = new Appuntamento("10-12-2021", "13-30", "30", "Milano", "Luca");
		assertEquals("10-12-2021", a1.getData());
		assertEquals("13-30", a1.getOrario());
		assertEquals("30", a1.getDurata());
		assertEquals("Milano", a1.getLuogo());
		assertEquals("Luca", a1.getPersona());
		assertTrue(a1.matchPersona("Luca"));
		assertFalse(a1.matchPersona("Giacomo"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DATA, a1.getData()));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_ORARIO, a1.getOrario()));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DURATA, a1.getDurata()));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_LUOGO, a1.getLuogo()));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_NOME, a1.getPersona()));
		
		String[] param={"01-02-1998", "09-00", "80", "Sala Conferenze", "Luca"};
		String[] param2={"01-02-1998", "09-00", "80", "Sala Conferenze", "Luca"};
		String[] param3={"01-02-1998", "08-00", "45", "Viale Roma 22", "Giovanna Pascoli"};
		String[] param4={"01-02-1998", "08-00", "45000", "New York", "Johhny"};
		String[] param5={"29-02-2022", "07-00", "30", "Casa mia", "Giacomo"};
		String[] param6={"01-02-1998", "08-00", "45", "Ristorante", "1135"};
		assertDoesNotThrow(() -> new Appuntamento(param));
		assertDoesNotThrow(() -> new Appuntamento(param2));
		assertDoesNotThrow(() -> new Appuntamento(param3));
		assertThrows(AppuntamentoException.class, () -> new Appuntamento(param4));
		assertThrows(AppuntamentoException.class, () -> new Appuntamento(param5));
		assertThrows(AppuntamentoException.class, () -> new Appuntamento(param6));
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
	void testControlloData() throws AppuntamentoException{
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DATA, "25-12-2023"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DATA, "29-02-2020"));
		assertTrue(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DATA, "29-02-2024"));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DATA, " "));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DATA, ""));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DATA, "25/12/2022"));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DATA, "30-02-2021"));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DATA, "40-05-2023"));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DATA, "00-00-0000"));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DATA, "2020-02-12"));
		assertFalse(ControlloDati.controlloPer(TipoControllo.CONTROLLO_DATA, "08-31-2023"));
	}
	
	@Test
	void testMatchSingoloNome() throws AppuntamentoException {
		assertTrue(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchPersona("LUCA"));
		assertTrue(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchPersona("luca"));
		assertTrue(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchPersona("Luca"));
		assertTrue(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchPersona("lUca"));
		assertTrue(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchPersona("luCA"));
		assertTrue(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchPersona("LUca"));
		assertFalse(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchPersona("Marco"));
		assertFalse(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchPersona(" "));
		assertFalse(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchPersona(""));
	}
	
	@Test
	void testMatchData() throws AppuntamentoException {
		assertTrue(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchData("10-12-2021"));
		assertFalse(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchData("2021-10-12"));
		assertFalse(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchData("12-10-2021"));
		assertFalse(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchData("10/12/2021"));
		assertFalse(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchData(""));
		assertFalse(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchData(" "));
	}
	
	@Test
	void testMatchDataOrario() throws AppuntamentoException {
		assertTrue(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchDataOrario("10-12-2021", "23-30"));
		assertFalse(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchDataOrario("2021-10-12", "23-30"));
		assertFalse(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchDataOrario("12-10-2021", "23-30"));
		assertFalse(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchDataOrario("10/12/2021", "23-30"));
		assertFalse(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchDataOrario("10-12-2021", "23:30"));
		assertFalse(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchDataOrario("12-10-2021", "23/30"));
		assertFalse(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchDataOrario("12-10-2021", "13-00"));
		assertFalse(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchDataOrario("", ""));
		assertFalse(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchDataOrario(" ", " "));
		assertFalse(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchDataOrario(" ", "23-30"));
		assertFalse(new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca").matchDataOrario("10-12-2021", " "));
	}
	
	@Test
	void testIsCompatible() throws AppuntamentoException {
		Appuntamento a1 = new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca");
		Appuntamento a2 = new Appuntamento("10-12-2021", "23-45", "30", "Milano", "Luca");
		Appuntamento a3 = new Appuntamento("11-12-2021", "13-30", "30", "Milano", "Luca");
		Appuntamento a4 = new Appuntamento("10-12-2021", "14-00", "30", "Milano", "Luca");
		Appuntamento a5 = new Appuntamento("10-12-2021", "13-45", "30", "Milano", "Luca");
		Appuntamento a6 = new Appuntamento("11-12-2021", "13-45", "30", "Milano", "Luca");
		assertFalse(a1.isCompatible(a2));
		assertFalse(a4.isCompatible(a5));
		assertFalse(a3.isCompatible(a6));
		assertTrue(a1.isCompatible(a3));
		assertTrue(a1.isCompatible(a4));
		assertTrue(a1.isCompatible(a5));
	}
	
	@Test
	void testToString() throws AppuntamentoException {
		Appuntamento a1 = new Appuntamento("10-12-2021", "23-30", "30", "Milano", "Luca");
		assertEquals("10-12-2021|23-30|30min|Milano|Luca\n", a1.toString());
	}
}
