package model.elements;

/**
 * Klasse zur implementierung eines Elemtes eines Graphen. Diese besitzt zur
 * Identifikation eine eindeutige Id.
 *
 */
public abstract class PGraphElement {
	private final String id;

	/**
	 * Konstruktor
	 * 
	 * @param id Die eindeutige Id
	 */
	public PGraphElement(String id) {
		this.id = id;
	}

	/**
	 * Getter Methode für die Id des Graph Elementes
	 * 
	 * @return die Id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Methode um festzustellen ob die ID dieses PGraphElemnts dem eines anderen
	 * PGraphElements entspricht
	 * 
	 * @param other das zu vergleichende Element
	 * @return sind die Id's gleich?
	 */
	public boolean equalsInId(PGraphElement other) {
		return this.getId().equals(other.getId());
	}

	/**
	 * Methode um das Label des Graph Elementes zur Darstellung seiner Informationen
	 * zu erhalten.
	 * 
	 * @return Das Label
	 */
	public abstract String getLabel();
}
