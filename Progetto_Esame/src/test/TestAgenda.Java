package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

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
		assertEquals("01-02-1998|12-30|60min|Ufficio|Capo\n", appuntamenti.get(0).toString());
		assertEquals("01-02-1998|14-30|60min|Ufficio|Luca\n", appuntamenti.get(1).toString());
		assertEquals("03-02-1998|11-30|60min|Ufficio|Luca\n", appuntamenti.get(2).toString());
		assertEquals("03-02-1998|12-30|60min|Ufficio|Capo\n", appuntamenti.get(3).toString());	
		
		/*Testo se aggiungendo un appuntamento non valido, costruisco un agenda vuota nonostante gli passi l'arraylist */
		appuntamenti.add(new Appuntamento("01-02-1998", "14-30", "60", "Ufficio", "Luca"));
		assertEquals(0, new Agenda(appuntamenti).getDimensioneAgenda());
	}
	
	@Test
	void testLeggiScriviSuFile() throws AppuntamentoException, IOException {
		ArrayList<Appuntamento> appuntamenti = new ArrayList<>();
		appuntamenti.add(new Appuntamento("01-02-1998", "09-00", "80", "Sala Conferenze", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-1998", "08-00", "45", "Viale Roma 22", "Giovanna Pascoli"));
		appuntamenti.add(new Appuntamento("02-02-1998", "07-00", "30", "Casa Mia", "Me Stesso"));
		appuntamenti.add(new Appuntamento("02-02-1998", "06-30", "30", "Casa Mia", "Me Stesso"));
		appuntamenti.add(new Appuntamento("02-02-1998", "14-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("03-02-1998", "12-30", "60", "Ufficio", "Capo"));
		Agenda agenda = new Agenda("Pippo", appuntamenti);
		assertFalse(agenda.isSaved());
		assertTrue(agenda.salvaAgendaSuFile());
		assertTrue(agenda.isSaved());
		Agenda agenda2 = new Agenda(new File(Agenda.PATHNAME, agenda.getNomeAgenda() + ".txt"));
		assertTrue(agenda.isSaved());
		assertTrue(agenda.equals(agenda2));
		assertTrue(agenda2.salvaAgendaSuFile());
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
		Appuntamento daNonInserire = new Appuntamento("05-02-1998", "16-00", "80", "Sala Conferenze", "Luca");
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
	void testElencaPerPersona() throws AppuntamentoException {
		ArrayList<Appuntamento> appuntamenti = new ArrayList<>();
		appuntamenti.add(new Appuntamento("02-02-1998", "14-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-1998", "06-30", "45", "Viale Roma 22", "Giovanna Pascoli"));
		appuntamenti.add(new Appuntamento("02-02-1998", "07-00", "30", "Casa Mia", "Giacomo"));
		appuntamenti.add(new Appuntamento("02-02-1998", "06-30", "30", "Casa Mia", "Giacomo"));
		appuntamenti.add(new Appuntamento("03-02-1998", "06-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-1998", "07-20", "80", "Sala Conferenze", "Luca"));
		Agenda agenda = new Agenda(appuntamenti);
		assertEquals("01-02-1998|07-20|80min|Sala Conferenze|Luca\n"
					+ "02-02-1998|14-30|60min|Ufficio|Luca\n"
					+ "03-02-1998|06-30|60min|Ufficio|Luca\n", agenda.elencaPerPersona("luca"));
		assertEquals("02-02-1998|06-30|30min|Casa Mia|Giacomo\n"
					+ "02-02-1998|07-00|30min|Casa Mia|Giacomo\n", agenda.elencaPerPersona("GiAcomo"));
		assertEquals("01-02-1998|06-30|45min|Viale Roma 22|Giovanna Pascoli\n", agenda.elencaPerPersona("Giovanna Pascoli"));
		assertEquals("", agenda.elencaPerPersona("nome"));
	}
	
	
	@Test
	void testElencaPerData() throws AppuntamentoException {
		ArrayList<Appuntamento> appuntamenti = new ArrayList<>();
		appuntamenti.add(new Appuntamento("02-02-1998", "14-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-1998", "06-30", "45", "Viale Roma 22", "Giovanna Pascoli"));
		appuntamenti.add(new Appuntamento("02-02-1998", "07-00", "30", "Casa Mia", "Giacomo"));
		appuntamenti.add(new Appuntamento("02-02-1998", "06-30", "30", "Casa Mia", "Giacomo"));
		appuntamenti.add(new Appuntamento("03-02-1998", "06-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-1998", "07-20", "80", "Sala Conferenze", "Luca"));
		Agenda agenda = new Agenda(appuntamenti);
		assertEquals("01-02-1998|06-30|45min|Viale Roma 22|Giovanna Pascoli\n"
					+ "01-02-1998|07-20|80min|Sala Conferenze|Luca\n", agenda.elencaPerData("01-02-1998"));
		assertEquals("02-02-1998|06-30|30min|Casa Mia|Giacomo\n"
					+ "02-02-1998|07-00|30min|Casa Mia|Giacomo\n"
					+ "02-02-1998|14-30|60min|Ufficio|Luca\n", agenda.elencaPerData("02-02-1998"));
		assertEquals("03-02-1998|06-30|60min|Ufficio|Luca\n", agenda.elencaPerData("03-02-1998"));
		assertEquals("", agenda.elencaPerData("05-02-1998"));
	}
	
	
	@Test
	void testToString() throws AppuntamentoException {
		ArrayList<Appuntamento> appuntamenti = new ArrayList<>();
		appuntamenti.add(new Appuntamento("02-02-1998", "14-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-1998", "06-30", "45", "Viale Roma 22", "Giovanna Pascoli"));
		appuntamenti.add(new Appuntamento("02-02-1998", "07-00", "30", "Casa Mia", "Giacomo"));
		appuntamenti.add(new Appuntamento("02-02-1998", "06-30", "30", "Casa Mia", "Giacomo"));
		appuntamenti.add(new Appuntamento("03-02-1998", "06-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-1998", "07-20", "80", "Sala Conferenze", "Luca"));
		Agenda agenda = new Agenda("Tutto", appuntamenti);
		assertEquals("Agenda: Tutto\n"
					+ "01-02-1998|06-30|45min|Viale Roma 22|Giovanna Pascoli\n"
					+ "01-02-1998|07-20|80min|Sala Conferenze|Luca\n"
					+ "02-02-1998|06-30|30min|Casa Mia|Giacomo\n"
					+ "02-02-1998|07-00|30min|Casa Mia|Giacomo\n"
					+ "02-02-1998|14-30|60min|Ufficio|Luca\n"
					+ "03-02-1998|06-30|60min|Ufficio|Luca\n", agenda.toString());
		assertEquals("Agenda: Agenda Vuota\n", new Agenda("Agenda Vuota").toString());
	}
	
	
	@Test
	void testRimuoviPerData() throws AppuntamentoException {
		ArrayList<Appuntamento> appuntamenti = new ArrayList<>();
		appuntamenti.add(new Appuntamento("02-02-2023", "14-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-2023", "06-30", "45", "Viale Roma 22", "Giovanna Pascoli"));
		appuntamenti.add(new Appuntamento("02-02-2023", "07-00", "30", "Casa Mia", "Giacomo"));
		appuntamenti.add(new Appuntamento("02-02-2023", "06-30", "30", "Ristorante", "Giacomo"));
		appuntamenti.add(new Appuntamento("03-02-2023", "06-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-2023", "07-20", "80", "Sala Conferenze", "Luca"));
		Agenda agenda = new Agenda(appuntamenti);
		assertEquals(6, agenda.getDimensioneAgenda());
		assertTrue(agenda.rimuoviPerData("02-02-2023"));
		assertEquals(3, agenda.getDimensioneAgenda());
		assertFalse(agenda.rimuoviPerData("02-02-2023"));
		assertEquals(3, agenda.getDimensioneAgenda());
		assertTrue(agenda.rimuoviPerData("01-02-2023"));
		assertEquals(1, agenda.getDimensioneAgenda());
		assertFalse(agenda.rimuoviPerData("01-02-2023"));
		assertTrue(agenda.rimuoviPerData("03-02-2023"));
		assertEquals(0, agenda.getDimensioneAgenda());
		assertFalse(agenda.rimuoviPerData("03-02-2023"));	
	}
	
	
	@Test
	void testRimuoviPerPersona() throws AppuntamentoException {
		ArrayList<Appuntamento> appuntamenti = new ArrayList<>();
		appuntamenti.add(new Appuntamento("06-10-2023", "14-30", "30", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("01-10-2023", "06-30", "25", "Viale Roma 22", "Giovanna Pascoli"));
		appuntamenti.add(new Appuntamento("02-10-2023", "07-00", "60", "Casa Mia", "Giacomo"));
		appuntamenti.add(new Appuntamento("04-10-2023", "06-30", "120", "Ristorante", "Giacomo"));
		appuntamenti.add(new Appuntamento("03-10-2023", "06-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("01-10-2023", "07-20", "80", "Sala Conferenze", "Luca"));
		Agenda agenda = new Agenda(appuntamenti);
		assertEquals(6, agenda.getDimensioneAgenda());
		assertTrue(agenda.rimuoviPerPersona("luca"));
		assertEquals(3, agenda.getDimensioneAgenda());
		assertFalse(agenda.rimuoviPerPersona("luca"));
		assertEquals(3, agenda.getDimensioneAgenda());
		assertTrue(agenda.rimuoviPerPersona("giacomo"));
		assertEquals(1, agenda.getDimensioneAgenda());
		assertFalse(agenda.rimuoviPerPersona("giacomo"));
		assertTrue(agenda.rimuoviPerPersona("giovanna pascoli"));
		assertEquals(0, agenda.getDimensioneAgenda());
		assertFalse(agenda.rimuoviPerPersona("giovanna pascoli"));	
	}
	
	
	@Test
	void testRimuoviPerDataOrario() throws AppuntamentoException {
		ArrayList<Appuntamento> appuntamenti = new ArrayList<>();
		appuntamenti.add(new Appuntamento("02-02-2023", "14-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-2023", "06-30", "45", "Viale Roma 22", "Giovanna Pascoli"));
		appuntamenti.add(new Appuntamento("02-02-2023", "07-00", "30", "Casa Mia", "Giacomo"));
		appuntamenti.add(new Appuntamento("02-02-2023", "06-30", "30", "Ristorante", "Giacomo"));
		appuntamenti.add(new Appuntamento("03-02-2023", "06-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-2023", "07-20", "80", "Sala Conferenze", "Luca"));
		Agenda agenda = new Agenda(appuntamenti);
		assertEquals(6, agenda.getDimensioneAgenda());
		assertTrue(agenda.rimuoviPerDataOrario("02-02-2023", "07-00"));
		assertEquals(5, agenda.getDimensioneAgenda());
		assertFalse(agenda.rimuoviPerDataOrario("02-02-2023", "07-00"));
		assertEquals(5, agenda.getDimensioneAgenda());
		assertTrue(agenda.rimuoviPerDataOrario("02-02-2023", "06-30"));
		assertEquals(4, agenda.getDimensioneAgenda());
		assertTrue(agenda.rimuoviPerDataOrario("01-02-2023", "06-30"));
		assertEquals(3, agenda.getDimensioneAgenda());
		assertFalse(agenda.rimuoviPerDataOrario("01-02-2023", "06-30"));
		assertEquals(3, agenda.getDimensioneAgenda());
		assertTrue(agenda.rimuoviPerDataOrario("03-02-2023", "06-30"));
		assertEquals(2, agenda.getDimensioneAgenda());
		assertTrue(agenda.rimuoviPerDataOrario("02-02-2023", "14-30"));
		assertEquals(1, agenda.getDimensioneAgenda());
		assertFalse(agenda.rimuoviPerDataOrario("02-02-2023", "14-30"));
		assertEquals(1, agenda.getDimensioneAgenda());
		assertTrue(agenda.rimuoviPerDataOrario("01-02-2023", "07-20"));
		assertEquals(0, agenda.getDimensioneAgenda());
	}
	
	
	@Test
	void testModificaAppuntamento() throws AppuntamentoException {
		ArrayList<Appuntamento> appuntamenti = new ArrayList<>();
		appuntamenti.add(new Appuntamento("02-02-2023", "14-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-2023", "06-30", "45", "Viale Roma 22", "Giovanna Pascoli"));
		appuntamenti.add(new Appuntamento("02-02-2023", "07-00", "30", "Casa Mia", "Giacomo"));
		appuntamenti.add(new Appuntamento("02-02-2023", "06-30", "30", "Ristorante", "Giacomo"));
		appuntamenti.add(new Appuntamento("03-02-2023", "06-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-2023", "07-20", "80", "Sala Conferenze", "Luca"));
		Agenda agenda = new Agenda(appuntamenti);
		
		assertEquals(0, agenda.modificaAppuntamento("05-02-2023", "06-30", "data", "01-02-2023"));
		assertEquals(0, agenda.modificaAppuntamento("02-02-2023", "00-30", "orario", "03-00"));
		assertEquals(-2, agenda.modificaAppuntamento("02-02-2023", "07-00", "data", "00-00-0000"));
		assertEquals(-2, agenda.modificaAppuntamento("02-02-2023", "07-00", "orario", "24-00"));
		assertEquals(-2, agenda.modificaAppuntamento("02-02-2023", "07-00", "durata", "15.30"));
		assertEquals(-2, agenda.modificaAppuntamento("02-02-2023", "07-00", "persona", "12345"));
		assertEquals(-2, agenda.modificaAppuntamento("02-02-2023", "07-00", "luogo", "!&$(!$"));
		assertEquals(-3, agenda.modificaAppuntamento("02-02-2023", "07-00", "", "02-04-2023"));
		assertEquals(-3, agenda.modificaAppuntamento("02-02-2023", "07-00", "ora", "12-00"));
		
		assertEquals("Casa Mia", agenda.searchAppuntamentoPerDataOrario("02-02-2023", "07-00").get(0).getLuogo());
		assertEquals(1, agenda.modificaAppuntamento("02-02-2023", "07-00", "luogo  ", "Casa Giacomo"));
		assertEquals("Casa Giacomo", agenda.searchAppuntamentoPerDataOrario("02-02-2023", "07-00").get(0).getLuogo());
		
		assertEquals("Giacomo", agenda.searchAppuntamentoPerDataOrario("02-02-2023", "07-00").get(0).getPersona());
		assertEquals(1, agenda.modificaAppuntamento("02-02-2023", "07-00", " persona", "Giacomino"));
		assertEquals("Giacomino", agenda.searchAppuntamentoPerDataOrario("02-02-2023", "07-00").get(0).getPersona());
		
		assertTrue(agenda.searchAppuntamentoPerDataOrario("02-02-2023", "06-55").isEmpty());
		assertEquals(-1, agenda.modificaAppuntamento("02-02-2023", "07-00", "orario", "06-55"));
		assertTrue(agenda.searchAppuntamentoPerDataOrario("02-02-2023", "06-55").isEmpty());
		assertFalse(agenda.searchAppuntamentoPerDataOrario("02-02-2023", "07-00").isEmpty());
		
		assertEquals(-1, agenda.modificaAppuntamento("01-02-2023", "06-30", "orario", "07-25"));
		assertTrue(agenda.searchAppuntamentoPerDataOrario("01-02-2023", "07-25").isEmpty());
		assertFalse(agenda.searchAppuntamentoPerDataOrario("01-02-2023", "06-30").isEmpty());
		
		assertTrue(agenda.searchAppuntamentoPerData("04-02-2023").isEmpty());
		assertEquals(1, agenda.modificaAppuntamento("02-02-2023", "06-30", "data", "04-02-2023"));
		assertFalse(agenda.searchAppuntamentoPerData("04-02-2023").isEmpty());
		
		assertEquals(-1, agenda.modificaAppuntamento("04-02-2023", "06-30", "data", "03-02-2023"));
		assertFalse(agenda.searchAppuntamentoPerData("04-02-2023").isEmpty());
		
		assertEquals(-1, agenda.modificaAppuntamento("04-02-2023", "06-30", "data", "01-02-2023"));
		assertFalse(agenda.searchAppuntamentoPerData("04-02-2023").isEmpty());
	
		assertTrue(agenda.searchAppuntamentoPerDataOrario("04-02-2023", "05-30").isEmpty());
		assertEquals(1, agenda.modificaAppuntamento("04-02-2023", "06-30", "Orario", "05-30"));
		assertFalse(agenda.searchAppuntamentoPerDataOrario("04-02-2023", "05-30").isEmpty());
		
		assertEquals("08-40", agenda.searchAppuntamentoPerDataOrario("01-02-2023", "07-20").get(0).getDataTimeFine().getOrarioToString());
		assertEquals(1, agenda.modificaAppuntamento("01-02-2023", "07-20", "DURATA", "120"));
		assertEquals("09-20", agenda.searchAppuntamentoPerDataOrario("01-02-2023", "07-20").get(0).getDataTimeFine().getOrarioToString());
		
		assertEquals(1, agenda.modificaAppuntamento("01-02-2023", "07-20", "DURATA", "40"));
		assertEquals("08-00", agenda.searchAppuntamentoPerDataOrario("01-02-2023", "07-20").get(0).getDataTimeFine().getOrarioToString());
		
		assertEquals("07-15", agenda.searchAppuntamentoPerDataOrario("01-02-2023", "06-30").get(0).getDataTimeFine().getOrarioToString());
		assertEquals(-1, agenda.modificaAppuntamento("01-02-2023", "06-30", "Durata", "55"));
		assertEquals("07-15", agenda.searchAppuntamentoPerDataOrario("01-02-2023", "06-30").get(0).getDataTimeFine().getOrarioToString());
		
		assertFalse(agenda.searchAppuntamentoPerDataOrario("02-02-2023", "14-30").isEmpty());
		assertEquals("02-02-2023", agenda.searchAppuntamentoPerDataOrario("02-02-2023", "14-30").get(0).getDataTimeFine().getDataToString());
		assertEquals(1, agenda.modificaAppuntamento("02-02-2023", "14-30", "orario", "23-30"));
		assertTrue(agenda.searchAppuntamentoPerDataOrario("02-02-2023", "14-30").isEmpty());
		assertFalse(agenda.searchAppuntamentoPerDataOrario("02-02-2023", "23-30").isEmpty());
		assertEquals("03-02-2023", agenda.searchAppuntamentoPerDataOrario("02-02-2023", "23-30").get(0).getDataTimeFine().getDataToString());
		assertEquals("00-30", agenda.searchAppuntamentoPerDataOrario("02-02-2023", "23-30").get(0).getDataTimeFine().getOrarioToString());
		assertEquals(1, agenda.modificaAppuntamento("02-02-2023", "23-30", " durata ", "20"));
		assertEquals("02-02-2023", agenda.searchAppuntamentoPerDataOrario("02-02-2023", "23-30").get(0).getDataTimeFine().getDataToString());
		assertEquals("23-50", agenda.searchAppuntamentoPerDataOrario("02-02-2023", "23-30").get(0).getDataTimeFine().getOrarioToString());	
	}
	
	
	@Test
	void testIterabile() throws AppuntamentoException {
		ArrayList<Appuntamento> appuntamenti = new ArrayList<>();
		appuntamenti.add(new Appuntamento("02-02-2023", "14-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-2023", "06-30", "45", "Viale Roma 22", "Giovanna Pascoli"));
		appuntamenti.add(new Appuntamento("02-02-2023", "07-00", "30", "Casa Mia", "Giacomo"));
		appuntamenti.add(new Appuntamento("02-02-2023", "06-30", "30", "Ristorante", "Giacomo"));
		appuntamenti.add(new Appuntamento("03-02-2023", "06-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-2023", "07-20", "80", "Sala Conferenze", "Luca"));
		Agenda agenda = new Agenda(appuntamenti);
		
		assertDoesNotThrow( () -> {
			for(Appuntamento elemento: agenda) {
				for(Appuntamento elemento2: agenda) {
					elemento.matchData(elemento2.getData());
				}
				
			}
		});
		
		
		assertThrows(ConcurrentModificationException.class, () -> {
			for(Appuntamento elemento: agenda) {
				agenda.rimuoviPerData(elemento.getData());
			}
		});
		
		assertDoesNotThrow(() -> {
			for(Appuntamento elemento: agenda.clone()) {
				agenda.rimuoviPerData(elemento.getData());
			}
		});
	}
	
	
	@Test
	void testCloneEquals() throws AppuntamentoException {
		ArrayList<Appuntamento> appuntamenti = new ArrayList<>();
		appuntamenti.add(new Appuntamento("02-02-2023", "14-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-2023", "06-30", "45", "Viale Roma 22", "Giovanna Pascoli"));
		appuntamenti.add(new Appuntamento("02-02-2023", "07-00", "30", "Casa Mia", "Giacomo"));
		appuntamenti.add(new Appuntamento("02-02-2023", "06-30", "30", "Ristorante", "Giacomo"));
		appuntamenti.add(new Appuntamento("03-02-2023", "06-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-2023", "07-20", "80", "Sala Conferenze", "Luca"));
		Agenda agenda1 = new Agenda(appuntamenti);
		Agenda agenda2 = new Agenda("Pippo", appuntamenti);
		Agenda agenda3 = new Agenda("Pippo");
		
		assertTrue(agenda1.equals(agenda1.clone()));
		assertTrue(agenda2.equals(agenda2.clone()));
		assertFalse(agenda1.equals(agenda2));
		assertFalse(agenda2.equals(agenda3));
	}
	
	
	@Test
	void testIsSaved() throws AppuntamentoException, IOException {
		ArrayList<Appuntamento> appuntamenti = new ArrayList<>();
		appuntamenti.add(new Appuntamento("02-02-2023", "14-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("01-02-2023", "06-30", "45", "Viale Roma 22", "Giovanna Pascoli"));
		appuntamenti.add(new Appuntamento("02-02-2023", "07-00", "30", "Casa Mia", "Giacomo"));
		appuntamenti.add(new Appuntamento("02-02-2023", "06-30", "30", "Ristorante", "Giacomo"));
		Agenda varie = new Agenda("Varie", appuntamenti);
		assertFalse(varie.isSaved());
		assertTrue(varie.salvaAgendaSuFile());
		assertTrue(varie.isSaved());
		assertEquals(-1, varie.modificaAppuntamento("02-02-2023", "14-30", "orario", "07-15"));
		assertTrue(varie.isSaved());
		assertEquals(1, varie.modificaAppuntamento("02-02-2023", "14-30", "orario", "13-30"));
		assertFalse(varie.isSaved());
		assertTrue(varie.salvaAgendaSuFile());
		assertTrue(varie.isSaved());
		assertFalse(varie.aggiungiAppuntamento("01-02-2023", "06-35", "15", "Ufficio", "Paolo"));
		assertTrue(varie.isSaved());
		assertTrue(varie.aggiungiAppuntamento("01-02-2023", "12-00", "15", "Ufficio", "Paolo"));
		assertFalse(varie.isSaved());
		assertTrue(varie.salvaAgendaSuFile());
		assertTrue(varie.isSaved());
		assertFalse(varie.rimuoviPerPersona("romolo"));
		assertTrue(varie.isSaved());
		assertTrue(varie.rimuoviPerPersona("giacomo"));
		assertFalse(varie.isSaved());
		assertTrue(varie.salvaAgendaSuFile());
		assertTrue(varie.isSaved());
	}

}
