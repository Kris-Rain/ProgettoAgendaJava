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
		Agenda agenda = new Agenda(appuntamenti);
		assertEquals("Lavoro", lavoro.getNomeAgenda());
		assertEquals(4, lavoro.getDimensioneAgenda());
		assertEquals("Agenda", agenda.getNomeAgenda());
		assertEquals(4, agenda.getDimensioneAgenda());
		
		/*Testo se ho ordinato tramite costruttore*/
		appuntamenti = lavoro.getAppuntamenti();
		assertEquals("01-02-1998 12-30 60 Ufficio Capo\n", appuntamenti.get(0).toString());
		assertEquals("01-02-1998 14-30 60 Ufficio Luca\n", appuntamenti.get(1).toString());
		assertEquals("03-02-1998 11-30 60 Ufficio Luca\n", appuntamenti.get(2).toString());
		assertEquals("03-02-1998 12-30 60 Ufficio Capo\n", appuntamenti.get(3).toString());	
		
		/*Testo se aggiungendo un appuntamento non valido, costruisco un agenda vuota nonostante gli passi l'arraylist */
		appuntamenti.add(new Appuntamento("01-02-1998", "14-30", "60", "Ufficio", "Luca"));
		assertEquals(0, new Agenda(appuntamenti).getDimensioneAgenda());
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
	void testIsAgenda() throws AppuntamentoException {
		ArrayList<Appuntamento> appuntamenti = new ArrayList<>();
		assertTrue(Agenda.isAgenda(appuntamenti));
		appuntamenti.add(new Appuntamento("01-02-1998", "09-00", "80", "Sala Conferenze", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-1998", "08-00", "45", "Viale Roma 22", "Giovanna Pascoli"));
		appuntamenti.add(new Appuntamento("01-02-1998", "07-00", "30", "Casa Mia", "Me Stesso"));
		appuntamenti.add(new Appuntamento("01-02-1998", "06-30", "30", "Casa Mia", "Me Stesso"));
		appuntamenti.add(new Appuntamento("01-02-1998", "14-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-1998", "12-30", "60", "Ufficio", "Capo"));
		assertTrue(Agenda.isAgenda(appuntamenti));
		
		appuntamenti.add(new Appuntamento("01-02-1998", "13-00", "10", "Ufficio", "Luca"));
		assertFalse(Agenda.isAgenda(appuntamenti));	
		
		appuntamenti.removeAll(appuntamenti);
		assertTrue(Agenda.isAgenda(appuntamenti));
		appuntamenti.add(new Appuntamento("23-07-2023", "09-00", "30", "Casa Mia", "Me Stesso"));
		appuntamenti.add(new Appuntamento("23-07-2023", "08-00", "60", "Ufficio", "Capo"));
		appuntamenti.add(new Appuntamento("23-07-2023", "09-30", "30", "Casa Mia", "Me Stesso"));
		appuntamenti.add(new Appuntamento("23-07-2023", "11-30", "30", "Barbiere", "Francesco"));
		appuntamenti.add(new Appuntamento("23-07-2023", "12-12", "60", "New York", "Luca"));
		appuntamenti.add(new Appuntamento("23-07-2023", "06-00", "60", "Ufficio", "Capo"));
		assertTrue(Agenda.isAgenda(appuntamenti));
		
		appuntamenti.add(new Appuntamento("23-07-2023", "06-59", "10", "Ufficio", "Luca"));
		assertFalse(Agenda.isAgenda(appuntamenti));	
		
	}
	
	@Test
	void testIsCompatible() throws AppuntamentoException {
		ArrayList<Appuntamento> appuntamenti = new ArrayList<>();
		appuntamenti.add(new Appuntamento("01-02-1998", "09-00", "80", "Sala Conferenze", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-1998", "08-00", "45", "Viale Roma 22", "Giovanna Pascoli"));
		appuntamenti.add(new Appuntamento("02-02-1998", "07-00", "30", "Casa Mia", "Me Stesso"));
		appuntamenti.add(new Appuntamento("02-02-1998", "06-30", "30", "Casa Mia", "Me Stesso"));
		appuntamenti.add(new Appuntamento("02-02-1998", "14-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("03-02-1998", "12-30", "60", "Ufficio", "Capo"));
		Agenda agenda = new Agenda(appuntamenti);
		
		assertTrue(agenda.isCompatible(new Appuntamento("04-02-1998", "12-30", "60", "Ufficio", "Capo")));
		assertTrue(agenda.isCompatible(new Appuntamento("01-02-1998", "07-00", "60", "Ufficio", "Luca")));
		assertTrue(agenda.isCompatible(new Appuntamento("03-02-1998", "13-30", "30", "Poste", "Capo")));
		assertTrue(agenda.isCompatible(new Appuntamento("02-02-1998", "11-00", "30", "Casa", "Mamma")));
		assertFalse(agenda.isCompatible(new Appuntamento("01-02-1998", "09-00", "80", "Sala Conferenze", "Luca")));
		assertFalse(agenda.isCompatible(new Appuntamento("02-02-1998", "06-45", "30", "Ufficio", "Capo")));
		assertFalse(agenda.isCompatible(new Appuntamento("02-02-1998", "06-59", "30", "Scuola", "Marta")));
		assertFalse(agenda.isCompatible(new Appuntamento("03-02-1998", "11-00", "120", "Ufficio", "Giacomo")));
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
	
	
	@Test
	void testRicercaAppuntamentoPerData() throws AppuntamentoException {
		ArrayList<Appuntamento> appuntamenti = new ArrayList<>();
		appuntamenti.add(new Appuntamento("01-02-1998", "09-00", "80", "Sala Conferenze", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-1998", "08-00", "45", "Viale Roma 22", "Giovanna Pascoli"));
		appuntamenti.add(new Appuntamento("02-02-1998", "07-00", "30", "Casa Mia", "Me Stesso"));
		appuntamenti.add(new Appuntamento("02-02-1998", "06-30", "30", "Casa Mia", "Me Stesso"));
		appuntamenti.add(new Appuntamento("02-02-1998", "14-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("03-02-1998", "12-30", "60", "Ufficio", "Capo"));
		Agenda agenda = new Agenda(appuntamenti);
		
		assertEquals(1, agenda.searchAppuntamentoPerData("03-02-1998").size());
		assertEquals(3, agenda.searchAppuntamentoPerData("02-02-1998").size());
		assertEquals(2, agenda.searchAppuntamentoPerData("01-02-1998").size());
		assertEquals(0, agenda.searchAppuntamentoPerData("04-02-1998").size());
		assertEquals(0, agenda.searchAppuntamentoPerData("04-02").size());
	}
	
	
	@Test
	void testRicercaAppuntamentoPerPersona() throws AppuntamentoException {
		ArrayList<Appuntamento> appuntamenti = new ArrayList<>();
		appuntamenti.add(new Appuntamento("01-02-1998", "07-20", "80", "Sala Conferenze", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-1998", "06-30", "45", "Viale Roma 22", "Giovanna Pascoli"));
		appuntamenti.add(new Appuntamento("02-02-1998", "07-00", "30", "Casa Mia", "Giacomo"));
		appuntamenti.add(new Appuntamento("02-02-1998", "06-30", "30", "Casa Mia", "Giacomo"));
		appuntamenti.add(new Appuntamento("02-02-1998", "14-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("03-02-1998", "06-30", "60", "Ufficio", "Luca"));
		Agenda agenda = new Agenda(appuntamenti);
		
		assertEquals(3, agenda.searchAppuntamentoPerPersona("luca").size());
		assertEquals(2, agenda.searchAppuntamentoPerPersona("GIACOMO").size());
		assertEquals(1, agenda.searchAppuntamentoPerPersona("GioVanna pascoli").size());
		assertEquals(0, agenda.searchAppuntamentoPerPersona("paolo").size());
		assertEquals(0, agenda.searchAppuntamentoPerPersona("Luc").size());
	}
	
	
	@Test
	void testRicercaAppuntamentoPerDataOrario() throws AppuntamentoException {
		ArrayList<Appuntamento> appuntamenti = new ArrayList<>();
		appuntamenti.add(new Appuntamento("01-02-1998", "07-20", "80", "Sala Conferenze", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-1998", "06-30", "45", "Viale Roma 22", "Giovanna Pascoli"));
		appuntamenti.add(new Appuntamento("02-02-1998", "07-00", "30", "Casa Mia", "Giacomo"));
		appuntamenti.add(new Appuntamento("02-02-1998", "06-30", "30", "Casa Mia", "Giacomo"));
		appuntamenti.add(new Appuntamento("02-02-1998", "14-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("03-02-1998", "06-30", "60", "Ufficio", "Luca"));
		Agenda agenda = new Agenda(appuntamenti);
		
		assertEquals(1, agenda.searchAppuntamentoPerDataOrario("01-02-1998", "06-30").size());
		assertEquals(1, agenda.searchAppuntamentoPerDataOrario("01-02-1998", "07-20").size());
		assertEquals(1, agenda.searchAppuntamentoPerDataOrario("03-02-1998", "06-30").size());
		assertEquals(1, agenda.searchAppuntamentoPerDataOrario("02-02-1998", "06-30").size());
		assertEquals(1, agenda.searchAppuntamentoPerDataOrario("02-02-1998", "07-00").size());
		assertEquals(1, agenda.searchAppuntamentoPerDataOrario("02-02-1998", "14-30").size());
		
		assertTrue(agenda.searchAppuntamentoPerDataOrario("02-02-1998", "17-30").isEmpty());
		assertTrue(agenda.searchAppuntamentoPerDataOrario("04-02-1998", "07-20").isEmpty());
		assertTrue(agenda.searchAppuntamentoPerDataOrario("04-02-1998", "06-30").isEmpty());
		assertTrue(agenda.searchAppuntamentoPerDataOrario("01-02-1998", "23-00").isEmpty());
		assertTrue(agenda.searchAppuntamentoPerDataOrario("03-02-1998", "07-00").isEmpty());
	}
	
	@Test
	void test() throws AppuntamentoException {
		ArrayList<Appuntamento> appuntamenti = new ArrayList<>();
		appuntamenti.add(new Appuntamento("01-02-1998", "07-20", "80", "Sala Conferenze", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-2000", "06-30", "45", "Viale Roma 22", "Giovanna Pascoli"));
		Agenda agenda = new Agenda(appuntamenti);
		agenda.modificaAppuntamento("01-02-1998", "07-20", "data", "05-02-1998");
		System.out.println(agenda.searchAppuntamentoPerData("05-02-1998").toString());
	}
	

}
