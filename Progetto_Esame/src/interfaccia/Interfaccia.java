package interfaccia;

import java.io.IOException;
import java.util.NoSuchElementException;
import codice.*;
import jbook.util.Input;

public class Interfaccia {
	private static ContenitoreAgende box=new ContenitoreAgende();
	
	private static boolean inizializzaAgende() throws IOException {
		Agenda.createPathToAgende();
		return box.caricaMultiAgendeDaFiles();
	}
	
	private static void menuAgende() throws IOException {
		System.out.println(box.elencaNomiAgende());
		switch(Input.readString("1) Seleziona agenda\n2) Aggiungi agenda\n3) Rimuovi agenda\nq) Esci\n").toLowerCase()) {
		case "1", "seleziona", "seleziona agenda" -> {
			String selected=Input.readString("Digitare l'agenda desiderata (in modo preciso): ");
			try {
				menuSingolaAgenda(box.selezionaAgenda(selected));
			}
			catch(NoSuchElementException e) {
				System.err.println("L'agenda non esiste!");
			}
		}
		case "2", "aggiungi", "aggiungi agenda" -> {
			if(!aggiungiAgenda()) System.out.println("ATTENZIONE! Impossibile aggiungere l'agenda con il medesimo nome di un'altra!\n");
			else System.out.println("*** Agenda aggiunta con successo ***");
		}
		case "3", "rimuovi", "rimuovi agenda" -> {
			String selected=Input.readString("Digitare l'agenda desiderata (in modo preciso): ");
			try {
				rimuoviAgenda(box.selezionaAgenda(selected));
			}
			catch(NoSuchElementException e) {
				System.err.println("L'agenda non esiste!");
			}
		}
		case "4", "quit", "esci", "q" -> terminate();
		default -> System.out.println("Selezione non disponibile! Riprovare");
		}
	}
	
	private static boolean menuSingolaAgenda(Agenda agenda) {
		return false;
	}
	
	private static boolean rimuoviAgenda(Agenda agenda) {
		return box.removeAgenda(agenda.getNomeAgenda());
	}
	
	private static boolean aggiungiAgenda() {
		while(true) {
			switch(Input.readString("Creare una nuova agenda o importarla da file? ").toLowerCase()) {
			case "crea", "creare", "crea agenda", "crea nuova agenda" -> {
				return box.aggiungiAgenda(Input.readString("Inserire nome agenda: "));
			}
			case "importa", "importare", "importa da file" -> {
				try {
					return box.caricaAgendaDaFile(Input.readString("Inserire nome agenda da caricare: "));
				}
				catch(IOException e) {
					System.err.println("ATTENZIONE! Impossibile leggere l'agenda selezionata!");
					return false;
				}
			}
			default -> System.out.println("Selezione non disponibile! Riprovare");
			}
		}
	}
	
	private static void abort(String message, IOException e) {
		System.err.println(message);
		e.printStackTrace();
	}
	
	private static void terminate() throws IOException {
		if(!box.allSaved()) {
			if(askSomething("Alcune agende non sono state salvate, si desidera salvarle? ")) {
				try {
					box.salvaContenitoreSuFile();
				}
				catch(IOException e) {
					abort("ATTENZIONE! Salvataggio agende fallito!", e);
				}
			}
		}
		System.exit(0);
	}
	
	public static boolean askSomething(String question) {
        Boolean conferma = null;
        do {
            String risposta = Input.readString(question).strip().toLowerCase();
            switch(risposta) {
                case "y", "yes", "ye", "yeah", "yea", "si", "sì", "affermative" -> conferma = true;
                case "n", "no", "nop", "nope", "non", "not", "negative" -> conferma = false;
                default -> System.out.println("Scelta non valida!");
            }
        } while(conferma == null);

        return conferma;
    }
	
	public static void main(String[] args) throws IOException {
		try {
			if(!inizializzaAgende()) System.out.println("ATTENZIONE! Non è stato possibile caricare alcune agende!");
		}
		catch(IOException e) {
			abort("ATTENZIONE! Impossibile inizializzare:", e);
		}
		while(true) {
			menuAgende();
		}
	}
}
