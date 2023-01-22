/**
 * @author Nicolò Bianchetto
 * @author Kristian Rigo
 */

package interfaccia;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.function.BiPredicate;
import codice.*;
import jbook.util.*;
import codice.Appuntamento.ControlloDati;
import codice.Appuntamento.ControlloDati.TipoControllo;

public class Interfaccia {
	private static final String ANSI_RESET = "\u001B[0m";
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_YELLOW = "\u001B[33m";
	public static final String YELLOW_BRIGHT = "\033[0;93m";
	
    private static ContenitoreAgende box = new ContenitoreAgende();

    private static boolean inizializzaAgende() throws IOException {
		Agenda.createPathToAgende();
		return box.caricaMultiAgendeDaFiles();
	}
	
	private static void menuAgende() {
		System.out.println("Agende:\n"+box.elencaNomiAgende());
		switch(Input.readString("1) Seleziona agenda\n2) Aggiungi agenda\n3) Rimuovi agenda\nq) Esci\nInserire un'opzione: ").toLowerCase()) {
			case "1", "seleziona", "seleziona agenda" -> {
				String selected=Input.readString("Digitare l'agenda desiderata (il controllo è case-sensitive e tiene conto degli spazi): ");
				try {
					menuSingolaAgenda(box.selezionaAgenda(selected));
				}
				catch(NoSuchElementException e) {
					System.out.println(ANSI_YELLOW+"\nATTENZIONE: L'agenda non esiste!\n"+ANSI_RESET);
				}
			}
			case "2", "aggiungi", "aggiungi agenda" -> aggiungiAgenda();
			case "3", "rimuovi", "rimuovi agenda" -> {
				String selected=Input.readString("Digitare l'agenda desiderata (il controllo è case-sensitive e tiene conto degli spazi): ");
				try {
					rimuoviAgenda(box.selezionaAgenda(selected));
				}
				catch(NoSuchElementException e) {
					System.out.println(ANSI_YELLOW+"\nATTENZIONE: l'agenda non esiste!"+ANSI_RESET);
				}
			}
			case "4", "quit", "esci", "q" -> terminate();
			default -> System.out.println(ANSI_YELLOW+"ATTEZIONE: la scelta effettuata non è valida."+ANSI_RESET);
		}
	}
	
	private static void rimuoviAgenda(Agenda agenda) {
		if(askSomething(YELLOW_BRIGHT+"Sei sicuro di voler rimuovere l'agenda? "+ANSI_RESET)) {
			if(agenda.isSaved() && askSomething(YELLOW_BRIGHT+"Vuoi eliminare anche il file? "+ANSI_RESET)) {
				if(new File(Agenda.getFilesPath(), agenda.getNomeAgenda()+".txt").delete()) System.out.println(ANSI_GREEN+"\n*** Eliminazione del file avvenuta con successo ***"+ANSI_RESET);
				else System.err.println("\n*** Impossibile eliminare il file! ***\n");
			}
			box.removeAgenda(agenda.getNomeAgenda());
			System.out.println(ANSI_GREEN+"\n*** Rimozione agenda avvenuta con successo ***\n"+ANSI_RESET);
			
		}
		else System.out.println(ANSI_GREEN+"\n*** Rimozione annullata ***\n"+ANSI_RESET);
	}
	
	private static void aggiungiAgenda() {
		while(true) {
			switch(Input.readString("Creare una nuova agenda (c) o importarla da file (i)? ").toLowerCase().strip()) {
			case "c", "crea", "creare", "crea agenda", "crea nuova", "creare nuova", "crea nuova agenda", "creare nuova agenda" -> {
				System.out.println(box.aggiungiAgenda(Input.readString("Inserire nome agenda: ")) ? 
						ANSI_GREEN+"\n*** Agenda aggiunta con successo ***\n"+ANSI_RESET : ANSI_YELLOW+"\nATTENZIONE! Impossibile aggiungere l'agenda con il medesimo nome di un'altra!\n"+ANSI_RESET);
				return;
			}
			case "i", "importa", "importare", "importa da file", "importa file", "import" -> {
				try {
					System.out.println(box.caricaAgendaDaFile(Input.readString("Inserire nome agenda da caricare: ")) ? 
							ANSI_GREEN+"\n*** Agenda aggiunta con successo ***\n"+ANSI_RESET : ANSI_YELLOW+"\nATTENZIONE! Impossibile aggiungere l'agenda con il medesimo nome di un'altra!\n"+ANSI_RESET);
					return;
				}
				catch(IOException e) {
					System.err.println("\nATTENZIONE! Impossibile importare l'agenda da file!\n");
					return;
				}
			}
			default -> System.out.println(ANSI_YELLOW+"Attenzione, la scelta effettuata non è valida."+ANSI_RESET);
			}
		}
	}
    
    private static void menuSingolaAgenda(Agenda agenda) {
    	while(true) {
    		System.out.println("\n---------------------------\nNome Agenda: " + agenda.getNomeAgenda() + "\nCosa si desidera fare? ");
        	System.out.println("1) Crea Appuntamento\n"
        			+ "2) Elimina Appuntamento\n"
        			+ "3) Modifica Appuntamento\n"
        			+ "4) Elenca Appuntamenti\n"
        			+ "5) Salva Agenda\n"
        			+ "6) Modifica nome Agenda\n"
        			+ "r) Ritorna al menù precedente\n");
        	
    		String scelta = Input.readString("Inserire un'opzione: ");
        	switch(scelta.strip().toLowerCase()) {
        		case "1", "crea", "crea appuntamento" -> creaAppuntamento(agenda);
        		case "2", "elimina", "elimina appuntamento" -> rimuoviAppuntamento(agenda);
        		case "3", "modifica appuntamento" -> modificaAppuntamento(agenda);
        		case "4", "elenca", "elenca appuntamenti" -> elencaAppuntamenti(agenda);
        		case "5", "salva", "salva agenda" -> {
        			try { 
        				agenda.salvaAgendaSuFile();
        				System.out.println(ANSI_GREEN+"\n*** Salvataggio Agenda su file avvenuto con successo ***"+ANSI_RESET);
					} catch (IOException e) {
						System.err.println("\nATTENZIONE: salvataggio agenda su file non riuscito.");
					}
        		}
        		case "6", "modifica", "modifica nome", "modifica nome agenda" -> modificaNomeAgenda(agenda);
        		case "r", "ritorna", "return" -> { return; }
        		default -> System.out.println(ANSI_YELLOW+"Attenzione, la scelta effettuata non è valida."+ANSI_RESET);
        	}
    	}
    }
    
    private static void modificaNomeAgenda(Agenda agenda) {
        if(box.modificaNomeAgenda(agenda.getNomeAgenda(), Input.readString("Inserire il nuovo nome: "))) {
            System.out.println(ANSI_GREEN+"\nNuovo nome impostato con successo: " + agenda.getNomeAgenda() + "\n"+ANSI_RESET);
        } else System.out.println(ANSI_YELLOW+"\nModifica non riuscita: il nome inserito è già esistente "+ANSI_RESET);
    }
    
    private static String controlloDatoAppuntamento(TipoControllo tc, String request, String errMessage) {
    	String result;
    	boolean pass = false;
    	do {
    		result = Input.readString(request).strip();
    		pass = ControlloDati.controlloPer(tc, result);
    		if(!pass) System.out.println(ANSI_YELLOW+errMessage+ANSI_RESET);
    	}	while(!pass);
    	return result;
    }
    
    
    private static void elencaAppuntamenti(Agenda agenda) {
    	
    	while(true) {
    		switch(Input.readString("Si desidera elencare per:\n1) Data\n2) Persona\n3) Tutto\nInserire un'opzione: ").strip().toLowerCase()) {
	    		case "1", "data" -> {
	    			String data = Input.readString("Inserire la data degli appuntamenti da elencare: ").strip();
	    			System.out.println( agenda.elencaPerData(data).isEmpty() ? ANSI_YELLOW+"\n*** Nessun Appuntamento trovato con questa data ***"+ANSI_RESET : "\n"+agenda.elencaPerData(data));
	    			return;
	    		}
	    		case "2", "persona" -> {
	    			String persona = Input.readString("Inserire il nome della persona con cui effettuare la ricerca: ").strip();
	    			System.out.println( agenda.elencaPerPersona(persona).isEmpty() ? ANSI_YELLOW+"\n*** Nessun Appuntamento trovato con questa persona ***"+ANSI_RESET : "\n"+agenda.elencaPerPersona(persona));
	    			return;
	    		}
	    		case "3", "tutto" -> { System.out.println( agenda.toString().isEmpty() ? ANSI_YELLOW+"\n*** Agenda Vuota ***"+ANSI_RESET : "\n"+agenda.toString() ); return; }
	    		default -> System.out.println(ANSI_YELLOW+"Attenzione, la scelta effettuata non è valida."+ANSI_RESET);
    		}
    	}
    }
    
    private static void creaAppuntamento(Agenda agenda) {
    	String[] parametri = new String[5];
    	HashMap<TipoControllo, RequestApp> requests = new HashMap<>();
    	requests.put(TipoControllo.CONTROLLO_DATA, new RequestApp("Inserire una data valida nel formato dd-MM-aaaa: ", "La data non è valida!"));
    	requests.put(TipoControllo.CONTROLLO_ORARIO, new RequestApp("Inserire un orario valido nel formato HH-mm: ", "L'orario non è valido!"));
    	requests.put(TipoControllo.CONTROLLO_DURATA, new RequestApp("Inserire una durata in minuti valida (solo valori interi): ", "La durata non è valida!"));
    	requests.put(TipoControllo.CONTROLLO_LUOGO, new RequestApp("Inserire il nome di un luogo valido (sono ammessi solo caratteri alfabetici o al più indirizzi (numero indirizzo alla fine, separato da spazio)): ", "Il luogo non è valido!"));
    	requests.put(TipoControllo.CONTROLLO_NOME, new RequestApp("Inserire il nome della persona con cui si ha l'appuntamento (ammessi solo caratteri alfanumerici, ma NON SOLO numeri): ", "Il nome non è valido!"));
    	int index = 0;
    	for(TipoControllo tc : TipoControllo.values()) {
    		parametri[index++] = controlloDatoAppuntamento(tc, requests.get(tc).getRequest(), requests.get(tc).getErrMessage());
    	}
    	if(agenda.aggiungiAppuntamento(parametri[0], parametri[1], parametri[2], parametri[3], parametri[4])) System.out.println(ANSI_GREEN+"\n*** Appuntamento aggiunto con successo ***"+ANSI_RESET);
    	else System.out.println(ANSI_YELLOW+"\nATTENZIONE: Appuntamento non compatibile con l'agenda"+ANSI_RESET);
    }
    
    private static void rimuoviPer(Agenda agenda, String request, BiPredicate<Agenda, String> removeMethod, BiPredicate<Agenda, String> isEmpty) {
    	String value = Input.readString(request).strip();
		if(isEmpty.test(agenda, value)) System.out.println(ANSI_YELLOW+"\nATTENZIONE: Appuntamento/i non esistenti nell'Agenda."+ANSI_RESET);
		else if(askSomething(YELLOW_BRIGHT+"Appuntamento/i trovati. Sei sicuro di voler procedere all'eliminazione? "+ANSI_RESET)) {
				removeMethod.test(agenda, value);
				System.out.println(ANSI_GREEN+"\n*** Rimozione completata ***"+ANSI_RESET);
		} 
		else System.out.println(ANSI_GREEN+"\n*** Rimozione annullata ***"+ANSI_RESET);
    }
    
    private static void rimuoviAppuntamento(Agenda agenda) {
 
    	while(true) {
    		System.out.println("1) Rimuovi secondo data\n2) Rimuovi secondo persona\n3) Rimuovi secondo data orario\n4) Rimuovi tutto\nr) Annulla");
    		switch(Input.readString("Inserire come si desidera procedere: ").strip().toLowerCase()) {
    			case "1", "data", "rimuovi data", "rimuovi secondo data" ->  {
    				rimuoviPer(agenda, "Inserire la data: ", (diary, data) -> diary.rimuoviPerData(data), (diary, data) -> diary.searchAppuntamentoPerData(data).isEmpty());
    				return;
    			}
    			case "2", "persona", "rimuovi persona", "rimuovi secondo persona" ->  {
    				rimuoviPer(agenda, "Inserire la persona: ", (diary, persona) -> diary.rimuoviPerPersona(persona), (diary, persona) -> diary.searchAppuntamentoPerPersona(persona).isEmpty());
    				return;
    			}
    			case "3", "data orario", "rimuovi data orario", "rimuovi secondo data orario" -> {
    				rimuoviPer(agenda, "Inserire la data e l'orario (separati da almeno uno spazio): ", (diary, dataOrario) -> diary.rimuoviPerDataOrario(dataOrario), (diary, dataOrario) -> diary.searchAppuntamentoPerDataOrario(dataOrario).isEmpty());
    				return;
    			}
    			case "4", "tutto", "rimuovi tutto" -> {
    				if(askSomething(YELLOW_BRIGHT+"Sei sicuro di voler eliminare tutti gli appuntamenti? "+ANSI_RESET)) {
    					agenda.rimuoviTutto();
    					System.out.println(ANSI_GREEN+"\n*** Appuntamenti eliminati ***"+ANSI_RESET);
    				} else System.out.println(ANSI_GREEN+"\n*** Rimozione annullata ***"+ANSI_RESET);
    				return;
    			}
    			case "r", "annulla" -> { return; }
    			default -> System.out.println(ANSI_YELLOW+"Attenzione, la scelta effettuata non è valida."+ANSI_RESET);
    		}
    	}
    }
    
    private static void modificaAppuntamento(Agenda agenda) {
    	boolean repeat = true;
    	do {
    		int risultato = agenda.modificaAppuntamento(
    				Input.readString("Inserire \"DATA ORARIO\" dell'appuntamento da modificare (separati da spazio): ").strip(),
    				Input.readString("Inserire nome parametro da modificare (data, orario, durata, luogo, persona): "),
    				Input.readString("Inserire nuovo valore da inserire: ").strip());
    		switch(risultato) {
    			case 0 -> System.out.println(ANSI_YELLOW+"ATTENZIONE: l'Appuntamento selezionato non è esistente."+ANSI_RESET);
    			case 1 -> System.out.println(ANSI_GREEN+"\n*** Modifica avvenuta con successo ***"+ANSI_RESET);
    			case -1 -> System.out.println(ANSI_YELLOW+"ATTENZIONE: non è stato possibile modificare l'appuntamento poiché l'avrebbe reso non compatibile con l'Agenda."+ANSI_RESET);
    			case -2 -> System.out.println(ANSI_YELLOW+"ATTENZIONE: uno o più parametri inseriti non sono nel formato corretto"+ANSI_RESET);
    			case -3 -> System.out.println(ANSI_YELLOW+"ATTENZIONE: il nome del parametro da modificare non identifica alcun campo."+ANSI_RESET);
    		}
    		if(risultato != 1) repeat = askSomething(YELLOW_BRIGHT+"Si desidera riprovare la modifica? "+ANSI_RESET); else repeat = false;	
    	} while(repeat);
    }
    
    private static void terminate() {
    	if(!box.allSaved()) {
    		if(askSomething(YELLOW_BRIGHT+"Alcune agende non sono state salvate, si desidera salvarle? "+ANSI_RESET)) {
    			try {
    				box.salvaContenitoreSuFile();
    				System.out.println(ANSI_GREEN+"\n*** Salvataggio delle agende avvenuto con successo ***"+ANSI_RESET);
    			} catch(IOException e) { abort("\nATTENZIONE: salvataggio agende fallito.", e); }
    		}
    	}
    	System.exit(0);
    }
    
    private static void abort(String message, IOException e) {
    	System.err.println(message);
		e.printStackTrace();
		System.exit(1); 
    }
    
    private static boolean askSomething(String question) {
        Boolean conferma = null;
        do {
            String risposta = Input.readString(question).strip().toLowerCase();
            switch(risposta) {
                case "y", "yes", "ye", "yeah", "yea", "si", "sì", "ja", "oui", "affermative" -> conferma = true;
                case "n", "no", "nop", "nope", "non", "not", "nein", "negative" -> conferma = false;
                default -> System.out.println(ANSI_YELLOW+"Attenzione, la scelta effettuata non è valida."+ANSI_RESET);
            }
        } while(conferma == null);

        return conferma;
    }
    
    public static void main(String[] args) {
    	try {	
    		if(!inizializzaAgende()) System.out.println(ANSI_YELLOW+"Attenzione: non è stato possibile caricare alcune agende."+ANSI_RESET);
    	} catch(IOException e) { 
    		abort("ATTENZIONE! Impossibile inizializzare:", e);
    	}
    	while(true) {
    		menuAgende();
    	}
    }
}
