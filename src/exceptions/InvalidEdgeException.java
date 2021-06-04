package exceptions;

/**
 * Eine {@link Exception} die geworfen wird, falls im {@link model.DataModel} eine
 * Verknüpfung von 2 Knoten durch eine Kante nicht möglich war. Dies geschieht
 * nur im Petrinetz, wenn eine Zuweisung nicht eindeutig ist oder beide Knoten
 * der Kante entweder Stellen oder Transitionen sind.
 *
 */
public class InvalidEdgeException extends Exception {

	/**
	 * Default Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor
	 */
	public InvalidEdgeException() {
		super();
	}

	/**
	 * Konstruktor
	 * 
	 * @param message die Fehlernachricht
	 * @param cause   der Auslöser des Fehlers
	 */
	public InvalidEdgeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Konstruktor
	 * 
	 * @param message die Fehlernachricht
	 */
	public InvalidEdgeException(String message) {
		super(message);
	}

	/**
	 * Konstruktor
	 * 
	 * @param cause der Auslöser des Fehlers
	 */
	public InvalidEdgeException(Throwable cause) {
		super(cause);
	}

}
