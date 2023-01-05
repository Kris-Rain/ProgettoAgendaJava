package codice;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DataOrario {
	
	private LocalDate data;
	private LocalTime orario;
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-uuuu");
	
	
	public DataOrario(LocalDate data, LocalTime orario) {
		this.data = data;
		this.orario = orario;
	}
	
	public DataOrario(String data, String orario) {
		this(LocalDate.parse(data, formatter), LocalTime.parse(orario));
	}
	
	public DataOrario() {	
		this(LocalDate.now(), LocalTime.now());
	}

	
	public LocalDate getData() {
		return data;
	}
	
	public LocalTime getOrario() {
		return orario;
	}
	
	
	public String getDataToString() {
		return data.format(formatter);
	}
	
	public String getOrarioToString() {
		return orario.toString();
	}
	
	public void addTempoMinuti(String durata) {
		LocalDateTime ldt = data.atTime(orario).plusMinutes(Long.parseLong(durata));
		data = ldt.toLocalDate();
		orario = ldt.toLocalTime();
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
	
	
	public int compareTo(LocalDate data, LocalTime orario) {
		return compareTo(new DataOrario(data, orario));
	}
	
	@Override
	public String toString() {
		return data.format(formatter) + " " + getOrarioToString();
	}
}
