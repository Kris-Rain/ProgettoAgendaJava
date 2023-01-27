package codice;

/**
 * La classe {@code AppuntamentoException} è una forma di {@code Throwable} che estende {@link Exception}
 * e che indica le condizioni che si potrebbe voler catturare.<br>
 * Le eccezioni controllate devono essere dichiarate in una clausola {@code throws}
 * di un metodo o di un costruttore se possono essere generate dall'esecuzione
 * del metodo o del costruttore.<p>
 * Indica il tentativo di creare un {@link Appuntamento} con parametri errati, in quel caso verrà sollevata
 * l'eccezione {@code AppuntamentoException} che potrà essere gestita tramite una {@code catch}
 * utilizzando il costruttore {@link #AppuntamentoException} e passando un messaggio come argomento.
 * 
 * @see Appuntamento
 * 
 * @author Kristian Rigo (matr. 20046665)
 * @author Nicolò Bianchetto (matr. 20026606)
 */

public class AppuntamentoException extends Exception{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Costruttore per {@code AppuntamentoException} che prende un messaggio di tipo {@link String}
	 * come argomento e lo restituisce alla classe {@code super}.
	 * 
	 * @param messaggio da passare alla classe {@code super}.
	 */
	
	public AppuntamentoException(String messaggio) {
		super(messaggio);
	}
}
