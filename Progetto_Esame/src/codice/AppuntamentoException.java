/**
 * @author Kristian Rigo
 * @author Nicolò Bianchetto
 */

package codice;

public class AppuntamentoException extends Exception{
	private static final long serialVersionUID = 1L;
	public AppuntamentoException(String messaggio) {
		super(messaggio);
	}
}
