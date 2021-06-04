package model.elements;

/**
 * Klasse für eine Kante in einem Erreichbarkeitsgraphen, die zusätzlich die
 * {@link PTransition} deren Schaltung sie erzeugte speichert.
 * 
 *
 */

public class PAccEdge extends PEdge {

	private PTransition transition;

	/**
	 * Konstruktor
	 * 
	 * @param id         Die eindeutige Id der Kante
	 * @param from       Der Ausgangsknoten
	 * @param to         Der Zielknoten
	 * @param transition Die Transition durch deren Schaltung die Kante entstanden
	 *                   ist
	 */
	public PAccEdge(String id, PLabeledNode from, PLabeledNode to, PTransition transition) {
		super(id, from, to);
		this.transition = transition;
	}

	/**
	 * Konstruktor
	 * 
	 * @param id         Die eindeutige Id der Kante
	 * @param fromID     Die id des Ausgangsknotens
	 * @param toID       Die id des Zielknotens
	 * @param transition Die Transition durch deren Schaltung die Kante entstanden
	 *                   ist
	 */
	public PAccEdge(String id, String fromID, String toID, PTransition transition) {
		super(id, fromID, toID);
		this.transition = transition;
	}

	/**
	 * Getter Methode für die Transition deren Schaltung diese Kante erzeugt hat.
	 * 
	 * @return die Transition
	 */
	public PTransition getTransition() {
		return transition;
	}

	/**
	 * Methode um das Label des Knotens zur Darstellung im Graphen zu erhalten.
	 * 
	 * @return das Label
	 */
	@Override
	public String getLabel() {
		return transition.getLabel();
	}

}
