package interfaccia;
/**
 * {@code RequestApp} è una classe molto semplice la quale
 * si limita a salvare una coppia di stringhe e alla possibilità
 * di leggerle successivamente.
 * <p>Le stringhe sono: <ul>
 * <li> {@link #REQUEST}, che indica, come da nome, una richiesta generica;
 * <li> {@link #ERR_MESSAGE}, stringa che punta a descrivere l'eventuale errore associato alla richiesta.</ul>
 * <p>Nonostante l'utilizzo limitato, potrebbe essere utile, ad esempio, nei casi di richieste multiple.<br>
 * In particolare, potrebbe essere utile a compattare il codice evitando molte ripetizioni 
 * <em>e.g. attraverso un {@link java.util.HashMap} in cui ad ogni chiave è associata una {@code RequestApp}.</em>
 * <p><strong>Questa classe è immutabile. Una volta creata, le stringhe sono di sola lettura</strong>.
 * 
 * @see Interfaccia
 * 
 * @author Nicolò Bianchetto
 * @author Kristian Rigo
 */
public class RequestApp {
	/**
	 * Stringa che descrive la richiesta <em>e.g. una richiesta di input</em>.
	 */
	public final String REQUEST;
	/**
	 * Stringa che descrive l'eventuale errore per la richiesta <em>e.g. un input errato</em>.
	 */
	public final String ERR_MESSAGE;
	
	/**
	 * Crea un nuovo oggetto {@link RequestApp} ricevendo come parametri due stringhe.
	 * 
	 * @param request stringa che descrive la richiesta.
	 * @param errMessage stringa che descrive l'eventuale errore associato alla richiesta.
	 */
	
	public RequestApp(String request, String errMessage) {
		this.REQUEST = request;
		this.ERR_MESSAGE = errMessage;
	}

}
