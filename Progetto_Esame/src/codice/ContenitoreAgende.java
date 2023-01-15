package codice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Kristian Rigo
 * @author Nicolò Bianchetto
 */

public class ContenitoreAgende implements Iterable <Agenda> {
	
	private ArrayList<Agenda> agende;
	
	
	public ContenitoreAgende() {
		agende = new ArrayList<>();
	}
	
	
	public int getDimensione() {
		return agende.size();
	}
	
	public ArrayList<Agenda> getAgende() {
		return agende;
	}

	public class IteratoreContenitoreAgende implements Iterator<Agenda> {
		private int indiceAgende;
		private int lastIndex;
		
		public IteratoreContenitoreAgende() {
			indiceAgende = 0;
			lastIndex = agende.size();
		}
		
		@Override
		public boolean hasNext() {
			return indiceAgende < lastIndex;
		}

		@Override
		public Agenda next() throws ConcurrentModificationException {
			if(lastIndex != agende.size()) throw new ConcurrentModificationException();
			return agende.get(indiceAgende++);
		}
	}
	
	
	public boolean caricaAgendaDaFile(String fileName) throws IOException {
		File file = new File(Agenda.getFilesPath(), fileName);	
		Agenda newAgenda = new Agenda(file);
		return (this.contains(newAgenda.getNomeAgenda())) ? false : agende.add(newAgenda);
	}
	
	
	public boolean caricaMultiAgendeDaFiles() throws IOException {
		File cartella = new File(Agenda.getFilesPath());
		if(!cartella.exists()) throw new FileNotFoundException("Impossibile trovare la cartella per il caricamento delle Agende.");
		File[] listaFile = cartella.listFiles();
		boolean risultatoFinale = true;
		for(File file: listaFile) {
			if(!file.isDirectory()) {
				if(!caricaAgendaDaFile(file.getName())) risultatoFinale = false;
			}
		}
		return risultatoFinale;
	}
	
	
	public boolean salvaAgendaSuFile(String nomeAgenda) throws IOException {
		Agenda toSave;
		try {
			toSave = selezionaAgenda(nomeAgenda);
		} catch(NoSuchElementException e) { return false; }
		
		return toSave.salvaAgendaSuFile();
	}
	
	
	public boolean salvaContenitoreSuFile() throws IOException {
		for(Agenda elemento: this) {
			if(!elemento.isSaved()) elemento.salvaAgendaSuFile();
		}
		return true;
	}
	
	
	public boolean allSaved() {
		return agende.stream().allMatch( agenda -> agenda.isSaved() );
	}
	
	
	public boolean clear() {
		return agende.removeAll(agende);
	}
	
	
	public Agenda selezionaAgenda(String nomeAgenda) throws NoSuchElementException {
		for(Agenda elemento: this) {
			if(elemento.getNomeAgenda().equals(nomeAgenda)) return elemento;
		}
		throw new NoSuchElementException("Agenda non esiste!");
	}
	
	
	private boolean actionToAgenda(String nomeAgenda, Predicate<Agenda> actionIfFound, Predicate<Agenda> actionIfNotFound) {
		try {
			return actionIfFound.test(selezionaAgenda(nomeAgenda));
		}
		catch(NoSuchElementException e) {
			return actionIfNotFound.test(new Agenda(nomeAgenda));
		}
	}
	
	
	public boolean contains(String nomeAgenda) {
		return actionToAgenda(nomeAgenda, agenda -> true, agenda -> false);
	}
	
	
	public boolean aggiungiAgenda(String nomeAgenda) {
		return actionToAgenda(nomeAgenda, agenda -> false, agenda -> agende.add(agenda));
	}
	
	
	public boolean removeAgenda(String nomeAgenda) {
		return actionToAgenda(nomeAgenda, agenda -> agende.remove(agenda), agenda -> false);
	}
	
	
	public boolean aggiungiAgenda(Agenda toAdd) {
		return (this.contains(toAdd.getNomeAgenda())) ? false : agende.add(toAdd);
	}

	
	private String makeString(Function<Agenda, String> getString)  {
		ArrayList<Agenda> elenco = new ArrayList<>(agende);
		String finalString = "";
		elenco.sort( (first, second) -> first.getNomeAgenda().compareTo(second.getNomeAgenda()));
		for(Agenda elemento: elenco) finalString += getString.apply(elemento);
		return finalString;
	}
	
	public String elencaNomiAgende() {
		return makeString( agenda -> agenda.getNomeAgenda() + "\n");
	}
	
	@Override
	public String toString() {
		return makeString( agenda -> agenda.toString() + "\n------------------------------------------\n\n");
	}

	@Override
	public Iterator<Agenda> iterator() {
		return new IteratoreContenitoreAgende();
	}

}
