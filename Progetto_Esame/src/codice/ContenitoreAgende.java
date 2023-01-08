package codice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
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
	
	
	public boolean aggiungiAgendaDaFile(String filePath) throws FileNotFoundException, IOException {
		File file = new File(filePath);
		
		if(this.contains(file.getName())) return false;
		
		return agende.add(new Agenda(file));
	}
	
	
	public void clear() {
		agende.removeAll(agende);
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

	
	@Override
	public String toString() {
		String stringaAgende = "";
		for(Agenda agenda: this) stringaAgende+= agenda.toString() + "\n------------------------------------\n";
		return stringaAgende;
	}

	@Override
	public Iterator<Agenda> iterator() {
		return new IteratoreContenitoreAgende();
	}

}
