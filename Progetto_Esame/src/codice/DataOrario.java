package codice;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class DataOrario {
	
	private LocalDate data;
	private LocalTime orario;
	private final static DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT);
	private final static DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH-mm").withResolverStyle(ResolverStyle.STRICT);
	
	private DataOrario(LocalDate data, LocalTime orario) {
		this.data = data;
		this.orario = orario;
	}
	
	public DataOrario(String data, String orario) throws DateTimeParseException {
		this(LocalDate.parse(data, formatterData), LocalTime.parse(orario, formatterTime));
	}

	public LocalDate getData() {
		return data;
	}
	
	public LocalTime getOrario() {
		return orario;
	}
	
	public String getDataToString() {
		return data.format(formatterData);
	}
	
	public String getOrarioToString() {
		return orario.format(formatterTime);
	}
	
	public DataOrario plusMinuti(String durata) {
		LocalDateTime ldt = data.atTime(orario).plusMinutes(Long.parseLong(durata));
		return new DataOrario(ldt.toLocalDate(), ldt.toLocalTime());
	}
	
	public int compareTo(DataOrario other) {
		LocalDateTime thisDateTime = data.atTime(orario), otherDateTime = other.getData().atTime(other.getOrario());
		return thisDateTime.compareTo(otherDateTime);
	}
	
	public int compareTo(String data, String orario) {
		return compareTo(new DataOrario(data, orario));
	}
	
	@Override
	public String toString() {
		return getDataToString() + " " + getOrarioToString();
	}
	
	@Override
	public boolean equals(Object object) {
		if(object == null || object.getClass() != this.getClass()) return false;
		DataOrario other = (DataOrario) object;
		return (other.getData().equals(this.data)) && (other.getOrario().equals(this.orario));
	}
}
