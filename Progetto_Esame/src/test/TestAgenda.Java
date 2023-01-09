package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import codice.*;

class TestAgenda {

	@Test
	void testCostruttore() throws AppuntamentoException {
		assertEquals("Agenda", new Agenda().getNomeAgenda());
		assertEquals("Agenda", new Agenda("").getNomeAgenda());
		assertEquals(0, new Agenda().getDimensioneAgenda());
		
		assertEquals("Amici", new Agenda("Amici").getNomeAgenda());
		assertEquals(0, new Agenda().getDimensioneAgenda());
		
		ArrayList<Appuntamento> appuntamenti = new ArrayList<>();
		appuntamenti.add(new Appuntamento("01-02-1998", "14-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-1998", "12-30", "60", "Ufficio", "Capo"));
		appuntamenti.add(new Appuntamento("03-02-1998", "12-30", "60", "Ufficio", "Capo"));
		appuntamenti.add(new Appuntamento("03-02-1998", "11-30", "60", "Ufficio", "Luca"));	
		Agenda lavoro = new Agenda("Lavoro", appuntamenti);
		assertEquals("Lavoro", lavoro.getNomeAgenda());
		assertEquals(4, lavoro.getDimensioneAgenda());
		appuntamenti = lavoro.getAppuntamenti();
		/*Testo se ordinato tramite costruttore*/
		assertEquals("01-02-1998 12-30 60 Ufficio Capo\n", appuntamenti.get(0).toString());
		assertEquals("01-02-1998 14-30 60 Ufficio Luca\n", appuntamenti.get(1).toString());
		assertEquals("03-02-1998 11-30 60 Ufficio Luca\n", appuntamenti.get(2).toString());
		assertEquals("03-02-1998 12-30 60 Ufficio Capo\n", appuntamenti.get(3).toString());	
	}
	
	@Test
	void testCostruttoreDaFile() {
		/*test lettura da file*/
	}
	
	@Test
	void testSalvaAgendaSuFile() {
		/*test scrittura su file*/
	}
	
	@Test
	void testSetNomeAgenda() {
		Agenda test = new Agenda("Test");
		assertEquals("Test", test.getNomeAgenda());
		test.setNomeAgenda("Toast");
		assertEquals("Toast", test.getNomeAgenda());
	}
	
	@Test
	void testContains() throws AppuntamentoException {
		ArrayList<Appuntamento> appuntamenti = new ArrayList<>();
		Appuntamento daInserire = new Appuntamento("04-02-1998", "16-00", "80", "Sala Conferenze", "Luca");
		Appuntamento daNonInserire = new Appuntamento("04-02-1998", "16-00", "80", "Sala Conferenze", "Luca");
		appuntamenti.add(daInserire);
		appuntamenti.add(new Appuntamento("01-02-1998", "14-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("01-03-1998", "12-30", "60", "Ufficio", "Capo"));
		Agenda lavoro = new Agenda("Lavoro", appuntamenti);
		assertTrue(lavoro.contains(daInserire));
		assertFalse(lavoro.contains(daNonInserire));
		assertTrue(lavoro.contains("01-02-1998", "14-30"));
		assertTrue(lavoro.contains("01-03-1998", "12-30"));
		assertFalse(lavoro.contains("02-03-1998", "12-30"));
		assertFalse(lavoro.contains("01-03-1998", "12-35"));
	}
	
	@Test
	void testAggiungiAppuntamento() throws AppuntamentoException {
		Agenda lavoro = new Agenda("Lavoro");
		
		assertEquals(0, lavoro.getDimensioneAgenda());
		assertTrue(lavoro.aggiungiAppuntamento(new Appuntamento("04-02-1998", "16-00", "80", "Sala Conferenze", "Capo")));
		assertTrue(lavoro.aggiungiAppuntamento(new Appuntamento("04-02-1998", "17-20", "80", "Sala Conferenze", "Capo")));
		assertEquals(2, lavoro.getDimensioneAgenda());
		assertFalse(lavoro.aggiungiAppuntamento(new Appuntamento("04-02-1998", "16-30", "20", "Casa Mia", "Paolo")));
		assertFalse(lavoro.aggiungiAppuntamento(new Appuntamento("04-02-1998", "17-19", "15", "Casa Giovanni", "Giovanni")));
		assertEquals(2, lavoro.getDimensioneAgenda());
		
		assertTrue(lavoro.aggiungiAppuntamento("31-10-2020", "23-30", "120", "Casa Paolo", "Paolo"));
		assertTrue(lavoro.aggiungiAppuntamento("01-11-2020", "04-00", "30", "Aeroporto", "Giovanna"));
		assertTrue(lavoro.aggiungiAppuntamento("01-11-2020", "02-00", "20", "Casa mia", "Io"));
		assertTrue(lavoro.aggiungiAppuntamento("01-11-2020", "01-30", "15", "Casa Paolo", "Paolo"));
		assertTrue(lavoro.aggiungiAppuntamento("01-11-2020", "03-45", "15", "Aeroporto Parcheggio", "Giovanna"));
		assertEquals(7, lavoro.getDimensioneAgenda());
		
		assertFalse(lavoro.aggiungiAppuntamento("01-11-2020", "04-00", "30", "Aeroporto", "Giovanna"));
		assertFalse(lavoro.aggiungiAppuntamento("01-11-2020", "02-00", "20", "Casa mia", "Io"));
		assertFalse(lavoro.aggiungiAppuntamento("01-11-2020", "01-50", "20", "Ufficio", "Andrea"));
		assertFalse(lavoro.aggiungiAppuntamento("01-11-2020", "03-20", "26", "Bosco", "Er Falegname"));
		assertFalse(lavoro.aggiungiAppuntamento("01-11-2020", "03-50", "15", "Gattile", "Micio"));
		assertEquals(7, lavoro.getDimensioneAgenda());
	}

}
