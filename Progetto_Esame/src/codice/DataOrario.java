package codice;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * La classe {@code DataOrario} è una classe semplice che permette di creare oggetti di tipo {@link LocalTime} e {@link LocalDate}, 
 * parsificando le stringhe <strong>data</strong> e <strong>orario</strong> nel formato <strong>{@code dd-MM-uuuu}</strong> 
 * e nel formato <strong>{@code HH-mm}</strong> rispettivamente.<br>Questo avviene sfruttando la classe DateTimeFormatter di Java. 
 * Inoltre ciò agevola le operazioni su questi campi.<br>
 * 
 * In particolare:<ul>
 * <li>Permette di comparare due {@code DataOrario} tramite il metodo {@link #compareTo};
 * <li>Permette di rappresentare la data e l'orario nel tipo {@link String} tramite il metodo {@link #toString};
 * <li>Permette di aggiungere minuti ad un data-orario iniziale e ottenerne uno nuovo con data e orario aggiornati tramite il metodo {@link #plusMinuti};
 * <li>Permette di verificare se due {@code DataOrario} sono identici tramite il metodo {@link #equals};
 * <li>Permette di ottenere le informazioni di questo {@code DataOrario} attraverso dei {@code getter}:<ul>
 * <li>{@link #getData()} per recuperare la <strong>data</strong> nel tipo {@link LocalDate};
 * <li>{@link #getOrario()} per recuperare l'<strong>orario</strong> nel tipo {@link LocalTime};
 * <li>{@link #getDataToString()} per recuperare la <strong>data</strong> nel tipo {@link String};
 * <li>{@link #getOrarioToString()} per recuperare l'<strong>orario</strong> nel tipo {@link String}.</ul></ul>
 * 
 * Durante la creazione di un {@code DataOrario}, i parametri passati come argomenti nel tipo {@link String},
 * vengono <em>parsificati</em> tramite il metodo {@code parse} delle classi {@link LocalDate} per la <strong>data</strong>
 * e {@link LocalTime} per l'<strong>orario</strong>.<p>
 * 
 * Sia la <strong>data</strong> che l'<strong>orario</strong> sono rappresentati in un determinato formato,
 * selezionato tramite un <strong>pattern</strong> ed uno <strong>stile</strong> rispettivamente tramite il metodo {@code ofPattern} e {@code withResolverStyle}
 * della libreria {@link DateTimeFormatter}.<br>
 * In particolare:<ul>
 * <li>la <strong>data</strong> è rappresentata nel formato <strong>{@code dd-MM-uuuu}</strong> con uno stile ti dipo {@code STRICT};
 * <li>l'<strong>orario</strong> è rappresentato nel formato <strong>{@code HH-mm}</strong> con uno stile ti dipo {@code STRICT}.</ul>
 * 
 * @see Appuntamento
 * 
 * @author Kristian Rigo
 * @author Nicolò Bianchetto
 */

public class DataOrario {
	
	private LocalDate data;
	private LocalTime orario;
	private final static DateTimeFormatter FORMATTER_DATA = DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT);
	private final static DateTimeFormatter FORMATTER_TIME = DateTimeFormatter.ofPattern("HH-mm").withResolverStyle(ResolverStyle.STRICT);
	
	private DataOrario(LocalDate data, LocalTime orario) {
		this.data = data;
		this.orario = orario;
	}
	
	/**
	 * Crea un nuovo {@code DataOrario} tramite <strong>data</strong> e <strong>orario</strong> di tipo {@link String}.<br>
	 * <em>Parsifica</em> i parametri attraverso il metodo {@code parse}, convertendoli nel tipo {@link LocalDate} e {@link LocalTime} 
	 * passando come argomenti la data, l'orario e il {@link DateTimeFormatter} che identifica 
	 * il modo in cui sono convertiti, attraverso un <strong>pattern</strong> ed uno <strong>stile</strong>.<br>
	 * Lancia un eccezione {@link DateTimeParseException} se il parse non va a buon fine.
	 * 
	 * @param data stringa che identifica la data
	 * @param orario stringa che identifica l'orario
	 * @throws DateTimeParseException se non è stato possibile parsificare i parametri
	 */
	
	public DataOrario(String data, String orario) throws DateTimeParseException {
		this(LocalDate.parse(data, FORMATTER_DATA), LocalTime.parse(orario, FORMATTER_TIME));
	}
	
	/**
	 * Restituisce la data nel tipo {@link LocalDate}.
	 * 
	 * @return la data in {@link LocalDate}.
	 */

	public LocalDate getData() {
		return data;
	}
	
	/**
	 * Restituisce l'orario nel tipo {@link LocalTime}.
	 * 
	 * @return l'orario in {@link LocalTime}.
	 */
	
	public LocalTime getOrario() {
		return orario;
	}
	
	/**
	 * Restituisce la data tramite il metodo <strong>{@code format}</strong> della classe {@link LocalDate}
	 * che prende come argomento un parametro di tipo {@link DateTimeFormatter} che identifica 
	 * il modo in cui verrà convertita la data, attraverso un <strong>pattern</strong> ed uno <strong>stile</strong>.<br>
	 * E.g.:
	 * <pre>
	 * {@code DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT);}</pre>
	 * 
	 * @return la data nel tipo {@link String}.
	 */
	
	public String getDataToString() {
		return data.format(FORMATTER_DATA);
	}
	
	/**
	 * Restituisce l'orario tramite il metodo <strong>{@code format}</strong> della classe {@link LocalTime}
	 * che prende come argomento un parametro di tipo {@link DateTimeFormatter} che identifica 
	 * il modo in cui verrà convertito l'orario, attraverso un <strong>pattern</strong> ed uno <strong>stile</strong>.<br>
	 * E.g.:<pre>
	 * {@code DateTimeFormatter FORMATTER_TIME = DateTimeFormatter.ofPattern("HH-mm").withResolverStyle(ResolverStyle.STRICT);}</pre>
	 * 
	 * @return l'orario nel tipo {@link String}.
	 */
	
	public String getOrarioToString() {
		return orario.format(FORMATTER_TIME);
	}
	
	/**
	 * Somma il <strong>parametro</strong> di tipo {@link String} passata per argomento, ad una data-orario
	 * utilizzando il metodo <strong>{@code plusMinutes}</strong> della classe {@link LocalDateTime}.
	 * In questo modo il nuovo data-orario sarà aggiornato anche nel caso l'orario finale superi la mezzanotte, 
	 * la data finale verrà quindi aggiornata col giorno successivo.<br>
	 * E.g.:<blockquote><pre>
	 * {@code String data = "31-12-1999";
	 * String orario = "23-30";
	 * String durata = 45;
	 * 
	 * DataOrario dataTimeInizio -> "31-12-1999 23-30";
	 * DataOrario dataTimeFine = dataTimeInizio.plusMinuti(45) -> "01-01-2000 00-15";}</pre></blockquote>
	 * 
	 * @param durata stringa che identifica la durata
	 * @return un nuovo {@code DataOrario} con la data e l'orario aggiornati
	 */
	
	public DataOrario plusMinuti(String durata) {
		LocalDateTime ldt = data.atTime(orario).plusMinutes(Long.parseLong(durata));
		return new DataOrario(ldt.toLocalDate(), ldt.toLocalTime());
	}
	
	/**
	 * Assegna a due variabili di tipo {@link LocalDateTime} questo {@code DataOrario} e {@code Other} da confrontare,
	 * poichè il metodo {@code compareTo} prende come argomento solo oggetti di tipo {@link LocalDateTime}.<br>
	 * Compara i due oggeti e restituisce un valore negativo o positivo:<ul>
	 * <li>Positivo se questo {@code DataOrario} è più grande di {@code Other};
	 * <li>Negativo se questo {@code DataOrario} è più piccolo di {@code Other}.</ul>
	 * 
	 * @param other {@code DataOrario} da comparare con questo {@code DataOrario}.
	 * @return un valore di tipo {@code int} negativo se più piccolo o positivo se più grande.
	 */
	
	public int compareTo(DataOrario other) {
		LocalDateTime thisDateTime = data.atTime(orario), otherDateTime = other.getData().atTime(other.getOrario());
		return thisDateTime.compareTo(otherDateTime);
	}
	
	/**
	 * Passa come argomento a {@link #compareTo}, un nuovo {@code DataOrario}
	 * costruito tramite la <strong>data</strong> e l'<strong>orario</strong>
	 * di tipo {@link String} passati come argomento.<br>
	 * Compara i due oggeti e restituisce un valore negativo o positivo:<ul>
	 * <li>Positivo se questo {@code DataOrario} è più grande di {@code Other};
	 * <li>Negativo se questo {@code DataOrario} è più piccolo di {@code Other}.</ul>
	 * 
	 * @param data stringa che identifica la data
	 * @param orario stringa che identifica l'orario
	 * @return un valore di tipo {@code int} negativo se più piccolo o positivo se più grande.
	 */
	
	public int compareTo(String data, String orario) {
		return compareTo(new DataOrario(data, orario));
	}
	
	/**
	 * Restituisce una rappresentazione in stringa del data-orario.
	 * 
	 * @return una rappresentazione del data-orario nel formato {@code String}.
	 */
	
	@Override
	public String toString() {
		return getDataToString() + " " + getOrarioToString();
	}
	
	/**
	 * Verifica che l'oggetto passato come parametro sia uguale
	 * a questo {@code DataOrario}.
	 * <p>Più precisamente:<ul>
	 * <li> l'oggetto non deve essere {@code null};
	 * <li> la classe dell'oggetto deve essere {@code DataOrario};
	 * <li> la {@code data} del data-orario deve essere la medesima;
	 * <li> l'{@code orario} del data-orario deve essere il medesimo.</ul>
	 * <p>Restituisce {@code true} se queste condizioni sono soddisfatte.
	 * 
	 * @param object il riferimento all'oggetto da comparare.
	 * @return {@code true} se l'oggetto è uguale a questo data-orario, {@code false} altrimenti.
	 */
	
	@Override
	public boolean equals(Object object) {
		if(object == null || object.getClass() != this.getClass()) return false;
		DataOrario other = (DataOrario) object;
		return (other.getData().equals(this.data)) && (other.getOrario().equals(this.orario));
	}
}
