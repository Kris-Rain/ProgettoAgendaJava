package codice;

import java.time.*;

public class DataOrario {
	
	private LocalDate data;
	private LocalTime orario;
	
	public DataOrario() {	
		data = LocalDate.now();
		orario = LocalTime.now();
	}
	
	public DataOrario(String data, String orario) {
		this.data = LocalDate.parse(data);
		this.orario = LocalTime.parse(orario);
	}
	
	public void addTempo(String durata) {
		LocalDateTime ldt = data.atTime(orario).plusMinutes(Long.parseLong(durata));
		data.adjustInto(ldt);
		orario.adjustInto(ldt);
	}
}
