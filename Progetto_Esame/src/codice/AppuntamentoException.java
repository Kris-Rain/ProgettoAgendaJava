package codice;

/**
 * La classe {@code AppuntamentoException} è una forma di {@code Throwable}
 * che indica le condizioni che si potrebbe voler catturare.
 * Le eccezioni controllate devono essere dichiarate in una clausola {@code throws}
 * di un metodo o di un costruttore se possono essere generate dall'esecuzione 
 * del metodo o del costruttore.
 * 
 * @author Kristian Rigo
 * @author Nicolò Bianchetto
 */

public class AppuntamentoException extends Exception{
	private static final long serialVersionUID = 1L;
	public AppuntamentoException(String messaggio) {
		super(messaggio);
	}
}
