package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import codice.Agenda;
import codice.Appuntamento;
import codice.AppuntamentoException;
import codice.ContenitoreAgende;

/**
 * @author Nicolò Bianchetto (matr. 20026606)
 * @author Kristian Rigo (matr. 20046665)
 */

class TestContenitoreAgende {
	
	private ContenitoreAgende box;

	@BeforeEach
	void testSetup() throws AppuntamentoException {
		ArrayList<Appuntamento> appuntamenti = new ArrayList<>();
		appuntamenti.add(new Appuntamento("02-06-2023", "14-30", "60", "Ufficio", "Luca"));
		appuntamenti.add(new Appuntamento("01-06-2023", "06-30", "45", "Viale Roma 22", "Giovanna Pascoli"));
		appuntamenti.add(new Appuntamento("02-06-2023", "07-00", "30", "Casa Mia", "Giacomo"));
		appuntamenti.add(new Appuntamento("02-06-2023", "06-30", "30", "Ristorante", "Giacomo"));
		Agenda varie = new Agenda("Varie", appuntamenti);
		ArrayList<Appuntamento> appuntamenti2 = new ArrayList<>();
		appuntamenti2.add(new Appuntamento("02-02-1998", "14-30", "60", "Ufficio", "Luca"));
		appuntamenti2.add(new Appuntamento("01-02-1998", "06-30", "45", "Viale Roma 22", "Mario Rossi"));
		appuntamenti2.add(new Appuntamento("02-02-1998", "07-00", "30", "Casa Mia", "Giacomo"));
		appuntamenti2.add(new Appuntamento("02-02-1998", "06-30", "30", "Casa Mia", "Giacomo"));
		appuntamenti2.add(new Appuntamento("03-02-1998", "06-30", "60", "Ufficio", "Luca"));
		appuntamenti2.add(new Appuntamento("01-02-1998", "07-20", "80", "Sala Conferenze 1", "Luca"));
		Agenda personale = new Agenda("Personale", appuntamenti2);
		ArrayList<Appuntamento> appuntamenti3 = new ArrayList<>();
		appuntamenti3.add(new Appuntamento("12-12-2022", "17-30", "60", "Macelleria", "Paolo"));
		appuntamenti3.add(new Appuntamento("11-12-2022", "06-30", "45", "Negozio", "Mario Rossi"));
		appuntamenti3.add(new Appuntamento("12-12-2022", "07-00", "30", "Cinema Mazzini", "Giacomo"));
		appuntamenti3.add(new Appuntamento("13-12-2022", "23-30", "60", "Ufficio", "Paolo"));
		appuntamenti3.add(new Appuntamento("11-12-2022", "07-20", "80", "Sala Conferenze 3", "Paolo"));
		Agenda altro = new Agenda("Altro", appuntamenti3);
		box = new ContenitoreAgende();
		box.aggiungiAgenda(varie);
		box.aggiungiAgenda(personale);
		box.aggiungiAgenda(altro);
	}
	
	
	@Test
	void testCostruttore() {
		ContenitoreAgende test = new ContenitoreAgende();
		assertEquals(0, test.getDimensione());
	}
	
	
	@Test
	void testSelezionaAgenda() {
		assertDoesNotThrow( () -> assertEquals(4, box.selezionaAgenda("Varie").getDimensioneAgenda()));
		assertDoesNotThrow( () -> assertEquals(6, box.selezionaAgenda("Personale").getDimensioneAgenda()));
		assertDoesNotThrow( () -> assertEquals(5, box.selezionaAgenda("Altro").getDimensioneAgenda()));
		assertThrows(NoSuchElementException.class, () -> box.selezionaAgenda("Agenda"));
		assertThrows(NoSuchElementException.class, () -> box.selezionaAgenda("Test"));
		assertThrows(NoSuchElementException.class, () -> box.selezionaAgenda("Non Esisto"));
	}
	
	
	@Test
	void testContains() {
		assertTrue(box.contains("Personale"));
		assertTrue(box.contains("Varie"));
		assertTrue(box.contains("Altro"));
		assertFalse(box.contains("altro"));
		assertFalse(box.contains("   Altro"));
		assertFalse(box.contains("Non Esisto"));
	}
	
	
	@Test
	void testAggiungiAgenda() {
		assertEquals(3, box.getDimensione());
		assertTrue(box.aggiungiAgenda("Extra"));
		assertTrue(box.contains("Extra"));
		assertEquals(4, box.getDimensione());
		assertFalse(box.aggiungiAgenda("Extra"));
		assertEquals(4, box.getDimensione());
		assertTrue(box.aggiungiAgenda(new Agenda("Pippo")));
		assertEquals(5, box.getDimensione());
		assertTrue(box.contains("Pippo"));
		assertFalse(box.aggiungiAgenda(new Agenda("Pippo")));
	}
	
	@Test
	void testModificaNomeAgenda() {
		assertTrue(box.modificaNomeAgenda("Varie", "Vario"));
		assertTrue(box.modificaNomeAgenda("Personale", "Famiglia"));
		assertFalse(box.modificaNomeAgenda("Personale", "Famiglia"));
		assertFalse(box.modificaNomeAgenda("Non Esisto", "Esisto"));
	}
	
	@Test
	void testRimuoviAgenda() {
		assertTrue(box.contains("Varie"));
		assertEquals(3, box.getDimensione());
		assertTrue(box.removeAgenda("Varie"));
		assertEquals(2, box.getDimensione());
		assertFalse(box.contains("Varie"));
		assertFalse(box.removeAgenda("Varie"));
		
		assertTrue(box.contains("Personale"));
		assertTrue(box.removeAgenda("Personale"));
		assertEquals(1, box.getDimensione());
		assertFalse(box.contains("Personale"));
		assertFalse(box.removeAgenda("Personale"));
		
		assertTrue(box.contains("Altro"));
		assertTrue(box.removeAgenda("Altro"));
		assertTrue(box.getAgende().isEmpty());
		assertFalse(box.contains("Altro"));
		assertFalse(box.removeAgenda("Altro"));
	}
	
	
	@Test
	void testClear() {
		assertEquals(3, box.getDimensione());
		assertTrue(box.clear());
		assertTrue(box.getAgende().isEmpty());
	}
	
	
	@Test
	void testSalvaCaricaSingolaAgenda() {
		assertDoesNotThrow( () -> assertTrue(box.salvaAgendaSuFile("Varie")));
		assertDoesNotThrow( () -> assertTrue(box.salvaAgendaSuFile("Personale")));
		assertDoesNotThrow( () -> assertTrue(box.salvaAgendaSuFile("Altro")));
		assertDoesNotThrow( () -> assertFalse(box.salvaAgendaSuFile("altro")));
		assertDoesNotThrow( () -> assertFalse(box.salvaAgendaSuFile("Non Esisto")));
		box.clear();
		assertDoesNotThrow( () -> assertTrue(box.caricaAgendaDaFile("Varie.txt")));
		assertDoesNotThrow( () -> assertTrue(box.caricaAgendaDaFile("Personale.txt")));
		assertDoesNotThrow( () -> assertTrue(box.caricaAgendaDaFile("Altro.txt")));
		assertEquals(3, box.getDimensione());
		assertDoesNotThrow( () -> assertFalse(box.caricaAgendaDaFile("Varie.txt")));
		assertDoesNotThrow( () -> assertFalse(box.caricaAgendaDaFile("Personale.txt")));
		assertDoesNotThrow( () -> assertFalse(box.caricaAgendaDaFile("Altro.txt")));
	}
	
	
	@Test
	void testSalvaCaricaMultiAgende() {
		assertFalse(box.allSaved());
		assertDoesNotThrow( () -> assertTrue(box.salvaContenitoreSuFile()));
		assertTrue(box.allSaved());
		box.clear();
		assertFalse(box.contains("Varie"));
		assertFalse(box.contains("Altro"));
		assertFalse(box.contains("Personale"));
		assertDoesNotThrow( () -> assertTrue(box.caricaMultiAgendeDaFiles()));
		assertDoesNotThrow( () -> assertFalse(box.caricaMultiAgendeDaFiles()));
		assertTrue(box.allSaved());
		assertTrue(box.contains("Varie"));
		assertTrue(box.contains("Altro"));
		assertTrue(box.contains("Personale"));
	}
	
	
	@Test
	void testElencaOrdineAlfabetico() {
		assertEquals("- Altro*\n- Personale*\n- Varie*\n", box.elencaNomiAgende());
	}
	
	
	@Test
	void testToString() {
		assertEquals("Agenda: Altro\n"
				+ "11-12-2022|06-30|45min|Negozio|Mario Rossi\n"
				+ "11-12-2022|07-20|80min|Sala Conferenze 3|Paolo\n"
				+ "12-12-2022|07-00|30min|Cinema Mazzini|Giacomo\n"
				+ "12-12-2022|17-30|60min|Macelleria|Paolo\n"
				+ "13-12-2022|23-30|60min|Ufficio|Paolo\n"
				+ "\n------------------------------------------\n\n"
				+ "Agenda: Personale\n"
				+ "01-02-1998|06-30|45min|Viale Roma 22|Mario Rossi\n"
				+ "01-02-1998|07-20|80min|Sala Conferenze 1|Luca\n"
				+ "02-02-1998|06-30|30min|Casa Mia|Giacomo\n"
				+ "02-02-1998|07-00|30min|Casa Mia|Giacomo\n"
				+ "02-02-1998|14-30|60min|Ufficio|Luca\n"
				+ "03-02-1998|06-30|60min|Ufficio|Luca\n"
				+ "\n------------------------------------------\n\n"
				+ "Agenda: Varie\n"
				+ "01-06-2023|06-30|45min|Viale Roma 22|Giovanna Pascoli\n"
				+ "02-06-2023|06-30|30min|Ristorante|Giacomo\n"
				+ "02-06-2023|07-00|30min|Casa Mia|Giacomo\n"
				+ "02-06-2023|14-30|60min|Ufficio|Luca\n"
				+ "\n------------------------------------------\n\n", box.toString());
	}
	
	
	@Test
	void testIterabile() {
		assertDoesNotThrow( () -> {
			for(Agenda elemento1: box) {
				for(Agenda elemento2: box) {
					elemento1.getNomeAgenda().equals(elemento2.getNomeAgenda());
				}
				
			}
		});
		
		assertThrows(ConcurrentModificationException.class, () -> {
			for(Agenda elemento: box) {
				box.removeAgenda(elemento.getNomeAgenda());
			}
		});

	}
}
