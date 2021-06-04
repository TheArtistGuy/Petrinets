package model.elements;

/**
 * Klasse zur implementierung einer Stelle in einem Petrinetz. Die Stelle wird
 * durch eine eindeutige Id identifiziert. Die Marken die auf der Stelle liegen
 * werden in der Klasse selbst gespeichert.
 *
 */

public class PField extends PLabeledNode{

	private int tokens;

	/**
	 * Konstruktor Die Marken der Stelle werden mit 0 initialisiert.
	 * 
	 * @param id die eindeutige Id der Stelle.
	 */
	public PField(String id) {
		super(id);
		tokens = 0;
	}

	/**
	 * Getter Methode für die Anzahl der Marken, die auf der Stelle liegen.
	 * 
	 * @return Die Anzahl der Marken.
	 */
	public int getTokens() {
		return tokens;
	}

	/**
	 * Setter Methode für die Anzahl der Marken, die auf der Stelle liegen.
	 * 
	 * @param tokens Die Anzahl der Marken.
	 */
	public void setMarks(int tokens) {
		if (tokens >= 0) {
			this.tokens = tokens;
		}
	}

	/**
	 * Methode um die Anzahl der Marken auf der Stelle um eins zu erhöhen.
	 */
	public void increaseMarksByOne() {
		tokens++;
	}

	/**
	 * Methode um die Anzahl der Marken auf der Stelle um eins zu senken.
	 */
	public void decreaseMarksByOne() {
		if (tokens > 0)
			tokens--;
	}

	/**
	 * Methode um das Label der Stelle zur Darstellung im Graphen zur erhalten.
	 */
	@Override
	public String getLabel() {
		return "[" + getId() + "] " + getName() + " <" + tokens + ">";
	}

}
